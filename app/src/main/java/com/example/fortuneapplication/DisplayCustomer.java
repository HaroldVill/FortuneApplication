package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayCustomer extends AppCompatActivity {

    TextView u1,u2,u3,u4,u5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_customer);

        u1 = findViewById(R.id.u1);
        u2 = findViewById(R.id.u2);
        u3 = findViewById(R.id.u3);
        u4 = findViewById(R.id.u4);
        u5 = findViewById(R.id.u5);


        Intent intent = getIntent();
        String cid = intent.getStringExtra("ID");
        String cname = intent.getStringExtra("CNAME");
        String cadd = intent.getStringExtra("CADD");
        String cphone= intent.getStringExtra("CPHONE");
        String cperson= intent.getStringExtra("CPERSON");



        u1.setText(cid);
        u2.setText(cname);
        u3.setText(cadd);
        u4.setText(cphone);
        u5.setText(cperson);
    }
}