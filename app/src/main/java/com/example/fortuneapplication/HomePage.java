package com.example.fortuneapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity {


    ImageView  ie,  bots1, bots2, bots3, bots4, sin;
    CardView c1, c2, c3, c4;
    AlertDialog.Builder builder;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;
    TextView coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        ImageView bots1 = findViewById(R.id.bots1);
//        ImageView bots2 = findViewById(R.id.bots2);
//        ImageView bots3 = findViewById(R.id.bots3);
//        ImageView bots4 = findViewById(R.id.bots4);
        ImageView sin = findViewById(R.id.sin);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        ie = findViewById(R.id.ie);
        coordinates = findViewById(R.id.coordinates);

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
            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String get_coordinates=getLocation();
                            coordinates.setText(get_coordinates);
                            Log.i("GPS", get_coordinates);
                        }
                    });
                }
            },0,2000);

        }



        ie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(HomePage.this);
                logoutBuilder.setTitle("Do you want to LogOut?");
                logoutBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(PREF_USERNAME);
                        editor.apply();

                        // Navigate to the login page
                        Intent loginIntent = new Intent(HomePage.this, MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                });
                logoutBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog logoutDialog = logoutBuilder.create();
                logoutDialog.show();
            }
        });

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder passwordBuilder = new AlertDialog.Builder(HomePage.this);
                passwordBuilder.setTitle("Enter Password");
                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordBuilder.setView(input);
                passwordBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (password.equals("admin")) {
                            Intent ff = new Intent(HomePage.this, SyncDatas.class);
                            startActivity(ff);
                            Toast.makeText(HomePage.this, "Welcome Admin", Toast.LENGTH_LONG).show();
//                            finish();
                        } else {
                            Toast.makeText(HomePage.this, "You are not Authorized", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                passwordBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog passwordDialog = passwordBuilder.create();
                passwordDialog.show();
            }
        });

//        ie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.setTitle("Do you want to LogOut?");
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//});
//
//
//        sin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                builder.setTitle("Enter Password");
//
//                final EditText input = new EditText(getApplicationContext());
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                builder.setView(input);
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String password = input.getText().toString();
//                        if (password.equals("admin")) {
//                            Intent ff = new Intent(HomePage.this, SyncDatas.class);
//                            startActivity(ff);
//                            Toast.makeText(HomePage.this, "Welcome Admin", Toast.LENGTH_LONG).show();
//                            finish();
//                        } else {
//                            Toast.makeText(HomePage.this, "You are not Authorize", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//
//            }
//        });



        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent f = new Intent(HomePage.this,CustoDisplay.class );
                startActivity(f);

            }
        });


        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent P = new Intent(HomePage.this,ItemListDisplay.class );
                startActivity(P);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent PO = new Intent(HomePage.this,SOActivity.class );
                startActivity(PO);

            }
        });

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nb = new Intent(HomePage.this,History.class);
                startActivity(nb);

            }
        });

    }

    private String getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(HomePage.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomePage.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
        return ("MY CURRENT LOCATION\n\n"+"Longitude: "+longitude+"\nLatitude: "+latitude);
    }
}