package com.example.fortuneapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class DefaultRouteAdapter extends ArrayAdapter<DefaultRoute> {

    List<DefaultRoute> defaultRouteList;
    private Context context;

    public DefaultRouteAdapter(List<DefaultRoute> defaultRouteList, Context context) {

        super(context, R.layout.default_route_recycle, defaultRouteList);
        this.defaultRouteList = defaultRouteList;
        this.context = context;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewDefaultRoute = inflater.inflate(R.layout.default_route_recycle, null, true);

        TextView p1 = listViewDefaultRoute.findViewById(R.id.route_id);
        TextView p2 = listViewDefaultRoute.findViewById(R.id.route_name); //to be continued...


        DefaultRoute defaultRoute = defaultRouteList.get(position);

        p1.setText(defaultRoute.get_route_id());
        p2.setText(defaultRoute.get_route_name());


        return listViewDefaultRoute;
    }
}
