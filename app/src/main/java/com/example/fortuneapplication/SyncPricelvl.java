package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SyncPricelvl extends AppCompatActivity {
    private static final String JSON = "http://100.126.107.81:8082/MobileAPI/pricelevel.php";
    private List<PriceLvl> priceLvlList;
    ListView pid;
    Button brot;
    ProgressBar progressBar6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_pricelvl);

        pid = findViewById(R.id.pid);
        brot = findViewById(R.id.brot);
        priceLvlList = new ArrayList<>();
        progressBar6 = findViewById(R.id.progressBar6);



        brot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchplvl();
                progressBar6.setVisibility(View.VISIBLE);
            }
        });
    }

     public  void fetchplvl(){
         PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
         StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         try {
                             JSONObject obj = new JSONObject(response);
                             JSONArray Plvlarray = obj.getJSONArray("data");
                             for (int i = 0; i < Plvlarray.length(); i++){

                                 JSONObject jsonObject = Plvlarray.getJSONObject(i);

                                 String pid = jsonObject.getString("price_level_id");
                                 String pitemid = jsonObject.getString("item_id");
                                 String pcustom = jsonObject.getString("custom_price");
                                 String pcode = jsonObject.getString("code");
                                 String pdescription = jsonObject.getString("description");

                                 PriceLvl priceLvl = new PriceLvl(pid,pitemid,pcustom,pcode,pdescription);

//                                // boolean isStored = databaseHelper.storePriceLvl(priceLvl);
//                                 Toast.makeText(SyncPricelvl.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
//                                 if (isStored) {


                                 priceLvlList.add(priceLvl);
//                                 databaseHelper.storePriceLvl(priceLvl);

                             }
                             databaseHelper.UpdateSyncHistory(6);
                           SyncPricelvlAdapter adapter = new SyncPricelvlAdapter (priceLvlList, getApplicationContext());
                             pid.setAdapter(adapter);


                         } catch (JSONException e) {
                            e.printStackTrace();

                         } finally {
                             // Hide the progress bar
                             progressBar6.setVisibility(View.GONE);

                         }
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 progressBar6.setVisibility(View.GONE);
                 Toast.makeText(SyncPricelvl.this, "Network Error please Sync Again", Toast.LENGTH_SHORT).show();

             }
         });

         RequestQueue requestQueue = Volley.newRequestQueue(this);
         requestQueue.add(stringRequest);
     }
}