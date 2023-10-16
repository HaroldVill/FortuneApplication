package com.example.fortuneapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemListDisplay extends AppCompatActivity {
    EditText searchbaritem;
    private Spinner spinner2;
    private RecyclerView mRecyclerView;
    private ItemDIisplayAdapter mItemAdapter;
    private ArrayList<Item> mItemList = new ArrayList<>();
    private PazDatabaseHelper mDatabaseHelper;

    FloatingActionButton tuklod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_display);

        spinner2 = findViewById(R.id.spinner2);
        mRecyclerView = findViewById(R.id.item_recycle);
        searchbaritem = findViewById(R.id.searchbaritem);
        tuklod = findViewById(R.id.tuklod);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemAdapter = new ItemDIisplayAdapter(this, mItemList);
        mRecyclerView.setAdapter(mItemAdapter);
        mDatabaseHelper = new PazDatabaseHelper(this);
        mItemList.addAll(mDatabaseHelper.getAllItems());
        mItemAdapter.notifyDataSetChanged();

        String[] sortme = {"    SORT BY: ............", "Item OnHand", "Item Description"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        tuklod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent power = new Intent(ItemListDisplay.this,SOActivity.class);
                startActivity(power);
                finish();
            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newItem = spinner2.getSelectedItem().toString();
                sortData(newItem);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //FILTER DATA IN SEARCH VIEW//*

        searchbaritem.addTextChangedListener(new TextWatcher() {
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

    //* FILTER DATA FROM RECYCLEVIEW //*
    public void filterList(String text) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : mItemList) {
            if (item.getDescription().toLowerCase().contains(text.toLowerCase())
                    || item.getGroup().toLowerCase().contains(text.toLowerCase())
            || item.getCode().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
            mItemAdapter.setFilterdList(filteredList);
        }

        mItemAdapter.notifyDataSetChanged();

    }

    private void sortData(String selectedChoice) {
        Comparator<Item> comparator = null;

        switch (selectedChoice) {
            case "Item OnHand":
                comparator = new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        int quantity1 = Integer.parseInt(item1.getQuantity());
                        int quantity2 = Integer.parseInt(item2.getQuantity());
                        return Integer.compare(quantity2, quantity1);
                    }
                };

                break;
            case "Item Description":
                comparator = new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return item1.getDescription().compareTo(item2.getDescription());
                    }
                };
                break;
            // Add more sorting options if needed
        }

        if (comparator != null) {
            Collections.sort(mItemList, comparator);
            mItemAdapter.sortData(comparator);
            mItemAdapter.notifyDataSetChanged();



        }
    }


}



