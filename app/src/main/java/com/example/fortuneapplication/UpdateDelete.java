package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.ITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_RATE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_UNIT_MEASURE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITM;
import static com.example.fortuneapplication.PazDatabaseHelper.NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_ITEMS_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SOIID;
import static com.example.fortuneapplication.PazDatabaseHelper.TABLE_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.UNIT_MEASURE_TABLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class UpdateDelete extends AppCompatActivity {
    EditText q1, q2, q4, q5, q6;
    AlertDialog.Builder builder;
    TextView ps1, menid, balik, idlvl,datasam;

    Button updt;

    ImageView removena;

    private PazDatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);
        q6 = findViewById(R.id.q6);
        ps1 = findViewById(R.id.ps1);
        removena = findViewById(R.id.removena);
        balik = findViewById(R.id.balik);
        idlvl = findViewById(R.id.idlvl);
        datasam = findViewById(R.id. datasam);
        updt = findViewById(R.id.updt);


        menid = findViewById(R.id.menid);
        builder = new AlertDialog.Builder(this);
        mDatabaseHelper = new PazDatabaseHelper(this);

        String di = getIntent().getStringExtra("iditem");
        String code = getIntent().getStringExtra("code");
        String description = getIntent().getStringExtra("description");
        double rate = getIntent().getDoubleExtra("rate", 0.0);
        int menID = getIntent().getIntExtra("IDMEN", 0);
        int quantity = getIntent().getIntExtra("quantity", 0);
        Double amount = getIntent().getDoubleExtra("amount", 0.0);
        String uom = getIntent().getStringExtra("uom");

        idlvl.setText(di);
        q1.setText(code);
        q2.setText(description);
        q4.setText(String.valueOf(rate));
        q5.setText(String.valueOf(quantity));
        q6.setText(String.valueOf(amount));
        ps1.setText(uom);
        menid.setText(String.valueOf(menID));

        updt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


          String  uomz = ps1.getText().toString();
          String pricez = q4.getText().toString();
          String quantityz = q5.getText().toString();
          String totalpayablez = q6.getText().toString();
          String idi = menid.getText().toString();

                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("uom", uomz);
                values.put("rate", pricez);
                values.put("quantity", quantityz);
                values.put("amount", totalpayablez);

                String whereClause = "Id = ?";
                String[] whereArgs = {idi};

                int numRowsUpdated = db.update(SALES_ORDER_ITEMS_TABLE, values, whereClause, whereArgs);
              // db.close();
                if (numRowsUpdated > 0) {
                    Cursor cursor = db.rawQuery("UPDATE Sales_Order_table set amount = (SELECT SUM(rate * quantity) from Sales_Order_Items_Table where sales_order_id = (SELECT sales_order_id FROM Sales_Order_Items_Table where id ="+idi+")) where Sales_OrderID =(SELECT sales_order_id FROM Sales_Order_Items_Table where id ="+idi+")",null);
                    cursor.moveToFirst();
                    Toast.makeText(UpdateDelete.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    Toast.makeText(UpdateDelete.this, "Please Save the data after Updating ", Toast.LENGTH_LONG).show();


                    Intent lapdos = new Intent(UpdateDelete.this, TemporaryData.class);
                    startActivity(lapdos );
                    finish();

                } else {
                    Toast.makeText(UpdateDelete.this, "Please Contact Technical Support", Toast.LENGTH_SHORT).show();
                }
            }
        });

        balik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BLK = new Intent(UpdateDelete.this,TemporaryData.class);
                startActivity(BLK);
                finish();

            }
        });

        ps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(UpdateDelete.this, ps1);
                Menu menu = popupMenu.getMenu();
                ArrayList<Item> items = displaybay();

                for (Item item : items) {
                    String unitMeasure = item.getUnitquant();
                    String quantityString = item.getQuantity();

                    quantityString = quantityString.replaceAll("\\.0*$", "");
                    double quantity = Double.parseDouble(quantityString);

                    String menuText = unitMeasure + "(" + quantityString + ")";
                    menu.add(menuText);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String selectedMenuItemText = item.getTitle().toString();
                        ps1.setText(selectedMenuItemText);

                        String quantityString = selectedMenuItemText.replaceAll("[^0-9.]", "");
                        double quantity = Double.parseDouble(quantityString);
                        int int_quantity = Integer.parseInt(q5.getText().toString());

                        String agaynurse = datasam.getText().toString();
                        double X = Double.parseDouble(agaynurse);
                        double result = quantity * X;

                        DecimalFormat decimalFormat = new DecimalFormat("#.00");
                        String formattedResult = decimalFormat.format(result);
                        q4.setText(formattedResult);

                        if (!quantityString.isEmpty()) {

                            double price = Double.parseDouble(q4.getText().toString());
                            double computedValue = iphadong(int_quantity, price);

                            NumberFormat numberFormat = new DecimalFormat("#,###.00");
                            String formattedValue = numberFormat.format(computedValue);
                            q6.setText(formattedValue);

                        } else {
                            q6.setText("");
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }

        });

        removena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("WARNING!")
                        .setMessage("Do you want to Delete this Item?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteItem();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });

        q5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String quantityString = editable.toString();
                if (!quantityString.isEmpty()) {

                    int quantity = Integer.parseInt(quantityString);
                    double price = Double.parseDouble(q4.getText().toString());
                    double computedValue = iphadong(quantity, price);

                    NumberFormat numberFormat = new DecimalFormat("#,###.00");
                    String formattedValue = numberFormat.format(computedValue);
                    q6.setText(formattedValue);

                } else {
                    q6.setText("");
                }
            }
        });
    }

    private double iphadong(int quantity, double price){
        return quantity * price;
    }
    public void deleteItem() {
        String ddd = menid.getText().toString();
        SQLiteDatabase db2 = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db2.rawQuery("UPDATE Sales_Order_table set amount = (SELECT SUM(rate * quantity) from Sales_Order_Items_Table where sales_order_id = (SELECT sales_order_id FROM Sales_Order_Items_Table where id ="+ddd+") and id != "+ddd+") where Sales_OrderID =(SELECT sales_order_id FROM Sales_Order_Items_Table where id ="+ddd+")",null);
        cursor.moveToFirst();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String query = "DELETE FROM " + SALES_ORDER_ITEMS_TABLE + " WHERE " + SOIID + " = ?";
        db.execSQL(query, new String[]{ddd});
        Toast.makeText(this, "Item succesfully deleted.", Toast.LENGTH_LONG).show();
        Intent balik = new Intent(UpdateDelete.this, HomePage.class);
        startActivity(balik);
        finish();
    }

    @SuppressLint("Range")
    public ArrayList<Item> displaybay() {
        ArrayList<Item> itemhist = new ArrayList<>();

        String yawba = idlvl.getText().toString();
        String query = "SELECT " +
                TABLE_NAME + "." + ITEM_RATE + ", " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " + TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", '1' AS qty " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + TABLE_NAME + "." + ITEMID + " = " + yawba +
                " UNION ALL " +
                "SELECT " +
                TABLE_NAME + "." + ITEM_RATE + ", " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " + UNIT_MEASURE_TABLE + "." + NAME + ", " + UNIT_MEASURE_TABLE + "." + QUANTITY + " " +
                "FROM " + TABLE_NAME + " " +
                "INNER JOIN " + UNIT_MEASURE_TABLE + " " +
                "ON " + UNIT_MEASURE_TABLE + "." + ITM + " = " + TABLE_NAME + "." + ITEMID + " " +
                "WHERE " + UNIT_MEASURE_TABLE + "." + ITM + " = " + yawba;

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String rates = cursor.getString(cursor.getColumnIndex(ITEM_RATE));
                String description = cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION));
                String unitMeasure = cursor.getString(cursor.getColumnIndex(ITEM_UNIT_MEASURE));
                String quantity = cursor.getString(cursor.getColumnIndex("qty"));

                Item item = new Item();
                item.setRate(rates);
                item.setDescription(description);
                item.setUnitquant(unitMeasure);
                item.setQuantity(quantity);

                itemhist.add(item);
                datasam.setText(rates);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemhist;
    }

}






