package com.example.fortuneapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemDIisplayAdapter extends RecyclerView.Adapter<ItemDIisplayAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Item> itemList;
    private Comparator<Item> currentComparator;
    public ItemDIisplayAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setFilterdList(List<Item>filterdList){
        this.itemList = (ArrayList<Item>) filterdList;
        notifyDataSetChanged();
    }

    public void sortData(Comparator<Item> comparator) {
        if (currentComparator == comparator) {
            Collections.reverse(itemList);
        } else {
            Collections.sort(itemList, comparator);
            currentComparator = comparator;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);

        String rateString = item.getRate();
        String formattedRate;
        if (rateString != null) {
            try {
                double rate = Double.parseDouble(rateString);
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                formattedRate = decimalFormat.format(rate);
            } catch (NumberFormatException e) {
                formattedRate = "NO PRICE";
            }
        } else {
            formattedRate = "N/A";
        }

        holder.l1.setText(item.getCode());
        holder.l2.setText(item.getDescription());
        holder.l3.setText(formattedRate + " / " +item.getUnitquant());
        holder.l4.setText(item.getQuantity() );
       holder.jam.setText(item.getGroup());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent transfer = new Intent(context, FAShome.class);
                transfer.putExtra("ITEM_CODE", item.getCode());
                transfer.putExtra("DESCRIPTION", item.getDescription());
                transfer.putExtra("RATE", item.getRate());
                transfer.putExtra("QUANTITY", item.getQuantity());
                transfer.putExtra("UOM", item.getUnitquant());
                transfer.putExtra("GROUP", item.getGroup());

                context.startActivity(transfer);
                }
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView l1,l2,l3,l4,jam;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            l1 = itemView.findViewById(R.id.l1);
            l2 = itemView.findViewById(R.id.l2);
            l3 = itemView.findViewById(R.id.l3);
            l4 = itemView.findViewById(R.id.l4);
            jam =itemView.findViewById(R.id.jam);

            }
    }

}

