package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_monitoring);

        expandableListView = findViewById(R.id.expandableListView);
        progressBar = findViewById(R.id.progressBar);
        historyDatepicker = findViewById(R.id.history_datepicker);

        setCurrentDate();

        historyDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        fetchData(getCurrentDate());
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
    private void fetchData(String selectedDate) {
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String trimmedDate = selectedDate.trim();
                    String urlString = "http://100.111.39.128/mobileapi/SFA_SALESREP_LIST.php?selectedDate=" + trimmedDate;
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
                progressBar.setVisibility(View.GONE);

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

                        String groupTitle = name + " - " + orderingCustomersCount + ", " + skippedCustomersCount + ", " + nonOrderingCustomersCount;
                        groupTitles.add(groupTitle);

                        ArrayList<String> childData = new ArrayList<>();
                        childData.add("Details for " + name);
                        childItems.add(childData);
                    }

                    ExpandableListAdapter adapter = new ExpandableListAdapter(SFAMonitoring.this, groupTitles, childItems);
                    expandableListView.setAdapter((android.widget.ExpandableListAdapter) null); // Clear previous adapter
                    expandableListView.setAdapter(adapter);

                    Toast.makeText(SFAMonitoring.this, "Data loaded for " + selectedDate, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SFAMonitoring.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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
                        fetchData(formattedDate);
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
            textView.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.LTGRAY);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
