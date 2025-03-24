package com.example.fortuneapplication;

import static android.provider.ContactsContract.Intents.Insert.NOTES;
import static com.example.fortuneapplication.PazDatabaseHelper.AMOUNT;
import static com.example.fortuneapplication.PazDatabaseHelper.CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_NAME;
import static com.example.fortuneapplication.PazDatabaseHelper.CUSTOMER_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_CODE;
import static com.example.fortuneapplication.PazDatabaseHelper.ITEM_DESCRIPTION;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDERID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDERITEMID;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_ITEMS_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SOCUSTOMER_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOIID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_AMOUNT;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_ITEM_ID;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_RATE;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_UNIT_BASE_QUANTITY;
import static com.example.fortuneapplication.PazDatabaseHelper.SOI_UOM;
import static com.example.fortuneapplication.PazDatabaseHelper.TABLE_NAME;

import static java.util.Calendar.DATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;


import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import org.json.JSONObject;


public class TemporaryData extends AppCompatActivity implements PrintingCallback {
    TextView ref, names, moreinfo, notesam, dt;
    ImageView hm, print,deletebutton,print_picklist;
    EditText supertot;
    RecyclerView datadisp;
    Printing print_receipt;
    //    ImageView printbutton;
    private PazDatabaseHelper mDatabaseHelper;
    private TempoDataAdapter tempoDataAdapter;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ArrayList<SALESORDERITEMS> orderItemss;

    FloatingActionButton arayba;
    AlertDialog.Builder builder;
    Context context;

    // declaring width and height
    // for our PDF file.

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;


    // Button update ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_data);

        ref = findViewById(R.id.ref);
        datadisp = findViewById(R.id.datadisp);
        names = findViewById(R.id.names);
        supertot = findViewById(R.id.supertot);
        hm = findViewById(R.id.hm);
        notesam = findViewById(R.id.notesam);
        dt = findViewById(R.id.dt);
        arayba = findViewById(R.id.arayba);
        print = findViewById(R.id.print);
        print_picklist = findViewById(R.id.print_picklist);
        builder = new AlertDialog.Builder(this);
        Printooth.INSTANCE.init(this);
        deletebutton = findViewById(R.id.deletebutton);


        mDatabaseHelper = new PazDatabaseHelper(this);
        orderItemss = new ArrayList<>();

        datadisp.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SALESORDERITEMS> salesorderitems = displayOrderItem();
        tempoDataAdapter = new TempoDataAdapter(salesorderitems,this);
        datadisp.setAdapter(tempoDataAdapter);

        SharedPreferences preferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
        String oid = preferences.getString("REFID", "");
        String nameses = preferences.getString("CN", "");
        //  String sprt = preferences.getString("TOT", "");
        int salesOrderId = preferences.getInt("SID", 0);

        String salesOrderIdString = String.valueOf(salesOrderId);
        ref.setText(salesOrderIdString);
        names.setText(nameses);
        //supertot.setText(sprt);


        hm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uliboy = new Intent(TemporaryData.this, HomePage.class);
                startActivity(uliboy);
                finish();
            }
        });

        arayba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                builder.setTitle("WARNING!")
//                        .setMessage("Do you want to Save the Updated Data ?")
//                        .setCancelable(true)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                updateton();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        })
//                        .show();
                    Intent uliboy = new Intent(TemporaryData.this, History.class);
                    startActivity(uliboy);
                    finish();
            }
        });


//        bmp = BitmapFactory.decodeResource(getResources(), com.android.car.ui.R.drawable.car_ui_list_item_avatar_icon_outline);
//        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
//                generatePDF(salesOrderId);
                if(mDatabaseHelper.get_so_posted_flag(salesOrderId)  == 0){
                    Toast.makeText(print.getContext(), "Order must be posted before printing receipt", Toast.LENGTH_SHORT).show();
                }
                else {
                    connectBluetooth(mDatabaseHelper.get_bluetooth_device(), salesOrderId);
                }
            }
        });
        print_picklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
