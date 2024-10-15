package com.example.fortuneapplication;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.Toast;

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

public class CustomerSalesHistory extends AppCompatActivity {
    TextView title;
    private PazDatabaseHelper mdatabaseHelper;
    private String JSON_URL;
    RecyclerView item_history;
    SalesHistoryAdapter salesHistoryAdapter;
    private String x;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);
        Intent intent = getIntent();
        String code = intent.getStringExtra("customerid");
        String description = intent.getStringExtra("customername");
        String date_from = "";
        String date_to = "";
        item_history = findViewById(R.id.datadisp);
        title = findViewById(R.id.item_valuation_title);
        Log.d("ds", "ItemValuationTitle: "+description+"\n"+date_from+" - "+date_to);
        title.setText(description+"\n"+date_from+" - "+date_to);
        final PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        String location_id = databaseHelper.get_default_location_id();

        ArrayList<CONNECT> connectList = databaseHelper.SelectUPDT();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp();
            JSON_URL = "http://" + x + "/MobileAPI/get_customersales_history.php";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL+"?customer_id="+code+"&location_id="+location_id+"&date_from="+date_from+"&date_to="+date_to,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray itemArray = obj.getJSONArray("data");

                            // Delete existing data from the table before syncing new data
//                            databaseHelper.deleteExistingData();
                            ArrayList itemList = new ArrayList<>();

                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject jsonObject = itemArray.getJSONObject(i);
                                String date = jsonObject.getString("date")+"\n"+jsonObject.getString("location");
                                String refno = jsonObject.getString("refno");
                                String itemdesc = jsonObject.getString("itemdesc");
                                String quantity = jsonObject.getString("quantity");
                                String uom = jsonObject.getString("uom");

                                Item item =new Item(date, refno, itemdesc, quantity, uom,"","","","");
                                itemList.add(item);

                            }

                            salesHistoryAdapter = new SalesHistoryAdapter(CustomerSalesHistory.this,itemList);
                            item_history.setLayoutManager(new LinearLayoutManager(CustomerSalesHistory.this));
                            item_history.setAdapter(salesHistoryAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomerSalesHistory.this, "Network Error, Please check tailscale or mobile data", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @SuppressLint("Range")
    public ArrayList<CONNECT> SelectUPDT() {
        ArrayList<CONNECT> connectlist = new ArrayList<>();

        String query = "SELECT " +
                CONNECTION_TABLE + "." + CONNECTION_IP +
                " FROM " + CONNECTION_TABLE +
                " WHERE " + "defaultconn" + " = '1'";

        SQLiteDatabase db = mdatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String cip = cursor.getString(cursor.getColumnIndex(CONNECTION_IP));

                CONNECT connect = new CONNECT();
                connect.setIp(cip);
                connectlist.add(connect);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return connectlist;
    }
}
