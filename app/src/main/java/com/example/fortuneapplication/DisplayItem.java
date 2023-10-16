package com.example.fortuneapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
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

public class DisplayItem extends AppCompatActivity {
private static final String JSON_URL = "http://192.168.254.230:8880/api/items";
       ListView ListI;
       List<Item2> item2List2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        ListI = findViewById(R.id.ListI);
        item2List2 = new ArrayList<>();
        pleaseLoadData();
    }
    private void pleaseLoadData(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONArray itemarray = obj.getJSONArray("data");
                            for (int i = 0; i < itemarray.length(); i++){
                                JSONObject iObject = itemarray.getJSONObject(i);
                             //   Item2 item2 = new Item2(iObject.getInt("Id"),
//
//                                        iObject.getString("Code"),
//                                        iObject.getString("Description"),
//                                        iObject.getDouble("Rate"));

                            //  item2List2.add(item2);
                            }
                            ItemAdapter2 adapter = new ItemAdapter2(item2List2,getApplicationContext());
                            ListI.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DisplayItem.this, "Please check some Issues", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



}