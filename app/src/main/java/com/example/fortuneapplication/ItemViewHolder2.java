package com.example.fortuneapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder2 extends RecyclerView.ViewHolder {
    TextView des1,des2,des3,des4;



    public ItemViewHolder2(@NonNull View itemView) {
        super(itemView);


        des1 = itemView.findViewById(R.id.des1);
        des2 = itemView.findViewById(R.id.des2);
        des3 = itemView.findViewById(R.id.des3);
        des4 = itemView.findViewById(R.id.des4);
    }
}
