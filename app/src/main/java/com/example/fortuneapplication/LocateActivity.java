package com.example.fortuneapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LocateActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PazDatabaseHelper mdatabaseHelper;
    private EditText historyDatepicker;
    private EditText timepicker;
    private TableLayout salesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);

        salesTable = findViewById(R.id.salesreptable);
        historyDatepicker = findViewById(R.id.history_datepicker);
        timepicker = findViewById(R.id.history_timepicker);
        mdatabaseHelper = new PazDatabaseHelper(this);

        setCurrentDate();
        setCurrentTime();

        historyDatepicker.setOnClickListener(v -> showDatePickerDialog());
        timepicker.setOnClickListener(view -> showTimePickerDialog());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(9.865303, 124.224499);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 9));

//        LatLng testLocation = new LatLng(9.9, 124.2);
//        mMap.addMarker(new MarkerOptions().position(testLocation).title("Test marker"));

        refreshSalesData();
    }

    private void refreshSalesData() {
        mMap.clear();
        salesTable.removeAllViews();

        String date = historyDatepicker.getText().toString();
        String time = timepicker.getText().toString();
        String datetime = date + " " + time;

        ArrayList<SalesRepList> salesList = displaysalesrep(datetime);
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

        loadMapDataIndividually(date, datetime, salesList);
    }

    private void loadMapDataIndividually(String date, String datetime, ArrayList<SalesRepList> salesList) {

//        final boolean[] cameraMoved = {false};

        for (int i = 0; i < salesList.size(); i++) {
            SalesRepList salesRep = salesList.get(i);
            String salesRepId = salesRep.getSrid();
            final int markerIndex = i+1;

            String JSON_URL = "http://" + mdatabaseHelper.get_active_connection() +
                    "/mobileapi/get_salesrep_coordinates.php?selectedDate=" + date +
                    "&selectedDateTime=" + datetime +
                    "&salesrepid=" + salesRepId;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (jsonArray.length() == 0) {
                                Log.d("MARKER_DEBUG", "No data for rep ID: " + salesRepId);
                                return;
                            }

                            JSONObject json = jsonArray.getJSONObject(0);
                            double latitude = Double.parseDouble(json.getString("latitude"));
                            double longitude = Double.parseDouble(json.getString("longitude"));
                            String  hello = json.getString("datetime");
                            LatLng latLng = new LatLng(latitude, longitude);
                            Log.d("MARKER_DEBUG", "Salesrep #" + markerIndex + " @ " + latLng);

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .title(hello)
                                    .icon(getNumberedMarkerIcon(markerIndex));

                            mMap.addMarker(markerOptions);

                        } catch (JSONException e) {
                            Log.e("MARKER_DEBUG", "JSON parse error: " + e.getMessage());
                        }
                    },
                    error -> Log.e("MARKER_DEBUG", "Volley error: " + error.getMessage())
            );

            Volley.newRequestQueue(this).add(stringRequest);
        }

    }

    @SuppressLint("Range")
    public ArrayList<SalesRepList> displaysalesrep(String datetime) {
        ArrayList<SalesRepList> salesreplist = new ArrayList<>();
        String query = "SELECT salesrep_id, salesrep_name FROM sales_rep_table ORDER BY salesrep_name";

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
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    historyDatepicker.setText(dateFormat.format(selectedDate.getTime()));
                    refreshSalesData();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    timepicker.setText(formattedTime);
                    refreshSalesData();
                },
                hour, minute, true
        ).show();
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        historyDatepicker.setText(dateFormat.format(calendar.getTime()));
    }

    private void setCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timepicker.setText(timeFormat.format(calendar.getTime()));
    }

    private BitmapDescriptor getNumberedMarkerIcon(int number) {

        Bitmap base = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        Bitmap mutable = base.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutable);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        String text = String.valueOf(number);
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        float x = mutable.getWidth() / 2f;
        float y = mutable.getHeight() / 2f;

        canvas.drawText(text, x, y, textPaint);

        return BitmapDescriptorFactory.fromBitmap(mutable);
    }
}