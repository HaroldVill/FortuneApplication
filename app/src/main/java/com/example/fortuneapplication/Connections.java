package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONECTIONID;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS_VALUE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Set;

public class Connections extends AppCompatActivity {
    TextView creatcon, ids;
    TextView selection, connection_label, select_sales_type,sales_type_label,select_sales_rep_id,sales_rep_id_label,select_gps_mode,gps_label,select_verify_mode,verify_label,select_bluetooth_device,bluetooth_macaddress,select_default_location,default_location,select_change_location,change_location,sfa_label,select_sfa_mode;
    Button apply,apply_sales_type,apply_sales_rep_id,apply_gps_mode,apply_verify_mode,apply_bluetooth,apply_order_point,apply_safety,apply_max, apply_sfa_mode;
    EditText editText1,editText2,editText3;
    TextView label1,label2,label3;
    private PazDatabaseHelper mdatabaseHelper;
    public static final String RECEIVE_latLng = "com.bustracker.RECEIVE_latLng";

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
        select_default_location = findViewById(R.id.select_default_location);
        default_location = findViewById(R.id.default_location);
        select_change_location = findViewById(R.id.select_change_location);
        change_location = findViewById(R.id.change_location);
        mdatabaseHelper = new PazDatabaseHelper(this);
        connection_label = findViewById(R.id.connection_label);
        connection_label.setText(mdatabaseHelper.get_active_connection());
        sales_type_label.setText(mdatabaseHelper.get_active_salestype());
        sales_rep_id_label.setText(mdatabaseHelper.get_default_salesrep());
        default_location.setText(mdatabaseHelper.get_default_location_name(Integer.parseInt(mdatabaseHelper.get_default_location_id())));
        change_location.setText(mdatabaseHelper.get_location_settings());
        apply = findViewById(R.id.apply);
        ids = findViewById(R.id.ids);
        apply_sales_type=findViewById(R.id.apply_sales_type);
        apply_sales_rep_id = findViewById(R.id.apply_sales_rep_id);
        select_gps_mode = findViewById(R.id.select_gps_mode);
        select_verify_mode = findViewById(R.id.select_verify_mode);
        gps_label = findViewById(R.id.gps_label);
        verify_label = findViewById(R.id.verify_label);
        gps_label.setText(mdatabaseHelper.get_coverage_type());
        verify_label.setText(mdatabaseHelper.get_verify_type());
        apply_gps_mode = findViewById(R.id.apply_gps_mode);
        apply_verify_mode = findViewById(R.id.apply_verify_mode);
        select_bluetooth_device = findViewById(R.id.select_bluetooth_device);
        bluetooth_macaddress = findViewById(R.id.bluetooh_macaddress);
        bluetooth_macaddress.setText(mdatabaseHelper.get_bluetooth_device());
        apply_bluetooth = findViewById(R.id.apply_bluetooth);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);
        label3 = findViewById(R.id.label3);
        apply_order_point = findViewById(R.id.apply_order_point);
        apply_safety = findViewById(R.id.apply_safety);
        apply_max = findViewById(R.id.apply_max);
        label1.setText(mdatabaseHelper.get_order_point());
        label2.setText(mdatabaseHelper.get_safety());
        label3.setText(mdatabaseHelper.get_max());
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_latLng);
        bManager.registerReceiver(receiver, intentFilter);
        apply_sfa_mode = findViewById(R.id.apply_sfa_mode);
        sfa_label = findViewById(R.id.sfa_label);
        select_sfa_mode = findViewById(R.id.select_sfa_mode);
        sfa_label.setText(mdatabaseHelper.get_sfa_mode());
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

        apply_sfa_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              update_sfa_mode();
            }
        });

        apply_bluetooth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                update_bluetooth_device();
            }
        });

        apply_order_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_order_point();
                label1.setText(mdatabaseHelper.get_order_point());
            }
        });

        apply_safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_safety();
                label2.setText(mdatabaseHelper.get_safety());
            }
        });

        apply_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_max();
                label3.setText(mdatabaseHelper.get_max());
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

        select_sfa_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this,select_sfa_mode);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"1");
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"0");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String sfa_type = menuItem.getTitle().toString();

                        sfa_label.setText(sfa_type);
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

        select_default_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this, selection);
                Menu menu = popupMenu.getMenu();
                ArrayList<Location> locationlists = displaylocation();

                for (Location locationlist : locationlists) {
                    menu.add(Menu.NONE, Integer.parseInt(locationlist.getLocid().toString()), Menu.NONE, locationlist.getLocname().toString());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int locationid = item.getItemId();
//                        String salesrepname = item.getItemId();
//                        select_sales_rep_id.setText(Integer.toString(salesrepid));
                        default_location.setText(Integer.toString(locationid));
                        update_location_id();
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        select_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(Connections.this,select_change_location);
                Menu menu = popupMenu.getMenu();
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"Allow");
                menu.add(Menu.NONE,Menu.NONE,Menu.NONE,"Strict");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String gps_type = menuItem.getTitle().toString();
                        change_location.setText(gps_type);
                        update_change_location();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        select_bluetooth_device.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                int requestCode = 1;
                BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
                BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                if (ActivityCompat.checkSelfPermission(Connections.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Connections.this,new String[]
                            {Manifest.permission.BLUETOOTH_CONNECT},1);
                }
                else {
                    if (!bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 0);
                    }
                    else {
//                    onActivityResult(requestCode,300,discoverableIntent);
                        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                        if (pairedDevices.size() > 0) {
                            // There are paired devices. Get the name and address of each paired device.

                            PopupMenu popupMenu = new PopupMenu(Connections.this, select_bluetooth_device);
                            Menu menu = popupMenu.getMenu();

                            for (BluetoothDevice device : pairedDevices) {
                                String deviceName = device.getName();
                                String deviceHardwareAddress = device.getAddress(); // MAC address
                                Log.d("DEVICE_NAME", deviceName);
                                Log.d("DEVICE_ADDRESS", deviceHardwareAddress);
                                menu.add(Menu.NONE, Menu.NONE, Menu.NONE, deviceName + " = " + deviceHardwareAddress);
                            }
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    String bluetooth_device = menuItem.getTitle().toString();
                                    bluetooth_macaddress.setText(bluetooth_device);
                                    return true;
                                }
                            });
                            popupMenu.show();
                        }
                    }
                }
            }
        });
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(Connections.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
//        unregisterReceiver(receiver);
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

    public ArrayList<Location> displaylocation() {
        ArrayList<Location> Locationlist = new ArrayList<>();

        String query = "SELECT location_id,location_name from location_table";

        SQLiteDatabase db = mdatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setLocid(cursor.getString(0));
                location.setLocname(cursor.getString(1));
                Locationlist.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Locationlist;

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
            Toast.makeText(this, "Successfull Sales Rep Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");

//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_location_id() {

        String location_id = default_location.getText().toString();

        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", location_id);

        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"DEFAULT_LOCATION_ID"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();

        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Location Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");

//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_change_location() {

        String location_id = change_location.getText().toString();

        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", location_id);

        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"ALLOW_CHANGE_LOCATION"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();

        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Location Set", Toast.LENGTH_SHORT).show();
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


    public void update_bluetooth_device() {
        String bluetooth_device = bluetooth_macaddress.getText().toString();
        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", bluetooth_device);
        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"BLUETOOTH_DEVICE"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Bluetooth Connection", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");
//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_order_point() {
        String value = editText1.getText().toString();
        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", value);
        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"ORDER_POINT"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Order Point Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");
//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_safety() {
        String value = editText2.getText().toString();
        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", value);
        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"SAFETY"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Safety Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");
//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }

    public void update_max() {
        String value = editText3.getText().toString();
        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", value);
        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"MAX"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull Max Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");
//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }


    public void update_sfa_mode() {
        String value = sfa_label.getText().toString();
        SQLiteDatabase db = mdatabaseHelper.getWritableDatabase();
        ContentValues specificRowValues = new ContentValues();
        specificRowValues.put("VALUE", value);
        String whereClause = SYSTEM_SETTINGS_NAME + " = ?";
        String[] whereArgs = {"SFA"};

        int numSpecificRowUpdated = db.update(SYSTEM_SETTINGS, specificRowValues, whereClause, whereArgs);
        db.close();
        if (numSpecificRowUpdated > 0) {
            Toast.makeText(this, "Successfull SFA Set", Toast.LENGTH_SHORT).show();
//            selection.setText("");
//            connection_label.setText("");
//            Intent nanay = new Intent(Connections.this, SyncDatas.class);
//            startActivity(nanay);
//            finish();
        }
    }
}
