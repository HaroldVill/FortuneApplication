package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SOdisplayCustomer extends AppCompatActivity {
    RecyclerView sorecyle;
    private SoCustomerAdapter soCustomerAdapter;
    private ArrayList<Customer> customers = new ArrayList<>();
    private PazDatabaseHelper mDatabaseHelper;
    private Spinner spinner4;
    EditText searchbar;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sodisplay_customer);

        searchbar = findViewById(R.id.searchbar);
        spinner4 = findViewById(R.id.spinner4);
        sorecyle = findViewById(R.id.sorecyle);

        sorecyle.setLayoutManager(new LinearLayoutManager(this));
        ActivityCompat.requestPermissions(SOdisplayCustomer.this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        soCustomerAdapter = new SoCustomerAdapter(this, customers,this,locationManager);
        sorecyle.setAdapter(soCustomerAdapter);

        mDatabaseHelper = new PazDatabaseHelper(this);
        Intent intent = getIntent();
        String type = intent.getStringExtra("Type");
        Log.d("customerdisplaytype",type);
        if(type.equals("All")) {
            customers.addAll(mDatabaseHelper.getAllCustomer());
            soCustomerAdapter.notifyDataSetChanged();
        }
        else{
            customers.addAll(mDatabaseHelper.getCustomerFromCoveragePlan());
            soCustomerAdapter.notifyDataSetChanged();
        }
        String[] sortme = {"  SORT BY: ...........", "Customer Name", "Customer Address"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(adapter);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newCustomer = spinner4.getSelectedItem().toString();
                sortData(newCustomer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//* SEARCH//*
        searchbar.addTextChangedListener(new TextWatcher() {
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

// METHOD IN FILTERING DATA//*
    public void filterList(String text) {
        List<Customer> filteredList = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getCustomername().toLowerCase().contains(text.toLowerCase())
                    || customer.getPostaladdress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(customer);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
            soCustomerAdapter.setFilterdList(filteredList);
        }
        soCustomerAdapter.notifyDataSetChanged();
    }

// METHOD IN SORTING//*
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
            soCustomerAdapter.sortData(comparator);
        }
    }
}




