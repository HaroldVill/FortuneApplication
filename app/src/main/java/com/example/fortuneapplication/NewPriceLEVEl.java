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

public class NewPriceLEVEl extends AppCompatActivity {

    //private static final String JSON = "http://192.168.254.230:8082/MobileAPI/pricelevelnew.php";
    private List<NewPriceLvl> newPriceLvls;
    ListView newp;
    Button newd;
    ProgressBar progressBar10;
    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;
    TextView sync_datetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_price_level);
        newp = findViewById(R.id.newp);
        newPriceLvls = new ArrayList<>();
        newd = findViewById(R.id.newd);
        progressBar10 = findViewById(R.id.progressBar10);
        mdatabaseHelper = new PazDatabaseHelper(this);
        sync_datetime = findViewById(R.id.sync_history);
        sync_datetime.setText(mdatabaseHelper.get_sync_history(7));


        ArrayList<CONNECT> connectList = SelectUPDTK();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
            JSON_URL = "http://" + x + "/MobileAPI/pricelevelnew.php";
        }


        newd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//                databaseHelper.deletepricelevel();
                fetchnewPr();
                progressBar10.setVisibility(View.VISIBLE);
            }
        });

    }
    public void fetchnewPr(){
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject Obj = new JSONObject(response);
                            JSONArray jsonArray = Obj.getJSONArray("data");
                            databaseHelper.deletepricelevel();
                            for (int i =0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String ia = jsonObject.getString("id");
                                String ib = jsonObject.getString("code");
                                String ic = jsonObject.getString("description");

                                NewPriceLvl newPriceLvl = new NewPriceLvl(ia,ib,ic);

                                boolean isStored = databaseHelper.storepriceLevel(newPriceLvl);
                                Toast.makeText(NewPriceLEVEl.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                                if (isStored) {
//                                    PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//                                    databaseHelper.deletepricelevel();

                                }
                                newPriceLvls.add(newPriceLvl);
//                                databaseHelper.storepriceLevel(newPriceLvl);
//                                newPriceLvls.add(newPriceLvl);
                            }
                            databaseHelper.UpdateSyncHistory(6);
                            sync_datetime.setText(mdatabaseHelper.get_sync_history(6));
                            NewPriveLvlAdapter adapter = new NewPriveLvlAdapter(newPriceLvls,getApplicationContext());
                            newp.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        } finally {
                            // Hide the progress bar
                            progressBar10.setVisibility(View.GONE);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPriceLEVEl.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();
                progressBar10.setVisibility(View.GONE);

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
    public ArrayList<CONNECT> SelectUPDTK() {
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