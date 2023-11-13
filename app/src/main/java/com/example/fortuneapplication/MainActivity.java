package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import android.os.Handler;
import android.os.Looper;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    private SharedPreferences sharedPreferences;
    private boolean hasLoggedOut = false;
    private volatile boolean stopThread = false;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startThread();
        Log.d("Created","1");
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        run();

    }



    private void run() {
        String savedUsername = sharedPreferences.getString(PREF_USERNAME, "");

        if (!savedUsername.isEmpty() && !hasLoggedOut) {
            // User is already logged in, proceed to the home page
            Intent hm = new Intent(MainActivity.this, HomePage.class);
            startActivity(hm);
            finish();
        } else if (!savedUsername.isEmpty() && hasLoggedOut) {
            // User has logged out, clear the shared preferences and proceed to the login page
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent t = new Intent(MainActivity.this, LogIn.class);
            startActivity(t);
            Toast.makeText(MainActivity.this, "WELCOME TO PAZ_DISTRIBUTION APP", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // User is not logged in, proceed to the login page
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent t = new Intent(MainActivity.this, LogIn.class);

                    startActivity(t);

                    Toast.makeText(MainActivity.this, "WELCOME TO PAZ_DISTRIBUTION APP", Toast.LENGTH_LONG).show();

                    finish();
                }
            }, 1500);
        }
    }

    public void startThread() {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable(1000);
        new Thread(runnable).start();
        Log.d("Test", "1");
        /*
        ExampleThread thread = new ExampleThread(10);
        thread.start();
        */
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //work
            }
        }).start();
        */
    }



    class ExampleRunnable implements Runnable {
        int seconds;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            Log.d("StartThread", Integer.toString(seconds));
            for (int i = 0; i < seconds; i++) {
//                if (1!=0)
//                    return;
                if (i ==10 || i==20) {
                    Log.d("StartThread", "floof");
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            buttonStartThread.setText("50%");
//                        }
//                    });
                }
                Log.d(TAG, "startThread2: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



