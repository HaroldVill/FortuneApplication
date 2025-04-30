package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
    TextView select_sales_rep_id,sales_rep_id;
    TableLayout tableLayout, tableLayout1;
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
        tableLayout1 = findViewById(R.id.tablerow1);
        sales_rep_id = findViewById(R.id.sales_rep_id);

//        TableRow header = new TableRow(this);
//        String[] headers = {"Ref no", "Principal", "Customers", "Description", "Quantity", "Amount"};
//
//        for (String title:headers) {
//            TextView headerCell = new TextView(this);
//            headerCell.setText(title);
//            headerCell.setPadding(8,8,8,8);
//            headerCell.setGravity(Gravity.CENTER);
//            headerCell.setTextAppearance(android.R.style.TextAppearance_Medium);
//            headerCell.setTypeface(null, Typeface.BOLD);
//            header.addView(headerCell);
//        }
//        tableLayout1.addView(header);

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
                sales_rep_id.setText(Integer.toString(salesrepid));
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
                               tableLayout.removeViews(0, tableLayout.getChildCount());
                               tableLayout1.removeViews(0, tableLayout1.getChildCount());
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
                                   t1.setGravity(Gravity.START);
                                   t1.setText(principal);
                                   t1.setPadding(1, 1, 1, 1);

                                   TextView t2 = new TextView(SFA.this);
                                   t2.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t2.setGravity(Gravity.END);
                                   t2.setText(achieved);
                                   t2.setPadding(1, 1, 1, 1);

                                   TextView t3 = new TextView(SFA.this);
                                   t3.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t3.setGravity(Gravity.END);
                                   t3.setText(target);
                                   t3.setPadding(1, 1, 1, 1);

                                   TextView t4 = new TextView(SFA.this);
                                   t4.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                                   t4.setGravity(Gravity.END);
                                   t4.setText(percentage + "%");
                                   t4.setPadding(1, 1, 1, 1);

                                   row.addView(t1);
                                   row.addView(t2);
                                   row.addView(t3);
                                   row.addView(t4);

                                   row.setOnClickListener(v -> sampleData(principal));
                                   tableLayout.addView(row);

                               } if (dataArray.length() == 0) {
                               Toast.makeText(SFA.this, "NO DATA YET", Toast.LENGTH_SHORT).show();
                               tableLayout1.removeViews(0, tableLayout1.getChildCount());
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

    private void sampleData (String principalName) {
        String salesRepId = sales_rep_id.getText().toString();


        String JSON_URL = "http://"+mdatabaseHelper.get_active_connection() + "/mobileapi/get_sfa_details.PHP?principal=" + principalName + "&salesrepid=" + salesRepId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray dataArray = root.getJSONArray("data");

                    if (tableLayout1.getChildCount() > 1) {
                        tableLayout1.removeViews(0, tableLayout1.getChildCount());
                    }
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject rowArray = dataArray.getJSONObject(i);
                        Log.d("array", rowArray.toString());
                        String refno = rowArray.getString("refno");
                        String principal = rowArray.getString("principal");
                        String customer = rowArray.getString("customer");
                        String description = rowArray.getString("itemdesc");
                        String quantity = rowArray.getString("quantity");
                        String amount = rowArray.getString("amount");

                        TableRow row = new TableRow(SFA.this);

                        TextView t11 = new TextView(SFA.this);
                        t11.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                        t11.setGravity(Gravity.START);
                        t11.setText(refno);
                        t11.setTextSize(10);
                        //            refno.setPadding(1,1,1,1);

                        TextView t12 = new TextView(SFA.this);
                        t12.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                        t12.setGravity(Gravity.START);
                        t12.setText(principal);
                        t12.setTextSize(10);
//                        t12.setPadding(1, 1, 1, 1);

                        TextView t13 = new TextView(SFA.this);
                        t13.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                        t13.setGravity(Gravity.START);
                        t13.setText(customer);
                        t13.setTextSize(10);
//                        t13.setPadding(1, 1, 1, 1);

                        TextView t14 = new TextView(SFA.this);
                        t14.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 2f));
                        t14.setGravity(Gravity.START);
                        t14.setText(description);
                        t14.setTextSize(10);
//                        t14.setPadding(1, 1, 1, 1);

                        TextView t15 = new TextView(SFA.this);
                        t15.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                        t15.setGravity(Gravity.END);
                        t15.setText(quantity);
                        t15.setTextSize(10);
//                        t15.setPadding(1, 1, 1, 1);

                        TextView t16 = new TextView(SFA.this);
                        t16.setLayoutParams(new TableRow.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                        t16.setGravity(Gravity.END);
                        t16.setText(amount);
                        t16.setTextSize(10);
//                        t16.setPadding(1, 1, 1, 1);

                        row.addView(t11);
                        row.addView(t12);
                        row.addView(t13);
                        row.addView(t14);
                        row.addView(t15);
                        row.addView(t16);

                        tableLayout1.addView(row);
                    }
//                    Toast.makeText(SFA.this, principalName + " has been selected", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SFA.this, "Error displaying data", Toast.LENGTH_SHORT).show();
                }
            }

        }, error -> Toast.makeText(SFA.this, "Failed to connect", Toast.LENGTH_SHORT).show());
        //progressBar.setVisibility(View.INVISIBLE);

        Volley.newRequestQueue(SFA.this).add(stringRequest);
    }
}

