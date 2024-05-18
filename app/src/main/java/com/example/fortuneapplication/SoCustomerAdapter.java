package com.example.fortuneapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SoCustomerAdapter extends RecyclerView.Adapter<SoCustomerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Customer> customerList;
    private Comparator<Customer> currentComparator;
    Activity activity;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

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
        GetGPSLocation gps = new GetGPSLocation(context,activity,locationManager);
        String longitude1 = gps.get_longitude();
        String latitude1 = gps.get_latitude();
        String longitude2 = db.get_customer_longitude(customer_id);
        String latitude2 = db.get_customer_latitude(customer_id);

        if(Double.parseDouble(longitude2) > 0|| Double.parseDouble(latitude2) > 0) {
            holder.longitude.setText(longitude2);
            holder.latitude.setText(latitude2);
            holder.save_coordinate.setVisibility(View.VISIBLE);
//            holder.begin_order.setVisibility(View.VISIBLE);
            holder.save_coordinate.setText("LOCATE");
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
            if(get_distance.get_distance()<20 || coverage_type.equals("Allow")) {
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
                            editor.putString("ORDER_BEGIN",date.toString());
                            editor.apply();
                            context.startActivity(tra);
                            ((Activity) context).finish();
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
            holder.begin_order.setVisibility(View.INVISIBLE);
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
        TextView so1,so2,so3,so4,socontact,longitude,latitude,distance;
        Button save_coordinate,begin_order,skip_order;

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
            begin_order = itemView.findViewById(R.id.begin_order);
            skip_order = itemView.findViewById(R.id.skip_order);
        }
    }
}
