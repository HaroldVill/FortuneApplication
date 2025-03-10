package com.example.fortuneapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity implements LocationListener {
    private static final long MIN_TIME_BW_UPDATES = 1000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    ImageView ie, bots1, bots2, bots3, bots4, sin;
    CardView c1, c2, c3, c4,sfaid;
    AlertDialog.Builder builder;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_USERNAME = "username";
    private static final int REQUEST_LOCATION = 1;
    Button getlocationBtn;
    String latitude, longitude;
    LocationManager locationManager;
    LocationRequest locationRequest;
    private Location location;
    LocationListener locationListener;
    TextView coordinates;
    private volatile boolean stopThread = false;
    private static final String TAG = "DebugLogs";
    private PazDatabaseHelper mDatabaseHelper;
    ImageView sfa_button;
    private String api_url, x;
    RequestQueue request_queue;
    Criteria criteria;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        ImageView bots1 = findViewById(R.id.bots1);
//        ImageView bots2 = findViewById(R.id.bots2);
//        ImageView bots3 = findViewById(R.id.bots3);
//        ImageView bots4 = findViewById(R.id.bots4);
        ImageView sin = findViewById(R.id.sin);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        ie = findViewById(R.id.ie);
        sfaid = findViewById(R.id.sfaid);
        sfa_button = findViewById(R.id.imageView4);
        coordinates = findViewById(R.id.coordinates);
        startThread();
        mDatabaseHelper = new PazDatabaseHelper(this.context);
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps
            OnGPS();
        }
        else
        {
            //GPS is already On then
            new Timer().schedule(new TimerTask(){
                @Override
                public void run(){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String get_coordinates=getLocation();
                            coordinates.setText(get_coordinates);
                            Log.i("GPS", get_coordinates);
                        }
                    });
                }
            },0,60000);

        }



        ie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(HomePage.this);
                logoutBuilder.setTitle("Do you want to LogOut?");
                logoutBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(PREF_USERNAME);
                        editor.apply();

                        // Navigate to the login page
                        Intent loginIntent = new Intent(HomePage.this, MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                });
                logoutBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog logoutDialog = logoutBuilder.create();
                logoutDialog.show();
            }
        });

        sin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder passwordBuilder = new AlertDialog.Builder(HomePage.this);
                passwordBuilder.setTitle("Enter Password");
                final EditText input = new EditText(getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordBuilder.setView(input);
                passwordBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (password.equals("admin")) {
                            Intent ff = new Intent(HomePage.this, SyncDatas.class);
                            startActivity(ff);
                            Toast.makeText(HomePage.this, "Welcome Admin", Toast.LENGTH_LONG).show();
//                            finish();
                        } else {
                            Toast.makeText(HomePage.this, "You are not Authorized", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                passwordBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog passwordDialog = passwordBuilder.create();
                passwordDialog.show();
            }
        });

        sfa_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage.this, "Welcome Admin", Toast.LENGTH_LONG).show();
            }
        });
