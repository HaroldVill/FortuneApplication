package com.example.fortuneapplication;

import android.content.Context;
import android.content.Intent;
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

public class CustomerDisplayAdapter extends RecyclerView.Adapter< CustomerDisplayAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Customer>customerList;
    private Comparator<Customer> currentComparator;


    public CustomerDisplayAdapter(Context context, ArrayList<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }


    public void setFilterdList(List<Customer>filterdList){
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerdisplaylist, parent, false);
       return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.h1.setText(customer.getId());
        holder.h2.setText(customer.getCustomername());
        holder.h3.setText(customer.getPostaladdress());
        holder.h4.setText(customer.getMobilenumber());
        holder.h5.setText(customer.getContactperson());
        PazDatabaseHelper dbhelper = new PazDatabaseHelper(context);
        holder.lx.setText(dbhelper.get_customer_longitude(Integer.parseInt(customer.getId())));
        holder.ly.setText(dbhelper.get_customer_latitude(Integer.parseInt(customer.getId())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer = new Intent(context, DisplayCustomer.class);
                transfer.putExtra("ID", customer.getId());
                transfer.putExtra("CNAME", customer.getCustomername());
                transfer.putExtra("CADD", customer.getPostaladdress());
                transfer.putExtra("CPHONE", customer.getMobilenumber());
                transfer.putExtra("CPERSON", customer.getContactperson());
                context.startActivity(transfer);
            }
        });
    }
    @Override
    public int getItemCount() {
        return customerList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView h1,h2,h3,h4,h5,lx,ly;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            h1 = itemView.findViewById(R.id.h1);
            h2 = itemView.findViewById(R.id.h2);
            h3 = itemView.findViewById(R.id.h3);
            h4 = itemView.findViewById(R.id.h4);
            h5 = itemView.findViewById(R.id.h5);
            lx = itemView.findViewById(R.id.lx);
            ly = itemView.findViewById(R.id.ly);

        }
    }
}