//                generatePDF(salesOrderId);
                if(mDatabaseHelper.get_so_posted_flag(salesOrderId)  > 0){
                    Toast.makeText(print.getContext(), "Cannot picklist a posted order.", Toast.LENGTH_SHORT).show();
                }
                else {
                    connectBluetooth_PrintPicklist(mDatabaseHelper.get_bluetooth_device(), salesOrderId);
                }
            }
        });
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDatabaseHelper.get_so_posted_flag(salesOrderId)  > 0){
                    Toast.makeText(print.getContext(), "Cannot delete a posted order.", Toast.LENGTH_SHORT).show();
                }
                else {
                    builder.setTitle("WARNING")
                            .setMessage("Do you want to delete this transaction?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //save data to dashboard
                                    mDatabaseHelper.delete_so(salesOrderId);
                                    mDatabaseHelper.delete_so_items(salesOrderId);
                                    Toast.makeText(print.getContext(), "Order succesfully deleted.", Toast.LENGTH_SHORT).show();
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
            }
        });
    }


    public void connectBluetooth(String macAddress, Integer so_id) {

        try {
            Log.d("bluetoothtest", macAddress);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TemporaryData.this,new String[]
                        {Manifest.permission.BLUETOOTH_CONNECT},1);
            }
            else {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();


                mDatabaseHelper = new PazDatabaseHelper(TemporaryData.this);
                List<SALESORDERITEMS> salesOrderItemList = mDatabaseHelper.getSlsorderitems(so_id);
                for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {
    //            String quantity = Integer.toString(salesOrderItems.getSoiquantity());
    //            String rate = Double.toString(salesOrderItems.getSoirate())+"/"+salesOrderItems.getUom();
//                    pageHeight+=20;
                }
                String customer_name = "";
                String refno = "";
                String date = "";
                String total="";
                String salesrep="";
                List<SALESORDER> salesOrderList = mDatabaseHelper.getSlsorder(so_id);
                for (SALESORDER salesOrder : salesOrderList) {
                    customer_name ="Customer: "+salesOrder.getCustomer().getCustomername().toString();
                    refno="Ref#: "+salesOrder.getCode().toString();
                    date="Date: "+salesOrder.getDate().toString();
                    total = salesOrder.getAmount().toString();
                    salesrep= "Salesrep: " +salesOrder.get_sales_rep_name().toString();
                }

                outputStream = bluetoothSocket.getOutputStream();

//                String printCommand = "TEXT 0,0,\"0\",0,0,0,\"\"\n";
//                outputStream.write(printCommand.getBytes());
                String labelConfig = "SIZE 3,2\nGAP 0.11,0\n";
//                outputStream.write(labelConfig.getBytes());
                String CompanyName = "       PAZ DISTRIBUTION INC.\n";
                outputStream.write(CompanyName.getBytes());
                outputStream.write(customer_name.getBytes());
                Log.d("", "connectBluetooth: "+customer_name);
                outputStream.write(" \n".getBytes());
                outputStream.write(salesrep.getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write(refno.getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write(date.getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write(" \n".getBytes());
                Double TotalAmount = 0.00;
                Double TotalQuantity = 0.00;
                for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {

                    String Itemdesc = salesOrderItems.getitemdesc()+"\n";
                    outputStream.write(Itemdesc.getBytes());
                    Double amount = salesOrderItems.getSoirate() * salesOrderItems.getSoiquantity();
                    String quantity = Integer.toString(salesOrderItems.getSoiquantity())+" "+salesOrderItems.getUom()+" @ "
                            +String.format( "%,.2f",Float.parseFloat(Double.toString(salesOrderItems.getSoirate())))+"\n";
                    byte[] PRINT_RIGHT = {27, 70, 86};
                    String lineTotal = String.format("%1$"+30+ "s", String.format( "%,.2f",Float.parseFloat(Double.toString(amount))))+"\n";
                    outputStream.write(quantity.getBytes());
                    outputStream.write(lineTotal.getBytes());
//                    outputStream.write(PRINT_RIGHT);
//                    outputStream.write(lineTotal.getBytes());
//                    outputStream.write(" \n".getBytes());
                    TotalAmount= TotalAmount + amount;
                    TotalQuantity = TotalQuantity + salesOrderItems.getSoiquantity();
                }
                String AmountTotal = String.format("\n"+"%1$"+30+ "s", "TOTAL: "+String.format( "%,.2f",Float.parseFloat(Double.toString(TotalAmount))))+"\n";
                outputStream.write(AmountTotal.getBytes());
//                outputStream.write("________________________________".getBytes());

                bluetoothSocket.close();
            }

        } catch (IOException ex) {
//            handler.obtainMessage(1).sendToTarget();
        }
    }

    public void connectBluetooth_PrintPicklist(String macAddress, Integer so_id) {

        try {
            Log.d("bluetoothtest", macAddress);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TemporaryData.this,new String[]
                        {Manifest.permission.BLUETOOTH_CONNECT},1);
            }
            else {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();


                mDatabaseHelper = new PazDatabaseHelper(TemporaryData.this);
                List<SALESORDERITEMS> salesOrderItemList = mDatabaseHelper.getSlsorderitems(so_id);
                for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {
                    //            String quantity = Integer.toString(salesOrderItems.getSoiquantity());
                    //            String rate = Double.toString(salesOrderItems.getSoirate())+"/"+salesOrderItems.getUom();
//                    pageHeight+=20;
                }
                String customer_name = "";
                String refno = "";
                String date = "";
                String total="";
                String salesrep="";
                List<SALESORDER> salesOrderList = mDatabaseHelper.getSlsorder_unposted(so_id);
                for (SALESORDER salesOrder : salesOrderList) {
                    customer_name ="Customer: "+salesOrder.getCustomer().getCustomername().toString();
                    refno="Ref#: "+salesOrder.getCode().toString();
                    date="Date: "+salesOrder.getDate().toString();
                    total = salesOrder.getAmount().toString();
                    salesrep= "Salesrep: " +salesOrder.get_sales_rep_name().toString();
                }

                outputStream = bluetoothSocket.getOutputStream();

//                String printCommand = "TEXT 0,0,\"0\",0,0,0,\"\"\n";
//                outputStream.write(printCommand.getBytes());
                String labelConfig = "SIZE 3,2\nGAP 0.11,0\n";
//                outputStream.write(labelConfig.getBytes());
                String CompanyName = "       PAZ DISTRIBUTION INC.\n";
                outputStream.write(CompanyName.getBytes());
                outputStream.write(customer_name.getBytes());
                Log.d("", "connectBluetooth: "+customer_name);
                outputStream.write(" \n".getBytes());
                outputStream.write(salesrep.getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write(refno.getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write(date.getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write(" \n".getBytes());
                outputStream.write("NOTE: THIS IS A PICKLIST NOT A SALES RECEIPT. \n".getBytes());
                outputStream.write(" \n".getBytes());
                Double TotalAmount = 0.00;
                Double TotalQuantity = 0.00;
                for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {

                    String Itemdesc = salesOrderItems.getitemdesc()+"\n";
                    outputStream.write(Itemdesc.getBytes());
                    Double amount = salesOrderItems.getSoirate() * salesOrderItems.getSoiquantity();
                    String quantity = Integer.toString(salesOrderItems.getSoiquantity())+" "+salesOrderItems.getUom()+"\n";
                    byte[] PRINT_RIGHT = {27, 70, 86};
//                    String lineTotal = String.format("%1$"+30+ "s", String.format( "%,.2f",Float.parseFloat(Double.toString(amount))))+"\n";
                    outputStream.write(quantity.getBytes());
//                    outputStream.write(lineTotal.getBytes());
//                    outputStream.write(PRINT_RIGHT);
//                    outputStream.write(lineTotal.getBytes());
//                    outputStream.write(" \n".getBytes());
                    TotalAmount= TotalAmount + amount;
                    TotalQuantity = TotalQuantity + salesOrderItems.getSoiquantity();
                }
//                String AmountTotal = String.format("\n"+"%1$"+30+ "s", "TOTAL: "+String.format( "%,.2f",Float.parseFloat(Double.toString(TotalAmount))))+"\n";
//                outputStream.write(AmountTotal.getBytes());
//                outputStream.write("________________________________".getBytes());

                bluetoothSocket.close();
            }

        } catch (IOException ex) {
//            handler.obtainMessage(1).sendToTarget();
        }
    }

    private void generatePDF(int so_id) {
        int pageHeight = 55;
        int pagewidth = 168;
        //GET FIELDS

        mDatabaseHelper = new PazDatabaseHelper(TemporaryData.this);
        List<SALESORDERITEMS> salesOrderItemList = mDatabaseHelper.getSlsorderitems(so_id);
        for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {
//            String quantity = Integer.toString(salesOrderItems.getSoiquantity());
//            String rate = Double.toString(salesOrderItems.getSoirate())+"/"+salesOrderItems.getUom();
            pageHeight+=20;
        }
        String customer_name = "";
        String refno = "";
        String date = "";
        String total="";
        String salesrep="";
        List<SALESORDER> salesOrderList = mDatabaseHelper.getSlsorder(so_id);
        for (SALESORDER salesOrder : salesOrderList) {
            customer_name =salesOrder.getCustomer().getCustomername().toString();
            refno=salesOrder.getCode().toString();
            date=salesOrder.getDate().toString();
            total = salesOrder.getAmount().toString();
            salesrep= salesOrder.get_sales_rep_name().toString();
        }

        pageHeight+=20;
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(3);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.white));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.white));
        title.setTextSize(11);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("PAZ DISTRIBUTION", 90, 10, title);

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.white));
        title.setTextSize(10);
        canvas.drawText("Ref No.: "+refno, 90, 21, title);
        canvas.drawText("Date : "+date, 90, 32, title);
        canvas.drawText("Customer: "+customer_name, 90, 43, title);
        int initial_pageHeight = 75;
        for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {
            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.white));
            title.setTextSize(9);
            title.setTextAlign(Paint.Align.RIGHT);
            String quantity = Integer.toString(salesOrderItems.getSoiquantity())+" "+salesOrderItems.getUom()+" @ "
                    +Double.toString(salesOrderItems.getSoirate());
            canvas.drawText(quantity, 105, initial_pageHeight-3, title);

            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.white));
            title.setTextSize(9);
            title.setTextAlign(Paint.Align.RIGHT);
            Double amount = salesOrderItems.getSoirate() * salesOrderItems.getSoiquantity();
