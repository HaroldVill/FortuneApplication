package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CoverageAdapter extends ArrayAdapter<Coverage> {

    private List<Coverage> coverageList;
    private Context context;


    public CoverageAdapter(List<Coverage> coverageList, Context context) {

        super(context, R.layout.coveragerecycle,coverageList);
        this.coverageList = coverageList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewCoverage = inflater.inflate(R.layout.coveragerecycle, null, true);

        TextView w1 = listViewCoverage.findViewById(R.id.coverage_id);
        TextView w2 = listViewCoverage.findViewById(R.id.customer_id);
        TextView w3 = listViewCoverage.findViewById(R.id.sales_rep_id);
        TextView w4 = listViewCoverage.findViewById(R.id.coverage_day);
        TextView w5 = listViewCoverage.findViewById(R.id.frequency);
        TextView w6 = listViewCoverage.findViewById(R.id.frequency_week_schedule);

        Coverage coverage = coverageList.get(position);

        w1.setText(coverage.get_id());
        w2.setText(coverage.get_customer_id());
        w3.setText(coverage.get_salesrep_id());
        w4.setText(coverage.get_day());
        w5.setText(coverage.get_frequency());
        w6.setText(coverage.get_frequency_week_schedule());


        return listViewCoverage;


    }

}
