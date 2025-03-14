package com.example.fortuneapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SFAActivity extends AppCompatActivity {
    Button cl, adis;
    EditText cname, ccontact, caddd, slr, loc, ptt;
    private EditText tot;
    TextView datess, sr1, sr2, autref, save, ii, cid, location_id, sales_id, sonotes, textnote,BeginOrderTime;
    Button his;

    ImageView profile,customer_coverage_plan;
    private RecyclerView recyclerViews;
    private List<Item2> itemList = new ArrayList<>();

    private ADisplayItemAdapter aDisplayItemAdapter;
    private PazDatabaseHelper mDatabaseHelper;

    private static final String PREFS_KEY = "ReferenceNumberPrefs";
    private static final String REFERENCE_NUMBER_KEY = "reference_number";
    private int referenceNumber;
    ImageView sync_wsr;
    ProgressBar progressBar3;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfaactivity);
        cl = findViewById(R.id.cl);
        adis = findViewById(R.id.adis);
        cname = findViewById(R.id.cname);
        ccontact = findViewById(R.id.ccontact);
        caddd = findViewById(R.id.caddd);
        his = findViewById(R.id.his);
        datess = findViewById(R.id.datess);
        slr = findViewById(R.id.slr);
        loc = findViewById(R.id.loc);
        sr1 = findViewById(R.id.sr1);
        sr2 = findViewById(R.id.sr2);
        recyclerViews = findViewById(R.id.recyclerViews);
        tot = findViewById(R.id.tot);
        autref = findViewById(R.id.autref);
        save = findViewById(R.id.save);
        ptt = findViewById(R.id.ptt);
        ii = findViewById(R.id.ii);
        cid = findViewById(R.id.cid);
        location_id = findViewById(R.id.location_id);
        sales_id = findViewById(R.id.sales_id);
        sonotes = findViewById(R.id.sonotes);
        textnote = findViewById(R.id.textnote);
        profile = findViewById(R.id.profile);
        customer_coverage_plan = findViewById(R.id.customer_coverage_plan);
        BeginOrderTime = findViewById(R.id.BeginOrderTime);
        sync_wsr = findViewById(R.id.sync_wsr);
        progressBar3 = findViewById(R.id.progressBar3);
        mDatabaseHelper = new PazDatabaseHelper(this);


        // Load the last saved reference number from SharedPreferences
        SharedPreferences sharedPreferencesb = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        referenceNumber = sharedPreferencesb.getInt(REFERENCE_NUMBER_KEY, 0);
        updateReferenceNumberTextView();

        // Retrieve the value from SharedPreferences in notes
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savednotes = sharedPreferences.getString("NOTE", "");
        textnote.setText(savednotes);

//ItemList Order Recycleview Display//
        recyclerViews.setLayoutManager(new LinearLayoutManager(this));


        itemList.addAll(mDatabaseHelper.getAllOrderItem());
        ADisplayItemAdapter adapter = new ADisplayItemAdapter(this, itemList, mDatabaseHelper);
        recyclerViews.setAdapter(adapter);

//GET TOTAL PAYABLE//
        double totalPayable = mDatabaseHelper.getTotalPayable();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        String formattedTotal = decimalFormat.format(totalPayable);
        tot.setText(formattedTotal);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prof = new Intent(SFAActivity.this, Profileman.class);
                startActivity(prof);
                finish();

            }
        });

        sonotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sff = new Intent(SFAActivity.this, Notes.class);
                startActivity(sff);
                finish();
            }
        });

///////////////////////////////////////////////////////////////////////////////////////
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tindera = slr.getText().toString();
                String lugar = loc.getText().toString();
                if (tindera.isEmpty() || lugar.isEmpty()) {
                    Toast.makeText(SFAActivity.this, "Please Enter Valid Sales Representative or Location", Toast.LENGTH_LONG).show();
                } else if (itemList.isEmpty()) {
                    Toast.makeText(SFAActivity.this, "Please Select Items Before Saving", Toast.LENGTH_SHORT).show();
                } else {
                    maone();
                }
            }
        });


        sr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sss = new Intent(SFAActivity.this, SalesRep.class);
                startActivity(sss);
                finish();

            }
        });

        sr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDatabaseHelper.get_location_settings().equals("Strict")) {
//                    loc.setText(mDatabaseHelper.get_default_location_name(Integer.parseInt(mDatabaseHelper.get_default_location_id())));
//                    location_id.setText(mDatabaseHelper.get_default_location_id());
                }
                else {

                    Intent lll = new Intent(SFAActivity.this, Locations.class);
                    startActivity(lll);
                    finish();
                }
            }
        });
        //*DATES//*
        LocalDateTime currentdate = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentdate.format(myFormatObj);
        datess.setText(formattedDate.toString());


        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ht = new Intent(SFAActivity.this, History.class);
                startActivity(ht);
                finish();
            }
        });

        //* FETCH DATA FROM CUSTOMER LIST//*
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = preferences.edit();
        String iidd = preferences.getString("CID", "");
        String firstCname = preferences.getString("CNAME", "");
        String firstCcontact = preferences.getString("CCONTACT", "");
        String firstCadd = preferences.getString("CADD", "");
        String rpt = preferences.getString("DI", "");
        String order_begin = preferences.getString("ORDER_BEGIN","");

        String lvl = preferences.getString("prlvl", "");
        cname.setText(firstCname);
        cid.setText(iidd);
        ccontact.setText(firstCcontact);
        caddd.setText(firstCadd);
        ptt.setText(rpt);
        ii.setText(lvl);
        BeginOrderTime.setText(order_begin);


