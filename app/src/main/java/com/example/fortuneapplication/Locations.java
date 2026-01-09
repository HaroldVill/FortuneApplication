package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Locations extends AppCompatActivity {

    private RecyclerView locationlw;
    EditText sc1;
    private LocationDisplayAdapter locationDisplayAdapter;
    private ArrayList<Locationing> mLocation= new ArrayList<>();
    private PazDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        sc1 = findViewById(R.id.sc1);
        locationlw = findViewById(R.id.locationlw);
        locationlw.setLayoutManager(new LinearLayoutManager(this));
        locationDisplayAdapter = new LocationDisplayAdapter(this, mLocation);
        locationlw.setAdapter(locationDisplayAdapter);

        mDatabaseHelper = new PazDatabaseHelper(this);
        mLocation.addAll(mDatabaseHelper.getAllLocation());
        locationDisplayAdapter.notifyDataSetChanged();


        sc1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void filterList(String text) {
        List<Locationing> filterList = new ArrayList<>();
        for (Locationing location : mLocation) {

            if (location.getLocname().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(location);
            }
        }
        if (filterList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
            locationDisplayAdapter.setFilterdList(filterList);
        }
    }
}