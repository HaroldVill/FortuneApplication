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

public class PlvlinesAdapter extends ArrayAdapter<PlevelLines_list> {

    private List<PlevelLines_list>plevelLines_lists;
    private Context context;

    public PlvlinesAdapter(List<PlevelLines_list> plevelLines_lists, Context context) {
        super(context, R.layout.plvllinesdesign, plevelLines_lists);
        this.plevelLines_lists = plevelLines_lists;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View lvlp = inflater.inflate(R.layout.plvllinesdesign, null, true);


        TextView m1 = lvlp.findViewById(R.id.y1);
        TextView m2 = lvlp.findViewById(R.id.y2);
        TextView m3 = lvlp.findViewById(R.id.y3);
        TextView m4 = lvlp.findViewById(R.id.y4);
        PlevelLines_list plvl = plevelLines_lists.get(position);



        m1.setText(plvl.getId());
        m2.setText(plvl.getPricelvl_id());
        m3.setText(plvl.getItem_id());
        m4.setText(plvl.getCustomprice());
        return lvlp;

    }
}
