package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SalesRepAdapter extends ArrayAdapter<SalesRepList> {

    private List<SalesRepList> salesRepLists;
    private Context context;

    public SalesRepAdapter(List<SalesRepList> salesRepLists, Context context) {
        super(context, R.layout.salesrepdesign, salesRepLists);
        this.salesRepLists = salesRepLists;
        this.context = context;

}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewCustomer = inflater.inflate(R.layout.salesrepdesign, null, true);

        TextView w1 = listViewCustomer.findViewById(R.id.sl1);
        TextView w2 = listViewCustomer.findViewById(R.id.sl2);
        TextView w3 = listViewCustomer.findViewById(R.id.sl3);
        TextView w4 = listViewCustomer.findViewById(R.id.sl4);


        SalesRepList salesRepList= salesRepLists.get(position);

        w1.setText(salesRepList.getSrid());
        w2.setText(salesRepList.getSrname());
        w3.setText(salesRepList.getSraddress());
        w4.setText(salesRepList.getSrmoble());

        return listViewCustomer;


    }
}
