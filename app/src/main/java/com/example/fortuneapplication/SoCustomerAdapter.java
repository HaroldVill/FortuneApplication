package com.example.fortuneapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoCustomerAdapter extends RecyclerView.Adapter<SoCustomerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Customer> customerList;
    private Comparator<Customer> currentComparator;
    Activity activity;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    PazDatabaseHelper mdatabasehelper;
    RequestQueue request_queue;
    private volatile boolean stopThread = false;

    public SoCustomerAdapter(Context context, ArrayList<Customer> customerList,Activity activity, LocationManager locationManager) {
        this.context = context;
        this.customerList = customerList;
        this.activity = activity;
        this.locationManager = locationManager;
    }

    public void setFilterdList(List<Customer> filterdList){
        this.customerList = (ArrayList<Customer>) filterdList;
        notifyDataSetChanged();
    }


    public void sortData(Comparator<Customer> comparator) {
        if (currentComparator == comparator) {
            Collections.reverse(customerList);
        } else {
            Collections.sort(customerList, comparator);
            currentComparator = comparator;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SoCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.socustomerdesign, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoCustomerAdapter.MyViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.so1.setText(customer.getId());
        holder.so2.setText(customer.getCustomername());
        holder.so3.setText(customer.getPostaladdress());
        holder.so4.setText(customer.getMobilenumber());
        holder.socontact.setText(customer.getContactperson());
        PazDatabaseHelper db = new PazDatabaseHelper(context);
        int customer_id = Integer.parseInt(customer.getId());
//        Log.d("customer_id", customer_id);
        Integer verification = db.get_customer_verification(customer_id);
        String verification_description = "Pending";
        if(verification !=0){
            verification_description = "Verified";
        }
        holder.verification.setText(verification_description);

        GetGPSLocation gps = new GetGPSLocation(context,activity,locationManager);
        String longitude1 = gps.get_longitude();
        String latitude1 = gps.get_latitude();
        String longitude2 = db.get_customer_longitude(customer_id);
        String latitude2 = db.get_customer_latitude(customer_id);

        holder.request_repin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Double.parseDouble(longitude2) > 0|| Double.parseDouble(latitude2) > 0) {

                    final AlertDialog.Builder builder= new AlertDialog.Builder(context);
                    builder.setMessage("Request repin for this customer?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
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
                            mdatabasehelper = new PazDatabaseHelper(context);
                            if(mdatabasehelper.count_request_repin(customer_id) >0){
                                Toast.makeText(context, "Request for re-pin for this customer has already been sent.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(mdatabasehelper.request_repin(customer_id)){
                                Toast.makeText(context, "Request for re-pin successfully sent.", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(context, "An internal error has occurred please contact IT admin.", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                }
                else{
                    Toast.makeText(context, "Cannot request repin customer with no pin.", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(Double.parseDouble(longitude2) > 0|| Double.parseDouble(latitude2) > 0) {
            holder.longitude.setText(longitude2);
            holder.latitude.setText(latitude2);
            holder.save_coordinate.setVisibility(View.VISIBLE);
//            holder.begin_order.setVisibility(View.VISIBLE);
            holder.save_coordinate.setText("LOCATE");
            String verify_type = db.get_verify_type();
            Log.d("verify_type", verify_type);
            if(verify_type.equals("1")){
                holder.verify_pin.setVisibility(View.VISIBLE);
                holder.remove_pin.setVisibility(View.VISIBLE);
                holder.verify_pin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.VerifyPin(customer_id);
                        notifyDataSetChanged();
                    }
                });
                holder.remove_pin.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                        db.update_customer_coordinates(customer_id, longitude1, latitude1);
                        holder.longitude.setText(longitude1);
                        holder.latitude.setText(latitude1);
                        holder.save_coordinate.setVisibility(View.INVISIBLE);
                        notifyDataSetChanged();
                    }
                });
            }
            holder.save_coordinate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude2+","+longitude2);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(context,mapIntent,null);
                }
            });
            getDistance get_distance = new getDistance(Double.parseDouble(longitude1),Double.parseDouble(longitude2),Double.parseDouble(latitude1),Double.parseDouble(latitude2),0,0);
            holder.distance.setText(Double.toString(get_distance.get_distance()));
            String coverage_type ;
            coverage_type = db.get_coverage_type();
            Log.d("coverage_type", coverage_type);
            if(get_distance.get_distance()<20 || coverage_type.equals("Allow") || customer_id ==1) {
                Log.d("Distance", Double.toString(get_distance.get_distance()));
                holder.skip_order.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Double.parseDouble(longitude2) == 0|| Double.parseDouble(latitude2) == 0){
                            Toast.makeText(context, "Please PIN customer first.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent tra = new Intent(context, SOActivity.class);
                            SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("CID", customer.getId());
                            editor.putString("CNAME", customer.getCustomername());
                            editor.putString("CCONTACT", customer.getMobilenumber());
                            editor.putString("CADD", customer.getPostaladdress());
                            editor.putString("prlvl", customer.getPricelevelid());
                            editor.putString("DI", customer.getPaymenTerm().getDescription());
                            LocalDateTime date = LocalDateTime.now();
                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                            String formattedDate = date.format(myFormatObj);
                            editor.putString("ORDER_BEGIN",formattedDate.toString());
                            editor.apply();
                            context.startActivity(tra);
                            ((Activity) context).finish();
                        }
                    }
                });

                holder.skip_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Double.parseDouble(longitude2) == 0|| Double.parseDouble(latitude2) == 0){
                            Toast.makeText(context, "Please PIN customer first.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final AlertDialog.Builder builder= new AlertDialog.Builder(context);
                            final EditText input = new EditText(context);
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            input.setHint("Please input reason for skipping this customer");
                            builder.setView(input);
                            builder.setMessage("Are you sure you want to skip this customer?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
                                    mdatabasehelper = new PazDatabaseHelper(context);
                                    if(TextUtils.isEmpty(input.getText())){
                                        Toast.makeText(context, "Reason for skipping is required.", Toast.LENGTH_LONG).show();
                                    }
                                    else if(mdatabasehelper.check_customer_skip(Integer.parseInt(customer.getId())) > 0){
                                        Toast.makeText(context, "Customer already skipped", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        SALESORDER dataModel = new SALESORDER();
                                        dataModel.setCustomerid(Integer.parseInt(customer.getId()));
                                        LocalDateTime date = LocalDateTime.now();
                                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        String formattedDate = date.format(myFormatObj);
                                        String end_order_time = formattedDate.toString();
                                        dataModel.set_end_order(end_order_time.toString());
                                        dataModel.set_reason(input.getText().toString());
                                        mdatabasehelper.SkipCustomerOrder(dataModel);
                                        alertDialog.dismiss();
                                    }
                                }
                            });
                        }

                    }
                });
            }
            else{
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Must be within 20 meters of the outlet area.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if(Double.parseDouble(longitude2) == 0|| Double.parseDouble(latitude2) == 0){
            holder.longitude.setText("0");
            holder.latitude.setText("0");
            holder.distance.setText("0");
            holder.save_coordinate.setVisibility(View.VISIBLE);
            holder.verify_pin.setVisibility(View.INVISIBLE);
            holder.skip_order.setVisibility(View.INVISIBLE);
            holder.save_coordinate.setText("PIN");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Double.parseDouble(longitude2) == 0|| Double.parseDouble(latitude2) == 0){
                        Toast.makeText(context, "Please PIN customer first.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.save_coordinate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    db.update_customer_coordinates(customer_id,longitude1,latitude1);
                    holder.longitude.setText(longitude1);
                    holder.latitude.setText(latitude1);
                    holder.save_coordinate.setVisibility(View.INVISIBLE);
                    notifyDataSetChanged();
                }
            });
        }



    }


    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView so1,so2,so3,so4,socontact,longitude,latitude,distance,verification;
        Button save_coordinate,verify_pin,skip_order,remove_pin,request_repin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            so1 = itemView.findViewById(R.id.so1);
            so2 = itemView.findViewById(R.id.so2);
            so3 = itemView.findViewById(R.id.so3);
            so4 = itemView.findViewById(R.id.so4);
            socontact = itemView.findViewById(R.id.socontact);
            longitude = itemView.findViewById(R.id.longitude);
            latitude = itemView.findViewById(R.id.latitude);
            distance = itemView.findViewById(R.id.distance);
            save_coordinate = itemView.findViewById(R.id.save_coordinate);
            verify_pin = itemView.findViewById(R.id.verify_pin);
            skip_order = itemView.findViewById(R.id.skip_order);
            remove_pin = itemView.findViewById(R.id.remove_pin);
            verification = itemView.findViewById(R.id.verfication);
            request_repin  =itemView.findViewById(R.id.request_repin);
        }
    }

    public void startThread() {
        stopThread = true;
        stopThread = false;
        SoCustomerAdapter.ExampleRunnable runnable = new SoCustomerAdapter.ExampleRunnable(30000);
        new Thread(runnable).start();
        Log.d("Test", "1");
        /*
        ExampleThread thread = new ExampleThread(10);
        thread.start();
        */
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                //work
            }
        }).start();
        */
    }



    class ExampleRunnable implements Runnable {
        int seconds;
        private Context context1;
        private String api_url;
        private String x;
        private PazDatabaseHelper mDatabaseHelper;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            Log.d("StartThread", Integer.toString(seconds));
            mDatabaseHelper = new PazDatabaseHelper(context);
            for (int i = 1; i < seconds; i++) {
                String JSON_URL="";
                ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
                if (!connectList.isEmpty()) {
                    x = connectList.get(0).getIp();
                    JSON_URL = "http://" + x + "/MobileAPI/items.php";
                }
                if (i % 15 == 0) {
                    int sales_order_id = mDatabaseHelper.get_open_sales_order();
                    if(sales_order_id !=0){
                        try {
                            ArrayList<CONNECT> connectList2 = mDatabaseHelper.SelectUPDT();
                            if (!connectList2.isEmpty()) {
                                x = connectList2.get(0).getIp(); // Assuming the first IP address is what you need
                                String sales_type = mDatabaseHelper.sales_type();
                                Log.d("sales_type",sales_type);
                                api_url = "http://" + x + "/MobileAPI/"+sales_type;
                            }
//                            PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
                            List<SALESORDER> salesOrderList = mDatabaseHelper.getSlsorder(sales_order_id);
                            for (SALESORDER salesOrder : salesOrderList) {
                                JSONArray json_soitems = new JSONArray();
                                List<SALESORDERITEMS> salesOrderItemList = mDatabaseHelper.getSlsorderitems(sales_order_id);
                                for (SALESORDERITEMS salesOrderItems : salesOrderItemList) {

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("item_id", salesOrderItems.getSoiitemid());
                                    jsonObject.put("quantity", salesOrderItems.getSoiquantity());
                                    jsonObject.put("rate", salesOrderItems.getSoirate());
                                    jsonObject.put("amount", salesOrderItems.getSoiamount());
                                    jsonObject.put("unit_base_qty", salesOrderItems.getSoiunitbasequantity());
                                    jsonObject.put("uom", salesOrderItems.getUom());
                                    jsonObject.put("price_level_id", salesOrderItems.getSoipricelevelid());
                                    json_soitems.put(jsonObject);
                                }
                                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                                        response -> {Log.d("Success","Success");
                                            if(response.contains("succesfully") || response.contains("has already been")){
                                                mDatabaseHelper.update_so_status(sales_order_id);}},
                                        error -> Log.d("Error","Connection Error")){

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params =new HashMap<>();
                                        params.put("refno", salesOrder.getCode().toString());
                                        params.put("customer_id", salesOrder.getCustomer().getId().toString());
                                        params.put("total", salesOrder.getAmount().toString());
                                        params.put("date", salesOrder.getDate());
                                        params.put("sales_rep_id", Integer.toString(salesOrder.getSalesrepid()));
                                        params.put("location_id",Integer.toString(salesOrder.getLocationid()));
                                        params.put("begin_order", salesOrder.get_begin_order());
                                        params.put("end_order", salesOrder.get_end_order());
                                        params.put("sales_order_items", json_soitems.toString());
                                        return params;
                                    }
                                };
                                request_queue = Volley.newRequestQueue(context);
                                request_queue.add(send_invoices);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }

                }
                Log.d("Threadticker", "ThreadTicker: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