//        ie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.setTitle("Do you want to LogOut?");
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//});
//
//
//        sin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                builder.setTitle("Enter Password");
//
//                final EditText input = new EditText(getApplicationContext());
//                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                builder.setView(input);
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String password = input.getText().toString();
//                        if (password.equals("admin")) {
//                            Intent ff = new Intent(HomePage.this, SyncDatas.class);
//                            startActivity(ff);
//                            Toast.makeText(HomePage.this, "Welcome Admin", Toast.LENGTH_LONG).show();
//                            finish();
//                        } else {
//                            Toast.makeText(HomePage.this, "You are not Authorize", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//
//            }
//        });



        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_sync = mDatabaseHelper.get_max_sync_history();
                if(check_sync!=""){
                    Toast.makeText(HomePage.this, "Please sync "+check_sync+".", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent f = new Intent(HomePage.this,CustoDisplay.class );
                startActivity(f);

            }
        });


        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_sync = mDatabaseHelper.get_max_sync_history();
                if(check_sync!=""){
                    Toast.makeText(HomePage.this, "Please sync "+check_sync+".", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent P = new Intent(HomePage.this,ItemListDisplay.class );
                startActivity(P);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_sync = mDatabaseHelper.get_max_sync_history();
                if(check_sync!=""){
                    Toast.makeText(HomePage.this, "Please sync "+check_sync+".", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent PO = new Intent(HomePage.this,SOActivity.class );
                startActivity(PO);

            }
        });

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_sync = mDatabaseHelper.get_max_sync_history();
                if(check_sync!=""){
                    Toast.makeText(HomePage.this, "Please sync "+check_sync+".", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent nb = new Intent(HomePage.this,History.class);
                startActivity(nb);

            }
        });

        sfaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check_sync = mDatabaseHelper.get_max_sync_history();
                if(check_sync!=""){
                    Toast.makeText(HomePage.this, "Please sync "+check_sync+".", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent nb = new Intent(HomePage.this, SFAItemDisplay.class);
                startActivity(nb);

            }
        });

    }



    public void startThread() {
        stopThread = true;
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

    @Override
    public void onLocationChanged(@NonNull Location location) {

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
            mDatabaseHelper = new PazDatabaseHelper(HomePage.this);
            for (int i = 1; i < seconds; i++) {
                String JSON_URL="";
                ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                if (!connectList.isEmpty()) {
                    x = connectList.get(0).getIp();
                    JSON_URL = "http://" + x + "/MobileAPI/items.php";
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
                                    jsonObject.put("location", salesOrderItems.getLocationId());
                                    json_soitems.put(jsonObject);
                                }
                                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                                        response -> {Log.d("Success","Success");
                                                if(response.contains("succesfully") || response.contains("has already been")){
                                                    mDatabaseHelper.update_so_status(sales_order_id);}
                                                else{
                                                    mDatabaseHelper.update_so_status_error(sales_order_id);
                                                }
                                            },
                                        error -> Log.d("Error","Connection Error")){

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params =new HashMap<>();
                                        params.put("refno", salesOrder.getCode().toString());
                                        params.put("customer_id", salesOrder.getCustomer().getId().toString());
                                        params.put("total", salesOrder.getAmount().toString());
                                        params.put("date", salesOrder.getDate());
                                        params.put("sales_rep_id", Integer.toString(salesOrder.getSalesrepid()));
                                        params.put("location_id",Integer.toString(salesOrder.getLocationid()));
                                        params.put("begin_order", salesOrder.get_begin_order());
                                        params.put("end_order", salesOrder.get_end_order());
                                        params.put("sales_order_items", json_soitems.toString());
                                        return params;
                                    }
                                };
                                request_queue = Volley.newRequestQueue(HomePage.this);
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
                if(i%5==0){
                    int customer_skip_id = mDatabaseHelper.get_unsynced_skipped_orders();
                    String default_salesrep_id = mDatabaseHelper.get_default_salesrep_id();
                    if(customer_skip_id !=0){
                        try {
                            ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                            if (!connectList2.isEmpty()) {
                                x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
                                String sales_type = mDatabaseHelper.sales_type();
                                Log.d("sales_type",sales_type);
                                api_url = "http://" + x + "/MobileAPI/sync_customer_skip.php";
                            }
//                            PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
                            List<SALESORDER> salesOrderList = mDatabaseHelper.get_customer_skip_order(customer_skip_id);
                            for (SALESORDER salesOrder : salesOrderList) {
                                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                                        response -> {Log.d("Success","Success");
                                            if(response.contains("succesfully") || response.contains("has already been")){
                                                mDatabaseHelper.update_customer_skip_status(customer_skip_id);}},
                                        error -> Log.d("Error","Connection Error")){

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params =new HashMap<>();
                                        params.put("customer_id", Integer.toString(salesOrder.getCustomerid()));
                                        params.put("sales_rep_id", default_salesrep_id);
                                        params.put("datetime", salesOrder.get_end_order());
                                        params.put("reason", salesOrder.get_reason());
                                        return params;
                                    }
                                };
                                request_queue = Volley.newRequestQueue(HomePage.this);
                                request_queue.add(send_invoices);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                }
                if(i%5==0){
                    int customer_repin_id = mDatabaseHelper.get_unsynced_request_repin();
                    if(customer_repin_id !=0){
                        try {
                            ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                            if (!connectList2.isEmpty()) {
                                x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
                                String sales_type = mDatabaseHelper.sales_type();
                                Log.d("sales_type",sales_type);
                                api_url = "http://" + x + "/MobileAPI/sync_request_repin.php";
                            }
//                            PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
                            List<SALESORDER> salesOrderList = mDatabaseHelper.get_repin_customer(customer_repin_id);
                            for (SALESORDER salesOrder : salesOrderList) {
                                Log.d(TAG, Integer.toString(salesOrder.getCustomerid()));
                                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                                        response -> {Log.d("Success","Success");
                                            if(response.contains("succesfully") || response.contains("has already been")){
                                                mDatabaseHelper.update_customer_repin_status(customer_repin_id);}},
                                        error -> Log.d("Error","Connection Error")){

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params =new HashMap<>();
                                        params.put("customer_id", Integer.toString(salesOrder.getCustomerid()));
                                        params.put("date", salesOrder.get_end_order());
                                        return params;
                                    }
                                };
                                request_queue = Volley.newRequestQueue(HomePage.this);
                                request_queue.add(send_invoices);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                }
                if(i%10 == 0){
                    int customer_id = mDatabaseHelper.get_customer_pin_flag();
                    if(customer_id >=0){
                        try {
                            ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                            if (!connectList2.isEmpty()) {
                                x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
                                api_url = "http://" + x + "/MobileAPI/update_customer_pin.php";
                            }
                            String longitude=mDatabaseHelper.get_customer_longitude(customer_id);
                            String latitude=mDatabaseHelper.get_customer_latitude(customer_id);
                            String verify_pin= mDatabaseHelper.get_verify_pin(customer_id);
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
                                    params.put("verify", verify_pin);
                                    return params;
                                }
                            };
                            request_queue = Volley.newRequestQueue(HomePage.this);
                            request_queue.add(send_customer_pin);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
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

    private String getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(HomePage.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomePage.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
            else
                if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.d("LOCATION", "Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
                if (LocationPassive !=null)
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
        return ("MY CURRENT LOCATION\n\n"+"Longitude: "+longitude+"\nLatitude: "+latitude);
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
}