package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SFAMonitoring extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ProgressBar progressBar;
    private EditText historyDatepicker;
    private ArrayList<String> groupTitles = new ArrayList<>();
    private ArrayList<ArrayList<String>> childItems = new ArrayList<>();
    private PazDatabaseHelper mdatabaseHelper;
    String JSON_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_monitoring);

        expandableListView = findViewById(R.id.expandableListView);
        progressBar = findViewById(R.id.progressBar);
        historyDatepicker = findViewById(R.id.history_datepicker);
        mdatabaseHelper = new PazDatabaseHelper(SFAMonitoring.this);
        JSON_URL= "http://" + mdatabaseHelper.get_active_connection() + "/mobileapi";

        setCurrentDate();

        historyDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        fetchData(getCurrentDate(),JSON_URL);
    }

    private void setCurrentDate() {
        String currentDate = getCurrentDate();
        historyDatepicker.setText(currentDate);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchData(String selectedDate, String JSON_URL) {
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {

                    String urlString = JSON_URL+"/SFA_SALESREP_LIST.php?selectedDate=" + selectedDate;
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setReadTimeout(15000);

                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        return response.toString();
                    } else {
                        return "Error: HTTP " + responseCode;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {


                if (result == null || result.startsWith("Error:")) {
                    Toast.makeText(SFAMonitoring.this, "Error fetching data: " + result, Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(result);

                    if (!jsonResponse.has("data")) {
                        Toast.makeText(SFAMonitoring.this, "No 'data' found in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    groupTitles.clear();
                    childItems.clear();

                    if (dataArray.length() == 0) {
                        Toast.makeText(SFAMonitoring.this, "No data for selected date", Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject salesRepData = dataArray.getJSONObject(i);
                        String name = salesRepData.getString("name");
                        String orderingCustomersCount = salesRepData.getString("ordering_customers_count");
                        String skippedCustomersCount = salesRepData.getString("skipped_customers_count");
                        String nonOrderingCustomersCount = salesRepData.getString("non_ordering_customers_count");
                        String id = salesRepData.getString("id");

                        String groupTitle = name + " - " + orderingCustomersCount + ", " + skippedCustomersCount + ", " + nonOrderingCustomersCount;
                        groupTitles.add(groupTitle);

                        ArrayList<String> childData = new ArrayList<>();

                        StringRequest stringRequest = getStringRequest(id, childData, name, JSON_URL, selectedDate);
                        RequestQueue requestQueue = Volley.newRequestQueue(SFAMonitoring.this);
                        requestQueue.add(stringRequest);

                        childItems.add(childData);
                    }

                    ExpandableListAdapter adapter = new ExpandableListAdapter(SFAMonitoring.this, groupTitles, childItems);
                    expandableListView.setAdapter((android.widget.ExpandableListAdapter) null); // Clear previous adapter
                    expandableListView.setAdapter(adapter);

                    Toast.makeText(SFAMonitoring.this, "Data loaded for " + selectedDate, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SFAMonitoring.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @NonNull
    private StringRequest getStringRequest(String id, ArrayList<String> childData, String name, String JSON_URL, String selectedDate) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL +"/SFA_SALESREP_LIST_DETAILS.php?selectedDate=" + selectedDate +"&sales_rep_id="+ id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray so_details_array = obj.getJSONArray("data");
                            for (int i = 0 ; i < so_details_array.length(); i++){
                                JSONObject jsonObject = so_details_array.getJSONObject(i);
                                childData.add(jsonObject.getString("so")+",\nLOCATION: "+jsonObject.getString("location")+",\nNAME: "+jsonObject.getString("customer_name")+",\nAMT: "+jsonObject.getString("amount"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        } finally {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SFAMonitoring.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();
                // Hide the progress bar
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SFAMonitoring.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        historyDatepicker.setText(formattedDate);
                        fetchData(formattedDate,JSON_URL);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    public static class ExpandableListAdapter extends android.widget.BaseExpandableListAdapter {

        private final ArrayList<String> groupTitles;
        private final ArrayList<ArrayList<String>> childItems;
        private final LayoutInflater inflater;

        public ExpandableListAdapter(SFAMonitoring context, ArrayList<String> groupTitles, ArrayList<ArrayList<String>> childItems) {
            this.groupTitles = groupTitles;
            this.childItems = childItems;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return groupTitles.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childItems.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupTitles.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childItems.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }

            TextView textView = convertView.findViewById(android.R.id.text1);
            String groupTitle = groupTitles.get(groupPosition);

            String[] parts = groupTitle.split(" - ");
            String name = parts[0];
            String[] counts = parts[1].split(", ");
            String orderingCustomersCount = counts[0];
            String skippedCustomersCount = counts[1];
            String nonOrderingCustomersCount = counts[2];

            String finalGroupTitle = name + " - "
                    + "<font color='#008000'>" + orderingCustomersCount + "</font>, "
                    + "<font color='#FF0000'>" + skippedCustomersCount + "</font>, "
                    + "<font color='#0000FF'>" + nonOrderingCustomersCount + "</font>";

            textView.setText(android.text.Html.fromHtml(finalGroupTitle));
            textView.setTypeface(Typeface.MONOSPACE);
            textView.setGravity(Gravity.START);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.WHITE);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TextView textView = convertView.findViewById(android.R.id.text1);
            String childText = childItems.get(groupPosition).get(childPosition);
            textView.setText(childText);
            textView.setTextColor(Color.parseColor("#006400"));
            if(childText.contains("SKIPPED -")){
                textView.setTextColor(Color.parseColor("#940a0f"));
            }
            if(childText.contains("UNVISITED -")){
                textView.setTextColor(Color.parseColor("#002278"));
            }
            convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
