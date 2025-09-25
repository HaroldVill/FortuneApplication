package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SpecialPriceLevelAdapter extends ArrayAdapter<SpecialPriceLevel> {

    List<SpecialPriceLevel> specialPriceLevelList;
    private Context context;

    public SpecialPriceLevelAdapter(List<SpecialPriceLevel> specialPriceLevelList, Context context) {

        super(context, R.layout.special_price_level_recycle, specialPriceLevelList);
        this.specialPriceLevelList = specialPriceLevelList;
        this.context = context;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewSpecialPriceLevel = inflater.inflate(R.layout.special_price_level_recycle, null, true);

        TextView p1 = listViewSpecialPriceLevel.findViewById(R.id.id_spl);
        TextView p2 = listViewSpecialPriceLevel.findViewById(R.id.recorded_on_spl);
        TextView p3 = listViewSpecialPriceLevel.findViewById(R.id.customer_id_spl);
        TextView p4 = listViewSpecialPriceLevel.findViewById(R.id.item_id_spl);
        TextView p5 = listViewSpecialPriceLevel.findViewById(R.id.sales_rep_id_spl);
        TextView p6 = listViewSpecialPriceLevel.findViewById(R.id.price_level_id_spl);
        TextView p7 = listViewSpecialPriceLevel.findViewById(R.id.approved_spl);
        TextView p8 = listViewSpecialPriceLevel.findViewById(R.id.approved_by_spl);
        TextView p9 = listViewSpecialPriceLevel.findViewById(R.id.approved_on_spl);

        SpecialPriceLevel specialPriceLevel = specialPriceLevelList.get(position);

        p1.setText(specialPriceLevel.get_id());
        p2.setText(specialPriceLevel.get_recorded_on());
        p3.setText(specialPriceLevel.get_customer_id());
        p4.setText(specialPriceLevel.get_item_id());
        p5.setText(specialPriceLevel.get_sales_rep_id());
        p6.setText(specialPriceLevel.get_price_level_id());
        p7.setText(specialPriceLevel.get_approved());
        p8.setText(specialPriceLevel.get_approved_by());
        p9.setText(specialPriceLevel.get_approved_on());

        return listViewSpecialPriceLevel;
    }
}
