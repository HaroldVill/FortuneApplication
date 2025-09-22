package com.example.fortuneapplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SlectItemAdapter extends RecyclerView.Adapter<SlectItemAdapter.MyViewHolder> {
    private ArrayList<Item> itemLista;
    private Context context;
    private Comparator<Item> currentComparator;
    String customer_id;

    public SlectItemAdapter(ArrayList<Item> itemLista, Context context,String customer_id) {
        this.itemLista = itemLista;
        this.context = context;
        this.customer_id = customer_id;
    }
    public void setFilterdList(List<Item> filterdList){
        this.itemLista = (ArrayList<Item>) filterdList;
        notifyDataSetChanged();
    }


    //sort data//
    public void sortData(Comparator<Item> comparator) {
        if (currentComparator == comparator) {
            Collections.reverse(itemLista);
        } else {
            Collections.sort(itemLista, comparator);
            currentComparator = comparator;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlectItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleitem2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlectItemAdapter.MyViewHolder holder, int position) {
        Item item = itemLista.get(position);

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
        holder.it6.setEnabled(true);
      //  FloatingActionButton floatt = holder.itemView.findViewById(R.id.floatt);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tra = new Intent(context, InputQuantity.class);
                SharedPreferences preferences = context.getSharedPreferences("MyItems", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("ITEMIO", item.getId());
                editor.putString("ICODE", item.getCode());
                editor.putString("IDESCRIPTION", item.getDescription());
                editor.putString("IRATE", item.getRate());
                editor.putString("IQUANT", item.getQuantity());
                editor.putString("pcs", item.getUnitquant());
                editor.putString("LVL", item.getNewPriceLvl().getPdescription());

                editor.putString("DI", item.getUnit().getUnit_id());
                editor.putString("UNITM", item.getUnit().getQuantity());
                editor.putString("name", item.getUnit().getName());
                editor.putString("PLVL", item.getNewPriceLvl().getPid());
                editor.putString("customer_id", customer_id);

                editor.apply();
                context.startActivity(tra);

            }
        });

        holder.it6.setOnClickListener( view -> {
            Intent approveIntent = new Intent(context, ApprovalPage.class);
            approveIntent.putExtra("ITEM_DESCRIPTION", item.getDescription());
            approveIntent.putExtra("ITEM_ID", item.getId());
            context.startActivity(approveIntent);
        });

//        holder.floatt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent tras = new Intent(context, SOActivity.class);
//                context.startActivity(tras);
//            }
//        });

    }

//        Item item = itemLista.get(position);
//        holder.it1.setText(item.getCode());
//        holder.it2.setText(item.getDescription());
//        holder.it3.setText(item.getRate()+ " / " + item.getUnitquant());
//       holder.it4.setText(item.getQuantity());
//        holder.it5.setText(item.getGroup());


    @Override
    public int getItemCount() {
        return itemLista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView it1,it2, it3,it4,it5, it6;
        FloatingActionButton floatt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            it1 = itemView.findViewById(R.id.it1);
            it2= itemView.findViewById(R.id.it2);
            it3 =itemView.findViewById(R.id.it3);
            it4 =itemView.findViewById(R.id.it4);
            it5 = itemView.findViewById(R.id.it5);
            it6 = itemView.findViewById(R.id.it6);
          // floatt = itemView.findViewById(R.id.floatt);
        }
    }
}
