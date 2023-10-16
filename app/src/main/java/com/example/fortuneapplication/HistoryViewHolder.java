package com.example.fortuneapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    TextView sam1,sam2,sam3,sam4;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        sam1 =itemView.findViewById(R.id.sam1);
        sam2 =itemView.findViewById(R.id.sam2);
        sam3 =itemView.findViewById(R.id.sam3);
        sam4 =itemView.findViewById(R.id.sam4);
    }
}
