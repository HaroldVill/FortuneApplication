package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustoDisplay extends AppCompatActivity {
   // SearchView cseach;
    private static final int REQUEST_LOCATION = 1;
    EditText searchbarcusto;
    private RecyclerView list1;
    private CustomerDisplayAdapter customerDisplayAdapter;
    private ArrayList<Customer> mIcustomer = new ArrayList<>();
    private PazDatabaseHelper mDatabaseHelper;
    private Spinner spinner3;
    Button pin;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custo_display);


        searchbarcusto = findViewById(R.id.searchbarcusto);
        list1 = findViewById(R.id.list1);
        spinner3 = findViewById(R.id.spinner3);

        list1.setLayoutManager(new LinearLayoutManager(this));
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GetGPSLocation gps = new GetGPSLocation(CustoDisplay.this,this,locationManager);
        double longitude = Double.parseDouble(gps.get_longitude());
        double latitude = Double.parseDouble(gps.get_latitude());
        customerDisplayAdapter = new CustomerDisplayAdapter(this, mIcustomer,longitude,latitude);
        list1.setAdapter(customerDisplayAdapter);

        mDatabaseHelper = new PazDatabaseHelper(this);
        mIcustomer.addAll(mDatabaseHelper.getAllCustomer());
        customerDisplayAdapter.notifyDataSetChanged();



        String[] sortme = {"SORT BY: ...........", "Customer Name", "Customer Address"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter);

//        pin = findViewById(R.id.pin);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newCustomer = spinner3.getSelectedItem().toString();
                sortData(newCustomer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchbarcusto.addTextChangedListener(new TextWatcher() {
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
        List<Customer> filterList = new ArrayList<>();

        for (Customer customer : mIcustomer) {
            if (customer.getCustomername().toLowerCase().contains(text.toLowerCase())
                    || customer.getPostaladdress().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(customer);
            }
        }
        if (filterList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
            customerDisplayAdapter.setFilterdList(filterList);
        }

        customerDisplayAdapter.notifyDataSetChanged();

    }

    //* SORT DATA//*
    private void sortData(String selectedChoice) {
        Comparator<Customer> comparator = null;

        switch (selectedChoice) {
            case "Customer Name":
                comparator = new Comparator<Customer>() {
                    @Override
                    public int compare(Customer customer1, Customer customer2) {
                        return customer1.getCustomername().compareTo(customer2.getCustomername());
                    }
                };
                break;
            case "Customer Address":
                comparator = new Comparator<Customer>() {
                    @Override
                    public int compare(Customer customer1, Customer customer2) {
                        return customer1.getPostaladdress().compareTo(customer2.getPostaladdress());
                    }
                };
                break;
            // Add more sorting options if needed
        }
        if (comparator != null) {
            customerDisplayAdapter.sortData(comparator);
        }
    }
}