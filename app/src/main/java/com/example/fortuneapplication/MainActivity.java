package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;


import android.os.Handler;
import android.os.Looper;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    private SharedPreferences sharedPreferences;
    private boolean hasLoggedOut = false;
    private volatile boolean stopThread = false;
    private static final String TAG = "MainActivity";
    private PazDatabaseHelper mDatabaseHelper;
    private String api_url,x;
    RequestQueue request_queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseHelper = new PazDatabaseHelper(this);
        Log.d("Created","1");
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        run();
        startThread();

    }



    private void run() {
        String savedUsername = sharedPreferences.getString(PREF_USERNAME, "");

        if (!savedUsername.isEmpty() && !hasLoggedOut) {
            // User is already logged in, proceed to the home page
            Intent hm = new Intent(MainActivity.this, HomePage.class);
            startActivity(hm);
            finish();
        } else if (!savedUsername.isEmpty() && hasLoggedOut) {
            // User has logged out, clear the shared preferences and proceed to the login page
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent t = new Intent(MainActivity.this, LogIn.class);
            startActivity(t);
            Toast.makeText(MainActivity.this, "WELCOME TO PAZ_DISTRIBUTION APP", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // User is not logged in, proceed to the login page
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent t = new Intent(MainActivity.this, LogIn.class);

                    startActivity(t);

                    Toast.makeText(MainActivity.this, "WELCOME TO PAZ_DISTRIBUTION APP", Toast.LENGTH_LONG).show();

                    finish();
                }
            }, 1500);
        }
    }

    public void startThread() {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable(30000);
        new Thread(runnable).start();
        Log.d("Test", "1");
        /*
        ExampleThread thread = new ExampleThread(10);
        thread.start();
        */
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //work
            }
        }).start();
        */
    }



    class ExampleRunnable implements Runnable {
        int seconds;
        private Context context;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            Log.d("StartThread", Integer.toString(seconds));
            for (int i = 0; i < seconds; i++) {
//                if (1!=0)
//                    return;
                if (i % 10 == 0) {
                    int sales_order_id = mDatabaseHelper.get_open_sales_order();
                    if(sales_order_id !=0){
                        try {
                            ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                            if (!connectList.isEmpty()) {
                                x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
                                String sales_type = mDatabaseHelper.sales_type();
                                Log.d("sales_type",sales_type);
                                api_url = "http://" + x + "/MobileAPI/"+sales_type;
                            }
//                            PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
                            List<SALESORDER> salesOrderList = mDatabaseHelper.getSlsorder(sales_order_id);
                            for (SALESORDER salesOrder : salesOrderList) {
                                JSONArray json_soitems = new JSONArray();
                                List<SALESORDERITEMS> salesOrderItemList = mDatabaseHelper.getSlsorderitems(sales_order_id);
                                for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("item_id", salesOrderItems.getSoiitemid());
                                    jsonObject.put("quantity", salesOrderItems.getSoiquantity());
                                    jsonObject.put("rate", salesOrderItems.getSoirate());
                                    jsonObject.put("amount", salesOrderItems.getSoiamount());
                                    jsonObject.put("unit_base_qty", salesOrderItems.getSoiunitbasequantity());
                                    jsonObject.put("uom", salesOrderItems.getUom());
                                    jsonObject.put("price_level_id", salesOrderItems.getSoipricelevelid());
                                    json_soitems.put(jsonObject);
                                }
                                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                                        response -> {Log.d("Success","Success");
                                            if(response.contains("succesfully") || response.contains("has already been")){
                                                mDatabaseHelper.update_so_status(sales_order_id);}},
                                        error -> Log.d("Error","Connection Error")){

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params =new HashMap<>();
                                        params.put("refno", salesOrder.getCode().toString());
                                        params.put("customer_id", salesOrder.getCustomer().getId().toString());
                                        params.put("total", salesOrder.getAmount().toString());
                                        params.put("date", salesOrder.getDate());
                                        params.put("sales_rep_id", Integer.toString(salesOrder.getSalesrepid()));
                                        params.put("sales_order_items", json_soitems.toString());
                                        return params;
                                    }
                                };
                                request_queue = Volley.newRequestQueue(MainActivity.this);
                                request_queue.add(send_invoices);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
//                    Log.d("sales_order_id", Integer.toString(sales_order_id));
//                    sync(sales_order_id);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            buttonStartThread.setText("50%");
//                        }
//                    });
                }
                Log.d(TAG, "ThreadTicker: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



