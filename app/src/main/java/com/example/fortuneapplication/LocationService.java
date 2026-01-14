package com.example.fortuneapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    private static final long INTERVAL = 1000 * 60 * 2; // 2 minutes
    private final Handler handler = new Handler();
    private Runnable runnable;
    LocationManager locationManager;
    private Vibrator vibrating;
    private PhoneVibration phoneVibration;

    @Override
    public void onCreate() {
//        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        vibrating = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
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
//                    postingUnsyncedLocationOfSalesRep();
                }
            }
        };
        repeatingTask();
        checkingLocationWhetherOnOrOff(this);
        postingUnsyncedLocationOfSalesRep();

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

        postingUnsyncedLocationOfSalesRep();

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

    private void postingUnsyncedLocationOfSalesRep() {
        ArrayList<CONNECT> connectIP = mDatabaseHelper.SelectUPDT();
        if (!connectIP.isEmpty()) {
            IPADD = connectIP.get(0).getIp();
        }
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

                apiSendingAddress = "http://" + IPADD + "/MobileAPI/sync_salesrep_coordinates.php";
                StringRequest sendingToDatabase = new StringRequest(Request.Method.POST, apiSendingAddress,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "SUCCESSFULLY RESPONDED");
                                if(response.contains("succesfully") || response.contains("has already been")) {
                                    mDatabaseHelper.update_synced_coordinate_tracking(id);
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
//                sendingToDatabase.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request_queue = Volley.newRequestQueue(LocationService.this);
                request_queue.add(sendingToDatabase);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

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
//        Intent notificationIntent = new Intent(this, LogIn.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("GOOD DAY!")
                .setContentText("HAVE A GOOD DAY")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        handler.removeCallbacks(runnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void repeatingTask() {
        runnable = new Runnable() {
            @Override
            public void run() {
                postingUnsyncedLocationOfSalesRep();

                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.postDelayed(runnable, INTERVAL);
    }

    private void checkingLocationWhetherOnOrOff(Context context) {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isLocationDisabled()) {
                    phoneVibration = new PhoneVibration();
                    phoneVibration.checkTimeAndVibrate(context);
                    showLocationNotification();
                } else {
                    stopVibration();
                    cancelNotification(context);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void stopVibration() {
            vibrating.cancel(); // Stops any ongoing vibration
//            Toast.makeText(this, "Vibration stopped: Location turned on", Toast.LENGTH_SHORT).show();
            Log.d("DI NA MUVIBRATE", "DI NA MUVIBRATE");
    }

    private boolean isLocationDisabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showLocationNotification() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("LOCATION IS OFF")
                .setContentText("TAP THE NOTIFICATION TO TURN ON THE LOCATION")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Location Alerts", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(0, builder.build());
        }
    }

    private void cancelNotification(Context context) {
            NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
            notificationCompat.cancel(0);
    }
}
