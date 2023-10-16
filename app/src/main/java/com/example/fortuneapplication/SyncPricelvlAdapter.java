package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ConcurrentModificationException;
import java.util.List;

public class SyncPricelvlAdapter extends ArrayAdapter<PriceLvl> {

    private List<PriceLvl> priceLvlList;
    private Context context;


    public SyncPricelvlAdapter(List<PriceLvl> priceLvlList, Context context) {
        super(context, R.layout.syncpricellvl, priceLvlList);
        this.priceLvlList = priceLvlList;
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View listpayterm = inflater.inflate(R.layout.syncpricellvl, null, true);

        TextView t1 = listpayterm.findViewById(R.id.pl1);
        TextView t2 = listpayterm.findViewById(R.id.pl2);
        TextView t3 = listpayterm.findViewById(R.id.pl3);
        TextView t4 = listpayterm.findViewById(R.id.pl4);
        TextView t5 = listpayterm.findViewById(R.id.pl5);


        PriceLvl priceLvl = priceLvlList.get(position);


        t1.setText(priceLvl.getPricelvl_id());
        t2.setText(priceLvl.getItem_id());
        t3.setText(priceLvl.getCustom_price());
        t4.setText(priceLvl.getCode());
        t5.setText(priceLvl.getDescription());
        return listpayterm;


    }

    }
