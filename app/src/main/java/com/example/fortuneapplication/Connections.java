package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.CONECTIONID;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_IP;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.CONNECTION_TABLE;

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
    TextView selection, ipadd;
    Button apply;
    private PazDatabaseHelper mdatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        creatcon = findViewById(R.id.creatcon);
        selection = findViewById(R.id.selection);
        mdatabaseHelper = new PazDatabaseHelper(this);
        ipadd = findViewById(R.id.ipadd);
        apply = findViewById(R.id.apply);
        ids = findViewById(R.id.ids);


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatemen();
            }
        });

        creatcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cnt = new Intent(Connections.this, ConnecttionSetup.class);
                startActivity(cnt);
                finish();
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
                        ipadd.setText(ipconf);
                        ids.setText(String.valueOf(connectionId));

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
            selection.setText("");
            ipadd.setText("");

            Intent nanay = new Intent(Connections.this, SyncDatas.class);
            startActivity(nanay);
            finish();
        }
    }
}
