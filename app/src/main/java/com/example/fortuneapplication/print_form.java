package com.example.fortuneapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class print_form extends AppCompatActivity {
    private PazDatabaseHelper mDatabaseHelper;
    principal_adapter principal_adapter;
    RecyclerView principal_list;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_form);
        SharedPreferences preferences = getSharedPreferences("TemporaryData", Context.MODE_PRIVATE);
        int salesOrderId = preferences.getInt("SO_ID", 0);
        principal_list = findViewById(R.id.datadisp);

        mDatabaseHelper = new PazDatabaseHelper(this);
        ArrayList<SALESORDER> principal_performance = mDatabaseHelper.get_principal_performance();
        principal_adapter = new principal_adapter(this, principal_performance);
        Log.d("check_result", principal_performance.toString());
        principal_list.setLayoutManager(new LinearLayoutManager(this));
        principal_list.setAdapter(principal_adapter);
    }
}
