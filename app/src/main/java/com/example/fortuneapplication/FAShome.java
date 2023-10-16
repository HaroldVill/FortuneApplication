package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FAShome extends AppCompatActivity {


    TextView icode,ides,irate,iquant,grp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashome);
        icode = findViewById(R.id.icode);
        ides = findViewById(R.id.ides);
        irate = findViewById(R.id.irate);
        iquant = findViewById(R.id.iquant);
        grp = findViewById(R.id.grp);



        Intent intent = getIntent();
        String itemCode = intent.getStringExtra("ITEM_CODE");
        String itemdes = intent.getStringExtra("DESCRIPTION");
        String itemrate = intent.getStringExtra("RATE");
        String itemquant = intent.getStringExtra("QUANTITY");
        String itemuom = intent.getStringExtra("UOM");
        String itemgrp = intent.getStringExtra("GROUP");



        //String itemqua = intent.getStringExtra("QUANTITY");

        icode.setText(itemCode);
        ides.setText(itemdes);
        irate.setText(itemrate+" "+itemuom);
        iquant.setText(itemquant);
        grp.setText(itemgrp);




    }
}
