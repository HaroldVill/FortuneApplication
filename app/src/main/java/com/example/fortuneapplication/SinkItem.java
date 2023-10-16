package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SinkItem extends AppCompatActivity {

    CardView c22,c23,srrr,locc;
    ImageView backt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sink_item);

        c22 = findViewById(R.id.c22);
        c23 =findViewById(R.id.c23);
        srrr =findViewById(R.id.srrr);
        locc = findViewById(R.id.locc);
        backt = findViewById(R.id.backt);

        backt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jjj = new Intent(SinkItem.this,HomePage.class);
                startActivity(jjj);
            }
        });

        locc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ol = new Intent(SinkItem.this,SyncLocation.class);
                startActivity(ol);
            }
        });

        srrr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(SinkItem.this, "You have no permission", Toast.LENGTH_SHORT).show();
                Intent rs = new Intent(SinkItem.this,SyncSalesRep.class);
                startActivity(rs);
            }
        });

        c22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(SinkItem.this,SyncItemActivity.class);
                startActivity(ii);

            }
        });


        c23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent yy = new Intent(SinkItem.this,SyncCustomer.class);
                startActivity(yy);

            }
        });
    }

}