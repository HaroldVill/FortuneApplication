package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.ITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_UNIT_MEASURE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITM;
import static com.example.fortuneapplication.PazDatabaseHelper.NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.TABLE_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.UNIT_MEASURE_TABLE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SFAInputQuantity extends AppCompatActivity {
    EditText q1, q2, q3, q4, q5, q6,inv1;
    TextView balik, ps, itemid, idlvl, untbase, samvalue, datasam,basihan,dew,salesrate,sug2, ps2,inv_uom;
    Button itemsave,itemfree,lessbo,generate_valuation,generate_open_orders;
    private String JSON_URL;
    private String x;

    Spinner spinner6;
    private PazDatabaseHelper mdatabaseHelper;
    ArrayList<Unit> units;
    Context context;
    final Calendar history_calendar= Calendar.getInstance();
    final Calendar history_calendar_to= Calendar.getInstance();
    EditText date_from,date_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_input_quantity);
        mdatabaseHelper = new PazDatabaseHelper(this);
        q1 = findViewById(R.id.q1);
        q2 = findViewById(R.id.q2);
        q3 = findViewById(R.id.q3);
        q4 = findViewById(R.id.q4);
        q5 = findViewById(R.id.q5);
        q6 = findViewById(R.id.q6);
        balik = findViewById(R.id.balik);
        itemsave = findViewById(R.id.itemsave);
        itemfree = findViewById(R.id.itemfree);
        ps = findViewById(R.id.ps);
        itemid = findViewById(R.id.itemid);
        idlvl = findViewById(R.id.idlvl);
        //untbase = findViewById(R.id.untbase);
        samvalue = findViewById(R.id.samvalue);
        datasam = findViewById(R.id.datasam);
        basihan = findViewById(R.id.basihan);
        lessbo = findViewById(R.id.lessbo);
        dew = findViewById(R.id.dew);
        salesrate = findViewById(R.id.salesrate);
        date_from = findViewById(R.id.date_from);
        date_to = findViewById(R.id.date_to);
        generate_valuation = findViewById(R.id.generate_valuation);
        generate_open_orders = findViewById(R.id.generate_open_orders);
        date_from.setText(mdatabaseHelper.get_date_from());
        date_to.setText(mdatabaseHelper.get_date_to());
        sug2= findViewById(R.id.sug2);
        inv1 = findViewById(R.id.inv1);
        ps2 = findViewById(R.id.ps2);
        inv_uom = findViewById(R.id.inv_uom);
        q5.setText("0");
        q6.setText("0.00");





        units = new ArrayList<>();

        ArrayList<Item> itemList = displayUOM();

        balik.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gg = new Intent(SFAInputQuantity.this, ItemPricelvlChoiceDisplay.class);
                //startActivity(gg);
            }
        });

        SharedPreferences preferences = getSharedPreferences("MyItems", Context.MODE_PRIVATE);

        String oid = preferences.getString("ITEMIO", "");
        String icode = preferences.getString("ICODE", "");
        String ides = preferences.getString("IDESCRIPTION", "");
        String iquantity = preferences.getString("IQUANT", "");
        String irate = preferences.getString("IRATE", "");
        String UU = preferences.getString("pcs", "");
        String cquantity = preferences.getString("UNITM", "");
        String Uname = preferences.getString("name", "");
        String plvlid = preferences.getString("PLVL", "");
        salesrate.setText(preferences.getString("wsr", ""));
        String customer_id =  preferences.getString("customer_id", "");
        String customer_name = mdatabaseHelper.get_customer_name(Integer.parseInt(customer_id));
        String description = "";
        String date_from_ = "";
        String date_to_ = "";



        salesrate.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Intent open_valuation_breakdown  = new Intent(SFAInputQuantity.this,CustomerSalesByItemHistory.class);
                open_valuation_breakdown.putExtra("customerid",customer_id);
                open_valuation_breakdown.putExtra("customername",customer_name);
                open_valuation_breakdown.putExtra("itemcode",icode);
                startActivity(open_valuation_breakdown);
            }
        });



        // Toast.makeText(this, ""+Uname, Toast.LENGTH_SHORT).show();

        q1.setText(icode);
        q2.setText(ides);
        q3.setText(iquantity);
        q4.setText(irate);
        ps.setText(UU);
        ps2.setText("1");
        inv_uom.setText(UU);
        dew.setText(UU);
        itemid.setText(oid);
        idlvl.setText(plvlid);
        datasam.setText(irate);

        inv_uom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inv1.getText().toString().isEmpty()) {
                    inv1.setText("0");
                }
                PopupMenu popupMenu = new PopupMenu(SFAInputQuantity.this, ps);
                Menu menu = popupMenu.getMenu();

                ArrayList<Item> items = displayUOM();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String selectedOption = item.getTitle().toString();
                        String selectedQuantityString = selectedOption.substring(selectedOption.indexOf("(") + 1, selectedOption.indexOf(")"));
                        double selectedQuantity = Double.parseDouble(selectedQuantityString);
                        double safety = Double.parseDouble(mdatabaseHelper.get_safety());
                        double order_point = Double.parseDouble(mdatabaseHelper.get_order_point());
                        double max = Double.parseDouble(mdatabaseHelper.get_max());
                        double wsr = Double.parseDouble(preferences.getString("wsr", ""));
                        wsr = wsr/selectedQuantity;
                        DecimalFormat df = new DecimalFormat("0.00");
                        salesrate.setText(Double.toString(Double.parseDouble(df.format(wsr))));
                        double on_hand = Double.parseDouble(inv1.getText().toString());
                        safety = safety * wsr;
                        order_point = safety + (order_point * wsr);
                        max = order_point + (max * wsr);
                        double suggested = max - on_hand;
                        sug2.setText(Double.toString(Double.parseDouble(df.format(suggested))));
                        ps2.setText(Double.toString(selectedQuantity));
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        ps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  inv1.getText().toString().isEmpty()){
                    inv1.setText("0");
                }
                PopupMenu popupMenu = new PopupMenu(SFAInputQuantity.this, ps);
                Menu menu = popupMenu.getMenu();

                ArrayList<Item> items = displayUOM();

                if (!cquantity.isEmpty() && items.size() > 0) {
                    try {
                        double cquantityValue = Double.parseDouble(cquantity);
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        decimalFormat.setRoundingMode(RoundingMode.DOWN);

                        String formattedCQuantity = decimalFormat.format(cquantityValue);

                        for (Item item : items) {
                            String formatteddCQuantity = decimalFormat.format(Double.parseDouble(item.getQuantity()));
                            String formattedCaseValue = item.getUnitquant() + "(" + formatteddCQuantity + ")";
                            menu.add(formattedCaseValue);
                            //   basihan.setText(formatteddCQuantity);
                        }
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String selectedOption = item.getTitle().toString();

                                // Extracting the selected quantity value from the selectedOption
                                String selectedQuantityString = selectedOption.substring(selectedOption.indexOf("(") + 1, selectedOption.indexOf(")"));
                                double selectedQuantity = Double.parseDouble(selectedQuantityString);
                                String dd = datasam.getText().toString();

                                double ddValue = Double.parseDouble(dd);
                                double result = selectedQuantity * ddValue;
                                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                                String formattedResult = decimalFormat.format(result);
                                q4.setText(formattedResult);
                                String quantityString = q5.getText().toString();
                                if (!quantityString.isEmpty()) {

                                    int quantity = Integer.parseInt(quantityString);
                                    double price = Double.parseDouble(q4.getText().toString());
                                    double computedValue = computeValue(quantity, price);

                                    NumberFormat numberFormat = new DecimalFormat("#,###.00");
                                    String formattedValue = numberFormat.format(computedValue);
                                    q6.setText(formattedValue);

                                } else {
                                    q6.setText("");
                                }
                                basihan.setText(selectedQuantityString);
                                ps.setText(selectedOption);

                                String selectedUOM = selectedOption.substring(0, selectedOption.indexOf("("));
                                dew.setText(selectedUOM);
                                return true;
                            }
                        });

                        popupMenu.show();

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case where cquantity is empty or there are no items
                }
            }
        });


        itemsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                //  Intent qa = new Intent(InputQuantity.this, ItemPricelvlChoiceDisplay.class);

