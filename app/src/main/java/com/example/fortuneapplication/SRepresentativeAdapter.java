package com.example.fortuneapplication;

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
import java.util.List;

public class SRepresentativeAdapter extends RecyclerView.Adapter<SRepresentativeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SalesRepList> salesRepLists;

    public SRepresentativeAdapter(Context context, ArrayList<SalesRepList> salesRepLists) {
        this.context = context;
        this.salesRepLists = salesRepLists;
    }
    public void setFilterdList(List<SalesRepList> filterdList){
        this.salesRepLists = (ArrayList<SalesRepList>) filterdList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.salesrepdesign, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SalesRepList salesRepList = salesRepLists.get(position);
        holder.sl1.setText(salesRepList.getSrid());
        holder.sl2.setText(salesRepList.getSrname());
        holder.sl3.setText(salesRepList.getSraddress());
        holder.sl4.setText(salesRepList.getSrmoble());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tras = new Intent(context, SOActivity.class);
                SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("SRID", salesRepList.getSrid());
                editor.putString("SRNAME", salesRepList.getSrname());
                editor.apply();
                context.startActivity(tras);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salesRepLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sl1,sl2,sl3,sl4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sl1 = itemView.findViewById(R.id.sl1);
            sl2 = itemView.findViewById(R.id.sl2);
            sl3 = itemView.findViewById(R.id.sl3);
            sl4 = itemView.findViewById(R.id.sl4);

        }
    }
}
