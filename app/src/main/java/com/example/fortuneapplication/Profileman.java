package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.DASHBOARDID;
import static com.example.fortuneapplication.PazDatabaseHelper.DASHBOARD_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.D_CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.D_CUSTOMER_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.D_DATE;
import static com.example.fortuneapplication.PazDatabaseHelper.D_TOTAL;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_CODE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Profileman extends AppCompatActivity {

    TextView date1, date2, dt1, dt2;
    EditText totaldata;
    ImageView del;
    private PazDatabaseHelper mDatabaseHelper;
    AlertDialog.Builder builder;
    RecyclerView listHist;
    Button filt;

    ArrayList<Dashboardne> dashboardness;
    private ProfDataAdapter profDataAdapter;
    private Calendar selectedDateCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileman);
        mDatabaseHelper = new PazDatabaseHelper(this);
        del = findViewById(R.id.del);
        builder = new AlertDialog.Builder(this);
        filt = findViewById(R.id.filt);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        dt1 = findViewById(R.id.dt1);
        dt2 = findViewById(R.id.dt2);
        totaldata = findViewById(R.id.totaldata);
        listHist = findViewById(R.id.listHist);
        selectedDateCalendar = Calendar.getInstance();
//
//        double totalAmount = mDatabaseHelper.DashTot();
//        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
//        String formattedTotal = decimalFormat.format(totalAmount);
//        totaldata.setText(formattedTotal);


        filt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String dt = dt1.getText().toString();
               String td = dt2.getText().toString();

               if (!dt.isEmpty() && !td.isEmpty()) {

                   dashboardness = new ArrayList<>(queryDataInRange());
                   Log.d("Profileman", "Filtered data count: " + dashboardness.size());
                   listHist.setLayoutManager(new LinearLayoutManager(Profileman.this));
                   profDataAdapter = new ProfDataAdapter(dashboardness, Profileman.this);
                   listHist.setAdapter(profDataAdapter);

               }else{
                   Toast.makeText(Profileman.this, "Please Select Valid Date to Filter", Toast.LENGTH_SHORT).show();
               }


            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("WARNING")
                        .setMessage("Do you want Delete your Data?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PazDatabaseHelper databaseHelper = new PazDatabaseHelper(getApplicationContext());
                                databaseHelper.DeletealltotBOOK();
                                Toast.makeText(Profileman.this, "Successfully Deleted", Toast.LENGTH_LONG).show();
                                Intent labn = new Intent(Profileman. this, SOActivity.class);
                                startActivity(labn);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();

            }
        });
    }

    private void showDatePickerDialog() {
        int year = selectedDateCalendar.get(Calendar.YEAR);
        int month = selectedDateCalendar.get(Calendar.MONTH);
        int day = selectedDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                datePickerListener,
                year, month, day
        );
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            selectedDateCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDateCalendar.getTime());

            dt1.setText(formattedDate);
        }
    };

    private void showDatePickerDialog2() {
        int year = selectedDateCalendar.get(Calendar.YEAR);
        int month = selectedDateCalendar.get(Calendar.MONTH);
        int day = selectedDateCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                datePickerListener2,
                year, month, day
        );
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            selectedDateCalendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy", Locale.getDefault());
            String formattedDate = sdf.format(selectedDateCalendar.getTime());

            dt2.setText(formattedDate);
        }
    };

    @SuppressLint("Range")
    public List<Dashboardne> queryDataInRange() {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        List<Dashboardne> resultList = new ArrayList<>();

        String startDateString = dt1.getText().toString();
        String endDateString = dt2.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy", Locale.getDefault());
        Date startDate, endDate;
        try {
            startDate = sdf.parse(startDateString);
            endDate = sdf.parse(endDateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return resultList;
        }

        String query = "SELECT " +
                DASHBOARD_TABLE + "." + D_CODE + ", " +
                DASHBOARD_TABLE + "." + D_CUSTOMER_NAME + ", " +
                "REPLACE(" + DASHBOARD_TABLE + "." + D_TOTAL + ", ',', '') AS " + D_TOTAL + ", " +
                DASHBOARD_TABLE + "." + D_DATE +
                " FROM " + DASHBOARD_TABLE +
                " WHERE " + D_DATE + " BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{sdf.format(startDate), sdf.format(endDate)});

        double totalSum = 0.0;

        if (cursor.moveToFirst()) {
            do {
                Dashboardne dashboardne = new Dashboardne();
                dashboardne.setDcode(cursor.getString(cursor.getColumnIndex(D_CODE)));
                dashboardne.setDcustomername(cursor.getString(cursor.getColumnIndex(D_CUSTOMER_NAME)));
                double dTotalValue = cursor.getDouble(cursor.getColumnIndex(D_TOTAL));
                dashboardne.setDtotal(dTotalValue);
                dashboardne.setDdate(cursor.getString(cursor.getColumnIndex(D_DATE)));

                resultList.add(dashboardne);

                totalSum += dTotalValue;
            } while (cursor.moveToNext());
        }

        cursor.close();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        String formattedTotal = decimalFormat.format(totalSum);
        totaldata.setText(formattedTotal);

        return resultList;

    }
}
