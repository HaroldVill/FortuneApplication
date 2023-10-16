package com.example.fortuneapplication;

import static android.provider.ContactsContract.Intents.Insert.NOTES;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_ADDRESS;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.LOCATION_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.LOCATION_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.LOCATION_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.MOBILE_NO;
import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.SALESREP_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDERID;;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SOCUSTOMER_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOLOCATION_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOSALESREPID;

import static java.util.Calendar.DATE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class MoreInfoData extends AppCompatActivity {

    private PazDatabaseHelper mDatabaseHelper;
    TextView mr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info_data);
         mr = findViewById(R.id.mr);
        mDatabaseHelper = new PazDatabaseHelper(this);

        SharedPreferences preferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
        int salesOrderId = preferences.getInt("SID", 0);
        String salesOrderIdString = String.valueOf(salesOrderId);
        mr.setText(salesOrderIdString);
    }

    @SuppressLint("Range")
    public ArrayList<SALESORDER> displayOrderItem() {
        ArrayList<SALESORDER> histitemlists= new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
        int salesOrderId = preferences.getInt("SID", 0);
        String salesOrderIdString = String.valueOf(salesOrderId);
        mr.setText(salesOrderIdString);

        String datS = mr.getText().toString();
        String query = " SELECT " +

                SALES_ORDER_TABLE + "." + SOCUSTOMER_ID + ", " +
                SALES_ORDER_TABLE + "." + DATE + ", " +
                SALES_ORDER_TABLE + "." + NOTES + ", " + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_NAME + ", " +
                CUSTOMER_TABLE + "." + CUSTOMER_ADDRESS + ", " +
                CUSTOMER_TABLE + "." + MOBILE_NO + ", " +
                SALESREP_TABLE + "." +  SALESREP_NAME + ", " +
                LOCATION_TABLE + "." + LOCATION_NAME +

                " FROM " + SALES_ORDER_TABLE +
                " INNER JOIN " + CUSTOMER_TABLE +
                " ON " + SALES_ORDER_TABLE + "." + SOCUSTOMER_ID + " = " + CUSTOMER_TABLE + "." + CUSTOMER_ID +
                " INNER JOIN " + SALESREP_TABLE +
                " ON " + SALES_ORDER_TABLE + "." + SOSALESREPID + " = " + SALESREP_TABLE + "." +  SALESREP_ID +
                " INNER JOIN " + LOCATION_TABLE +
                " ON " + SALES_ORDER_TABLE + "." + SOLOCATION_ID + " = " + LOCATION_TABLE + "." + LOCATION_ID +
                " WHERE " + SALES_ORDER_TABLE + "." + SALES_ORDERID + " = " + datS;

                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

            SALESORDER salesorder = new SALESORDER();
            salesorder.setSalesorderid(cursor.getInt(cursor.getColumnIndex(SOCUSTOMER_ID)));
            salesorder.setDate(cursor.getString(cursor.getColumnIndex(String.valueOf(DATE))));
            salesorder.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));

           Customer customer = new Customer();
           customer.setCustomername(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
           customer.setPostaladdress(cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS)));
           customer.setMobilenumber(cursor.getString(cursor.getColumnIndex(MOBILE_NO)));

           SalesRepList salesRepList = new SalesRepList();
           salesRepList.setSrname(cursor.getString(cursor.getColumnIndex(SALESREP_NAME)));

           Location location = new Location();
           location.setLocname(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));


           salesorder.setSalesRepList(salesRepList);
           salesorder.setLocation(location);
           salesorder.setCustomer(customer);
           histitemlists.add(salesorder);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        Log.d("TemporaryData", "histitemlist size: " + histitemlists.size());
        return histitemlists;
    }

}
//
//    ArrayList<SALESORDER> salesOrders = displayOrderItem();
//
//// Display data in the TextViews
//if (!salesOrders.isEmpty()) {
//        SALESORDER order = salesOrders.get(0); // Assuming you want to display data from the first sales order
//
//        textView1.setText("Customer Name: " + order.getCustomer().getCustomername());
//        textView2.setText("Address: " + order.getCustomer().getPostaladdress());
//        textView3.setText("Mobile Number: " + order.getCustomer().getMobilenumber());
//        textView4.setText("Sales Rep Name: " + order.getSalesRepList().getSrname());
//        textView5.setText("Location: " + order.getLocation().getLocname());
//        textView6.setText("Date: " + order.getDate());
//        textView7.setText("Notes: " + order.getNotes());
//        } else {
//        // Handle the case when no sales order data is retrieved
//        textView1.setText("No sales order data available");
//        textView2.setText("");
//        textView3.setText("");
//        textView4.setText("");
//        textView5.setText("");
//        textView6.setText("");
//        textView7.setText("");
//        }