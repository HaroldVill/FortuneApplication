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

public class LocationDisplayAdapter extends RecyclerView.Adapter<LocationDisplayAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Location> locations;

    public LocationDisplayAdapter(Context context, ArrayList<Location> locations) {
        this.context = context;
        this.locations = locations;
    }

    public void setFilterdList(List<Location> filterdList){
        this.locations = (ArrayList<Location>) filterdList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locationdesign, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.locid.setText(location.getLocid());
        holder.locname.setText(location.getLocname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lol = new Intent(context, SOActivity.class);
                SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("LOCID",location.getLocid());
                editor.putString("LOC",location.getLocname());
                editor.apply();
                context.startActivity(lol);


            }
        });

    }
    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView locid,locname;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            locid = itemView.findViewById(R.id.locid);
            locname = itemView.findViewById(R.id.locname);
        }
    }
}
