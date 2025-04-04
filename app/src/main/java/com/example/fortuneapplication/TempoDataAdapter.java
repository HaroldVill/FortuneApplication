package com.example.fortuneapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TempoDataAdapter extends RecyclerView.Adapter<TempoDataAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SALESORDERITEMS> salesorderitems;

    public TempoDataAdapter(ArrayList<SALESORDERITEMS> salesorderitems, Context context) {
        this.salesorderitems = salesorderitems;
        this.context = context;
    }

    @NonNull
    @Override
    public TempoDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temporarydatadesign, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TempoDataAdapter.ViewHolder holder, int position) {
        SALESORDERITEMS salesorder = salesorderitems.get(position);
       holder.fb1.setText(salesorder.getItem().getCode());
       holder.fb2.setText(salesorder.getItem().getDescription());

       holder.fb3.setText(String.valueOf(salesorder.getSoirate() + " / " +salesorder.getUom()));
       holder.fb4.setText(String.valueOf(salesorder.getSoiquantity()));
        double soiamount = salesorder.getSoiamount();
        holder.fb5.setText(String.valueOf(soiamount));
        holder.fb21.setText(Double.toString(salesorder.getInventory()) +" - "+salesorder.getInvUom());
        holder.fb22.setText(Double.toString(salesorder.getWsr()) +" - "+salesorder.getInvUom());
        holder.fb23.setText(Double.toString(salesorder.getSuggested()) +" - "+salesorder.getInvUom());
       holder.agay.setText(String.valueOf(salesorder.getId()));

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(v.getContext(),UpdateDelete.class);


               intent.putExtra("iditem",salesorder.getItem().getId());
               intent.putExtra("code", salesorder.getItem().getCode());
               intent.putExtra("description", salesorder.getItem().getDescription());
               intent.putExtra("rate", salesorder.getSoirate());
               intent.putExtra("quantity", salesorder.getSoiquantity());

               intent.putExtra("amount", salesorder.getSoiamount());
               intent.putExtra("uom", salesorder.getUom());
               intent.putExtra("IDMEN",salesorder.getId());
               PazDatabaseHelper mDatabaseHelper = new PazDatabaseHelper(context);
//               Log.d("", "so_id: "+salesorder.getId());
               if(mDatabaseHelper.get_so_items_posted_flag(salesorder.getId()) >0){
                   Toast.makeText(context, "Cannot edit a posted order.", Toast.LENGTH_SHORT).show();
               }
               else {
                   v.getContext().startActivity(intent);
                   ((TemporaryData) context).finish();
               }
           }
       });
   }

    @Override
    public int getItemCount() {
        return  salesorderitems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fb1,fb2,fb3,fb4,fb5,samok,agay,fb21,fb22,fb23;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fb1 = itemView.findViewById(R.id.fb1);
            fb2 = itemView.findViewById(R.id.fb2);
            fb3 = itemView.findViewById(R.id.fb3);
            fb4 = itemView.findViewById(R.id.fb4);
            fb5 = itemView.findViewById(R.id.fb5);
            fb21= itemView.findViewById(R.id.fb21);
            fb22= itemView.findViewById(R.id.fb22);
            fb23= itemView.findViewById(R.id.fb23);
            // samok = itemView.findViewById(R.id.samok);
            agay= itemView.findViewById(R.id.agay);



        }
    }
}
