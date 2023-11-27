package com.example.fortuneapplication;
import com.example.fortuneapplication.SALESORDERITEMS;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class principal_adapter extends RecyclerView.Adapter<principal_adapter.ViewHolder> {
    private Context context;
    private ArrayList<SALESORDER> get_principal_performance;
    private PazDatabaseHelper mDatabaseHelper;

    public principal_adapter(Context context,ArrayList<SALESORDER> get_principal_performance){
        this.context = context;
        this.get_principal_performance = get_principal_performance;
    }


    @NonNull
    @Override
    public principal_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.principal_layout, parent, false);
        return new principal_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull principal_adapter.ViewHolder holder, int position) {
        SALESORDER get_principal = get_principal_performance.get(position);
        Log.d("check_results", get_principal.get_item_group());
        holder.sam1.setText(get_principal.get_item_group());
        holder.sam2.setText(get_principal.getAmount());


    }
    @Override
    public int getItemCount() {
        return get_principal_performance.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sam1,sam2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sam1= itemView.findViewById(R.id.sam1);
            sam2 = itemView.findViewById(R.id.sam2);

        }
    }
}
