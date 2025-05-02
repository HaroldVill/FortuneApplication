package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class SyncPriceleveLines extends AppCompatActivity {

   // private static final String JSON_URL = "http://100.126.107.81:8082/MobileAPI/price_level_lines.php";
    private List<PlevelLines_list>plevelLines_lists;
    ListView plines;
    Button boto;
    ProgressBar progressBar11;
    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;
    TextView sync_datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_priceleve_lines);

        plines = findViewById(R.id.plines);
        boto = findViewById(R.id.boto);
        progressBar11 = findViewById(R.id.progressBar11);
        plevelLines_lists = new ArrayList<>();
        mdatabaseHelper = new PazDatabaseHelper(this);
        sync_datetime = findViewById(R.id.sync_history);
        sync_datetime.setText(mdatabaseHelper.get_sync_history(8));

        ArrayList<CONNECT> connectList = SelectUPDT();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp();
            JSON_URL = "http://" + x + "/MobileAPI/price_level_lines.php";
        }

        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SyncPriceleveLines", "Sync button clicked");
                getDatalines();
                progressBar11.setVisibility(View.VISIBLE);
            }
        });

    }
public void getDatalines() {
    PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jbo = new JSONObject(response);
                        JSONArray jsonArray = jbo.getJSONArray("data");
//                        databaseHelper.deleteplines();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String linesid = jsonObject.getString("id");
                            String pricelevelid = jsonObject.getString("price_level_id");
                            String itemid = jsonObject.getString("item_id");
                            String cust = jsonObject.getString("custom_price");

                            PlevelLines_list plevelLines_list = new PlevelLines_list(linesid, pricelevelid, itemid, cust);
                            if(databaseHelper.deleteExistingPLevelLinesId(linesid)){
                                boolean isStored = databaseHelper.storePLVLines(plevelLines_list);
                            }


                            plevelLines_lists.add(plevelLines_list);
                        }
                            PlvlinesAdapter adapter = new PlvlinesAdapter(plevelLines_lists, getApplicationContext());
                            plines.setAdapter(adapter);
                            databaseHelper.UpdateSyncHistory(7);
                        sync_datetime.setText(mdatabaseHelper.get_sync_history(7));
                            Toast.makeText(SyncPriceleveLines.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Log.e("SyncPriceleveLines", "JSON parsing error: " + e.getMessage());
                        e.printStackTrace();

                    } finally {
                        // Hide the progress bar
                        progressBar11.setVisibility(View.GONE);
                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(SyncPriceleveLines.this, "Network Error Please Sync Again", Toast.LENGTH_SHORT).show();
            progressBar11.setVisibility(View.GONE);

        }
    });

//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                // Here goes the new timeout 3 minutes
//                return 3*60*1000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                // The max number of attempts
//                return 5;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
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
