package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONECTIONID;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS_VALUE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Connections extends AppCompatActivity {
    TextView creatcon, ids;
    TextView selection, connection_label, select_sales_type,sales_type_label,select_sales_rep_id,sales_rep_id_label,select_gps_mode,gps_label,select_verify_mode,verify_label;
    Button apply,apply_sales_type,apply_sales_rep_id,apply_gps_mode,apply_verify_mode;
    private PazDatabaseHelper mdatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        creatcon = findViewById(R.id.creatcon);
        selection = findViewById(R.id.selection);
        select_sales_type = findViewById(R.id.select_sales_type);
        sales_type_label = findViewById(R.id.sales_type_label);
        select_sales_rep_id = findViewById(R.id.select_sales_rep_id);
        sales_rep_id_label = findViewById(R.id.sales_rep_id_label);
        mdatabaseHelper = new PazDatabaseHelper(this);
        connection_label = findViewById(R.id.connection_label);
        connection_label.setText(mdatabaseHelper.get_active_connection());
        sales_type_label.setText(mdatabaseHelper.get_active_salestype());
        sales_rep_id_label.setText(mdatabaseHelper.get_default_salesrep());
        apply = findViewById(R.id.apply);
        ids = findViewById(R.id.ids);
        apply_sales_type=findViewById(R.id.apply_sales_type);
        apply_sales_rep_id = findViewById(R.id.apply_sales_rep_id);
        select_gps_mode = findViewById(R.id.select_gps_mode);
        select_verify_mode = findViewById(R.id.select_verify_mode);
        gps_label = findViewById(R.id.gps_label);
        verify_label = findViewById(R.id.verify_label);
        gps_label.setText(mdatabaseHelper.get_coverage_type());
        apply_gps_mode = findViewById(R.id.apply_gps_mode);
        apply_verify_mode = findViewById(R.id.apply_verify_mode);
        apply_sales_rep_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });
        apply_sales_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_sales_type();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatemen();
            }
        });

        apply_gps_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_coverage_type();
            }
        });

        apply_verify_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_verify_type();
            }
        });


        creatcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cnt = new Intent(Connections.this, ConnecttionSetup.class);
                startActivity(cnt);
//                finish();
            }
        });
        select_sales_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this,select_sales_type);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"sync_sales_order.php");
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"sync_sales_receipt.php");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String sales_type = menuItem.getTitle().toString();
                        select_sales_type.setText(sales_type);
                        sales_type_label.setText(sales_type);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        select_gps_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this,select_gps_mode);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"Allow");
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"Strict");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String gps_type = menuItem.getTitle().toString();

                        gps_label.setText(gps_type);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        select_verify_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this,select_verify_mode);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"1");
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"0");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String verify_type = menuItem.getTitle().toString();

                        verify_label.setText(verify_type);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        selection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(Connections.this, selection);
                Menu menu = popupMenu.getMenu();
                ArrayList<CONNECT> connects = displayselect();

                for (CONNECT connect : connects) {
                    menu.add(Menu.NONE, connect.getId(), Menu.NONE, connect.getName());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int connectionId = item.getItemId();
                        String connectionName = item.getTitle().toString();
                        String ipconf = getIpForConnectionId(connectionId, connects);
                        selection.setText(connectionName);
                        connection_label.setText(ipconf);
                        ids.setText(String.valueOf(connectionId));
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        select_sales_rep_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this, selection);
                Menu menu = popupMenu.getMenu();
                ArrayList<SalesRepList> salesreplists = displaysalesrep();

                for (SalesRepList salesreplist : salesreplists) {
                    menu.add(Menu.NONE, Integer.parseInt(salesreplist.getSrid().toString()), Menu.NONE, salesreplist.getSrname().toString());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int salesrepid = item.getItemId();
                        String salesrepname = item.getTitle().toString();
//                        select_sales_rep_id.setText(Integer.toString(salesrepid));
                        sales_rep_id_label.setText(salesrepname);
                        update_sales_rep_id();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @SuppressLint("Range")
    public ArrayList<CONNECT> displayselect() {
        ArrayList<CONNECT> connectlist = new ArrayList<>();

        String query = "SELECT " +
                CONNECTION_TABLE + "." + CONECTIONID + ", " +
                CONNECTION_TABLE + "." + CONNECTION_NAME + ", " +
                CONNECTION_TABLE + "." + CONNECTION_IP +
                " FROM " + CONNECTION_TABLE;

        SQLiteDatabase db = mdatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int sdsf = cursor.getInt(cursor.getColumnIndex(CONECTIONID));
                String coname = cursor.getString(cursor.getColumnIndex(CONNECTION_NAME));
                String cip = cursor.getString(cursor.getColumnIndex(CONNECTION_IP));

                CONNECT connect = new CONNECT();
                connect.setId(sdsf);
                connect.setName(coname);
                connect.setIp(cip);
                connectlist.add(connect);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return connectlist;

    }
    @SuppressLint("Range")
    public ArrayList<SalesRepList> displaysalesrep() {
        ArrayList<SalesRepList> salesreplist = new ArrayList<>();

        String query = "SELECT salesrep_id,salesrep_name from sales_rep_table";

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

    private String getIpForConnectionId(int connectionId, ArrayList<CONNECT> connects) {
        for (CONNECT connect : connects) {
            if (connect.getId() == connectionId) {
                return connect.getIp();
            }
        }

        return "";
    }

    public void updatemen() {

        String idcon = ids.getText().toString();

        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();

        ContentValues allRowsValues = new ContentValues();
        allRowsValues.put("defaultconn", "0");
        int numAllRowsUpdated = db.update(CONNECTION_TABLE, allRowsValues, null, null);

        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("defaultconn", "1");

        String whereClause = CONECTIONID + " = ?";
        String[] whereArgs = {idcon};

        int numSpecificRowUpdated = db.update(CONNECTION_TABLE, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numAllRowsUpdated > 0) {

        }

        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfully IP Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");

//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_sales_type() {

        String sales_type = sales_type_label.getText().toString();

        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", sales_type);

        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"SALES_TYPE"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();

        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfully Sales Type Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");

//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_sales_rep_id() {

        String sales_rep_id = sales_rep_id_label.getText().toString();

        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", sales_rep_id);

        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"DEFAULT_SALES_REP_ID"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();

        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfully Sales Rep Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");

//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_coverage_type() {

        String coverage_type = gps_label.getText().toString();

        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", coverage_type);

        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"ALLOW_STRICT_COVERAGE"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();

        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfully Coverage Type Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");

//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }
    public void update_verify_type() {
        String verify_type = verify_label.getText().toString();
        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", verify_type);
        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"ALLOW_PIN_VERIFY"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Verify Type Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");
//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }
}
