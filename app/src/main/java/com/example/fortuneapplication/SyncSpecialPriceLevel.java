package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.SPECIAL_PRICE_LEVEL_TABLE;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
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

public class SyncSpecialPriceLevel extends AppCompatActivity {
    private PazDatabaseHelper mdatabaseHelper;
    private ProgressBar progressBar;
    private ImageView chome;
    private String JSON_URL, apiSpecialPriceLevel;
    private Button syncSpecialPriceLevel;
    private List<SpecialPriceLevel> specialPriceLevelList;
    private ListView syncspl;
    private TextView sync_history;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_special_price_level);
        progressBar = findViewById(R.id.progressBar);
        chome = findViewById(R.id.chome);
        syncSpecialPriceLevel = findViewById(R.id.syncSpecialPriceLevel);
        sync_history = findViewById(R.id.sync_history);
        mdatabaseHelper = new PazDatabaseHelper(this);
        specialPriceLevelList = new ArrayList<>();
        syncspl = findViewById(R.id.syncspl);

        ArrayList<CONNECT> setUpConnection = mdatabaseHelper.SelectUPDT();
        if(!setUpConnection.isEmpty()) {
            JSON_URL = setUpConnection.get(0).getIp();
            Log.d("IP ADD.", JSON_URL);
        }

        chome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToHome = new Intent(SyncSpecialPriceLevel.this, HomePage.class);
                startActivity(backToHome);
                finish();
            }
        });

        syncSpecialPriceLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specialPriceLevelList.clear();
                fetchSpecialPriceLevel();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    } //onCreate

    public void fetchSpecialPriceLevel() {
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        apiSpecialPriceLevel = "http://" + JSON_URL + "/MobileAPI/get_special_price_level.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiSpecialPriceLevel,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray special_price_level_array = object.getJSONArray("data");
                            for (int i = 0; i < special_price_level_array.length(); i++) {
                                JSONObject jsonObject = special_price_level_array.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String recorded_on = jsonObject.getString("recorded_on");
                                String customer_id = jsonObject.getString("customer_id");
                                String item_id = jsonObject.getString("item_id");
                                String sales_rep_id = jsonObject.getString("sales_rep_id");
                                String price_level_id = jsonObject.getString("price_level_id");
                                String approved = jsonObject.getString("approved");
                                String approved_by = jsonObject.getString("approved_by");
                                String approved_on = jsonObject.getString("approved_on");
                                SpecialPriceLevel specialPriceLevel = new SpecialPriceLevel(id, recorded_on, customer_id, item_id, sales_rep_id, price_level_id, approved, approved_by, approved_on);
                                Log.d("SpecialPriceLevel", specialPriceLevel.toString());
                                specialPriceLevelList.add(specialPriceLevel);
//                                databaseHelper.StoreSpecialPriceLevel(specialPriceLevel);

                                if (approved.equals("1")) {
                                    databaseHelper.StoreSpecialPriceLevel(specialPriceLevel);
//                                    deleteIfNotApproved();
                                }
                            }
                            SpecialPriceLevelAdapter specialPriceLevelAdapter = new SpecialPriceLevelAdapter(specialPriceLevelList, getApplicationContext());
                            syncspl.setAdapter(specialPriceLevelAdapter);
                            databaseHelper.UpdateSyncHistory(9);
                            sync_history.setText(mdatabaseHelper.get_sync_history(9));
                            Toast.makeText(SyncSpecialPriceLevel.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SyncSpecialPriceLevel.this, "Error sync data", Toast.LENGTH_SHORT).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void deleteIfNotApproved() {
        PazDatabaseHelper pdbHelper = new PazDatabaseHelper(this);
        SQLiteDatabase db = pdbHelper.getWritableDatabase();
        String[] whereArgs = { String.valueOf("1") };

        try {
            db.beginTransaction();
            db.delete(SPECIAL_PRICE_LEVEL_TABLE, "approved = ?", whereArgs);
            db.setTransactionSuccessful();
            Toast.makeText(SyncSpecialPriceLevel.this, "Delete successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


} //SyncSpecialPriceLevel
