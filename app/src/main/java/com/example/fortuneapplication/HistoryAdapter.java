package com.example.fortuneapplication;

import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_ITEMS_TABLE;
import static com.example.fortuneapplication.PazDatabaseHelper.SALES_ORDER_TABLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private ArrayList<SALESORDER> salesOrderList;
    private Context context;

    public HistoryAdapter(ArrayList<SALESORDER> salesOrderList, Context context) {
        this.salesOrderList = salesOrderList;
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
                            .setMessage("Do you want to Delete this transaction?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    int salesOrderId = salesOrder.getSalesorderid();
                                    deleteSalesOrderAndItems(salesOrderId);
                                    salesOrderList.remove(position);
                                    notifyItemRemoved(position);
                                    dialogInterface.dismiss();
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

    }



