package com.example.fortuneapplication;

import android.annotation.SuppressLint;
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
    String customer_id;

    public SFAItemAdapter(Context context, ArrayList<Item> itemList,String customer_id) {
        this.context = context;
        this.itemList = itemList;
        this.customer_id = customer_id;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);



        holder.it2.setText(item.getDescription());
        holder.it3.setText(item.getRate() +" / "+item.getUnitquant());
        holder.it4.setText(item.getQuantity());
        holder.it5.setText(item.getGroup());
        holder.it6.setText(item.getWsr());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tra = new Intent(context, SFAInputQuantity.class);
                SharedPreferences preferences = context.getSharedPreferences("MyItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("ITEMIO", item.getId());
                editor.putString("ICODE", item.getCode());
                editor.putString("IDESCRIPTION", item.getDescription());
                editor.putString("IRATE", item.getRate());
                editor.putString("IQUANT", item.getQuantity());
                editor.putString("pcs", item.getUnitquant());
                editor.putString("LVL", item.getNewPriceLvl().getPdescription());
                editor.putString("wsr",item.getWsr());
                editor.putString("DI", item.getUnit().getUnit_id());
                editor.putString("UNITM", item.getUnit().getQuantity());
                editor.putString("name", item.getUnit().getName());
                editor.putString("PLVL", item.getNewPriceLvl().getPid());
                editor.putString("customer_id", customer_id);

                editor.apply();
                context.startActivity(tra);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView it1,it2,it3,it4,it5,it6;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            it1 = itemView.findViewById(R.id.it1);
            it2 = itemView.findViewById(R.id.it2);
            it3 = itemView.findViewById(R.id.it3);
            it4 =itemView.findViewById(R.id.it4);
            it5 = itemView.findViewById(R.id.it5);
            it6 = itemView.findViewById(R.id.it6);


        }
    }
}
