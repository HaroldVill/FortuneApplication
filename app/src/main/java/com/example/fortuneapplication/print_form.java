package com.example.fortuneapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class print_form extends AppCompatActivity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_form);
        SharedPreferences preferences = getSharedPreferences("TemporaryData", Context.MODE_PRIVATE);
        int salesOrderId = preferences.getInt("SO_ID", 0);
    }
}
