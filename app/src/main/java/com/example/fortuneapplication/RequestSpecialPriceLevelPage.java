package com.example.fortuneapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestSpecialPriceLevelPage extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView customertextView, descriptiontextView, splAtextView, splBtextView, splCtextView;
    private LinearLayout wholeA, wholeB, wholeC;
    private Button requestApprovalButton;
    private Button syncPriceChoice;
    private String apiForPostingSpecialPriceLevelLines, apiForSpecialPriceLevelA, ipAddress, apiForIndividualSyncing;
    private String customerId, itemId, salesRepId, priceLevelID;
    private PazDatabaseHelper mDatabaseHelper;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder buildering;
    private List<SpecialPriceLevel> specialPriceLevelList;
    private final String SPA = "6751";
    private final String SPB = "6761";
    private final String SPC = "6771";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_special_price_level);
        mDatabaseHelper = new PazDatabaseHelper(this);
        customertextView = findViewById(R.id.customerName);
        descriptiontextView = findViewById(R.id.descriptionNames);
        wholeA = findViewById(R.id.whole_A);
        wholeB = findViewById(R.id.whole_B);
        wholeC = findViewById(R.id.whole_C);
        splAtextView = findViewById(R.id.special_price_A);
        splBtextView = findViewById(R.id.special_price_B);
        splCtextView = findViewById(R.id.special_price_C);
        requestApprovalButton = findViewById(R.id.request_approval_button);
        builder = new AlertDialog.Builder(this);
        buildering = new AlertDialog.Builder(this);
        syncPriceChoice = findViewById(R.id.syncPriceChoice);
        specialPriceLevelList = new ArrayList<>();

        initializationOfSomethings();
        setUpCustomerAndDescriptionDisplay();
        specialPriceLevelA();
        specialPriceLevelB();
        specialPriceLevelC();
        priceLevelSelection();
        syncChoice();
