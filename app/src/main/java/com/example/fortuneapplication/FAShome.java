package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class FAShome extends AppCompatActivity {


    TextView icode,ides,irate,iquant,grp,coordinates;

    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn,generate_valuation,generate_open_orders;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;
    final Calendar history_calendar= Calendar.getInstance();
    final Calendar history_calendar_to= Calendar.getInstance();
    EditText date_from,date_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashome);
        icode = findViewById(R.id.icode);
        ides = findViewById(R.id.ides);
        irate = findViewById(R.id.irate);
        iquant = findViewById(R.id.iquant);
        grp = findViewById(R.id.grp);
        date_from = findViewById(R.id.date_from);
        date_to = findViewById(R.id.date_to);
        generate_valuation = findViewById(R.id.generate_valuation);
        generate_open_orders = findViewById(R.id.generate_open_orders);
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

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                history_calendar.set(Calendar.YEAR, year);
                history_calendar.set(Calendar.MONTH,month);
                history_calendar.set(Calendar.DAY_OF_MONTH,day);
                update_datefrom();
                Log.d("check_date_changed", date_from.getText().toString());
//                populate_table(date_from.getText().toString());
            }
        };

        DatePickerDialog.OnDateSetListener dialog_date_to =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                history_calendar_to.set(Calendar.YEAR, year);
                history_calendar_to.set(Calendar.MONTH,month);
                history_calendar_to.set(Calendar.DAY_OF_MONTH,day);
                update_date_to();
                Log.d("check_date_changed", date_to.getText().toString());
//                populate_table(date_from.getText().toString());
            }
        };
        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FAShome.this,date,history_calendar.get(Calendar.YEAR),history_calendar.get(Calendar.MONTH),history_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FAShome.this,dialog_date_to,history_calendar_to.get(Calendar.YEAR),history_calendar_to.get(Calendar.MONTH),history_calendar_to.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        generate_valuation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_valuation_breakdown  = new Intent(FAShome.this,ItemValuationDetail.class);
                open_valuation_breakdown.putExtra("code",icode.getText().toString());
                open_valuation_breakdown.putExtra("description",ides.getText().toString());
                open_valuation_breakdown.putExtra("date_from",date_from.getText().toString());
                open_valuation_breakdown.putExtra("date_to",date_to.getText().toString());
                startActivity(open_valuation_breakdown);
            }
        });

        generate_open_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_valuation_breakdown  = new Intent(FAShome.this,OpenSalesOrder.class);
                open_valuation_breakdown.putExtra("code",icode.getText().toString());
                open_valuation_breakdown.putExtra("description",ides.getText().toString());
                open_valuation_breakdown.putExtra("date_from",date_from.getText().toString());
                open_valuation_breakdown.putExtra("date_to",date_to.getText().toString());
                startActivity(open_valuation_breakdown);
            }
        });


    }
    private void update_datefrom(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.TAIWAN);
        date_from.setText(dateFormat.format(history_calendar.getTime()));
    }
    private void update_date_to(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.TAIWAN);
        date_to.setText(dateFormat.format(history_calendar_to.getTime()));
    }


}
