package com.example.fortuneapplication;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class AutoStartWhenBootBroadcastReceiver extends BroadcastReceiver {

    LocationManager locationManager;
    Vibrator vibrating;
    private PhoneVibration phoneVibration;

    @Override
    public void onReceive(Context context, Intent intent) {
        vibrating = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, LocationService.class);
            locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled || !isNetworkEnabled) {
                phoneVibration = new PhoneVibration();
                phoneVibration.checkTimeAndVibrate(context);
            } else {
                stopVibration();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
                Toast.makeText(context, "HINGGANA SYA PO", Toast.LENGTH_SHORT).show();
            } else {
                context.startService(serviceIntent);
            }
        }

    }

    private void stopVibration() {
            vibrating.cancel(); // Stops any ongoing vibration
    }
}

