package com.example.fortuneapplication;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView sd1,sd2,sd3,sd4;
    CardView cards;


    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        cards = itemView.findViewById(R.id.cards);
//        sd1 = itemView.findViewById(R.id.sd1);
//        sd2 = itemView.findViewById(R.id.sd2);
//        sd3 = itemView.findViewById(R.id.sd3);
//        sd4 = itemView.findViewById(R.id.sd4);



    }


}
