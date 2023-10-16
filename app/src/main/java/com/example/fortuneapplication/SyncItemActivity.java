package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.List;

public class SyncItemActivity extends AppCompatActivity {

   // private static  final String JSON_URL = "http://100.126.107.81:8082/MobileAPI/items.php";
    private List<Item> itemList;
    ListView listview_item;
    Button bbt;
    ImageView hm;
    ProgressBar progressBar1;
    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_item);
        listview_item = findViewById(R.id.listview_item);
        hm = findViewById(R.id.hm);
        itemList = new ArrayList<>();
        bbt = findViewById(R.id.bbt);
        progressBar1= findViewById(R.id.progressBar1);
        mdatabaseHelper = new PazDatabaseHelper(this);


        ArrayList<CONNECT> connectList = SelectUPDT();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp();
            JSON_URL = "http://" + x + "/MobileAPI/items.php";
        }



        hm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent h = new Intent(SyncItemActivity.this,HomePage.class);
                startActivity(h);
                finish();

            }
        });

      bbt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                //  * DELETE THE DATA IN DATBASE//*
//              PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//             databaseHelper.deleteExistingData();
              fetchdatafromJson();
              progressBar1.setVisibility(View.VISIBLE);
          }
      });
    }
    public void fetchdatafromJson(){
        final PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray itemArray = obj.getJSONArray("data");

                            // Delete existing data from the table before syncing new data
                            databaseHelper.deleteExistingData();

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

                                boolean isStored = databaseHelper.StoreData(item);
                                itemList.add(item);
                            }

                            ItemAdapter adapter = new ItemAdapter(itemList, getApplicationContext());
                            listview_item.setAdapter(adapter);

                            Toast.makeText(SyncItemActivity.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {

                            progressBar1.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SyncItemActivity.this, "Network Error, Please Sync Again", Toast.LENGTH_SHORT).show();
                progressBar1.setVisibility(View.GONE);
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