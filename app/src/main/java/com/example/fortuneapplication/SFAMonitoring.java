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
    private ProgressBar progressBar; // ProgressBar for loading indication
    private EditText historyDatepicker; // The EditText for date picker functionality
    private ArrayList<String> groupTitles = new ArrayList<>();  // This will hold the names + counts for the sales reps (group titles)
    private ArrayList<ArrayList<String>> childItems = new ArrayList<>();  // This will hold the details for each sales rep (child items)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_monitoring);  // Connects the XML layout to the Activity

        expandableListView = findViewById(R.id.expandableListView);
        progressBar = findViewById(R.id.progressBar);  // Initialize ProgressBar
        historyDatepicker = findViewById(R.id.history_datepicker);  // Initialize the EditText for date picker

        // Set up the date picker when the EditText is clicked
        historyDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Fetch data from the URL to populate groupTitles and childItems
        fetchData();
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchData() {
        // Show progress bar before starting the network operation
        progressBar.setVisibility(View.VISIBLE);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String datepicker = historyDatepicker.getText().toString();
                    URL url = new URL("http://100.111.39.128/mobileapi/SFA_SALESREP_LIST.php?date_picker="+datepicker);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(15000);  // Timeout for connection
                    urlConnection.setReadTimeout(15000);     // Timeout for reading data

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
                        return "Error: " + responseCode;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                // Hide progress bar after data is fetched
                progressBar.setVisibility(View.GONE);

                if (result != null) {
                    if (result.startsWith("Error:")) {
                        Toast.makeText(SFAMonitoring.this, "Error fetching data: " + result, Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(result);
                            JSONArray dataArray = jsonResponse.getJSONArray("data");

                            // Clear the existing data
                            groupTitles.clear();
                            childItems.clear();

                            // Populate the groupTitles and childItems with the data from the JSON
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject salesRepData = dataArray.getJSONObject(i);
                                String name = salesRepData.getString("name");
                                String orderingCustomersCount = salesRepData.getString("ordering_customers_count");
                                String skippedCustomersCount = salesRepData.getString("skipped_customers_count");
                                String nonOrderingCustomersCount = salesRepData.getString("non_ordering_customers_count");

                                // Combine name and customer counts into one string for group title
                                String groupTitle = name + " - " + orderingCustomersCount
                                        + ", " + skippedCustomersCount
                                        + ", " + nonOrderingCustomersCount;

                                // Add the combined string as the group title
                                groupTitles.add(groupTitle);

                                // Prepare child data (if any)
                                ArrayList<String> childData = new ArrayList<>();
                                childData.add("Details for " + name);  // This can be expanded if more info is needed

                                // Add the child data for this group
                                childItems.add(childData);
                            }

                            // Set the adapter for the ExpandableListView
                            ExpandableListAdapter adapter = new ExpandableListAdapter(SFAMonitoring.this, groupTitles, childItems);
                            expandableListView.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(SFAMonitoring.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(SFAMonitoring.this, "Error: No response from server", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    // Method to show the DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SFAMonitoring.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format the selected date to "YYYY-MM-DD"
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Format the date to a string
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        // Set the selected date in the EditText
                        historyDatepicker.setText(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR), // Set the current year
                calendar.get(Calendar.MONTH), // Set the current month
                calendar.get(Calendar.DAY_OF_MONTH) // Set the current day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Custom ExpandableListAdapter to display the data
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

            // Split the group title to get the name and customer counts
            String[] parts = groupTitle.split(" - ");
            String name = parts[0];
            String[] counts = parts[1].split(", ");
            String orderingCustomersCount = counts[0];
            String skippedCustomersCount = counts[1];
            String nonOrderingCustomersCount = counts[2];

            // Apply the green color to the orderingCustomersCount
            String finalGroupTitle = name + " - "
                    + "<font color='#008000'>" + orderingCustomersCount + "</font>" + ", "
                    + "<font color='#FF0000'>" + skippedCustomersCount + "</font>" + ", "
                    + "<font color='#0000FF'>" + nonOrderingCustomersCount + "</font>";

            // Set the text to the TextView with HTML tags for the color
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

            // Get the child data
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
