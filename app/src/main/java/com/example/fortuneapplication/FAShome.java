package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
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


    TextView icode,ides,irate,iquant,grp,coordinates;

    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashome);
        icode = findViewById(R.id.icode);
        ides = findViewById(R.id.ides);
        irate = findViewById(R.id.irate);
        iquant = findViewById(R.id.iquant);
        grp = findViewById(R.id.grp);
//        coordinates = findViewById(R.id.coordinates);



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

        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps

//            OnGPS();
        }
        else
        {
            //GPS is already On then

//            GetLocation gl= new GetLocation();
//            String coordinates=gl.get_location();
        }


    }
}
