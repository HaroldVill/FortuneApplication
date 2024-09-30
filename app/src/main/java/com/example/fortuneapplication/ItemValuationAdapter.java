package com.example.fortuneapplication;
import android.content.Context;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemValuationAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sam1,sam2,sam3,sam4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sam1= itemView.findViewById(R.id.sam1);
            sam2 = itemView.findViewById(R.id.sam2);
            sam3 = itemView.findViewById(R.id.sam3);
            sam4 = itemView.findViewById(R.id.sam4);

        }
    }
}
