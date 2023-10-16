package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ADisplayItemAdapter extends RecyclerView.Adapter<ADisplayItemAdapter.MyViewHolder> {
    private Context context;
    private final List<Item2> itemList;
    private PazDatabaseHelper database;

    public ADisplayItemAdapter(Context context, List<Item2> itemList, PazDatabaseHelper database) {
        this.context = context;
        this.itemList = itemList;
        this.database = database;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adddisplayitemdesign, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Item2 item2 = itemList.get(position);
        holder.fb1.setText(item2.getCode());
        holder.fb2.setText(item2.getDescription());
        holder.fb3.setText(item2.getPrice() + "/" + item2.getUnitmeasure() + "(" + item2.getUnitbase() + ")");
        //holder.fb3.setText(item2.getPrice()+ " / " +item2.getUnitmeasure() + ""+(item2.getUnitbase()));
        holder.fb4.setText(item2.getQuantity());
       //holder.samok.setText(item2.getUnitmeasure());
        holder.fb5.setText(String.valueOf((double) item2.getTotal()));

        holder.delitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();

                if (position >= 0 && position < itemList.size()) {
                    Item2 item = itemList.get(position);
                    // Get the item code
                    String code = item.getCode();

                    // Call the deleteItem method from the PazDatabaseHelper
                    if (database != null) {
                        database.deleteItem(code);

                        // Update the total payable
                        double updatedTotal = database.getTotalPayable();
                        // Call the updateTotalPayable method in the MainActivity
                        if (context instanceof SOActivity) {
                            ((SOActivity) context).updateTotalPayable(updatedTotal);
                        }
                    }
                    // Remove the item from the list and update the adapter
                    itemList.remove(position);
                    notifyItemRemoved(position);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView fb1,fb2,fb3,fb4,fb5,delitem,samok;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fb1 = itemView.findViewById(R.id.fb1);
            fb2 = itemView.findViewById(R.id.fb2);
            fb3 = itemView.findViewById(R.id.fb3);
            fb4 = itemView.findViewById(R.id.fb4);
            fb5 = itemView.findViewById(R.id.fb5);
            delitem = itemView.findViewById(R.id.delitem);
          //  samok = itemView.findViewById(R.id.samok);


        }
    }

}
