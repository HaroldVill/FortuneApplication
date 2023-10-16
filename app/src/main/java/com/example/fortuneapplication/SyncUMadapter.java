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

public class SyncUMadapter extends ArrayAdapter<Unit> {

    private List<Unit>units;
    private Context context;

    public SyncUMadapter(List<Unit> units, Context context) {
        super(context, R.layout.syncumdesign, units);
        this.units = units;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View listm = inflater.inflate(R.layout.syncumdesign, null, true);


        TextView w1 = listm.findViewById(R.id.e1);
        TextView w2 = listm.findViewById(R.id.e2);
        TextView w3 = listm.findViewById(R.id.e3);
        TextView w4 =listm.findViewById(R.id.e4);
        Unit unit = units.get(position);


        w1.setText(unit.getItem_id());
        w2.setText(unit.getName());
        w3.setText(unit.getQuantity());
        w4.setText(unit.getUnit_id());

        return listm;




    }
}
