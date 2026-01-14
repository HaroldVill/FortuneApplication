package com.example.fortuneapplication;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class PhoneVibration {

    private static final LocalTime WORKDAY_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime WORKDAY_END_TIME = LocalTime.of(17, 0);
    private static final String TAG = "VIBRATION";

    public void checkTimeAndVibrate(Context context) {
        if(!shouldCheckVibration()) {
            return;
        }

        if(isWithinWorkingHours()) {
            triggerVibration(context);
        } else {
            stopVibration(context);
        }
    }

    private boolean shouldCheckVibration() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private boolean isWithinWorkingHours() {
        LocalTime currentTime = LocalTime.now();
        logCurrentTime(currentTime);

        return !currentTime.isBefore(WORKDAY_START_TIME) && !currentTime.isAfter(WORKDAY_END_TIME.minus(1, ChronoUnit.SECONDS));
    }

    private void logCurrentTime(LocalTime currentTime) {
        Log.d(TAG, "Current time: " + currentTime);
    }

    private void triggerVibration(Context context) {
        vibratePhone(context);
//        vibrationToastMessage(context);
    }

    private void vibratePhone(Context context) {
        Vibrator vibrating = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrating != null) {
            long [] timings = {0, 1000};
            int [] amplitudes = {0, VibrationEffect.DEFAULT_AMPLITUDE};

            VibrationEffect effect = VibrationEffect.createWaveform(timings, amplitudes, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrating.vibrate(effect);
                Log.d("MUVIBRATE NA UNTA NI", "MUVIBRATE NA UNTA NI");
            }
        }
    }

//    private void vibrationToastMessage(Context context) {
//        Toast.makeText(context, TOAST_MESSAGE_VIBRATING, Toast.LENGTH_SHORT).show();
//    }
    private void stopVibration(Context context) {
        Vibrator vibrating = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrating.cancel(); // Stops any ongoing vibration
    //            Toast.makeText(this, "Vibration stopped: Location turned on", Toast.LENGTH_SHORT).show();
        Log.d("DI NA MUVIBRATE", "DI NA MUVIBRATE");
    }
}
