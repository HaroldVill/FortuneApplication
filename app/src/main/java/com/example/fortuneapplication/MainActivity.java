package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
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
    private static final String TAG = "DebugLogs";
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
                String JSON_URL="";
                ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                if (!connectList.isEmpty()) {
                    x = connectList.get(0).getIp();
                    JSON_URL = "http://" + x + "/MobileAPI/items.php";
                }
//                if(i%505 ==0) {
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    try {
//                                        JSONObject obj = new JSONObject(response);
//                                        JSONArray umarray = obj.getJSONArray("data");
//                                        mDatabaseHelper.deleteexistUm();
//
//                                        for (int i = 0; i< umarray.length(); i++){
//                                            JSONObject jsonObject = umarray.getJSONObject(i);
//                                            String idum = jsonObject.getString("item_id");
//                                            String nameum = jsonObject.getString("name");
//                                            String quantityum = jsonObject.getString("quantity");
//                                            String unitid = jsonObject.getString("unit_id");
//
//                                            Unit unit = new Unit(idum,nameum,quantityum,unitid);
//                                            boolean isStored = mDatabaseHelper.storeUm(unit);
//
//                                        }
//
//
//                                        Log.d(TAG, "ItemUnits: Sync Success");
//
//                                    } catch (JSONException e) {
//                                        Log.d(TAG, "ItemUnits: Error "+e.getMessage());
//
//                                    } finally {
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d(TAG, "onErrorResponse: "+error.getMessage());
//
//                        }
//                    });
//                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//                    requestQueue.add(stringRequest);
//                }
                if(i%25==0){
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    try {
//                                        JSONObject jbo = new JSONObject(response);
//                                        JSONArray jsonArray = jbo.getJSONArray("data");
//                                        mDatabaseHelper.deleteplines();
//
//                                        for (int i = 0; i < jsonArray.length(); i++) {
//                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                            String linesid = jsonObject.getString("id");
//                                            String pricelevelid = jsonObject.getString("price_level_id");
//                                            String itemid = jsonObject.getString("item_id");
//                                            String cust = jsonObject.getString("custom_price");
//
//                                            PlevelLines_list plevelLines_list = new PlevelLines_list(linesid, pricelevelid, itemid, cust);
//
//                                            boolean isStored = mDatabaseHelper.storePLVLines(plevelLines_list);
//
//
//                                        }
//
//
//                                        Log.d(TAG, "PriceLevelSync: Success");
//
//                                    } catch (JSONException e) {
//                                        Log.e("SyncPriceleveLines", "JSON parsing error: " + e.getMessage());
//                                        e.printStackTrace();
//
//                                    } finally {
//                                        // Hide the progress bar
//
//                                    }
//
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d(TAG, "onErrorResponse: "+error.getMessage());
//
//                        }
//                    });
//                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//                    requestQueue.add(stringRequest);
                }
                if(i%15 == 0){

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject obj = new JSONObject(response);
                                        JSONArray itemArray = obj.getJSONArray("data");

                                        // Delete existing data from the table before syncing new data
                                        mDatabaseHelper.deleteExistingData();

                                        for (int i = 0; i < itemArray.length(); i++) {
                                            JSONObject jsonObject = itemArray.getJSONObject(i);

                                            String id = jsonObject.getString("id");
                                            String code = jsonObject.getString("code");
                                            String description = jsonObject.getString("description");
                                            String rate = jsonObject.getString("rate");
                                            String group = jsonObject.getString("group");
                                            String quant = jsonObject.getString("qty");
                                            String uom = jsonObject.getString("uom");
                                            String vend = jsonObject.getString("vendor");

                                            Item item = new Item(id, code, description, rate, group, quant, uom, vend);

                                            boolean isStored = mDatabaseHelper.StoreData(item);

                                        }


                                        Log.d(TAG, "ItemSync:  Success");;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "ItemSync: Error "+e.getMessage());
                                    } finally {
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: "+ error.getMessage());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }
                if (i % 10 == 0) {
                    int sales_order_id = mDatabaseHelper.get_open_sales_order();
                    if(sales_order_id !=0){
                        try {
                            ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                            if (!connectList2.isEmpty()) {
                                x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
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



