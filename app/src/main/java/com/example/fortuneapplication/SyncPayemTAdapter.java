package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SyncPayemTAdapter extends ArrayAdapter<PaymenTerm>{

    private List<PaymenTerm>paymenTerms;
    private Context context;

    public SyncPayemTAdapter(List<PaymenTerm> paymenTerms, Context context) {
        super(context, R.layout.syncpaymentdesign, paymenTerms);
        this.paymenTerms = paymenTerms;
        this.context = context;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listpayterm = inflater.inflate(R.layout.syncpaymentdesign, null, true);

        TextView w1 = listpayterm.findViewById(R.id.i1);
        TextView w2 = listpayterm.findViewById(R.id.i2);
        TextView w3 = listpayterm.findViewById(R.id.i3);
        TextView w4 = listpayterm.findViewById(R.id.i4);

        PaymenTerm paymenTerm = paymenTerms.get(position);

        w1.setText(paymenTerm.getId());
        w2.setText(paymenTerm.getCode());
        w3.setText(paymenTerm.getDescription());
        w4.setText(paymenTerm.getNetdue());


        return listpayterm;

    }
}
