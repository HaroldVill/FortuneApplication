package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.ITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_GROUP;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_RATE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_UNIT_MEASURE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITM;
import static com.example.fortuneapplication.PazDatabaseHelper.NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.PRICE_LEVEL_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.PRI_LEVEL_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.PRCUSTOM_PRICE;
import static com.example.fortuneapplication.PazDatabaseHelper.PRICE_LEVELID;
import static com.example.fortuneapplication.PazDatabaseHelper.PRICE_LEVEL_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.PRICE_LEVEL_LINES_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.PRICE_LEVEL_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.PRITEM_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.PRI_LEVEL_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDERITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_ITEMS_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.TABLE_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.UNIT_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SPECIAL_PRICE_LEVEL_PRICE_LEVEL_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SPECIAL_PRICE_LEVEL_ITEM_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SPECIAL_PRICE_LEVEL_CUSTOMER_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SPECIAL_PRICE_LEVEL_TABLE;

import static com.example.fortuneapplication.PazDatabaseHelper.UNIT_MEASURE_TABLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.car.ui.core.SearchResultsProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ItemPricelvlChoiceDisplay extends AppCompatActivity {

    private RecyclerView ress;
    private SlectItemAdapter slectItemAdapter;
    private ArrayList<Item> itemLista;
    private PazDatabaseHelper mDatabaseHelper;
    private TextView level,customer_id;
    EditText searchbarr;
    Spinner spinner8;
    FloatingActionButton floatt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pricelvl_choice_display);

        level = findViewById(R.id.level);
        customer_id = findViewById(R.id.customer_id);
        ress = findViewById(R.id.ress);
        searchbarr = findViewById(R.id.searchbarr);
        spinner8 = findViewById(R.id.spinner8);

        floatt = findViewById(R.id.floatt);

        floatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tyd = new Intent(ItemPricelvlChoiceDisplay.this, SOActivity.class);
                startActivity(tyd);
              finish();
            }
        });

        itemLista = new ArrayList<>();
        // Get the pricelevel from customer
        Intent intent = getIntent();
        String str = intent.getStringExtra("PRI");
        level.setText(str);
        customer_id.setText(intent.getStringExtra("CUSTOMER_ID"));

        ress.setLayoutManager(new LinearLayoutManager(this));
        slectItemAdapter = new SlectItemAdapter(itemLista, this,customer_id.getText().toString());
        ress.setAdapter(slectItemAdapter);
        mDatabaseHelper = new PazDatabaseHelper(this);
        checkvalue();


        //Sort data //
        String[] sortme = {"  SORT BY: ............", "Sort by item OnHand", "Sort by Description"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortme);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner8.setAdapter(adapter);

        spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newItem = spinner8.getSelectedItem().toString();
                sortData(newItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//Filter data //

        searchbarr.addTextChangedListener(new TextWatcher() {
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
   @SuppressLint("Range")
   public void checkvalue() {
       ArrayList<Item> itemList = new ArrayList<>();
    String query = "SELECT " +
           TABLE_NAME + "." + ITEMID + "," +
            TABLE_NAME + "." + ITEM_CODE + ", " +
            TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
            TABLE_NAME + "." + ITEM_RATE + ", " +
            TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
            TABLE_NAME + "." + ITEM_QUANTITY + ", " +
            TABLE_NAME + "." + ITEM_GROUP + ", " +
            UNIT_MEASURE_TABLE + "." + NAME + ", " +
            UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
            UNIT_MEASURE_TABLE + "." + UNIT_ID + ", " +
            PRICE_LEVEL_LINES_TABLE + "." + PRCUSTOM_PRICE + ", " +
            PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID + ", " +
            PRICE_LEVEL_TABLE + "." + PRICE_LEVELID + ", " +
            PRICE_LEVEL_TABLE + "." + PRICE_LEVEL_DESCRIPTION +

            " FROM " + TABLE_NAME +
            " LEFT JOIN " + UNIT_MEASURE_TABLE +
            " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + ITM +
            " LEFT JOIN " + PRICE_LEVEL_LINES_TABLE +
            " ON " + TABLE_NAME + "." + ITEMID + " = " + PRICE_LEVEL_LINES_TABLE + "." + PRITEM_ID +
            " AND "+PRICE_LEVEL_LINES_TABLE+"."+PRI_LEVEL_ID+" = (SELECT PRICE_LEVEL_ID FROM CUSTOMER_TABLE WHERE CUSTOMER_ID ="+customer_id.getText().toString()+")" +
           " LEFT JOIN " + PRICE_LEVEL_TABLE +
           " ON " + PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID + " = " + PRICE_LEVEL_TABLE + "." + PRICE_LEVELID+
            " LEFT JOIN " + SPECIAL_PRICE_LEVEL_TABLE +
            " ON " + SPECIAL_PRICE_LEVEL_TABLE + "." + SPECIAL_PRICE_LEVEL_PRICE_LEVEL_ID + " = " + PRICE_LEVEL_TABLE + "." + PRICE_LEVELID +
            " ON " + SPECIAL_PRICE_LEVEL_TABLE + "." + SPECIAL_PRICE_LEVEL_ITEM_ID + " = " + TABLE_NAME + "." + ITEMID +
            " ON " + SPECIAL_PRICE_LEVEL_TABLE + "." + SPECIAL_PRICE_LEVEL_CUSTOMER_ID + " = " + customer_id.getText().toString();

    SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
    String levelValue = level.getText().toString();
       Log.d("TAG", "query_price_level: " +query);
        try(
                //NAPAKA POTANG INANG CODE INCOMING
    Cursor cursor = db.rawQuery(query, null))
    {
        if (cursor.moveToFirst()) {
            HashMap<String, Item> itemMap = new HashMap<>();
            do {
                String itemCode = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_CODE));
                Item existingItem = itemMap.get(itemCode);
                String priceLevelId = cursor.getString(cursor.getColumnIndexOrThrow(PRI_LEVEL_ID));
                String special_price_level_price_level_id = cursor.getString(cursor.getColumnIndexOrThrow(SPECIAL_PRICE_LEVEL_PRICE_LEVEL_ID));
                if (existingItem == null) {
                    // Create a new item if it doesn't exist in the map
                    Item item = new Item();
                    item.setCode(itemCode);
                    item.setId(cursor.getString(cursor.getColumnIndexOrThrow(ITEMID)));
                    item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_DESCRIPTION)));
                    item.setUnitquant(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_UNIT_MEASURE)));
                    item.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_QUANTITY)));
                    item.setGroup(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_GROUP)));

                    Unit unit = new Unit();
                    unit.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                    unit.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(QUANTITY)));
                    unit.setUnit_id(cursor.getString(cursor.getColumnIndexOrThrow(UNIT_ID)));

                    NewPriceLvl newPriceLvl = new NewPriceLvl();
                    newPriceLvl.setPid(cursor.getString(cursor.getColumnIndexOrThrow(PRICE_LEVELID)));

                    item.setNewPriceLvl(newPriceLvl);
                    item.setUnit(unit);


                    if (priceLevelId != null) {
                        // Display the custom price if a match is found
                        // Display the regular rate if custom price is not available
                        item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(PRCUSTOM_PRICE)));

                    }
                    else if (special_price_level_price_level_id != null) {
                        // Display the custom price if a match is found
                        // Display the regular rate if custom price is not available
                        item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(PRCUSTOM_PRICE)));

                    }
                    else{
                        item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
                    }
                    itemMap.put(itemCode, item);

                } else {

                    if (priceLevelId != null) {
                        // Display the custom price if a match is found
                            // Display the regular rate if custom price is not available
                        existingItem.setRate(cursor.getString(cursor.getColumnIndexOrThrow(PRCUSTOM_PRICE)));

                    }
                    else if (special_price_level_price_level_id != null) {
                        // Display the custom price if a match is found
                        // Display the regular rate if custom price is not available
                        existingItem.setRate(cursor.getString(cursor.getColumnIndexOrThrow(PRCUSTOM_PRICE)));

                    }
                    else{
                        existingItem.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
                    }
                }
            } while (cursor.moveToNext());

            itemList.addAll(itemMap.values());
        }
    } finally {
            db.close();

    }

        Collections.sort(itemList,new Comparator<Item>()

    {
        @Override
        public int compare (Item item1, Item item2){
        return item1.getCode().compareTo(item2.getCode());
    }
    });

        itemLista.clear(); // Clear the existing items in the list
        itemLista.addAll(itemList); // Add the retrieved items to the list
        slectItemAdapter.notifyDataSetChanged();

    }

    //filter data//
    public void filterList(String text) {
        List<Item> filteredList = new ArrayList<>();
        for (Item item : itemLista) {
            if (item.getDescription().toLowerCase().contains(text.toLowerCase())
                    || item.getGroup().toLowerCase().contains(text.toLowerCase())
            ||item.getCode().toLowerCase().contains(text.toLowerCase())){

                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
            slectItemAdapter.setFilterdList(filteredList);
        }

        slectItemAdapter.notifyDataSetChanged();
    }

    //*SORT DATA//*
    private void sortData(String selectedChoice) {
        Comparator<Item> comparator = null;

        switch (selectedChoice) {
            case "Sort by item OnHand":
                comparator = new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        int quantity1 = Integer.parseInt(item1.getQuantity());
                        int quantity2 = Integer.parseInt(item2.getQuantity());
                        return Integer.compare(quantity2, quantity1);
                    }
                };

                break;
            case "Sort by Description":
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
            Collections.sort(itemLista, comparator);
            slectItemAdapter.sortData(comparator);
            slectItemAdapter.notifyDataSetChanged();
        }
    }
}