//            String amount = Double.toString(salesOrderItems.getSoiamount());
            canvas.drawText(Double.toString(amount), 160, initial_pageHeight-3, title);

            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.white));
            title.setTextSize(9);
            title.setTextAlign(Paint.Align.LEFT);
            String itemdesc = salesOrderItems.getitemdesc();
            canvas.drawText(itemdesc, 1, initial_pageHeight-13, title);
            initial_pageHeight+=25;
        }
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setColor(ContextCompat.getColor(this, R.color.white));
        canvas.drawText("_____________________", 165, initial_pageHeight-33, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setColor(ContextCompat.getColor(this, R.color.white));
        title.setTextSize(8);
        title.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(total, 160, initial_pageHeight-13, title);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.white));
        title.setTextSize(9);
        title.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Salesrep: "+salesrep, 10, initial_pageHeight-10, title);


        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(), refno+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(TemporaryData.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }




    @SuppressLint("Range")
        public ArrayList<SALESORDERITEMS> displayOrderItem() {
        ArrayList<SALESORDERITEMS> histitemlist = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
        int salesOrderId = preferences.getInt("SID", 0);
        String salesOrderIdString = String.valueOf(salesOrderId);
        ref.setText(salesOrderIdString);

        String datS = ref.getText().toString();
        String query = " SELECT " +

                    SALES_ORDER_ITEMS_TABLE + "." + SOI_RATE + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_QUANTITY + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_AMOUNT + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_UOM + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOI_UNIT_BASE_QUANTITY +", " +
                    TABLE_NAME + "." + ITEMID + ", "+
                    TABLE_NAME + "." + ITEM_CODE + ", " +
                    TABLE_NAME + "." + ITEM_DESCRIPTION + "," +
                    SALES_ORDER_TABLE + "." + NOTES + ", " +
                    SALES_ORDER_TABLE + "." + "date" + ", " +
                    SALES_ORDER_ITEMS_TABLE + "." + SOIID  +
                    ",Sales_Order_Items_Table.INVENTORY"+
                    ", Sales_Order_Items_Table.WSR"+
                    ", Sales_Order_Items_Table.SUGGESTED"+

                   " FROM " + SALES_ORDER_ITEMS_TABLE +
                   " INNER JOIN " + SALES_ORDER_TABLE +
                   " ON " + SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID + " = " + SALES_ORDER_TABLE + "." + SALES_ORDERID +
                   " INNER JOIN " + TABLE_NAME +
                   " ON " + SALES_ORDER_ITEMS_TABLE + "." + SOI_ITEM_ID + " = " + TABLE_NAME + "." + ITEMID +
                    " WHERE " + SALES_ORDER_ITEMS_TABLE + "." + SALES_ORDERITEMID + " = " + datS;

            SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    SALESORDERITEMS salesorderitems = new SALESORDERITEMS();
                    salesorderitems.setSoiunitbasequantity(cursor.getInt(cursor.getColumnIndex(SOI_UNIT_BASE_QUANTITY)));
                    salesorderitems.setSoirate(cursor.getDouble(cursor.getColumnIndex(SOI_RATE)));
                    salesorderitems.setSoiquantity(cursor.getInt(cursor.getColumnIndex(SOI_QUANTITY)));
                    String soiAmountString = cursor.getString(cursor.getColumnIndex(SOI_AMOUNT));
                    double soiAmount = Double.parseDouble(soiAmountString.replace(",", ""));
                    salesorderitems.setSoiamount(soiAmount);
                    salesorderitems.setUom(cursor.getString(cursor.getColumnIndex(SOI_UOM)));
                    salesorderitems.setId(cursor.getInt(cursor.getColumnIndex(SOIID)));
                    salesorderitems.setInventory(cursor.getDouble(12));
                    salesorderitems.setWsr(cursor.getDouble(13));
                    salesorderitems.setSuggested(cursor.getDouble(14));

                    SALESORDER salesorder = new SALESORDER();
                    salesorder.setAmount(cursor.getString(cursor.getColumnIndex(AMOUNT)));

                    Item item = new Item();
                    item.setId(cursor.getString(cursor.getColumnIndex(ITEMID)));

                    item.setCode(cursor.getString(cursor.getColumnIndex(ITEM_CODE)));
                    item.setDescription(cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION)));

                    SALESORDER salesorder11 = new SALESORDER();
                     salesorder11.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
                    salesorder11.setDate(cursor.getString(cursor.getColumnIndex("date")));

                     String notes = cursor.getString(cursor.getColumnIndex(NOTES));
                     notesam.setText(notes);
                     String dateStr = cursor.getString(cursor.getColumnIndex("date"));
                     dt.setText(dateStr);

                    salesorderitems.setSalesorder(salesorder);
                    salesorderitems.setItem(item);
                   histitemlist.add(salesorderitems);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

        double totalAmount = 0.0;
        for (SALESORDERITEMS item : histitemlist) {


            totalAmount += item.getSoiamount();
            DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
            String formattedTotal = decimalFormat.format(totalAmount);
            supertot.setText(String.valueOf(formattedTotal));
        }

        Log.d("TemporaryData", "histitemlist size: " + histitemlist.size());
        return histitemlist;
        }

        //update sa mga item
       public void updateton(){
           String tot = supertot.getText().toString();
                String salesid = ref.getText().toString();

                SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put("AMOUNT", tot);

                int rowsAffected = db.update("SALES_ORDER_TABLE", values, "SALES_ORDERID = ?", new String[]{salesid});

                if (rowsAffected > 0) {
                    Toast.makeText(getApplicationContext(), "Order updated successfully", Toast.LENGTH_SHORT).show();

                    Intent agoroy = new Intent(TemporaryData.this, History.class);
                    startActivity(agoroy);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(), "Failed to update amount", Toast.LENGTH_SHORT).show();
                }
               db.close();
       }

    @Override
    public void connectingWithPrinter() {

    }

    @Override
    public void connectionFailed(@NonNull String s) {

    }

    @Override
    public void disconnected() {

    }

    @Override
    public void onError(@NonNull String s) {

    }

    @Override
    public void onMessage(@NonNull String s) {

    }

    @Override
    public void printingOrderSentSuccessfully() {

    }
}
