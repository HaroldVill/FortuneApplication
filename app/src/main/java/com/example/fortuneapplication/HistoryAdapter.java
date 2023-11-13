package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_ITEMS_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_TABLE;
//import static com.example.fortuneapplication.PazDatabaseHelper.

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private ArrayList<SALESORDER> salesOrderList;
    private ArrayList<SALESORDERITEMS> salesOrderItemList;
    private Context context;
    RequestQueue request_queue;

    private PazDatabaseHelper mDatabaseHelper;
    private String api_url;
    private String x;

    public HistoryAdapter(ArrayList<SALESORDER> salesOrderList, Context context,ArrayList<SALESORDERITEMS> salesOrderItemList) {
        this.salesOrderList = salesOrderList;
        this.salesOrderItemList = salesOrderItemList;
        this.context = context;
    }

//    public HistoryAdapter(ArrayList<SALESORDER> salesOrderList) {
//        this.salesOrderList = salesOrderList;
//    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historylayout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        SALESORDER salesOrder = salesOrderList.get(position);
        PazDatabaseHelper db = new PazDatabaseHelper(context);
        int posted = db.get_so_posted_flag(salesOrder.getSalesorderid());
        if(posted !=0){

            holder.sam1.setTextColor(Color.rgb(0,89,27));
            holder.sam2.setTextColor(Color.rgb(0,89,27));
            holder.sam4.setTextColor(Color.rgb(0,89,27));
//            holder.removes.setEnabled(false);
            holder.sam1.setTypeface(Typeface.DEFAULT_BOLD);
            holder.sam2.setTypeface(Typeface.DEFAULT_BOLD);
            holder.sam3.setTypeface(Typeface.DEFAULT_BOLD);
            holder.sam4.setTypeface(Typeface.DEFAULT_BOLD);

        }
        holder.sam1.setText(salesOrder.getCode());
        holder.sam2.setText(salesOrder.getCustomer().getCustomername());
        holder.sam3.setText(salesOrder.getAmount());
        holder.sam4.setText(salesOrder.getDate());
        holder.sampot.setText(String.valueOf(salesOrder.getSalesorderid()));
        holder.removes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // Set the title and message for the dialog
                    builder.setTitle("WARNING")
                            .setMessage("Do you want to post this transaction?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    int salesOrderId = salesOrder.getSalesorderid();
//                                    sync(salesOrderId);
                                    PazDatabaseHelper db = new PazDatabaseHelper(context);
                                    db.update_so_posted_flag(salesOrderId);
                                    Toast.makeText(context, salesOrder.getCode().toString() +" Succesfully Posted.", Toast.LENGTH_LONG).show();
                                    int posted = db.get_so_posted_flag(salesOrder.getSalesorderid());
                                    if(posted !=0){

                                        holder.sam1.setTextColor(Color.rgb(0,89,27));
                                        holder.sam2.setTextColor(Color.rgb(0,89,27));
                                        holder.sam4.setTextColor(Color.rgb(0,89,27));
//                                      holder.removes.setEnabled(false);
                                        holder.sam1.setTypeface(Typeface.DEFAULT_BOLD);
                                        holder.sam2.setTypeface(Typeface.DEFAULT_BOLD);
                                        holder.sam3.setTypeface(Typeface.DEFAULT_BOLD);
                                        holder.sam4.setTypeface(Typeface.DEFAULT_BOLD);

                                    }
                                    notifyItemChanged(position,null);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent transfer = new Intent(context, TemporaryData.class);
                SharedPreferences preferences = context.getSharedPreferences("HISTORY", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CN", salesOrder.getCustomer().getCustomername());
               // editor.putString("TOT", salesOrder.getAmount());
                editor.putInt("SID", salesOrder.getSalesorderid());

                editor.apply();
               context.startActivity(transfer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return salesOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sam1,sam2,sam3,sam4, sampot;
        ImageView removes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sam1= itemView.findViewById(R.id.sam1);
            sam2 = itemView.findViewById(R.id.sam2);
            sam3 = itemView.findViewById(R.id.sam3);
            sam4 = itemView.findViewById(R.id.sam4);
            sampot = itemView.findViewById(R.id.sampot);
            removes= itemView.findViewById(R.id.removes);

        }
    }
    private void deleteSalesOrderAndItems(int salesOrderId) {

        PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(SALES_ORDER_TABLE, "Sales_OrderID"+ " = ?", new String[]{String.valueOf(salesOrderId)});
            db.delete(SALES_ORDER_ITEMS_TABLE, "sales_order_id" + " = ?", new String[]{String.valueOf(salesOrderId)});

            db.setTransactionSuccessful();
            Toast.makeText(context, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void sync(int salesOrderId){
        try {
//
            mDatabaseHelper = new PazDatabaseHelper(context);
            ArrayList<CONNECT> connectList = mDatabaseHelper.SelectUPDT();
            if (!connectList.isEmpty()) {
                x = connectList.get(0).getIp(); // Assuming the first IP address is what you need
                String sales_type = mDatabaseHelper.sales_type();
                Log.d("sales_type",sales_type);
                api_url = "http://" + x + "/MobileAPI/"+sales_type;
            }
            PazDatabaseHelper dbHelper = new PazDatabaseHelper(context);
            List<SALESORDER> salesOrderList = dbHelper.getSlsorder(salesOrderId);
            for (SALESORDER salesOrder : salesOrderList) {
//                Toast.makeText(context, salesOrder.getCode().toString(), Toast.LENGTH_LONG).show();
//                System.exit(0);
//                //For Items
                JSONArray json_soitems = new JSONArray();
                List<SALESORDERITEMS> salesOrderItemList = dbHelper.getSlsorderitems(salesOrderId);
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
//                        RequestFuture <JSONObject> future = RequestFuture.newFuture();
                StringRequest send_invoices = new StringRequest(Request.Method.POST, api_url,
                        response -> {Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        if(response.contains("succesfully") || response.contains("has already been")){
                        dbHelper.update_so_status(salesOrderId);}},
                        error -> Toast.makeText(context, "Connection Error", Toast.LENGTH_LONG).show()){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params =new HashMap<>();
                        params.put("refno", salesOrder.getCode().toString());
                        params.put("customer_id", salesOrder.getCustomer().getId().toString());
                        params.put("total", salesOrder.getAmount().toString());
                        params.put("date", salesOrder.getDate());
                        params.put("sales_rep_id", Integer.toString(salesOrder.getSalesrepid()));
                        params.put("sales_order_items", json_soitems.toString());
                        return params;
                    }
                };
                request_queue = Volley.newRequestQueue(context);
                request_queue.add(send_invoices);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    }



