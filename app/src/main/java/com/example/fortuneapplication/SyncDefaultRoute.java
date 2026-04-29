package com.example.fortuneapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SyncDefaultRoute extends AppCompatActivity {
    PazDatabaseHelper mDatabaseHelper;
    List<DefaultRoute> defaultRoute;
    String IP_ADDRESS, link;
    ProgressBar sdr_progressBar;
    TextView sdr_textView;
    Button sdr_button;
    ListView sdr_listView;
    String TAG = "SyncPriceLevelLines2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_default_route);

        mDatabaseHelper = new PazDatabaseHelper(this);
        defaultRoute = new ArrayList<>();
        sdr_progressBar = findViewById(R.id.sdr_pb);
        sdr_textView = findViewById(R.id.sdr_sync_history);
        sdr_button = findViewById(R.id.sdr_button);
        sdr_listView = findViewById(R.id.sdr_list_view);

        ArrayList<CONNECT> setUpConnection = mDatabaseHelper.SelectUPDT();
        if (!setUpConnection.isEmpty()) {
            IP_ADDRESS = setUpConnection.get(0).getIp();
            link = "http://"+IP_ADDRESS+"/MobileAPI/get_delivery_type.php";
        }

        sdr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchRoutes();
                sdr_progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchRoutes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            mDatabaseHelper.deleteroute();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonDataObject = jsonArray.getJSONObject(i);

                                String route_id = jsonDataObject.getString("id");
                                String route_name = jsonDataObject.getString("name");

                                DefaultRoute defaultRoute1 = new DefaultRoute(route_id, route_name);
                                defaultRoute.add(defaultRoute1);
                                mDatabaseHelper.storeRoutes(defaultRoute1);
//                                Log.d(TAG, route_name); //(GOODS NAAY DATA)
                            }
                            DefaultRouteAdapter defaultRouteAdapter = new DefaultRouteAdapter(defaultRoute, getApplicationContext());
                            sdr_listView.setAdapter(defaultRouteAdapter);
                            mDatabaseHelper.UpdateSyncHistory(12);
                            sdr_textView.setText(mDatabaseHelper.get_sync_history(12));
                            Toast.makeText(SyncDefaultRoute.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing Error" + e.getMessage());
                            throw new RuntimeException(e);
                        } finally {
                            sdr_progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SyncDefaultRoute.this, "Sync Error. Please check tail scale or internet then try again.", Toast.LENGTH_SHORT).show();
                        sdr_progressBar.setVisibility(View.GONE);
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

} //SyncDefaultRoute