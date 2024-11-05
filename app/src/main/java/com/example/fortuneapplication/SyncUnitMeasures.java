package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class SyncUnitMeasures extends AppCompatActivity {
    //private static final String JSON = "http://100.126.107.81:8082/MobileAPI/unit.php";
    private List<Unit> unitList;
    ListView listum;
    Button um;
    ProgressBar progressBar5;
    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;
    TextView sync_datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_unit_measures);

        um = findViewById(R.id.um);
        listum = findViewById(R.id.listum);
        unitList = new ArrayList<>();
        progressBar5 = findViewById(R.id.progressBar5);
        mdatabaseHelper = new PazDatabaseHelper(this);
        sync_datetime = findViewById(R.id.sync_history);
        sync_datetime.setText(mdatabaseHelper.get_sync_history(5));


        ArrayList<CONNECT> connectList = SelectUPDTL();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp();
            JSON_URL = "http://" + x + "/MobileAPI/unit.php";
        }

        um.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//                databaseHelper.deleteexistUm();
                SyncUm();
                progressBar5.setVisibility(View.VISIBLE);
            }
        });
    }
    public void SyncUm(){
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray umarray = obj.getJSONArray("data");
                            databaseHelper.deleteexistUm();

                            for (int i = 0; i< umarray.length(); i++){
                                JSONObject jsonObject = umarray.getJSONObject(i);
                                String idum = jsonObject.getString("item_id");
                                String nameum = jsonObject.getString("name");
                                String quantityum = jsonObject.getString("quantity");
                                String unitid = jsonObject.getString("unit_id");

                                Unit unit = new Unit(idum,nameum,quantityum,unitid);
                                boolean isStored = databaseHelper.storeUm(unit);
                                unitList.add(unit);
                            }
                            databaseHelper.UpdateSyncHistory(8);
                            sync_datetime.setText(mdatabaseHelper.get_sync_history(8));
                           SyncUMadapter adapter = new SyncUMadapter(unitList,getApplicationContext());
                            listum.setAdapter(adapter);

                            Toast.makeText(SyncUnitMeasures.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        } finally {
                            progressBar5.setVisibility(View.GONE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SyncUnitMeasures.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();
                progressBar5.setVisibility(View.GONE);

            }
        });

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                // Here goes the new timeout 3 minutes
                return 3*60*1000;
            }

            @Override
            public int getCurrentRetryCount() {
                // The max number of attempts
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
    @SuppressLint("Range")
    public ArrayList<CONNECT> SelectUPDTL() {
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