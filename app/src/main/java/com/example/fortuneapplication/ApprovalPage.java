package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ApprovalPage extends AppCompatActivity {

    private TextView customertextView, descriptiontextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_page);
        customertextView = findViewById(R.id.customerName);
        descriptiontextView = findViewById(R.id.descriptionNames);
        customerAndDescriptionDisplay();
    }

    @SuppressLint("SetTextI18n")
    private void customerAndDescriptionDisplay() {
        SharedPreferences sharedPreferencesCustomer = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String customerName = sharedPreferencesCustomer.getString("CNAME", "");
        customertextView.setText("CUSTOMER NAME: " + customerName);

        String descriptionName = getIntent().getStringExtra("ITEM_DESCRIPTION");
        if (descriptionName != null) {
            descriptiontextView.setText("ITEM DESCRIPTION: " + descriptionName);
        }
    }

} // ApprovalPage
