package com.example.fortuneapplication;

import static androidx.core.content.ContextCompat.startActivity;

import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS;
import static com.example.fortuneapplication.PazDatabaseHelper.SYSTEM_SETTINGS_NAME;



import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
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
        PazDatabaseHelper db = new PazDatabaseHelper(context);
        holder.so1.setText(customer.getId());
        holder.so2.setText(customer.getCustomername());
        holder.so3.setText(customer.getPostaladdress());
        holder.so4.setText(customer.getMobilenumber());
        holder.socontact.setText(customer.getContactperson());
        String price_level = db.get_price_level_name(Integer.parseInt(customer.getPricelevelid()));
//        if(customer.getPricelevelid().equals("0")) {
//            price_level = "WHOLESALE";
//        }
        holder.price_level.setText(price_level);

        int customer_id = Integer.parseInt(customer.getId());
        String customer_name = customer.getCustomername();
//        Log.d("customer_id", customer_id);
        Integer verification = db.get_customer_verification(customer_id);
        String verification_description = "Pending";
        if(verification !=0){
            verification_description = "Verified";
        }
        holder.verification.setText(verification_description);
        LocalDateTime current_date = LocalDateTime.now();
        DateTimeFormatter datenow = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        holder.is_visited.setText("UNVISITED");
        if(db.check_customer_skip(Integer.parseInt(customer.getId()))>0){
            holder.is_visited.setText("SKIPPED");
        }
        else if(db.check_customer_order(Integer.parseInt(customer.getId()),current_date.format(datenow))>0){
            holder.is_visited.setText("ORDERED");
        }

        GetGPSLocation gps = new GetGPSLocation(context,activity,locationManager);
        String longitude1 = gps.get_longitude();
        String latitude1 = gps.get_latitude();
        String longitude2 = db.get_customer_longitude(customer_id);
        String latitude2 = db.get_customer_latitude(customer_id);
        holder.open_customer_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "customer: "+customer.getCustomername()+" "+customer.getId());
                Intent open_valuation_breakdown  = new Intent(context,CustomerSalesHistory.class);
                open_valuation_breakdown.putExtra("customerid",Integer.toString(customer_id));
                open_valuation_breakdown.putExtra("customername",customer_name);
                context.startActivity(open_valuation_breakdown);
            }
        });

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
            holder.skip_order.setVisibility(View.VISIBLE);
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
                        if(db.get_customer_verification(customer_id) == 1){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("Nahuman naman ug verify ni nga customer.")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
                        getDistance get_distance = new getDistance(Double.parseDouble(longitude1),Double.parseDouble(longitude2),Double.parseDouble(latitude1),Double.parseDouble(latitude2),0,0);
                        if(get_distance.get_distance()<100) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Gusto nmo i verify ang customer? naa lageh ka sa outlet? Dili na raba ka maka re-pin sa customer location kung ma verify na. Palihog double check sa una kung sakto ba kay dili naka maka order sunod basta dili mao ag verified location sa customer.").setCancelable(false).setPositiveButton("VERIFY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.VerifyPin(customer_id);
                                    Toast.makeText(context, "Customer pin successfully verified.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else{
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("You must be within 100 meters radius of customer coordinates. Dapat NAA ka sa outlet para maka verify sa iyang location. Kung tinuod nga naa man gani ka sa outlet i RE-PIN palihog ang iyang location para ma VERIFY nimo!")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                        }
                    }
                });
                holder.remove_pin.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(db.get_customer_verification(customer_id) == 1){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("Dli na ma re-pin kay na verify na ag location ani nga customer.")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Sure na lageh ka nga i re-pin nmo ag location sa customer? Siguradoa nga naa kas tapad sa tindahan ha!").setCancelable(false).setPositiveButton("RE-PIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.update_customer_coordinates(customer_id, longitude1, latitude1);
                                    holder.longitude.setText(longitude1);
                                    holder.latitude.setText(latitude1);
                                    holder.save_coordinate.setVisibility(View.INVISIBLE);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Customer coordinates successfully re-pinned.", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                });
            }
            holder.save_coordinate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude2 + "," + longitude2 + "?q=" + Uri.encode(latitude2+ "," + longitude2 ));
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
            if(get_distance.get_distance()<100 || coverage_type.equals("Allow") || customer_id ==829) {
                Log.d("Distance", Double.toString(get_distance.get_distance()));
                holder.skip_order.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(db.get_customer_verification(customer_id) != 1){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("Wala pa na VERIFY ag location sa customer, TUPLOKA ang VERIFY button para maka order. ")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
                        if(Double.parseDouble(longitude2) == 0|| Double.parseDouble(latitude2) == 0){
                            Toast.makeText(context, "Please PIN customer first.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mdatabasehelper = new PazDatabaseHelper(context);
                            mdatabasehelper.deleOrderSample();
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
                        getDistance get_distance = new getDistance(Double.parseDouble(longitude1),Double.parseDouble(longitude2),Double.parseDouble(latitude1),Double.parseDouble(latitude2),0,0);
                        if(db.get_customer_verification(customer_id) != 1){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("Wala pa na VERIFY ag location sa customer, TUPLOKA ang VERIFY button para maka order. ")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
                        if(get_distance.get_distance()>100) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("You must be within 100 meters radius of customer coordinates. Kung tnuod man gani nga naa najud ka sa customer i RE-PIN iya coordinates")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
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
                        if(db.get_customer_verification(customer_id) != 1){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("Wala pa na VERIFY ag location sa customer, TUPLOKA ang VERIFY button para maka order. ")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                        // Set the title and message for the dialog
                        builder.setTitle("WARNING")
                                .setMessage("You must be within 100 meters radius of customer coordinates.")
                                .setCancelable(true)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    }
                });

                holder.skip_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDistance get_distance = new getDistance(Double.parseDouble(longitude1),Double.parseDouble(longitude2),Double.parseDouble(latitude1),Double.parseDouble(latitude2),0,0);
                        if(db.get_customer_verification(customer_id) != 1){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("Wala pa na VERIFY ag location sa customer, TUPLOKA ang VERIFY button para maka order. ")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
                        if(get_distance.get_distance()>100) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                            // Set the title and message for the dialog
                            builder.setTitle("WARNING")
                                    .setMessage("You must be within 100 meters radius of customer coordinates.")
                                    .setCancelable(true)
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
                            return;
                        }
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
        }
        if(Double.parseDouble(longitude2) == 0|| Double.parseDouble(latitude2) == 0){
            holder.longitude.setText("0");
            holder.latitude.setText("0");
            holder.distance.setText("0");
            holder.save_coordinate.setVisibility(View.VISIBLE);
            holder.verify_pin.setVisibility(View.INVISIBLE);
            holder.skip_order.setVisibility(View.INVISIBLE);
            holder.remove_pin.setVisibility(View.INVISIBLE);
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
        TextView so1,so2,so3,so4,socontact,longitude,latitude,distance,verification,is_visited,price_level;
        Button save_coordinate,verify_pin,skip_order,remove_pin,request_repin;
        FloatingActionButton open_customer_history;

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
            is_visited = itemView.findViewById(R.id.is_visited);
            price_level = itemView.findViewById(R.id.price_level);
            open_customer_history = itemView.findViewById(R.id.open_customer_history);
        }
    }


}
