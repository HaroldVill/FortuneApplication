package com.example.fortuneapplication;

import static android.provider.ContactsContract.Intents.Insert.NOTES;
import static com.example.fortuneapplication.PazDatabaseHelper.AMOUNT;
import static com.example.fortuneapplication.PazDatabaseHelper.CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDERID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDERITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_ITEMS_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SOCUSTOMER_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOIID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_AMOUNT;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_ITEM_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_RATE;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_UNIT_BASE_QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_UOM;
import static com.example.fortuneapplication.PazDatabaseHelper.TABLE_NAME;

import static java.util.Calendar.DATE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class TemporaryData extends AppCompatActivity {
    TextView ref, names,moreinfo,notesam,dt;
    ImageView hm,print;
    EditText supertot;
    RecyclerView datadisp;
//    ImageView printbutton;
    private PazDatabaseHelper mDatabaseHelper;
    private TempoDataAdapter tempoDataAdapter;
    ArrayList<SALESORDERITEMS> orderItemss;

    FloatingActionButton arayba;
    AlertDialog.Builder builder;


   // Button update ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_data);

        ref = findViewById(R.id.ref);
        datadisp = findViewById(R.id.datadisp);
        names = findViewById(R.id.names);
        supertot = findViewById(R.id.supertot);
        hm = findViewById(R.id.hm);
        notesam = findViewById(R.id.notesam);
        dt = findViewById(R.id.dt);
        arayba = findViewById(R.id.arayba);
        print = findViewById(R.id.print);
        builder = new AlertDialog.Builder(this);


        mDatabaseHelper = new PazDatabaseHelper(this);
        orderItemss = new ArrayList<>();

        datadisp.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SALESORDERITEMS> salesorderitems = displayOrderItem();
        tempoDataAdapter = new TempoDataAdapter(salesorderitems);
        datadisp.setAdapter(tempoDataAdapter);

        SharedPreferences preferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
        String oid = preferences.getString("REFID","");
        String nameses = preferences.getString("CN", "");
      //  String sprt = preferences.getString("TOT", "");
        int salesOrderId = preferences.getInt("SID", 0);

        String salesOrderIdString = String.valueOf(salesOrderId);
        ref.setText(salesOrderIdString);
        names.setText(nameses);
       //supertot.setText(sprt);


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent print_form = new Intent(TemporaryData.this, print_form.class);
                SharedPreferences preferences = TemporaryData.this.getSharedPreferences("print_form",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("SO_ID", salesOrderId);
                startActivity(print_form);
//                finish();
            }
        });

        hm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uliboy = new Intent(TemporaryData.this, HomePage.class);
                startActivity(uliboy);
                finish();
            }
        });

        arayba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("WARNING!")
                        .setMessage("Do you want to Save the Updated Data ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateton();
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

   }
    @SuppressLint("Range")
        public ArrayList<SALESORDERITEMS> displayOrderItem() {
        ArrayList<SALESORDERITEMS> histitemlist = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
        int salesOrderId = preferences.getInt("SID", 0);
        String salesOrderIdString = String.valueOf(salesOrderId);
        ref.setText(salesOrderIdString);

        String datS = ref.getText().toString();
        String query = " SELECT " +

                    SALES_ORDER_ITEMS_TABLE + "." + SOI_RATE + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_QUANTITY + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_AMOUNT + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_UOM + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_UNIT_BASE_QUANTITY +", " +
                    TABLE_NAME + "." + ITEMID + ", "+
                    TABLE_NAME + "." + ITEM_CODE + ", " +
                    TABLE_NAME + "." + ITEM_DESCRIPTION + "," +
                    SALES_ORDER_TABLE + "." + NOTES + ", " +
                    SALES_ORDER_TABLE + "." + "date" + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOIID  +

                   " FROM " + SALES_ORDER_ITEMS_TABLE +
                   " INNER JOIN " + SALES_ORDER_TABLE +
                   " ON " + SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID + " = " + SALES_ORDER_TABLE + "." + SALES_ORDERID +
                   " INNER JOIN " + TABLE_NAME +
                   " ON " + SALES_ORDER_ITEMS_TABLE + "." + SOI_ITEM_ID + " = " + TABLE_NAME + "." + ITEMID +
                    " WHERE " + SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID + " = " + datS;

            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SALESORDERITEMS salesorderitems = new SALESORDERITEMS();
                    salesorderitems.setSoiunitbasequantity(cursor.getInt(cursor.getColumnIndex(SOI_UNIT_BASE_QUANTITY)));
                    salesorderitems.setSoirate(cursor.getDouble(cursor.getColumnIndex(SOI_RATE)));
                    salesorderitems.setSoiquantity(cursor.getInt(cursor.getColumnIndex(SOI_QUANTITY)));
                    String soiAmountString = cursor.getString(cursor.getColumnIndex(SOI_AMOUNT));
                    double soiAmount = Double.parseDouble(soiAmountString.replace(",", ""));
                    salesorderitems.setSoiamount(soiAmount);
                    salesorderitems.setUom(cursor.getString(cursor.getColumnIndex(SOI_UOM)));
                    salesorderitems.setId(cursor.getInt(cursor.getColumnIndex(SOIID)));

                    SALESORDER salesorder = new SALESORDER();
                    salesorder.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));

                    Item item = new Item();
                    item.setId(cursor.getString(cursor.getColumnIndex(ITEMID)));

                    item.setCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                    item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));

                    SALESORDER salesorder11 = new SALESORDER();
                     salesorder11.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
                    salesorder11.setDate(cursor.getString(cursor.getColumnIndex("date")));

                     String notes = cursor.getString(cursor.getColumnIndex(NOTES));
                     notesam.setText(notes);
                     String dateStr = cursor.getString(cursor.getColumnIndex("date"));
                     dt.setText(dateStr);

                    salesorderitems.setSalesorder(salesorder);
                    salesorderitems.setItem(item);
                   histitemlist.add(salesorderitems);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

        double totalAmount = 0.0;
        for (SALESORDERITEMS item : histitemlist) {


            totalAmount += item.getSoiamount();
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            String formattedTotal = decimalFormat.format(totalAmount);
            supertot.setText(String.valueOf(formattedTotal));
        }

        Log.d("TemporaryData", "histitemlist size: " + histitemlist.size());
        return histitemlist;
        }

        //update sa mga item
       public void updateton(){
           String tot = supertot.getText().toString();
                String salesid = ref.getText().toString();

                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put("AMOUNT", tot);

                int rowsAffected = db.update("SALES_ORDER_TABLE", values, "SALES_ORDERID = ?", new String[]{salesid});

                if (rowsAffected > 0) {
                    Toast.makeText(getApplicationContext(), "Order updated successfully", Toast.LENGTH_SHORT).show();

                    Intent agoroy = new Intent(TemporaryData.this, History.class);
                    startActivity(agoroy);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(), "Failed to update amount", Toast.LENGTH_SHORT).show();
                }
               db.close();
       }
}