//*FETCH DATA FROM SALES_REP//*
        SharedPreferences preferencess = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String namesr = preferencess.getString("SRNAME", "");
        String srid = preferencess.getString("SRID", "");
        String idloc = preferencess.getString("LOCID", "");
        String cola = preferencess.getString("LOC", "");

        slr.setText(namesr);
        if(mDatabaseHelper.get_location_settings().equals("Strict")) {
            loc.setText(mDatabaseHelper.get_default_location_name(Integer.parseInt(mDatabaseHelper.get_default_location_id())));
            location_id.setText(mDatabaseHelper.get_default_location_id());
        }
        else if(mDatabaseHelper.get_location_settings().equals("Allow") && idloc.equals("")){
            loc.setText(mDatabaseHelper.get_default_location_name(Integer.parseInt(mDatabaseHelper.get_default_location_id())));
            location_id.setText(mDatabaseHelper.get_default_location_id());
        }
        else{
            loc.setText(cola);
            location_id.setText(idloc);
        }
        sales_id.setText(srid);
        sales_id.setText(mDatabaseHelper.get_default_salesrep_id());
        slr.setText(mDatabaseHelper.get_default_salesrep());

        adis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCustomer();


            }
        });
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent P = new Intent(SFAActivity.this, SOdisplayCustomer.class);
                P.putExtra("Type","CoveragePlan");
                startActivity(P);
                finish();
            }
        });

        customer_coverage_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent P = new Intent(SFAActivity.this, SOdisplayCustomer.class);
                P.putExtra("Type","All");
                startActivity(P);
                finish();
            }
        });

        sync_wsr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressBar3.setVisibility(View.VISIBLE);
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                ArrayList<CONNECT> connectList = databaseHelper.SelectUPDT();
                String ip = connectList.get(0).getIp();
                String JSON_URL = "http://" + ip + "/MobileAPI/SFA.php?customer_id="+cid.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray locationArray = obj.getJSONArray("data");
                                    for (int i=0; i < locationArray.length(); i++){
                                        JSONObject jsonObject = locationArray.getJSONObject(i);
                                        String customer_id = cid.getText().toString();
                                        String item_id= jsonObject.getString("item_id");

                                        String wsr = jsonObject.getString("WEEKLY_SALES_RATE");
                                        String date = jsonObject.getString("date");
                                        String [] customer_wsr = {customer_id,item_id,wsr,date};
                                        boolean isStored = databaseHelper.storecustomerwsr(customer_wsr);
                                        if (isStored) {
                                            Toast.makeText(SFAActivity. this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                } finally {
                                    // Hide the progress bar
                                    progressBar3.setVisibility(View.GONE);
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SFAActivity.this, "Network Error Pleas Sync Again", Toast.LENGTH_SHORT).show();
                        progressBar3.setVisibility(View.GONE);

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(SFAActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    // GET date//
    private String getCurrentDate() {
        LocalDateTime currentdate = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentdate.format(myFormatObj);
        return formattedDate.toString();
    }

    public void updateTotalPayable(double totalPayable) {
        tot.setText(String.valueOf(totalPayable));
    }

    public void checkCustomer() {
        if (!cname.getText().toString().isEmpty()) {
            adis.setEnabled(true);
            String value = ii.getText().toString();
            String customer_id = cid.getText().toString();

            Intent ik = new Intent(SFAActivity.this, SFAItemDisplay.class);
            ik.putExtra("PRI", value);
            ik.putExtra("CUSTOMER_ID", customer_id);
            startActivity(ik);
            finish();

        } else {
            //adis.setEnabled(false);
            Toast.makeText(this, "Please Select Customer", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateReferenceNumberTextView() {
        String formattedReferenceNumber = String.format("%04d", referenceNumber);
        autref.setText(formattedReferenceNumber);
    }
    public void saveSalesOrders() {
        // long insertedRows = mDatabaseHelper.insertITEMSO();
        String nts = textnote.getText().toString();
        String total = tot.getText().toString();
        String idlocation = location_id.getText().toString();
        String custoid = cid.getText().toString();
        String reff = autref.getText().toString();
        String salesrep_id = sales_id.getText().toString();
        String currentDate = getCurrentDate();

    }
    public void maone () {
        String nts = textnote.getText().toString();
        String idlocation = location_id.getText().toString();
        String custoid = cid.getText().toString();
        String reff = autref.getText().toString();
        String salesrep_id = sales_id.getText().toString();
        String currentDate = getCurrentDate();
        String total = tot.getText().toString();

        //SALES ORDER TABLE
        SALESORDER dataModel = new SALESORDER();
        dataModel.setCode(reff);
        dataModel.setNotes(nts);
        dataModel.setAmount(total);
        dataModel.setSalesrepid(Integer.parseInt(salesrep_id));
        dataModel.setLocationid(Integer.parseInt(idlocation));
        dataModel.setCustomerid(Integer.parseInt(custoid));
        dataModel.setDate(currentDate);
        dataModel.set_begin_order(BeginOrderTime.getText().toString());
        Log.d("BeginOrderTime", BeginOrderTime.getText().toString());
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = date.format(myFormatObj);
        String end_order_time = formattedDate.toString();
        dataModel.set_end_order(end_order_time.toString());
        mDatabaseHelper.inserSO(dataModel);

        Toast.makeText(SFAActivity.this, "Saving", Toast.LENGTH_SHORT).show();
        referenceNumber++;
        updateReferenceNumberTextView();

        // Save the new reference number to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences MyPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        MyPrefs.edit().remove("CID").apply();
        MyPrefs.edit().remove("CNAME").apply();
        MyPrefs.edit().remove("CCONTACT").apply();
        MyPrefs.edit().remove("CADD").apply();
        MyPrefs.edit().remove("ORDER_BEGIN").apply();
        MyPrefs.edit().remove("prlvl").apply();
        MyPrefs.edit().remove("DI").apply();
        editor.putInt(REFERENCE_NUMBER_KEY, referenceNumber);
        editor.apply();
        Toast.makeText(SFAActivity.this, "Successfully Order Save", Toast.LENGTH_LONG).show();
        long insert = mDatabaseHelper.insertDataIDAndITEMSO();
//                cname.setText("");
//                cid.setText("");
        PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
        databaseHelper.deleOrderSample();
//                recyclerViews.setLayoutManager(new LinearLayoutManager(this));
//                mDatabaseHelper = new PazDatabaseHelper(this);
//                itemList.addAll(mDatabaseHelper.getAllOrderItem());
//                ADisplayItemAdapter adapter = new ADisplayItemAdapter(this, itemList, mDatabaseHelper);
//                recyclerViews.setAdapter(adapter);
//                autref.setText(referenceNumber);
        startActivity(getIntent());
        finish();
    }


}


//                String nts = textnote.getText().toString();
//                String idlocation = location_id.getText().toString();
//                String custoid = cid.getText().toString();
//                String reff = autref.getText().toString();
//                String salesrep_id = sales_id.getText().toString();
//                String currentDate = getCurrentDate();
//                String total = tot.getText().toString();
//
////SALES ORDER TABLE
//                    SALESORDER dataModel = new SALESORDER();
//                    dataModel.setCode(reff);
//                    dataModel.setNotes(nts);
//                    dataModel.setAmount(total);
//                    dataModel.setSalesrepid(Integer.parseInt(salesrep_id));
//                    dataModel.setLocationid(Integer.parseInt(idlocation));
//                    dataModel.setCustomerid(Integer.parseInt(custoid));
//                    dataModel.setDate(currentDate);
//                    mDatabaseHelper.inserSO(dataModel);
//
//                    Toast.makeText(SFAActivity.this, "Saving", Toast.LENGTH_SHORT).show();
//                    referenceNumber++;
//                    updateReferenceNumberTextView();
//
//                    // Save the new reference number to SharedPreferences
//                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt(REFERENCE_NUMBER_KEY, referenceNumber);
//                    editor.apply();
//                    Toast.makeText(SFAActivity.this, "Successfully Order Save", Toast.LENGTH_LONG).show();
//                    long insert = mDatabaseHelper.insertDataIDAndITEMSO();
//
//                    PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
//                    databaseHelper.deleOrderSample();
//                    startActivity(getIntent());
