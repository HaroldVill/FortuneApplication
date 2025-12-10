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

public class SyncPriceLevelLines2 extends AppCompatActivity {
    PazDatabaseHelper mDatabaseHelper;
    List<PlevelLines_list> plevelLines_lists2;
    String IP_ADDRESS, link;
    ProgressBar pll2_progressBar;
    TextView pll2_textView;
    Button pll2_button;
    ListView pll2_listView;
    String TAG = "SyncPriceLevelLines2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_price_level_lines2);

        mDatabaseHelper = new PazDatabaseHelper(this);
        plevelLines_lists2 = new ArrayList<>();
        pll2_progressBar = findViewById(R.id.pll2_pb);
        pll2_textView = findViewById(R.id.pll2_sync_history);
        pll2_button = findViewById(R.id.pll2_button);
        pll2_listView = findViewById(R.id.pll2_list_view);

        ArrayList<CONNECT> setUpConnection = mDatabaseHelper.SelectUPDT();
        if (!setUpConnection.isEmpty()) {
            IP_ADDRESS = setUpConnection.get(0).getIp();
            link = "http://"+IP_ADDRESS+"/MobileAPI/price_level_lines2.php";
        }

        pll2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPriceLevelLines2();
                pll2_progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchPriceLevelLines2() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            mDatabaseHelper.deleteplines2();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonDataObject = jsonArray.getJSONObject(i);

                                String lines2_id = jsonDataObject.getString("id");
                                String price_level_lines2_id = jsonDataObject.getString("price_level_id");
                                String item2_id = jsonDataObject.getString("item_id");
                                String custom_price2 = jsonDataObject.getString("custom_price");

                                PlevelLines_list plevelLines_list2 = new PlevelLines_list(lines2_id, price_level_lines2_id, item2_id, custom_price2);
                                plevelLines_lists2.add(plevelLines_list2);
                                mDatabaseHelper.storePLVLines2(plevelLines_list2);
                            }
                            PlvlinesAdapter plvlinesAdapter = new PlvlinesAdapter(plevelLines_lists2, getApplicationContext());
                            pll2_listView.setAdapter(plvlinesAdapter);
                            mDatabaseHelper.UpdateSyncHistory(11);
                            pll2_textView.setText(mDatabaseHelper.get_sync_history(11));
                            Toast.makeText(SyncPriceLevelLines2.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing Error" + e.getMessage());
                            throw new RuntimeException(e);
                        } finally {
                            pll2_progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SyncPriceLevelLines2.this, "Sync Error. Please check tail scale or internet then try again.", Toast.LENGTH_SHORT).show();
                        pll2_progressBar.setVisibility(View.GONE);
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

} //SyncPriceLevelLines2