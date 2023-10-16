package com.example.fortuneapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SoCustomerAdapter extends RecyclerView.Adapter<SoCustomerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Customer> customerList;
    private Comparator<Customer> currentComparator;

    public SoCustomerAdapter(Context context, ArrayList<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    public void setFilterdList(List<Customer> filterdList){
        this.customerList = (ArrayList<Customer>) filterdList;
        notifyDataSetChanged();
    }


    public void sortData(Comparator<Customer> comparator) {
        if (currentComparator == comparator) {
            Collections.reverse(customerList);
        } else {
            Collections.sort(customerList, comparator);
            currentComparator = comparator;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SoCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.socustomerdesign, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoCustomerAdapter.MyViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.so1.setText(customer.getId());
        holder.so2.setText(customer.getCustomername());
        holder.so3.setText(customer.getPostaladdress());
        holder.so4.setText(customer.getMobilenumber());
        holder.socontact.setText(customer.getContactperson());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tra = new Intent(context, SOActivity.class);
                SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CID",customer.getId());
                editor.putString("CNAME", customer.getCustomername());
                editor.putString("CCONTACT", customer.getMobilenumber());
                editor.putString("CADD", customer.getPostaladdress());
                editor.putString("prlvl", customer.getPricelevelid());
                editor.putString("DI", customer.getPaymenTerm().getDescription());
                editor.apply();
                context.startActivity(tra);
                ((Activity) context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView so1,so2,so3,so4,socontact;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            so1 = itemView.findViewById(R.id.so1);
            so2 = itemView.findViewById(R.id.so2);
            so3 = itemView.findViewById(R.id.so3);
            so4 = itemView.findViewById(R.id.so4);
            socontact = itemView.findViewById(R.id.socontact);


        }
    }
}
