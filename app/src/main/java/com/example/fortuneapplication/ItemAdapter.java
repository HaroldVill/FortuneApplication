package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>{

    private List<Item>itemList;
    private Context context;

    public ItemAdapter(List<Item>itemList, Context context){

        super(context,R.layout.recycleitem,itemList);
        this.itemList = itemList;
        this.context= context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewItem = inflater.inflate(R.layout.recycleitem, null, true);

        TextView des1 = listViewItem.findViewById(R.id.l1);
        TextView des2 = listViewItem.findViewById(R.id.l2);
        TextView des3 = listViewItem.findViewById(R.id.l3);
        TextView des4 = listViewItem.findViewById(R.id.l4);
        TextView jam  = listViewItem.findViewById(R.id.jam);

        Item item = itemList.get(position);

        des1.setText(item.getCode());
        des2.setText(item.getDescription());
        des3.setText(item.getRate());
        des4.setText(item.getQuantity());
        jam .setText(item.getGroup());

        return listViewItem;

        }
    }

