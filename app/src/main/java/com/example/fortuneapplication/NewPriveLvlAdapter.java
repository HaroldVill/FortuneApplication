package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewPriveLvlAdapter extends ArrayAdapter<NewPriceLvl> {


    private List<NewPriceLvl> newPriceLvls;
    private Context context;


    public NewPriveLvlAdapter(List<NewPriceLvl> newPriceLvls, Context context) {
        super(context, R.layout.newpleveldesign, newPriceLvls);
        this.newPriceLvls = newPriceLvls;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View listm = inflater.inflate(R.layout.newpleveldesign, null, true);

        TextView l1 = listm.findViewById(R.id.y1);
         TextView l2 = listm.findViewById(R.id.y2);
           TextView l3 = listm.findViewById(R.id.y3);

        NewPriceLvl newPriceLvl = newPriceLvls.get(position);

        l1.setText(newPriceLvl.getPid());
        l2.setText(newPriceLvl.getPcode());
        l3.setText(newPriceLvl.getPdescription());

        return listm;

    }
}
