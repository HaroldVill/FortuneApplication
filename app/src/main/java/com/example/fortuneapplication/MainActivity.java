package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    private SharedPreferences sharedPreferences;
    private boolean hasLoggedOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}



