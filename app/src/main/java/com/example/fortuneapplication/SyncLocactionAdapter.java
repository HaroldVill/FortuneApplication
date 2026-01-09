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

public class SyncLocactionAdapter extends ArrayAdapter<Locationing> {

    private List<Locationing> locations;
    private Context context;

    public SyncLocactionAdapter(List<Locationing> locations, Context context) {
        super(context, R.layout.locationdesign, locations);
        this.locations = locations;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listlocation = inflater.inflate(R.layout.locationdesign, null, true);


        TextView w1 = listlocation.findViewById(R.id.locid);
        TextView w2 = listlocation.findViewById(R.id.locname);

        Locationing location = locations.get(position);

        w1.setText(location.getLocid());
        w2.setText(location.getLocname());

        return listlocation;
    }


}