//        setupPhysicalBackButton();
    }

    //Kuha-on niya ang i.p. nga naa sa setup connection
    private void initializationOfSomethings() {
        ArrayList<CONNECT> connect = mDatabaseHelper.SelectUPDT();
        if(!connect.isEmpty()) {
            ipAddress = connect.get(0).getIp();
            Log.d("IPADDRESS", ipAddress);
        }

//        SharedPreferences sharedPreferencesIds = getSharedPreferences("MyItems", Context.MODE_PRIVATE);
//        customerId = sharedPreferencesIds.getString("customer_id", "");
        //Kinuha nako ni sila sa SlectItemAdapter.java
        customerId = getIntent().getStringExtra("CUSTOMER_ID");
        itemId = getIntent().getStringExtra("ITEM_ID");

        Log.d("logged", customerId);
        assert itemId != null;
        Log.d("logged!", itemId);
    }

    @SuppressLint("SetTextI18n")
    private void setUpCustomerAndDescriptionDisplay() {
        SharedPreferences sharedPreferencesCustomer = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String customerName = sharedPreferencesCustomer.getString("CNAME", "");
        customertextView.setText("CUSTOMER NAME: " + customerName);

        String descriptionName = getIntent().getStringExtra("ITEM_DESCRIPTION");
        if (descriptionName != null) {
            descriptiontextView.setText("ITEM DESCRIPTION: " + descriptionName);
        }
    }

    private void priceLevelSelection() {
        wholeA.setOnClickListener(view -> {
            resetButtonColors();
            wholeA.setBackground(ContextCompat.getDrawable(RequestSpecialPriceLevelPage.this, R.drawable.button_when_pressed));
//            requestApprovalButton.setEnabled(true);
            requestApprovalButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    alertDialogForRequestApprovalButton(SPA);
                }
            });
        });
        wholeB.setOnClickListener(view -> {
            resetButtonColors();
            wholeB.setBackground(ContextCompat.getDrawable(RequestSpecialPriceLevelPage.this, R.drawable.button_when_pressed));
//            requestApprovalButton.setEnabled(true);
            requestApprovalButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    alertDialogForRequestApprovalButton(SPB);
                }
            });
        });
        wholeC.setOnClickListener(view -> {
            resetButtonColors();
            wholeC.setBackground(ContextCompat.getDrawable(RequestSpecialPriceLevelPage.this, R.drawable.button_when_pressed));
//            requestApprovalButton.setEnabled(true);
            requestApprovalButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    alertDialogForRequestApprovalButton(SPC);
                }
            });
        });
    }

    private void resetButtonColors() {
        wholeA.setBackground(ContextCompat.getDrawable(RequestSpecialPriceLevelPage.this, R.drawable.border_radius));
        wholeB.setBackground(ContextCompat.getDrawable(RequestSpecialPriceLevelPage.this, R.drawable.border_radius));
        wholeC.setBackground(ContextCompat.getDrawable(RequestSpecialPriceLevelPage.this, R.drawable.border_radius));
    }

    public void postingToSpecialPriceLevelLinesTable(String plID) {
        this.priceLevelID = plID;
                apiForPostingSpecialPriceLevelLines = "http://" +ipAddress+ "/MobileAPI/sync_special_price_level_lines.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, apiForPostingSpecialPriceLevelLines,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(response);
                                    String yow = jsonObject1.getString("No Special Price Set");
                                    if (yow.equals("No Special Price Set")) {
                                        alertDialogCheckIfZero();
                                    }
                                } catch (JSONException e) {
                                    Log.d("yow", response);
                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Log.d("Volley JSON Response", "Received response: " + response);
                                    String syncedSuccessfully = jsonObject.getString("Successfully synced!");
                                    if (syncedSuccessfully.equals("Successfully synced!")) {
//                                        builder.setTitle("SENT SUCCESSFULLY TO DATABASE. PLEASE SYNC NOW!")
//                                                .setMessage("PLEASE PRESS SYNC BUTTON IN THE UPPER-RIGHT CORNER")
//                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        dialogInterface.cancel();
//                                                    }
//                                                })
//                                                .show();
                                        Toast.makeText(RequestSpecialPriceLevelPage.this, "APPROVAL REQUEST SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("message", response);
                                }
                                try {
                                    JSONObject jsonObject2 = new JSONObject(response);
                                    String alreadyExist = jsonObject2.getString("Already exist");
                                    if (alreadyExist.equals("Already exist")) {
                                        builder.setTitle("OOOOOPPPPPS!")
                                                .setMessage("NAA NA SIYA SA DATABASE. DI NA KA KAREQUEST BALIK")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.cancel();
                                                    }
                                                })
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    Log.d("Volley JSON Error", "JSON parsing error: " + e.getMessage());
                                    Log.d("Volley JSON Response", "Received response: " + response);
                                }
                                Log.d("Success: ", "Connected successfully!");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialogIfFailedToPost();
                            }
                        }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        salesRepId = mDatabaseHelper.get_default_salesrep_id();
                        Log.d("logged?", salesRepId);
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", customerId);
                        params.put("item_id", itemId);
                        params.put("sales_rep_id", salesRepId);
                        params.put("price_level_id", plID);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue = Volley.newRequestQueue(RequestSpecialPriceLevelPage.this);
                requestQueue.add(stringRequest);
    }

    private void alertDialogIfFailedToPost() {
        builder.setTitle("OOOOOOPPPPPS!")
                .setMessage("WA NASUD SA DATABASE. CHECK CONNECTION OR CALL THE I.T. THEN BALIKA PALIHOG")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
        requestApprovalButton.setEnabled(true);
    }

    //if zero ang price
    private void alertDialogCheckIfZero() {
        builder.setTitle("STOP!")
                .setMessage("ZERO ANG PRICE, STOP THE TRANSACTION")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();
    }

    private void alertDialogForRequestApprovalButton(String number) {
        buildering.setTitle("ARE YOU SURE?")
                .setMessage("DI NA NI MA-USOB ONCE NGA MU-YES NA KA")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postingToSpecialPriceLevelLinesTable(number);
                    }
                })
                .show();
    }

    private void specialPriceLevelA() {
        apiForSpecialPriceLevelA = "http://" +ipAddress+ "/MobileAPI/get_special_price_a.php?customer_id=" +customerId+ "&item_id="+itemId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiForSpecialPriceLevelA,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onResponse(String response) {
                        Log.d("Success", "Successfully connected to apiSPLevelLinesA");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String specialPriceA = jsonObject.getString("special_price_a");
                            double price = Double.parseDouble(specialPriceA);
                            if (price != 0.00) {
                                requestApprovalButton.setEnabled(true);
                            }
                            splAtextView.setText("₱"+String.format("%.2f", price));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Failed", "Failed to connect apiSPLevelLinesA");
                    }
                });
        requestQueue = Volley.newRequestQueue(RequestSpecialPriceLevelPage.this);
        requestQueue.add(stringRequest);
    }

    private void specialPriceLevelB() {
        apiForSpecialPriceLevelA = "http://" +ipAddress+ "/MobileAPI/get_special_price_b.php?customer_id=" +customerId+ "&item_id="+itemId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiForSpecialPriceLevelA,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onResponse(String response) {
                        Log.d("Success", "Successfully connected to apiSPLevelLinesB");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String specialPriceB = jsonObject.getString("special_price_a");
                            double price = Double.parseDouble(specialPriceB);
                            if (price != 0.00) {
                                requestApprovalButton.setEnabled(true);
                            }
                            splBtextView.setText("₱"+String.format("%.2f", price));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Failed", "Failed to connect apiSPLevelLinesB");
                    }
                });
        requestQueue = Volley.newRequestQueue(RequestSpecialPriceLevelPage.this);
        requestQueue.add(stringRequest);
    }

    private void specialPriceLevelC() {
        apiForSpecialPriceLevelA = "http://" +ipAddress+ "/MobileAPI/get_special_price_c.php?customer_id=" +customerId+ "&item_id="+itemId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiForSpecialPriceLevelA,
                new Response.Listener<String>() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onResponse(String response) {
                        Log.d("Success", "Successfully connected to apiSPLevelLinesC");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String specialPriceC = jsonObject.getString("special_price_a");
                            double price = Double.parseDouble(specialPriceC);
                            if (price != 0.00) {
                                requestApprovalButton.setEnabled(true);
                            }
                            splCtextView.setText("₱"+String.format("%.2f", price));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Failed", "Failed to connect apiSPLevelLinesC");
                    }
                });
        requestQueue = Volley.newRequestQueue(RequestSpecialPriceLevelPage.this);
        requestQueue.add(stringRequest);
    }

    private void syncChoice () {
        syncPriceChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PazDatabaseHelper pazDatabaseHelper = new PazDatabaseHelper(getApplicationContext());
                apiForIndividualSyncing = "http://" + ipAddress + "/MobileAPI/get_special_price_level_filtered.php?customer_id=" + customerId + "&item_id=" + itemId + "&price_level_id=" + priceLevelID;
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, apiForIndividualSyncing,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    JSONArray special_price_level_array = object.getJSONArray("data");
                                    for (int i = 0; i < special_price_level_array.length(); i++) {
                                        JSONObject jsonObject = special_price_level_array.getJSONObject(i);
                                        String id = jsonObject.getString("id");
                                        String recorded_on = jsonObject.getString("recorded_on");
                                        String customer_id = jsonObject.getString("customer_id");
                                        String item_id = jsonObject.getString("item_id");
                                        String sales_rep_id = jsonObject.getString("sales_rep_id");
                                        String price_level_id = jsonObject.getString("price_level_id");
                                        String approved = jsonObject.getString("approved");
                                        String approved_by = jsonObject.getString("approved_by");
                                        String approved_on = jsonObject.getString("approved_on");
                                        String custom_price = jsonObject.getString("custom_price");
                                        SpecialPriceLevel specialPriceLevel = new SpecialPriceLevel(id, recorded_on, customer_id, item_id, sales_rep_id, price_level_id, approved, approved_by, approved_on, custom_price);
                                        Log.d("SpecialPriceLevel", specialPriceLevel.toString());
                                        specialPriceLevelList.add(specialPriceLevel);
//                                databaseHelper.StoreSpecialPriceLevel(specialPriceLevel);

                                        if (approved.equals("1")) {
                                            pazDatabaseHelper.StoreSpecialPriceLevel(specialPriceLevel);
//                                    deleteIfNotApproved();
                                        }
                                    }
                                    SpecialPriceLevelAdapter specialPriceLevelAdapter = new SpecialPriceLevelAdapter(specialPriceLevelList, getApplicationContext());
                                    Toast.makeText(RequestSpecialPriceLevelPage.this, "Successfully Sync Data", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                stringRequest1.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(RequestSpecialPriceLevelPage.this);
                requestQueue.add(stringRequest1);
            }
        });
    }

//    private void setupPhysicalBackButton() {
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                   Intent intent = new Intent(RequestSpecialPriceLevelPage.this, ItemPricelvlChoiceDisplay.class);
//                   startActivity(intent);
//               }
//        };
//        getOnBackPressedDispatcher().addCallback(this, callback);
//    }

} // ApprovalPage