//        String query = "SELECT " +
//                TABLE_NAME + "." + ITEM_CODE + ", " +
//                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
//                TABLE_NAME + "." + ITEM_RATE + ", " +
//                TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
//                TABLE_NAME + "." + ITEM_QUANTITY + ", " +
//                TABLE_NAME + "." + ITEM_GROUP + ", " +
//                UNIT_MEASURE_TABLE + "." + NAME + ", " +
//                UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
//                UNIT_MEASURE_TABLE + "." + UNIT_ID + ", " +
//                PRICE_LEVEL_LINES_TABLE + "." + PRCUSTOM_PRICE + ", " +
//                PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID +
//                " FROM " + TABLE_NAME +
//                " JOIN " + UNIT_MEASURE_TABLE +
//                " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + "ITEM_ID" +
//                " LEFT JOIN " + PRICE_LEVEL_LINES_TABLE +
//                " ON " + TABLE_NAME + "." + ITEMID + " = " + PRICE_LEVEL_LINES_TABLE + "." + PRITEM_ID;
//
//              //  "  JOIN " +  PRICE_LEVEL_TABLE +
//        //   " ON " + PRICE_LEVEL_LINES_TABLE + "." + PRICE_LEVELID + " = " + PRICE_LEVEL_TABLE + "." + PRICE_LEVELID;
//
//                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        String levelValue = level.getText().toString(); // Get the value from the level TextView
//        try (Cursor cursor = db.rawQuery(query, null)) {
//            if (cursor.moveToFirst()) {
//                HashMap<String, Item> itemMap = new HashMap<>();
//                do {
//                    String itemCode = cursor.getString(cursor.getColumnIndexOrThrow(ITEM_CODE));
//                    Item existingItem = itemMap.get(itemCode);
//
//                    if (existingItem == null) {
//                        // Create a new item if it doesn't exist in the map
//                        Item item = new Item();
//                        item.setCode(itemCode);
//                        item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_DESCRIPTION)));
//                        item.setUnitquant(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_UNIT_MEASURE)));
//                        item.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_QUANTITY)));
//                        item.setGroup(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_GROUP)));
//
//                        Unit unit = new Unit();
//                        unit.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
//                        unit.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(QUANTITY)));
//                        unit.setUnit_id(cursor.getString(cursor.getColumnIndexOrThrow(UNIT_ID)));
//
//                        item.setUnit(unit);
//                        item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
//                        itemMap.put(itemCode, item);
//                    } else {
//                        // Update the item rate if a price level match is found
//                        String priceLevelId = cursor.getString(cursor.getColumnIndexOrThrow(PRI_LEVEL_ID));
//                        String customPrice = cursor.getString(cursor.getColumnIndexOrThrow(PRCUSTOM_PRICE));
//
//                        if (priceLevelId != null && priceLevelId.equals(levelValue) && customPrice != null && !customPrice.isEmpty()) {
//                            existingItem.setRate(customPrice);
//                        } else {
//                            // Set a default custom price here if custom price is not available
//                            existingItem.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
//
//                        }
//                    }
//                } while (cursor.moveToNext());
//
//                itemList.addAll(itemMap.values());
//            }
//        } finally {
//            db.close();
//        }
//
//        Collections.sort(itemList, new Comparator<Item>() {
//            @Override
//            public int compare(Item item1, Item item2) {
//                return item1.getCode().compareTo(item2.getCode());
//            }
//        });
//
//        itemLista.clear(); // Clear the existing items in the list
//        itemLista.addAll(itemList); // Add the retrieved items to the list
//        slectItemAdapter.notifyDataSetChanged();
//--------------------------------------------


