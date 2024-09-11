package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.ITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_UNIT_MEASURE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITM;
import static com.example.fortuneapplication.PazDatabaseHelper.NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.TABLE_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.UNIT_MEASURE_TABLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class InputQuantity extends AppCompatActivity {
    EditText q1, q2, q3, q4, q5, q6;
    TextView balik, ps, itemid, idlvl, untbase, samvalue, datasam,basihan,dew;
    Button itemsave,itemfree;

    Spinner spinner6;
    private PazDatabaseHelper mdatabaseHelper;
    ArrayList<Unit> units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_quantity);

        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);
        q6 = findViewById(R.id.q6);
        balik = findViewById(R.id.balik);
        itemsave = findViewById(R.id.itemsave);
        itemfree = findViewById(R.id.itemfree);
        ps = findViewById(R.id.ps);
        itemid = findViewById(R.id.itemid);
        idlvl = findViewById(R.id.idlvl);
        //untbase = findViewById(R.id.untbase);
        samvalue = findViewById(R.id.samvalue);
         datasam = findViewById(R.id.datasam);
         basihan = findViewById(R.id.basihan);
         dew = findViewById(R.id.dew);



        mdatabaseHelper = new PazDatabaseHelper(this);
        units = new ArrayList<>();

        ArrayList<Item> itemList = displayUOM();

        balik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gg = new Intent(InputQuantity.this, ItemPricelvlChoiceDisplay.class);
                //startActivity(gg);
            }
        });

        SharedPreferences preferences = getSharedPreferences("MyItems", Context.MODE_PRIVATE);

        String oid = preferences.getString("ITEMIO", "");
        String icode = preferences.getString("ICODE", "");
        String ides = preferences.getString("IDESCRIPTION", "");
        String iquantity = preferences.getString("IQUANT", "");
        String irate = preferences.getString("IRATE", "");
        String UU = preferences.getString("pcs", "");
        String cquantity = preferences.getString("UNITM", "");
        String Uname = preferences.getString("name", "");
        String plvlid = preferences.getString("PLVL", "");
        // Toast.makeText(this, "value"+ plvlid, Toast.LENGTH_SHORT).show();

       // Toast.makeText(this, ""+Uname, Toast.LENGTH_SHORT).show();

        q1.setText(icode);
        q2.setText(ides);
        q3.setText(iquantity);
        q4.setText(irate);
        ps.setText(UU);
        dew.setText(UU);
        itemid.setText(oid);
        idlvl.setText(plvlid);
        datasam.setText(irate);

        ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(InputQuantity.this, ps);
                Menu menu = popupMenu.getMenu();

                ArrayList<Item> items = displayUOM();

                if (!cquantity.isEmpty() && items.size() > 0) {
                    try {
                        double cquantityValue = Double.parseDouble(cquantity);
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        decimalFormat.setRoundingMode(RoundingMode.DOWN);

                        String formattedCQuantity = decimalFormat.format(cquantityValue);

                        for (Item item : items) {
                            String formatteddCQuantity = decimalFormat.format(Double.parseDouble(item.getQuantity()));
                            String formattedCaseValue = item.getUnitquant() + "(" + formatteddCQuantity + ")";
                            menu.add(formattedCaseValue);
                         //   basihan.setText(formatteddCQuantity);
                        }
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                 String selectedOption = item.getTitle().toString();

                                // Extracting the selected quantity value from the selectedOption
                                String selectedQuantityString = selectedOption.substring(selectedOption.indexOf("(") + 1, selectedOption.indexOf(")"));
                                double selectedQuantity = Double.parseDouble(selectedQuantityString);
                                String dd = datasam.getText().toString();

                                double ddValue = Double.parseDouble(dd);
                                double result = selectedQuantity * ddValue;
                                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                String formattedResult = decimalFormat.format(result);
                                q4.setText(formattedResult);
                                String quantityString = q5.getText().toString();
                                if (!quantityString.isEmpty()) {

                                    int quantity = Integer.parseInt(quantityString);
                                    double price = Double.parseDouble(q4.getText().toString());
                                    double computedValue = computeValue(quantity, price);

                                    NumberFormat numberFormat = new DecimalFormat("#,###.00");
                                    String formattedValue = numberFormat.format(computedValue);
                                    q6.setText(formattedValue);

                                } else {
                                    q6.setText("");
                                }
                                basihan.setText(selectedQuantityString);
                                ps.setText(selectedOption);

                                String selectedUOM = selectedOption.substring(0, selectedOption.indexOf("("));
                                dew.setText(selectedUOM);
                                return true;
                            }
                        });

                        popupMenu.show();

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case where cquantity is empty or there are no items
                }
            }
        });


        itemsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
              //  Intent qa = new Intent(InputQuantity.this, ItemPricelvlChoiceDisplay.class);

                if (q3.getText().toString().equals("0")) {
                    Toast.makeText(InputQuantity.this, "Out of Stock Please Select Another Item", Toast.LENGTH_SHORT).show();
                } else if (!q5.getText().toString().isEmpty() && !q6.getText().toString().isEmpty()) {

                   // String cqa = untbase.getText().toString();
                    String dataa = idlvl.getText().toString();
                    String data0 = itemid.getText().toString();
                    String data1 = q1.getText().toString();
                    String data2 = q2.getText().toString();
                    String datass = dew.getText().toString();
                    String data3 = q3.getText().toString();
                    String data4 = q4.getText().toString();
                    String data5 = q5.getText().toString();
                    String data6 = q6.getText().toString();
                    String asd = basihan.getText().toString();

                    Item2 item2 = new Item2(asd,dataa, data0, data1, data2,datass, data3, data4, data5, data6);
                    //long itemId = databaseHelper.storeOrderItem(item2);
                    databaseHelper.storeOrderItem(item2);
                    Toast.makeText(InputQuantity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                    idlvl.setText("");
                    q1.setText("");
                    q2.setText("");
                    q3.setText("");
                    q4.setText("");
                    q5.setText("");
                    q6.setText("");
                    itemid.setText("");
                   // startActivity(qa);
                    finish();
                } else {
                    Toast.makeText(InputQuantity.this, "Please Check All Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

        itemfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                //  Intent qa = new Intent(InputQuantity.this, ItemPricelvlChoiceDisplay.class);

                if (q3.getText().toString().equals("0")) {
                    Toast.makeText(InputQuantity.this, "Out of Stock Please Select Another Item", Toast.LENGTH_SHORT).show();
                } else if (!q5.getText().toString().isEmpty() && !q6.getText().toString().isEmpty()) {

                    // String cqa = untbase.getText().toString();
                    String dataa = idlvl.getText().toString();
                    String data0 = itemid.getText().toString();
                    String data1 = q1.getText().toString();
                    String data2 = q2.getText().toString();
                    String datass = dew.getText().toString();
                    String data3 = q3.getText().toString();
                    String data4 = q4.getText().toString();
                    String data5 = q5.getText().toString();
                    String data6 = q6.getText().toString();
                    String asd = basihan.getText().toString();

                    Item2 item2 = new Item2(asd,dataa, data0, data1, data2,datass, data3, "0.00", data5, "0.00");
                    //long itemId = databaseHelper.storeOrderItem(item2);
                    databaseHelper.storeOrderItem(item2);
                    Toast.makeText(InputQuantity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                    idlvl.setText("");
                    q1.setText("");
                    q2.setText("");
                    q3.setText("");
                    q4.setText("");
                    q5.setText("");
                    q6.setText("");
                    itemid.setText("");
                    // startActivity(qa);
                    finish();
                } else {
                    Toast.makeText(InputQuantity.this, "Please Check All Details", Toast.LENGTH_SHORT).show();
                }
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
                    double computedValue = computeValue(quantity, price);

                    NumberFormat numberFormat = new DecimalFormat("#,###.00");
                    String formattedValue = numberFormat.format(computedValue);
                    q6.setText(formattedValue);

                } else {
                    q6.setText("");
                }

            }
        });

    }

    //* FORMULA IN COMPUTING THE TOTAL PAYABLE IN THE S/O //*
    private double computeValue(int quantity, double price) {
        return quantity * price;
    }

    @SuppressLint("Range")
    public ArrayList<Item> displayUOM() {
        ArrayList<Item> itemhist = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("MyItems", Context.MODE_PRIVATE);
        String oid = preferences.getString("ITEMIO", "");

        itemid.setText(oid);
        String dtss = itemid.getText().toString();

        String query = "SELECT " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " + TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", '1' AS qty " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + TABLE_NAME + "." + ITEMID + " = " + dtss +
                " UNION ALL " +
                "SELECT " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " + UNIT_MEASURE_TABLE + "." + NAME + ", " + UNIT_MEASURE_TABLE + "." + QUANTITY + " " +
                "FROM " + TABLE_NAME + " " +
                "INNER JOIN " + UNIT_MEASURE_TABLE + " " +
                "ON " + UNIT_MEASURE_TABLE + "." + ITM + " = " + TABLE_NAME + "." + ITEMID + " " +
                "WHERE " + UNIT_MEASURE_TABLE + "." + ITM + " = " + dtss;

        SQLiteDatabase db = mdatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String description = cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION));
                String unitMeasure = cursor.getString(cursor.getColumnIndex(ITEM_UNIT_MEASURE));
                String quantity = cursor.getString(cursor.getColumnIndex("qty"));

                Item item = new Item();
                item.setDescription(description);
                item.setUnitquant(unitMeasure);
                item.setQuantity(quantity);

                itemhist.add(item);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemhist;
    }
    // i want every time i select in pupup  it perform
}

