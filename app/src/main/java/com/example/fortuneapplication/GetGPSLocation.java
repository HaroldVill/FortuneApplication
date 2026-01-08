package com.example.fortuneapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Button;

import java.util.concurrent.Executor;

public class GetGPSLocation extends AppCompatActivity implements LocationListener{
    private static final long MIN_TIME_BW_UPDATES = 1000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;
    private Context context;
    Activity activity;
    public GetGPSLocation(Context context,Activity activity, LocationManager locationManager){
        this.locationManager = locationManager;
        this.context = context;
        this.activity = activity;
    }
    public String get_longitude(){//int delcaration to determine if longitude(0) or latitude(1) is asked.
        return get_location(0);
    }
    public String get_latitude(){//int delcaration to determine if longitude(0) or latitude(1) is asked.
        return get_location(1);
    }
    public String get_location(int i) {//int delcaration to determine if longitude(0) or latitude(1) is asked.
        //Check Permissions again
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,

                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetGPSLocation.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            android.location.Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            android.location.Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.d("LOCATION", "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);
            }
            else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.d("LOCATION", "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);
            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.d("LOCATION", "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);
            }
            else {
                latitude = "0";
                longitude = "0";
            }


        }
        if(i == 0){
            return longitude;
        }
        else {
            return latitude;
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        latitude = "0";
//        longitude = "0";
    }
}
