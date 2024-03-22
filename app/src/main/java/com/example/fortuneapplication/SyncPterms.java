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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SyncPterms extends AppCompatActivity {

  //  private static final String JSON = "http://100.126.107.81:8082/MobileAPI/paymentterms.php";
    private List<PaymenTerm> paymenTerms;
    ListView plist;
    Button dsgs;
    ProgressBar progressBar6;
    private String x;
    private String JSON_URL;
    private PazDatabaseHelper mdatabaseHelper;
    TextView sync_datetime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_pterms);

        paymenTerms = new ArrayList<>();
        plist = findViewById(R.id.plist);
        dsgs = findViewById(R.id.dsgs);
        progressBar6 = findViewById(R.id.progressBar6);
        mdatabaseHelper = new PazDatabaseHelper(this);
        sync_datetime = findViewById(R.id.sync_history);
        sync_datetime.setText(mdatabaseHelper.get_sync_history(6));

        ArrayList<CONNECT> connectList = SelectUPDTS();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
            JSON_URL = "http://" + x + "/MobileAPI/paymentterms.php";
        }


        dsgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
               databaseHelper.deletePT();
                getpterms();
                progressBar6.setVisibility(View.VISIBLE);
            }
        });
    }
    public void getpterms() {
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray ptarray = obj.getJSONArray("data");
                            for (int i = 0; i < ptarray.length(); i++) {
                                JSONObject jsonObject = ptarray.getJSONObject(i);
                                String ptid = jsonObject.getString("id");
                                String ptcode = jsonObject.getString("code");
                                String ptdescription = jsonObject.getString("description");
                                String ptnet_due = jsonObject.getString("net_due");

                                PaymenTerm paymenTerm = new PaymenTerm(ptid, ptcode, ptdescription, ptnet_due);
//                                databaseHelper.storePT(paymenTerm);
//                                Toast.makeText(SyncPterms.this, "SUCCESSFULLY SYNC", Toast.LENGTH_SHORT).show();
                                boolean isStored = databaseHelper.storePT(paymenTerm);
                                Toast.makeText(SyncPterms.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                                if (isStored) {

                                }


                                paymenTerms.add(paymenTerm);

                            }
                            databaseHelper.UpdateSyncHistory(6);
                            sync_datetime.setText(mdatabaseHelper.get_sync_history(6));
                            SyncPayemTAdapter adapter = new SyncPayemTAdapter(paymenTerms, getApplicationContext());
                            plist.setAdapter(adapter);


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
                Toast.makeText(SyncPterms.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("Range")
    public ArrayList<CONNECT> SelectUPDTS() {
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
