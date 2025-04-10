package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SyncCoverage extends AppCompatActivity {
    private List<Coverage> coverageList;
    ListView syncc;
    Button synCoverage;
    ImageView chome;
    ProgressBar progressBar;
    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;
    TextView sync_datetime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_coverage);
        coverageList = new ArrayList<>();
        syncc = findViewById(R.id.syncc);
        synCoverage = findViewById(R.id.synCoverage);
        chome = findViewById(R.id.chome);
        progressBar = findViewById(R.id.progressBar);
        mdatabaseHelper = new PazDatabaseHelper(this);
        sync_datetime = findViewById(R.id.sync_history);
        sync_datetime.setText(mdatabaseHelper.get_sync_history(2));


        ArrayList<CONNECT> connectList = SelectUPDT();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
            JSON_URL = "http://" + x + "/mobileapi/customers_coverage.php";
        }

        chome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ooo = new Intent(SyncCoverage.this, HomePage.class);
                startActivity(ooo);
                finish();
            }
        });

        synCoverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //* DELETE THE DATA IN DATBASE//*
//                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//                databaseHelper.deleteCustomerData();
                fetchCustomerList();
                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }
    public void fetchCustomerList(){
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray coverageArray = obj.getJSONArray("data");
                            for (int i = 0 ; i < coverageArray.length(); i++){
                                JSONObject jsonObject = coverageArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String customer_id = jsonObject.getString("customer_id");
                                String salesrep_id = jsonObject.getString("sales_rep_id");
                                String day = jsonObject.getString("frequency_day");
                                String frequency = jsonObject.getString("frequency");
                                String frequency_week_schedule = jsonObject.getString("frequency_week_schedule");
                                Coverage coverage= new Coverage(id,customer_id,salesrep_id,day,frequency,frequency_week_schedule);
                                Log.d("Coverage", coverage.toString());
                                boolean isStored = databaseHelper.StoreCoverage(coverage);
                                coverageList.add(coverage);
                            }
                            CoverageAdapter adapter = new CoverageAdapter(coverageList, getApplicationContext());
                            syncc.setAdapter(adapter);
                            databaseHelper.UpdateSyncHistory(9);
                            sync_datetime.setText(mdatabaseHelper.get_sync_history(9));
                            Toast.makeText(SyncCoverage.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        } finally {
                            // Hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SyncCoverage.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();
                // Hide the progress bar
                progressBar.setVisibility(View.GONE);

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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