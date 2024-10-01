package com.example.fortuneapplication;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemValuationAdapter extends RecyclerView.Adapter<ItemValuationAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Item> itemList;
    public  ItemValuationAdapter(Context context, ArrayList<Item> itemList){
        this.context = context;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ItemValuationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_valuation_detail_adapter, parent, false);
        return new ItemValuationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemValuationAdapter.ViewHolder holder, int position) {
        Item get_item = itemList.get(position);
        Log.d("check_results", get_item.getId());
        holder.sam1.setText(get_item.getId());
        holder.sam2.setText(get_item.getCode());
        holder.sam3.setText(get_item.getDescription());
        holder.sam4.setText(get_item.getRate());
        holder.sam5.setText(get_item.getGroup());

    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sam1,sam2,sam3,sam4,sam5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sam1= itemView.findViewById(R.id.sam1);
            sam2 = itemView.findViewById(R.id.sam2);
            sam3 = itemView.findViewById(R.id.sam3);
            sam4 = itemView.findViewById(R.id.sam4);
            sam5 = itemView.findViewById(R.id.sam5);

        }
    }
}
