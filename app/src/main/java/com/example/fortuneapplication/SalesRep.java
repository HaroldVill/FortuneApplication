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

public class SalesRep extends AppCompatActivity {
    EditText rep1;
    RecyclerView slrep;
    private SRepresentativeAdapter sRepresentativeAdapter;
    private ArrayList<SalesRepList> mIsalerep = new ArrayList<>();
    private PazDatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_rep);
        slrep = findViewById(R.id.slrep);
        rep1 = findViewById(R.id.rep1);
        slrep.setLayoutManager(new LinearLayoutManager(this));
        sRepresentativeAdapter = new SRepresentativeAdapter(this, mIsalerep);
        slrep.setAdapter(sRepresentativeAdapter);

        mDatabaseHelper = new PazDatabaseHelper(this);
        mIsalerep.addAll(mDatabaseHelper.getAllSalesRep());
      sRepresentativeAdapter.notifyDataSetChanged();

      rep1.addTextChangedListener(new TextWatcher() {
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
        List<SalesRepList> filterList = new ArrayList<>();
        for (SalesRepList salesRepList : mIsalerep) {

            if (salesRepList.getSrname().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(salesRepList);
            }
        }
        if (filterList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
           sRepresentativeAdapter.setFilterdList(filterList);
        }

    }

}