package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SFAItemDisplay extends AppCompatActivity {
    EditText searchbaritems;
    private Spinner spinner5;
    private RecyclerView mRecyclerView;
    private SFAItemAdapter mItemAdapter;
    private ArrayList<Item> mItemList = new ArrayList<>();
    private PazDatabaseHelper mDatabaseHelper;
    TextView dsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfaitem_display);

        dsp = findViewById(R.id.dsp);
        Intent intent = getIntent();
        String str = intent.getStringExtra("PRI");
        dsp.setText(str);

        spinner5 = findViewById(R.id.spinner5);
        mRecyclerView = findViewById(R.id.item_recycle2);
        searchbaritems = findViewById(R.id.searchbaritems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItemAdapter = new SFAItemAdapter(this, mItemList);


        mRecyclerView.setAdapter(mItemAdapter);
        mDatabaseHelper = new PazDatabaseHelper(this);
        mItemList.addAll(mDatabaseHelper.combinedata());
        // mItemList.addAll(mDatabaseHelper.getAllupdatedItems());
        mItemAdapter.notifyDataSetChanged();

        // mItemList.addAll(mDatabaseHelper.getAllItems());

        String [] sortme = { "SORT..","OnHand","Description"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,sortme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(adapter);


        //*FILTER Data//*

        searchbaritems.addTextChangedListener(new TextWatcher() {
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


        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String newItem = spinner5.getSelectedItem().toString();
                sortData(newItem);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    //*SORT DATA//*
    private void sortData(String selectedChoice) {
        Comparator<Item> comparator = null;

        switch (selectedChoice) {
            case "OnHand":
                comparator = new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        int quantity1 = Integer.parseInt(item1.getQuantity());
                        int quantity2 = Integer.parseInt(item2.getQuantity());
                        return Integer.compare(quantity2, quantity1);
                    }
                };

                break;
            case "Description":
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
//
//               switch (selectedChoice) {
//                   case "Sort by item OnHand":
//                       comparator = new Comparator<Item>() {
//                           @Override
//                           public int compare(Item item1, Item item2) {
//                               return item2.getQuantity().compareTo(item1.getQuantity());
//                           }
//                       };
//                       break;
//                   case "Sort by Description":
//                       comparator = new Comparator<Item>() {
//                           @Override
//                           public int compare(Item item1, Item item2) {
//                               return item1.getDescription().compareTo(item2.getDescription());
//                           }
//                       };
//                       break;
//                   // Add more sorting options if needed
//               }
//               if (comparator != null) {
//                   mItemAdapter.sortData(comparator);
        }
    }




    public void filterList(String text){
        List<Item> filteredList = new ArrayList<>();
        for (Item item : mItemList) {
            if (item.getDescription().toLowerCase().contains(text.toLowerCase())
                    || item.getGroup().toLowerCase().contains(text.toLowerCase())) {
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
//        for (Item item : mItemList){
//
//            if (item.getDescription().toLowerCase().contains(text.toLowerCase())){
//                filterList.add(item);
//            }
//            if (item.getGroup().toLowerCase().contains(text.toLowerCase())){
//                filterList.add(item);
//
//            }
//        }
//        if  (filterList.isEmpty()){
//            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
//        }else {
//            mItemAdapter.setFilterdList(filterList);
//            mItemAdapter.notifyDataSetChanged();


    public void checkData(){
        String value = dsp.getText().toString();
        if (value != null && !value.equals("null")) {

        }
    }

}