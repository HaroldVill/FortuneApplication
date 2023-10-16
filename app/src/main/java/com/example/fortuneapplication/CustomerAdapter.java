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

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private List<Customer> customerList;
    private Context context;


    public CustomerAdapter(List<Customer> customerList, Context context) {

        super(context, R.layout.customerrecycle, customerList);
        this.customerList = customerList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewCustomer = inflater.inflate(R.layout.customerrecycle, null, true);

        TextView w1 = listViewCustomer.findViewById(R.id.cid);
        TextView w2 = listViewCustomer.findViewById(R.id.cname);
        TextView w3 = listViewCustomer.findViewById(R.id.caddre);
        TextView w4 = listViewCustomer.findViewById(R.id.cphone);

       Customer customer = customerList.get(position);

        w1.setText(customer.getId());
        w2.setText(customer.getCustomername());
        w3.setText(customer.getPostaladdress());
        w4.setText(customer.getMobilenumber());


        return listViewCustomer;


    }

}
