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
import android.widget.TextView;
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

public class SyncLocation extends AppCompatActivity {
   // private static final String JSON_URL = "http://192.168.254.230:8082/MobileAPI/LOCATION.php";
    private List<Location> locations;
    ListView syncloca;
    Button snclo;
    ImageView humn;
    ProgressBar progressBar3;

    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;
    TextView sync_datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_location);

        syncloca = findViewById(R.id.syncloca);
        locations = new ArrayList<>();
        snclo= findViewById(R.id.snclo);
        humn = findViewById(R.id.humn);
        progressBar3 = findViewById(R.id.progressBar3);
        mdatabaseHelper = new PazDatabaseHelper(this);

        sync_datetime = findViewById(R.id.sync_history);
        sync_datetime.setText(mdatabaseHelper.get_sync_history(3));

        ArrayList<CONNECT> connectList = SelectUPDT();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
            JSON_URL = "http://" + x + "/MobileAPI/LOCATION.php";
        }



        humn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ss = new Intent(SyncLocation.this,HomePage.class);
                startActivity(ss);
            }
        });

        snclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                databaseHelper.deleteLocation();
                syncLocation();
                progressBar3.setVisibility(View.VISIBLE);
            }
        });
    }
        public void syncLocation(){
            PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray locationArray = obj.getJSONArray("data");
                                for (int i=0; i < locationArray.length(); i++){
                                    JSONObject jsonObject = locationArray.getJSONObject(i);
                                    String idlo = jsonObject.getString("id");
                                    String namlo = jsonObject.getString("name");

                                    Location location = new Location(idlo,namlo);


                                    boolean isStored = databaseHelper.storeLocation(location);
                                    Toast.makeText(SyncLocation. this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                                    if (isStored) {
//                                        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//                                        databaseHelper.deleteLocation();

                                    }


//                                    //*store data to SQLITE_DATBASE //*
//                                    databaseHelper.storeLocation(location);
//                                    Toast.makeText(SyncLocation.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();

                                    locations.add(location);
                                }
                                SyncLocactionAdapter adapter = new SyncLocactionAdapter(locations,getApplicationContext());
                                databaseHelper.UpdateSyncHistory(3);
                                sync_datetime.setText(mdatabaseHelper.get_sync_history(3));
                                syncloca.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();

                            } finally {
                                // Hide the progress bar
                                progressBar3.setVisibility(View.GONE);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SyncLocation.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();
                    progressBar3.setVisibility(View.GONE);

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