//        String query = "SELECT " +
//                TABLE_NAME + "." + ITEM_CODE + ", " +
//                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
//                TABLE_NAME + "." + ITEM_RATE + ", " +
//                TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
//                TABLE_NAME + "." + ITEM_QUANTITY + ", " +
//                TABLE_NAME + "." + ITEM_GROUP + ", " +
//                UNIT_MEASURE_TABLE + "." + NAME + ", " +
//                UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
//                UNIT_MEASURE_TABLE + "." + UNIT_ID + ", " +
//                PRICE_LEVEL_LINES_TABLE + "." + PRCUSTOM_PRICE + ", " +
//                PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID +
//                " FROM " + TABLE_NAME +
//                " JOIN " + UNIT_MEASURE_TABLE +
//                " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + "ITEM_ID" +
//                " LEFT JOIN " + PRICE_LEVEL_LINES_TABLE +
//                " ON " + TABLE_NAME + "." + ITEMID + " = " + PRICE_LEVEL_LINES_TABLE + "." + PRITEM_ID;
//
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        String levelValue = level.getText().toString(); // Get the value from the level TextView
//        try (Cursor cursor = db.rawQuery(query, null)) {
//            if (cursor.moveToFirst()) {
//                do {
//                    Item item = new Item();
//                    item.setCode(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_CODE)));
//                    item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_DESCRIPTION)));
//                    item.setUnitquant(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_UNIT_MEASURE)));
//                    item.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_QUANTITY)));
//                    item.setGroup(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_GROUP)));
//
//                    Unit unit = new Unit();
//                    unit.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
//                    unit.setQuantity(cursor.getString(cursor.getColumnIndexOrThrow(QUANTITY)));
//                    unit.setUnit_id(cursor.getString(cursor.getColumnIndexOrThrow(UNIT_ID)));
//
//                    item.setUnit(unit);
//                    String customPrice = cursor.getString(cursor.getColumnIndexOrThrow(PRCUSTOM_PRICE));
//                    String priceLevelId = cursor.getString(cursor.getColumnIndexOrThrow(PRI_LEVEL_ID));
//
//
//                    if (levelValue == null || levelValue.isEmpty()) {
//                        // If levelValue is null or empty, display all items using rate
//                        item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
//
//                    } else if (priceLevelId != null && priceLevelId.equals(levelValue)) {
//                        // Display the custom price if a match is found
//                        if (customPrice != null && !customPrice.isEmpty()) {
//                            item.setRate(customPrice);
//
//                        } else {
//                            // Display the regular rate if custom price is not available
//                            item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
//                        }
//                    } else {
//                        // Display the regular rate
//                        item.setRate(cursor.getString(cursor.getColumnIndexOrThrow(ITEM_RATE)));
//                    }
//
//                    itemList.add(item);
//                } while (cursor.moveToNext());
//            }
//        } finally {
//            db.close();
//        }
//
//        itemLista.clear(); // Clear the existing items in the list
//        itemLista.addAll(itemList); // Add the retrieved items to the list
//        slectItemAdapter.notifyDataSetChanged();
//---------------------------
//            TABLE_NAME + "." + ITEM_CODE + ", " +
//            TABLE_NAME + "." + ITEM_DESCRIPTION + ", " +
//            TABLE_NAME + "." + ITEM_RATE + ", " +MIC
//            TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", " +
//            TABLE_NAME + "." + ITEM_QUANTITY + ", " +
//            TABLE_NAME + "." + ITEM_GROUP + ", " +
//            UNIT_MEASURE_TABLE + "." + NAME + ", " +
//            UNIT_MEASURE_TABLE + "." + QUANTITY + ", " +
//            UNIT_MEASURE_TABLE + "." + UNIT_ID + ", " +
//            PRICE_LEVEL_LINES_TABLE + "." + PRCUSTOM_PRICE + ", " +
//            PRICE_LEVEL_LINES_TABLE + "." + PRI_LEVEL_ID +
//            " FROM " + TABLE_NAME +
//            " JOIN " + UNIT_MEASURE_TABLE +
//            " ON " + TABLE_NAME + "." + ITEMID + " = " + UNIT_MEASURE_TABLE + "." + "ITEM_ID" +
//            " LEFT JOIN " + PRICE_LEVEL_LINES_TABLE +
//            " ON " + TABLE_NAME + "." + ITEMID + " = " + PRICE_LEVEL_LINES_TABLE + "." + PRITEM_ID;
