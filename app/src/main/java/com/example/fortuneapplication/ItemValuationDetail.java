package com.example.fortuneapplication;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
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

public class ItemValuationDetail extends AppCompatActivity {
    TextView title;
    private PazDatabaseHelper mdatabaseHelper;
    private String JSON_URL;
    RecyclerView item_history;
    ItemValuationAdapter itemValuationAdapter;
    private String x;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");
        String description = intent.getStringExtra("description");
        String date_from = intent.getStringExtra("date_from");
        String date_to = intent.getStringExtra("date_to");
        item_history = findViewById(R.id.datadisp);
        title = findViewById(R.id.title);
        title.setText(description+"\n"+date_from+" - "+date_to);
        final PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        String location_id = databaseHelper.get_default_location_id();

        ArrayList<CONNECT> connectList = SelectUPDT();
        if (!connectList.isEmpty()) {
            x = connectList.get(0).getIp();
            JSON_URL = "http://" + x + "/MobileAPI/items.php";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL+"?item_code="+code+"&location_id="+location_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray itemArray = obj.getJSONArray("data");

                            // Delete existing data from the table before syncing new data
//                            databaseHelper.deleteExistingData();
                            ArrayList itemList = new ArrayList<>();

                            for (int i = 0; i < itemArray.length(); i++) {
                                JSONObject jsonObject = itemArray.getJSONObject(i);
                                String type = jsonObject.getString("type");
                                String refno = jsonObject.getString("refno");
                                String name = jsonObject.getString("name");
                                String quantity = jsonObject.getString("quantity");
                                String ENDING_QUANTITY = jsonObject.getString("ENDING_QUANTITY");

                                Item item =new Item(type, refno, name, quantity, ENDING_QUANTITY,"","","","");
                                itemList.add(item);

                            }

                            itemValuationAdapter = new ItemValuationAdapter(ItemValuationDetail.this,itemList);
                            item_history.setLayoutManager(new LinearLayoutManager(ItemValuationDetail.this));
                            item_history.setAdapter(itemValuationAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ItemValuationDetail.this, "Network Error, Please Sync Again", Toast.LENGTH_SHORT).show();
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
