package com.example.fortuneapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProfDataAdapter extends RecyclerView.Adapter<ProfDataAdapter.MyViewHolder> {
    private ArrayList<Dashboardne>daslist;
    private Context context;

    public ProfDataAdapter(ArrayList<Dashboardne> daslist, Context context) {
        this.daslist = daslist;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.designmen, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfDataAdapter.MyViewHolder holder, int position) {
        Dashboardne dashboardne = daslist.get(position);
        String formattedTotal = String.valueOf(dashboardne.getDtotal());
        holder.p1.setText(dashboardne.getDcode());
        holder.p2.setText(dashboardne.getDcustomername());
        holder.p3.setText(formattedTotal);
        holder.p4.setText(dashboardne.getDdate());
    }

    @Override
    public int getItemCount() {
        return daslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView p1, p2, p3,p4;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            p1 = itemView.findViewById(R.id.p1);
            p2 = itemView.findViewById(R.id.p2);
            p3 = itemView.findViewById(R.id.p3);
            p4= itemView.findViewById(R.id.p4);

        }
    }
}