//                if (q3.getText().toString().equals("0")) {
//                    Toast.makeText(InputQuantity.this, "Out of Stock Please Select Another Item", Toast.LENGTH_SHORT).show();
//                } else
                if (!q5.getText().toString().isEmpty() && !q6.getText().toString().isEmpty() && itemid.getText().toString().equals("19080") && !inv1.getText().toString().isEmpty()) { String dataa = idlvl.getText().toString();
                    String data0 = itemid.getText().toString();
                    String data1 = q1.getText().toString();
                    String data2 = q2.getText().toString();
                    String datass = "";
                    String data3 = q3.getText().toString();
                    String data4 = q4.getText().toString();
                    String data5 = q5.getText().toString();
                    String data6 = q6.getText().toString();
                    String asd = basihan.getText().toString();
                    Double inventory = Double.parseDouble(inv1.getText().toString());
                    Double wsr = Double.parseDouble(salesrate.getText().toString());
                    Double suggested = Double.parseDouble(sug2.getText().toString());

                    Item2 item2 = new Item2(asd,dataa, data0, data1, data2,datass, "-1", data5, "-1", data6,inventory,wsr,suggested,inv_uom.getText().toString());
                    //long itemId = databaseHelper.storeOrderItem(item2);
                    databaseHelper.storeOrderItem(item2);
                    Toast.makeText(SFAInputQuantity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                    idlvl.setText("");
                    q1.setText("");
                    q2.setText("");
                    q3.setText("");
                    q4.setText("");
                    q5.setText("");
                    q6.setText("");
                    itemid.setText("");
                    inv1.setText("");
                    salesrate.setText("");
                    sug2.setText("");
                    // startActivity(qa);
                    finish();
                    return;
                }
                if (!q5.getText().toString().isEmpty() && !q6.getText().toString().isEmpty() && !inv1.getText().toString().isEmpty()) {

                    // String cqa = untbase.getText().toString();
                    String dataa = idlvl.getText().toString();
                    String data0 = itemid.getText().toString();
                    String data1 = q1.getText().toString();
                    String data2 = q2.getText().toString();
                    String datass = dew.getText().toString();
                    String data3 = q3.getText().toString();
                    String data4 = q4.getText().toString();
                    String data5 = q5.getText().toString();
                    String data6 = q6.getText().toString();
                    String asd = basihan.getText().toString();
                    Double inventory = Double.parseDouble(inv1.getText().toString());
                    Double wsr = Double.parseDouble(salesrate.getText().toString());
                    Double suggested = Double.parseDouble(sug2.getText().toString());

                    Item2 item2 = new Item2(asd,dataa, data0, data1, data2,datass, data3, data4, data5, data6,inventory,wsr,suggested,inv_uom.getText().toString());
                    //long itemId = databaseHelper.storeOrderItem(item2);
                    databaseHelper.storeOrderItem(item2);
                    Toast.makeText(SFAInputQuantity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                    idlvl.setText("");
                    q1.setText("");
                    q2.setText("");
                    q3.setText("");
                    q4.setText("");
                    q5.setText("");
                    q6.setText("");
                    itemid.setText("");
                    inv1.setText("");
                    salesrate.setText("");
                    sug2.setText("");

                    // startActivity(qa);
                    finish();

                } else {
                    Toast.makeText(SFAInputQuantity.this, "Please Check fill up all details. (Inventory and qty) ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        itemfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                //  Intent qa = new Intent(InputQuantity.this, ItemPricelvlChoiceDisplay.class);

//                if (q3.getText().toString().equals("0")) {
//                    Toast.makeText(InputQuantity.this, "Out of Stock Please Select Another Item", Toast.LENGTH_SHORT).show();
//                }
//                else
                if (!q5.getText().toString().isEmpty() && !q6.getText().toString().isEmpty()) {
                    final AlertDialog.Builder builder= new AlertDialog.Builder(SFAInputQuantity.this);
                    builder.setMessage("You are about to add this item for free. Would you want to proceed?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    final AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // String cqa = untbase.getText().toString();
                            String dataa = idlvl.getText().toString();
                            String data0 = itemid.getText().toString();
                            String data1 = q1.getText().toString();
                            String data2 = q2.getText().toString();
                            String datass = dew.getText().toString();
                            String data3 = q3.getText().toString();
                            String data4 = q4.getText().toString();
                            String data5 = q5.getText().toString();
                            String data6 = q6.getText().toString();
                            String asd = basihan.getText().toString();
                            Double inventory = Double.parseDouble(inv1.getText().toString());
                            Double wsr = Double.parseDouble(salesrate.getText().toString());
                            Double suggested = Double.parseDouble(sug2.getText().toString());

                            Item2 item2 = new Item2(asd,dataa, data0, data1, data2,datass, data3, "0.00", data5, "0.00",inventory,wsr,suggested,inv_uom.getText().toString());
                            //long itemId = databaseHelper.storeOrderItem(item2);
                            databaseHelper.storeOrderItem(item2);
                            Toast.makeText(SFAInputQuantity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                            idlvl.setText("");
                            q1.setText("");
                            q2.setText("");
                            q3.setText("");
                            q4.setText("");
                            q5.setText("");
                            q6.setText("");
                            itemid.setText("");
                            inv1.setText("");
                            salesrate.setText("");
                            sug2.setText("");
                            // startActivity(qa);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(SFAInputQuantity.this, "Please Check All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lessbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                //  Intent qa = new Intent(InputQuantity.this, ItemPricelvlChoiceDisplay.class);

//                if (q3.getText().toString().equals("0")) {
//                    Toast.makeText(InputQuantity.this, "Out of Stock Please Select Another Item", Toast.LENGTH_SHORT).show();
//                }
//                else
                if (!q5.getText().toString().isEmpty() && !q6.getText().toString().isEmpty()) {
                    final AlertDialog.Builder builder= new AlertDialog.Builder(SFAInputQuantity.this);
                    builder.setMessage("You are about to add this item as less bo. Would you want to proceed?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    final AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // String cqa = untbase.getText().toString();
                            String dataa = idlvl.getText().toString();
                            String data0 = itemid.getText().toString();
                            String data1 = q1.getText().toString();
                            String data2 = q2.getText().toString();
                            String datass = dew.getText().toString();
                            String data3 = q3.getText().toString();
                            String data4 = q4.getText().toString();
                            String data5 = Integer.toString(Integer.parseInt(q5.getText().toString())*-1);
                            String data6 = Double.toString(Double.parseDouble(q6.getText().toString().replace(",",""))*-1.00);
                            String asd = basihan.getText().toString();
                            Double inventory = Double.parseDouble(inv1.getText().toString());
                            Double wsr = Double.parseDouble(salesrate.getText().toString());
                            Double suggested = Double.parseDouble(sug2.getText().toString());

                            Item2 item2 = new Item2(asd,dataa, data0, data1, data2,datass, data3, data4, data5, data6,inventory,wsr,suggested,inv_uom.getText().toString());
                            //long itemId = databaseHelper.storeOrderItem(item2);
                            databaseHelper.storeOrderItem(item2);
                            Toast.makeText(SFAInputQuantity.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();

                            idlvl.setText("");
                            q1.setText("");
                            q2.setText("");
                            q3.setText("");
                            q4.setText("");
                            q5.setText("");
                            q6.setText("");
                            itemid.setText("");
                            inv1.setText("");
                            salesrate.setText("");
                            sug2.setText("");
                            // startActivity(qa);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(SFAInputQuantity.this, "Please Check All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });


        q5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String quantityString = editable.toString();
                if (!quantityString.isEmpty()) {

                    int quantity = Integer.parseInt(quantityString);
                    double price = Double.parseDouble(q4.getText().toString());
                    double computedValue = computeValue(quantity, price);

                    NumberFormat numberFormat = new DecimalFormat("#,###.00");
                    String formattedValue = numberFormat.format(computedValue);
                    q6.setText(formattedValue);

                } else {
                    q6.setText("");
                }

            }
        });
        inv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(inv1.getText().toString().isEmpty()){
                    inv1.setText("0");
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
                double safety = Double.parseDouble(mdatabaseHelper.get_safety());
                double order_point = Double.parseDouble(mdatabaseHelper.get_order_point());
                double max = Double.parseDouble(mdatabaseHelper.get_max());
                double wsr = Double.parseDouble(preferences.getString("wsr", ""))/Double.parseDouble(ps2.getText().toString());
                DecimalFormat df = new DecimalFormat("0.00");
                salesrate.setText(Double.toString(Double.parseDouble(df.format(wsr))));
                double on_hand = Double.parseDouble(inv1.getText().toString());
                safety = safety * wsr;
                order_point = safety + (order_point * wsr);
                max = order_point + (max * wsr);
                double suggested = max - on_hand;
                sug2.setText(Double.toString(Double.parseDouble(df.format(suggested))));
            }
        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                history_calendar.set(Calendar.YEAR, year);
                history_calendar.set(Calendar.MONTH,month);
                history_calendar.set(Calendar.DAY_OF_MONTH,day);
                update_datefrom();
                Log.d("check_date_changed", date_from.getText().toString());
//                populate_table(date_from.getText().toString());
            }
        };

        DatePickerDialog.OnDateSetListener dialog_date_to =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                history_calendar_to.set(Calendar.YEAR, year);
                history_calendar_to.set(Calendar.MONTH,month);
                history_calendar_to.set(Calendar.DAY_OF_MONTH,day);
                update_date_to();
                Log.d("check_date_changed", date_to.getText().toString());
//                populate_table(date_from.getText().toString());
            }
        };
        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SFAInputQuantity.this,date,history_calendar.get(Calendar.YEAR),history_calendar.get(Calendar.MONTH),history_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SFAInputQuantity.this,dialog_date_to,history_calendar_to.get(Calendar.YEAR),history_calendar_to.get(Calendar.MONTH),history_calendar_to.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        generate_valuation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_valuation_breakdown  = new Intent(SFAInputQuantity.this,ItemValuationDetail.class);
                open_valuation_breakdown.putExtra("code",q1.getText().toString());
                open_valuation_breakdown.putExtra("description",q2.getText().toString());
                open_valuation_breakdown.putExtra("date_from",date_from.getText().toString());
                open_valuation_breakdown.putExtra("date_to",date_to.getText().toString());
                startActivity(open_valuation_breakdown);
            }
        });

        generate_open_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_valuation_breakdown  = new Intent(SFAInputQuantity.this,OpenSalesOrder.class);
                open_valuation_breakdown.putExtra("code",q1.getText().toString());
                open_valuation_breakdown.putExtra("description",q2.getText().toString());
                open_valuation_breakdown.putExtra("date_from",date_from.getText().toString());
                open_valuation_breakdown.putExtra("date_to",date_to.getText().toString());
                startActivity(open_valuation_breakdown);
            }
        });


    }

    //* FORMULA IN COMPUTING THE TOTAL PAYABLE IN THE S/O //*
    private double computeValue(int quantity, double price) {
        return quantity * price;
    }

    @SuppressLint("Range")
    public ArrayList<Item> displayUOM() {
        ArrayList<Item> itemhist = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("MyItems", Context.MODE_PRIVATE);
        String oid = preferences.getString("ITEMIO", "");

        itemid.setText(oid);
        String dtss = itemid.getText().toString();

        String query = "SELECT " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " + TABLE_NAME + "." + ITEM_UNIT_MEASURE + ", '1' AS qty " +
                "FROM " + TABLE_NAME + " " +
                "WHERE " + TABLE_NAME + "." + ITEMID + " = " + dtss +
                " UNION ALL " +
                "SELECT " +
                TABLE_NAME + "." + ITEM_DESCRIPTION + ", " + UNIT_MEASURE_TABLE + "." + NAME + ", " + UNIT_MEASURE_TABLE + "." + QUANTITY + " " +
                "FROM " + TABLE_NAME + " " +
                "INNER JOIN " + UNIT_MEASURE_TABLE + " " +
                "ON " + UNIT_MEASURE_TABLE + "." + ITM + " = " + TABLE_NAME + "." + ITEMID + " " +
                "WHERE " + UNIT_MEASURE_TABLE + "." + ITM + " = " + dtss;

        SQLiteDatabase db = mdatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String description = cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION));
                String unitMeasure = cursor.getString(cursor.getColumnIndex(ITEM_UNIT_MEASURE));
                String quantity = cursor.getString(cursor.getColumnIndex("qty"));

                Item item = new Item();
                item.setDescription(description);
                item.setUnitquant(unitMeasure);
                item.setQuantity(quantity);

                itemhist.add(item);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemhist;
    }
    private void update_datefrom(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.TAIWAN);
        date_from.setText(dateFormat.format(history_calendar.getTime()));
        mdatabaseHelper.update_date_from(dateFormat.format(history_calendar.getTime()));
    }
    private void update_date_to(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.TAIWAN);
        date_to.setText(dateFormat.format(history_calendar_to.getTime()));
        mdatabaseHelper.update_date_to(dateFormat.format(history_calendar_to.getTime()));
    }
    // i want every time i select in pupup  it perform
}

