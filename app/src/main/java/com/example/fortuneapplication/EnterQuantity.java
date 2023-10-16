package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EnterQuantity extends AppCompatActivity {

    TextView t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_quantity);

        t = findViewById(R.id.t);

        t.setText(getIntent().getExtras().getString("contact"));
    }
}