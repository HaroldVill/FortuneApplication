package com.example.fortuneapplication;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LocateActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PazDatabaseHelper mdatabaseHelper;
    private EditText historyDatepicker;
    private EditText timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        TableLayout salesTable = findViewById(R.id.salesreptable);
        historyDatepicker = findViewById(R.id.history_datepicker);
        timepicker = findViewById(R.id.history_timepicker);
        mdatabaseHelper = new PazDatabaseHelper(this);

        setCurrentDate();

        historyDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        setCurrentTime();

        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        ArrayList<SalesRepList> salesList = displaysalesrep();

        for (int i = 0; i < salesList.size(); i++) {
            SalesRepList rep = salesList.get(i);
            TableRow row = new TableRow(this);

            TextView number = new TextView(this);
            number.setText(String.valueOf(i + 1));
            number.setPadding(16, 16, 16, 16);

            TextView name = new TextView(this);
            name.setText(rep.getSrname());
            name.setPadding(16, 16, 16, 16);

            row.addView(number);
            row.addView(name);
            salesTable.addView(row);
        }
    }

    private void setCurrentDate() {
        String currentDate = getCurrentDate();
        historyDatepicker.setText(currentDate);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void setCurrentTime() {
        String currentTime = getCurrentTime();
        timepicker.setText(currentTime);
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(calendar.getTime());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(9.865303, 124.224499);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 9));
    }

    @SuppressLint("Range")
    public ArrayList<SalesRepList> displaysalesrep() {
        ArrayList<SalesRepList> salesreplist = new ArrayList<>();
        String query = "SELECT salesrep_id, salesrep_name FROM sales_rep_table order by salesrep_name";

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

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LocateActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        historyDatepicker.setText(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                LocateActivity.this, (view, hourOfDay, minute1) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    timepicker.setText(formattedTime);
        }, hour, minute, true
        );

        timePickerDialog.show();
    }
}