package com.example.fortuneapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LocationService extends Service {

    private static final String TAG = "Location Service";
    private static final String CHANNEL_ID = "LocationServiceChannel";
    private static final int NOTIFICATION_ID = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private PazDatabaseHelper mDatabaseHelper;
    private RequestQueue request_queue;
    private String IPADD, apiSendingAddress;
    private static final int INTERVAL = 1000 * 60 * 2; // 2 minutes

    @Override
    public void onCreate() {
        mDatabaseHelper = new PazDatabaseHelper(LocationService.this);
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    handleLocationUpdate(location);
                    postingUnsyncedLocationOfSalesRep();
                }
            }
        };

        ArrayList<CONNECT> connectIP = mDatabaseHelper.SelectUPDT();
        if (!connectIP.isEmpty()) {
            IPADD = connectIP.get(0).getIp();
        }


    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        requestLocationUpdates();

        return START_STICKY;
    }

    private void requestLocationUpdates() {
        com.google.android.gms.location.LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 600000)
                .setMinUpdateIntervalMillis(600000)
                .setMaxUpdateDelayMillis(600000)
                .build();

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e(TAG, "Lost location permissions: " + e.getMessage());
        }
    }

    private void handleLocationUpdate(Location location) {
        mDatabaseHelper = new PazDatabaseHelper(LocationService.this);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();

        Log.d(TAG, "Location Update: Lat=" + latitude + ", Lon=" + longitude +
                ", Accuracy=" + accuracy + "m");

        String default_salesrep_id = mDatabaseHelper.get_default_salesrep_id();
        String datetime = mDatabaseHelper.get_currentdatetime();
        String date = mDatabaseHelper.get_currentdate();
        String latitudeString = String.valueOf(latitude);
        String longitudeString = String.valueOf(longitude);

        Log.d(TAG, "SalesRepId: " + default_salesrep_id + " Datetime: " + datetime + " date: " + date);

        String[] coordinate_tracking = {default_salesrep_id, datetime, date, longitudeString, latitudeString, "0"};

        if (Integer.parseInt(default_salesrep_id) != 0) {
            try {
                mDatabaseHelper.storeCoordinateTracking(coordinate_tracking);
                Log.d(TAG, "storedInSQLiteDatabase");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void postingUnsyncedLocationOfSalesRep() {
        mDatabaseHelper = new PazDatabaseHelper(LocationService.this);
        try {
            ArrayList<String[]> unsynced_coordinates = mDatabaseHelper.get_unsynced_coordinates();
            for (String[] coordinates_array: unsynced_coordinates) {
                Log.d(TAG, "Data: " + Arrays.toString(coordinates_array));
                Integer id = Integer.parseInt(coordinates_array[0]);
                String default_salesrep_id = coordinates_array[1];
                String datetime = coordinates_array[2];
                String date = coordinates_array[3];
                String unsynced_longitude = coordinates_array[4];
                String unsynced_latitude = coordinates_array[5];

                apiSendingAddress = "http://" + IPADD + "/MobileAPIaaaaaaaa/sync_salesrep_coordinatesaaaaaaaa.php";
                StringRequest sendingToDatabase = new StringRequest(Request.Method.POST, apiSendingAddress,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "SUCCESSFULLY RESPONDED");
                                if(response.contains("succesfully") || response.contains("has already been")) {
//                                    mDatabaseHelper.update_synced_coordinate_tracking(id);
                                    Log.d(TAG, "SUCCESSFULLY_POSTED!");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "ERROR POSTING!");
                            }
                        }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("sales_rep_id", default_salesrep_id);
                        params.put("datetime", datetime);
                        params.put("date", date);
                        params.put("longitude", unsynced_longitude);
                        params.put("latitude", unsynced_latitude);
                        return params;
                    }
                };
                sendingToDatabase.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request_queue = Volley.newRequestQueue(LocationService.this);
                request_queue.add(sendingToDatabase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }
//        try {
//            ArrayList<String[]> unsynced_coordinates = mDatabaseHelper.get_unsynced_coordinates();
//
//            for (String[] coordinates_array : unsynced_coordinates) {
//                int id = Integer.parseInt(coordinates_array[0]);
//                default_salesrep_id = coordinates_array[1];
//                datetime = coordinates_array[2];
//                date = coordinates_array[3];
//                longitudeString = coordinates_array[4];
//                latitudeString = coordinates_array[5];
//
//                ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
//                if (!connectList2.isEmpty()) {
//                    x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
//                    String sales_type = mDatabaseHelper.sales_type();
//                    Log.d("sales_type",sales_type);
//                    api_url = "http://" + x + "/MobileAPI/sync_salesrep_coordinates.php";
//                }
////                           PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
//                String finalDefault_salesrep_id = default_salesrep_id;
//                String finalDatetime = datetime;
//                String finalDate = date;
//                String finalLongitudeString = longitudeString;
//                String finalLatitudeString = latitudeString;
//                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
//                        response -> {Log.d("Success","Success");
//                            if(response.contains("succesfully") || response.contains("has already been")){
//                                mDatabaseHelper.update_synced_coordinate_tracking(id);
//                                Log.d(TAG, "successful_log_coordinates");}},
//                        error -> Log.d("Error","Connection Error")){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params =new HashMap<>();
//                        params.put("sales_rep_id", finalDefault_salesrep_id);
//                        params.put("datetime", finalDatetime);
//                        params.put("date", finalDate);
//                        params.put("longitude", finalLongitudeString);
//                        params.put("latitude", finalLatitudeString);
//                        return params;
//                    }
//                };
//                request_queue = Volley.newRequestQueue(LocationService.this);
//                request_queue.add(send_invoices);
//                Log.d(TAG, "posted");
////                Thread.sleep(60000);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("Exception",e.getMessage());
//        }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Tracking location in background");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, HomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Tracking")
                .setContentText("Tracking your location in background")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
