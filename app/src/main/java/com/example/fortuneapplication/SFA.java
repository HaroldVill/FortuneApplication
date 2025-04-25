package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SFA extends AppCompatActivity {
    TextView select_sales_rep_id;
    TableLayout tableLayout;
    TextView sales_rep_name;
    private PazDatabaseHelper mdatabaseHelper;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa);

        tableLayout = findViewById(R.id.tablerow);
        select_sales_rep_id = findViewById(R.id.select_sales_rep_id);
        mdatabaseHelper = new PazDatabaseHelper(this);
        sales_rep_name = findViewById(R.id.sales_rep_name);
        //progressBar = findViewById(R.id.progressBar);

        select_sales_rep_id.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(SFA.this, select_sales_rep_id);
            Menu menu = popupMenu.getMenu();
            ArrayList<SalesRepList> salesreplists = displaysalesrep();
            //progressBar.setVisibility(View.INVISIBLE);

            for (SalesRepList salesreplist : salesreplists) {
                menu.add(Menu.NONE, Integer.parseInt(salesreplist.getSrid()), Menu.NONE, salesreplist.getSrname());
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                int salesrepid = item.getItemId();
                String salesrepname = Objects.requireNonNull(item.getTitle()).toString();
                sales_rep_name.setText(salesrepname);
                //progressBar.setVisibility(View.INVISIBLE);

                String JSON_URL = "http://"+mdatabaseHelper.get_active_connection() + "/mobileapi/SFA_TARGET.PHP?agent=" + salesrepid;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
                   @Override
                    public void onResponse(String response) {
                       //Toast.makeText(SFA.this, "Data Received", Toast.LENGTH_SHORT).show();
                       //Log.d("SFA", "Response " + response);
                       try {
                           JSONObject root = new JSONObject(response);
                           JSONArray dataArray = root.getJSONArray("data");

                           if (tableLayout.getChildCount() > 1) {
                               tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
                           }

                               for (int i = 0; i < dataArray.length(); i++) {
                                   JSONArray rowArray = dataArray.getJSONArray(i);

                                   String principal = rowArray.getString(1);
                                   String achieved = rowArray.getString(2);
                                   String target = rowArray.getString(3);
                                   String percentage = String.valueOf(rowArray.getDouble(4));

                                   TableRow row = new TableRow(SFA.this);

                                   TextView t1 = new TextView(SFA.this);
                                   t1.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t1.setGravity(Gravity.LEFT);
                                   t1.setText(principal);
                                   t1.setPadding(1, 1, 1, 1);

                                   TextView t2 = new TextView(SFA.this);
                                   t2.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t2.setGravity(Gravity.RIGHT);
                                   t2.setText(achieved);
                                   t2.setPadding(1, 1, 1, 1);

                                   TextView t3 = new TextView(SFA.this);
                                   t3.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t3.setGravity(Gravity.RIGHT);
                                   t3.setText(target);
                                   t3.setPadding(1, 1, 1, 1);

                                   TextView t4 = new TextView(SFA.this);
                                   t4.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t4.setGravity(Gravity.RIGHT);
                                   t4.setText(percentage + "%");
                                   t4.setPadding(1, 1, 1, 1);

                                   row.addView(t1);
                                   row.addView(t2);
                                   row.addView(t3);
                                   row.addView(t4);

                                   tableLayout.addView(row);
                               } if (dataArray.length() == 0) {
                               Toast.makeText(SFA.this, "NO DATA YET", Toast.LENGTH_SHORT).show();
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                           Toast.makeText(SFA.this, "Error displaying data", Toast.LENGTH_SHORT).show();
                       }
                   }
                }, error -> Toast.makeText(SFA.this, "Failed to connect", Toast.LENGTH_SHORT).show());
                //progressBar.setVisibility(View.INVISIBLE);

                Volley.newRequestQueue(SFA.this).add(stringRequest);

                return true;
            });

            popupMenu.show();
        });
    }

    @SuppressLint("Range")
    public ArrayList<SalesRepList> displaysalesrep() {
        ArrayList<SalesRepList> salesreplist = new ArrayList<>();
        String query = "SELECT salesrep_id, salesrep_name FROM sales_rep_table";

        SQLiteDatabase db = mdatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SalesRepList salesrep = new SalesRepList();
                salesrep.setSrid(cursor.getString(0));
                salesrep.setSrname(cursor.getString(1));
                salesreplist.add(salesrep);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return salesreplist;
    }
}


