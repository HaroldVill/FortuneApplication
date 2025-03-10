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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SFAItemAdapter extends RecyclerView.Adapter<SFAItemAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Item> itemList;
    private Comparator<Item> currentComparator;

    public SFAItemAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }
    public void setFilterdList(List<Item> filterdList){
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sfaitemadapter, parent, false);
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
        holder.it1.setText(item.getCode());
        holder.it2.setText(item.getDescription());
        holder.it3.setText(formattedRate+ " / " + item.getUnitquant());
        holder.it4.setText(item.getQuantity());
        holder.it5.setText(item.getGroup());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent tra = new Intent(context, InputQuantity.class);
//                SharedPreferences preferences = context.getSharedPreferences("MyItems", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//
//                //editor.putString("IID", item.getId());
//
//                editor.putString("ICODE", item.getCode());
//                editor.putString("IDESCRIPTION", item.getDescription());
//                editor.putString("IRATE", item.getRate());
//                editor.putString("IQUANT", item.getQuantity());
//                editor.putString("pcs", item.getUnitquant());
//
//
//                editor.putString("DI", item.getUnit().getUnit_id());
//                editor.putString("UNITM", item.getUnit().getQuantity());
//                editor.putString("name", item.getUnit().getName());
//
//
//                editor.apply();
//                context.startActivity(tra);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView it1,it2,it3,it4,it5;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            it1 = itemView.findViewById(R.id.it1);
            it2 = itemView.findViewById(R.id.it2);
            it3 = itemView.findViewById(R.id.it3);
            it4 =itemView.findViewById(R.id.it4);
            it5 = itemView.findViewById(R.id.it5);


        }
    }
}
