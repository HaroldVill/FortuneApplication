package com.example.fortuneapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class SFAMonitoring extends AppCompatActivity {

    // Declare views
    private TextView titleTextView;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_monitoring);  // Connects the XML layout to the Activity

        // Initialize the views
        titleTextView = findViewById(R.id.title);
        expandableListView = findViewById(R.id.expandableListView);

        // Set title text (this is optional if you want to change the text programmatically)
        titleTextView.setText("SFA Monitoring");

        // Set up the ExpandableListView (you can use your own adapter and data here)
        setUpExpandableListView();
    }

    // Method to set up the ExpandableListView (for demonstration purposes, using a dummy adapter)
    private void setUpExpandableListView() {
        String[] groupTitles = {"VALUE 1", "VALUE 2", "VALUE 3", "VALUE 4", "VALUE 5"};
        String[][] childItems = {
                {"VAL 1", "VAL 2", "VAL 3"},
                {"VAL 1", "VAL 2", "VAL 3"},
                {"VAL 1", "VAL 2", "VAL 3"},
                {"VAL 1", "VAL 2", "VAL 3"},
                {"VAL 1", "VAL 2", "VAL 3"}
        };

        // Create and set the adapter for the ExpandableListView
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, groupTitles, childItems);
        expandableListView.setAdapter(adapter);
    }

    // ExpandableListAdapter class inside the Activity
    public static class ExpandableListAdapter extends android.widget.BaseExpandableListAdapter {

        private final String[] groupTitles;
        private final String[][] childItems;
        private final LayoutInflater inflater;

        public ExpandableListAdapter(SFAMonitoring context, String[] groupTitles, String[][] childItems) {
            this.groupTitles = groupTitles;
            this.childItems = childItems;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return groupTitles.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childItems[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupTitles[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childItems[groupPosition][childPosition];
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
            textView.setText(groupTitles[groupPosition]);

            textView.setTextColor(Color.WHITE);
            convertView.setBackgroundColor(Color.BLUE);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            TextView textView = convertView.findViewById(android.R.id.text1);

            // Adding bullet (•) before the child item text
            String bullet = "\u2022 ";  // Unicode for bullet
            String childText = childItems[groupPosition][childPosition];

            // Set the text with bullet point
            textView.setText(bullet + childText);
            textView.setTextColor(Color.BLACK);
            convertView.setBackgroundColor(Color.GREEN);

            convertView.setClickable(true);

            convertView.setOnClickListener(v -> {

                String clickedChild = childItems[groupPosition][childPosition];

                Toast.makeText(v.getContext(), "Clicked on: " + clickedChild, Toast.LENGTH_SHORT).show();
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
