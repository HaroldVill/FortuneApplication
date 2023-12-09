package com.example.fortuneapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.location.LocationRequest;
import android.os.Build;
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

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.Manifest;

import android.provider.Settings;



public class MainActivity extends AppCompatActivity  {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    private SharedPreferences sharedPreferences;
    private boolean hasLoggedOut = false;
    private volatile boolean stopThread = false;
    private static final String TAG = "DebugLogs";
    private PazDatabaseHelper mDatabaseHelper;
    private String api_url, x;
    RequestQueue request_queue;
    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Created", "1");
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        run();
        startThread();
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps

//            OnGPS();
        }
        else
        {
            //GPS is already On then

            getLocation();
        }
    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
        private Context context1;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            Log.d("StartThread", Integer.toString(seconds));
            mDatabaseHelper = new PazDatabaseHelper(MainActivity.this);
            for (int i = 1; i < seconds; i++) {
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
                if(i%90 == 0){

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject obj = new JSONObject(response);
                                        JSONArray itemArray = obj.getJSONArray("data");

                                        // Delete existing data from the table before syncing new data
//                                        mDatabaseHelper.deleteExistingData();

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
                if (i % 20 == 0) {
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
                if(i%10 == 0){
                    int customer_id = mDatabaseHelper.get_customer_pin_flag();
                    if(customer_id !=0){
                        try {
                            ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                            if (!connectList2.isEmpty()) {
                                x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
                                api_url = "http://" + x + "/MobileAPI/update_customer_pin.php";
                            }
                            String longitude=mDatabaseHelper.get_customer_longitude(customer_id);
                            String latitude=mDatabaseHelper.get_customer_latitude(customer_id);
                            StringRequest send_customer_pin = new StringRequest(Request.Method.POST, api_url,
                                    response -> {Log.d("Success","Success");
                                    if(response.contains("succesfully") || response.contains("has already been")){
                                    mDatabaseHelper.update_customer_pin_flag(customer_id);}},
                                    error -> Log.d("Error","Connection Error")){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params =new HashMap<>();
                                    params.put("customer_id", Integer.toString(customer_id));
                                    params.put("longitude", longitude);
                                    params.put("latitude", latitude);
                                    return params;
                                }
                            };
                            request_queue = Volley.newRequestQueue(MainActivity.this);
                            request_queue.add(send_customer_pin);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                }
                if(i%565 ==0){
                    ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                    if (!connectList2.isEmpty()) {
                        x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
                        api_url = "http://" + x + "/MobileAPI/customers.php";
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, api_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        JSONArray customerArray = obj.getJSONArray("data");

                                        // Delete existing data from the table before syncing new data
//                            databaseHelper.deleteCustomerData();

                                        for (int i = 0 ; i < customerArray.length(); i++){
                                            JSONObject jsonObject = customerArray.getJSONObject(i);

                                            String code = jsonObject.getString("id");
                                            String cname = jsonObject.getString("customername");
                                            String caddres = jsonObject.getString("postal_address");
                                            String cperson = jsonObject.getString("contact_person");
                                            String ctelephone = jsonObject.getString("TELEPHONE_NO");
                                            String cmobile = jsonObject.getString("mobile_no");
                                            String cpaymentterm = jsonObject.getString("PAYMENT_TERMS_ID");
                                            String csalesrep = jsonObject.getString("sales_rep_id");
                                            String cpricelevel = jsonObject.getString("PRICE_LEVEL_ID");
                                            String longitude = jsonObject.getString("LONGITUDE");
                                            String latitude = jsonObject.getString("LATITUDE");
                                            Customer customer = new Customer(code,cname,caddres,cperson,ctelephone,cmobile,cpaymentterm,csalesrep,cpricelevel,longitude,latitude);

                                            boolean isStored = mDatabaseHelper.StroreCustomer(customer);
                                        }
                                        Log.d(TAG, "CustomerSync:  Success");;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "CustomerSync: Error "+e.getMessage());

                                    } finally {
                                        // Hide the progress bar
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onCustomerErrorResponse: "+ error.getMessage());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
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



