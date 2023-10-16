package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter2 extends ArrayAdapter<Item2> {

    private List<Item2> item2List2;
    private Context mmMctx;

    public ItemAdapter2(List<Item2> item2List2, Context mmMctx) {

        super(mmMctx, R.layout.recycleitem2, item2List2);
        this.item2List2 = item2List2;
        this.mmMctx = mmMctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(mmMctx);
        View listViewItem = inflater.inflate(R.layout.recycleitem2, null, true);
//
//        TextView sd1 = listViewItem.findViewById(R.id.sd1);
//        TextView sd2 = listViewItem.findViewById(R.id.sd2);
//        TextView sd3 = listViewItem.findViewById(R.id.sd3);
//        TextView sd4 = listViewItem.findViewById(R.id.sd4);
//
//        Item2 item2 = item2List2.get(position);
//
//        sd1.setText(item2.getId());
//        sd2.setText(item2.getCode());
//        sd3.setText(item2.getDescription());
//        sd4.setText(item2.getRate()+"");
//
        return listViewItem;
    }
}




