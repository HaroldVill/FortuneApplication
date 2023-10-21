package com.example.fortuneapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class History extends AppCompatActivity {
    private static final String SERVER_URL = "http://192.168.254.230:8082/mobileapi/sync_sales_order.php";
    private static final String AUTHORIZATION_HEADER = "Bearer YourAuthToken";
    AlertDialog.Builder builder;
    RecyclerView histview;
    ImageView homes, del, sinkme;
    HistoryAdapter historyAdapter;
    TextView booktot, numcustomer;
    FloatingActionButton aray;

    RequestQueue request_queue;

    private PazDatabaseHelper mDatabaseHelper;
    private List<HistoryGetSet> historyGetSets = new ArrayList<>();
    private String api_url;
    private String x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        builder = new AlertDialog.Builder(this);
        histview = findViewById(R.id.histview);
        homes = findViewById(R.id.homes);
        del = findViewById(R.id.del);
        booktot = findViewById(R.id.booktot);
        numcustomer = findViewById(R.id.numcustomer);
        aray = findViewById(R.id.aray);
        // sinkme = findViewById(R.id.sinkme);


        mDatabaseHelper = new PazDatabaseHelper(this);
        ArrayList<SALESORDER> salesOrderList = mDatabaseHelper.getSlsorder(0);
        ArrayList<SALESORDERITEMS> salesOrderItemsList = mDatabaseHelper.getSlsorderitems(0);
        historyAdapter = new HistoryAdapter(salesOrderList, this,salesOrderItemsList);
        histview.setLayoutManager(new LinearLayoutManager(this));
        histview.setAdapter(historyAdapter);

        //get total booking
        double totalAmount = mDatabaseHelper.getBookTotal();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        String formattedTotal = decimalFormat.format(totalAmount);
        booktot.setText(formattedTotal);

        int listdata = mDatabaseHelper.countData();
        numcustomer.setText(String.valueOf(listdata));


        aray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                    if (!connectList.isEmpty()) {
                        x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
                        api_url = "http://" + x + "/MobileAPI/sync_sales_order.php";
                    }

                    PazDatabaseHelper dbHelper = new PazDatabaseHelper(getApplicationContext());
                    List<SALESORDER> salesOrderList = dbHelper.getSlsorder(0);
                    for (SALESORDER salesOrder : salesOrderList) {
                        //For Items
                        JSONArray json_soitems = new JSONArray();
                        List<SALESORDERITEMS> salesOrderItemList = dbHelper.getSlsorderitems(0);
                        for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("item_id", salesOrderItems.getSoiitemid());
                            jsonObject.put("quantity", salesOrderItems.getSoiquantity());
                            jsonObject.put("rate", salesOrderItems.getSoirate());
                            jsonObject.put("amount", salesOrderItems.getSoiamount());
                            jsonObject.put("unit_base_qty", salesOrderItems.getSoiunitbasequantity());
                            jsonObject.put("uom", salesOrderItems.getUom());
                            json_soitems.put(jsonObject);
                        }
                        Log.v("so_items",json_soitems.toString());
//                        RequestFuture <JSONObject> future = RequestFuture.newFuture();
                        StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                                response -> Toast.makeText(History.this, response, Toast.LENGTH_LONG).show(),
                                error -> Toast.makeText(History.this, "Connection Error", Toast.LENGTH_LONG).show()){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError{
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
                        request_queue = Volley.newRequestQueue(History.this);
                        request_queue.add(send_invoices);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(History.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("WARNING")
                        .setMessage("Do you want to delete all Transactions?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //save data to dashboard
                                long insertedRowCount = mDatabaseHelper.insertDataIntoDashboard();

                                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                                databaseHelper.deletanan();
                                Toast.makeText(History.this, "Successfully Deleted", Toast.LENGTH_LONG).show();

//                                long insertedRowCount = mDatabaseHelper.insertDataIntoDashboard();
//                                Toast.makeText(History.this, "SUCCESSFULLY SAVE", Toast.LENGTH_SHORT).show();
//                                Intent dg = new Intent(History.this, Profileman.class);
//                                startActivity(dg);

                                Intent kepoy = new Intent(History.this, SOActivity.class);
                                startActivity(kepoy);
                                finish();
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
        homes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hm = new Intent(History.this, HomePage.class);
                startActivity(hm);
                finish();
            }
        });
    }

}









