package com.example.fortuneapplication;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class GetLocation extends AppCompatActivity{
    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;
    private Context context;
    private GetLocation(Context context){
        context = this.context;
    }

    public String get_location() {
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GetLocation.this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GetLocation.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            android.location.Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            android.location.Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.d("LOCATION", "Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);
            } else if (LocationNetwork != null) {
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
            } else {

            }

            //Thats All Run Your App
        }
        return ("LONGITUDE: "+longitude+"           LATITUDE: "+latitude);
    }
}
