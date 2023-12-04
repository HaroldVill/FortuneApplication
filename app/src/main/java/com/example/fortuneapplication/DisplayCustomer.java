package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayCustomer extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    TextView u1,u2,u3,u4,u5,Longitude,Latitude;
    Button pin;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer);

        u1 = findViewById(R.id.u1);
        u2 = findViewById(R.id.u2);
        u3 = findViewById(R.id.u3);
        u4 = findViewById(R.id.u4);
        u5 = findViewById(R.id.u5);
        Longitude.findViewById(R.id.Longitude);
        Latitude.findViewById(R.id.Latitude);


        Intent intent = getIntent();
        String cid = intent.getStringExtra("ID");
        String cname = intent.getStringExtra("CNAME");
        String cadd = intent.getStringExtra("CADD");
        String cphone= intent.getStringExtra("CPHONE");
        String cperson= intent.getStringExtra("CPERSON");



        u1.setText(cid);
        u2.setText(cname);
        u3.setText(cadd);
        u4.setText(cphone);
        u5.setText(cperson);

//        pin = findViewById(R.id.save_coordinates);

//        pin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Longitude.setText(getLongitude());
//                        Latitude.setText(getLatitude());
//                    }
//                });
//            }
//        });
    }
    private String getLatitude() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(DisplayCustomer.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DisplayCustomer.this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            android.location.Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            android.location.Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }
        return (latitude);
    }

    private String getLongitude() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(DisplayCustomer.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DisplayCustomer.this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            android.location.Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            android.location.Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }
        return (longitude);
    }
}