package com.example.fortuneapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class History extends AppCompatActivity {
    private static final String AUTHORIZATION_HEADER = "Bearer YourAuthToken";
    AlertDialog.Builder builder;
    RecyclerView histview;
    ImageView homes, del, sinkme;
    HistoryAdapter historyAdapter;
    TextView booktot, numcustomer;
    FloatingActionButton aray;
    Button principal_breakdown;
    EditText history_datepicker;
    final Calendar history_calendar= Calendar.getInstance();

    RequestQueue request_queue;
    EditText searchbaritem;
    private static final String TAG = "HistoryLogs";
    private volatile boolean stopThread = false;
    private PazDatabaseHelper mDatabaseHelper;
    private List<HistoryGetSet> historyGetSets = new ArrayList<>();
    private String api_url;
    private String x;
    private ArrayList<SALESORDER> filter_history = new ArrayList<>();


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
        principal_breakdown = findViewById(R.id.principal_breakdown);
        searchbaritem = findViewById(R.id.searchbaritem);
        history_datepicker = findViewById(R.id.history_datepicker);
        // sinkme = findViewById(R.id.sinkme);

        LocalDateTime datenow = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = datenow.format(myFormatObj);
        String end_order_time = formattedDate.toString();
        history_datepicker.setText(end_order_time);
        populate_table(end_order_time);



        filter_history.addAll(mDatabaseHelper.history_getSlsorder(history_datepicker.getText().toString()));
        historyAdapter.notifyDataSetChanged();
        startThread();



        searchbaritem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter_history.clear();
                filter_history.addAll(mDatabaseHelper.history_getSlsorder(history_datepicker.getText().toString()));
                historyAdapter.notifyDataSetChanged();
                filterList(s.toString());
                Log.d("search", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                history_calendar.set(Calendar.YEAR, year);
                history_calendar.set(Calendar.MONTH,month);
                history_calendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
                Log.d("check_date_changed", history_datepicker.getText().toString());
                populate_table(history_datepicker.getText().toString());
            }
        };
        history_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(History.this,date,history_calendar.get(Calendar.YEAR),history_calendar.get(Calendar.MONTH),history_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        principal_breakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_principal_breakdown  = new Intent(History.this,print_form.class);
                open_principal_breakdown.putExtra("date",history_datepicker.getText().toString());
                startActivity(open_principal_breakdown);
            }
        });

        aray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                    if (!connectList.isEmpty()) {
                        String sales_type = mDatabaseHelper.sales_type();
                        x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
                        api_url = "http://" + x + "/MobileAPI/"+sales_type;
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

    public void populate_table(String date){
        Log.d("check_date_value", date);
        mDatabaseHelper = new PazDatabaseHelper(this);
        ArrayList<SALESORDER> salesOrderList = mDatabaseHelper.history_getSlsorder(date);
        ArrayList<SALESORDERITEMS> salesOrderItemsList = mDatabaseHelper.getSlsorderitems(0);
        historyAdapter = new HistoryAdapter(salesOrderList, this,salesOrderItemsList);
        histview.setLayoutManager(new LinearLayoutManager(this));
        histview.setAdapter(historyAdapter);
        //get total booking
        double totalAmount = mDatabaseHelper.getBookTotal(date);
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        String formattedTotal = decimalFormat.format(totalAmount);
        booktot.setText(formattedTotal);

        int listdata = mDatabaseHelper.countData(date);
        numcustomer.setText(String.valueOf(listdata));
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.TAIWAN);
        history_datepicker.setText(dateFormat.format(history_calendar.getTime()));
    }

    public void filterList(String text) {
        List<SALESORDER> filteredList = new ArrayList<>();
        for (SALESORDER so : filter_history) {
            Log.d("test_search", so.getCode().toString().toLowerCase());
            if (so.get_customer_name().toString().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(so);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "NO MATCH DATA", Toast.LENGTH_SHORT).show();
        } else {
            historyAdapter.setFilterdList(filteredList);
        }

        historyAdapter.notifyDataSetChanged();

    }

    public void startThread() {
        stopThread = true;
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable(20);
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
            mDatabaseHelper = new PazDatabaseHelper(History.this);
            for (int i = 1; i < seconds; i++) {
                String JSON_URL="";
                ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                if (!connectList.isEmpty()) {
                    x = connectList.get(0).getIp();
                    JSON_URL = "http://" + x + "/MobileAPI/items.php";
                }
                if (i % 15 == 0) {
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
                                    jsonObject.put("inventory",salesOrderItems.getInventory());
                                    jsonObject.put("wsr",salesOrderItems.getWsr());
                                    jsonObject.put("suggested",salesOrderItems.getSuggested());
                                    jsonObject.put("inv_uom",salesOrderItems.getInvUom());
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
                                request_queue = Volley.newRequestQueue(History.this);
                                request_queue.add(send_invoices);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }

                }

                if (i % 25 == 0) {
                    int sales_order_id = mDatabaseHelper.get_open_sales_order_witherror();
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
                                    jsonObject.put("inventory",salesOrderItems.getInventory());
                                    jsonObject.put("wsr",salesOrderItems.getWsr());
                                    jsonObject.put("suggested",salesOrderItems.getSuggested());
                                    jsonObject.put("inv_uom",salesOrderItems.getInvUom());
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
                                request_queue = Volley.newRequestQueue(History.this);
                                request_queue.add(send_invoices);
                            }
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

}









