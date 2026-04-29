package com.example.fortuneapplication;

public class DefaultRoute {
    private String route_id;
    private String route_name;

    public DefaultRoute(String route_id, String route_name) {
        this.route_id = route_id;
        this.route_name = route_name;
    }

    public DefaultRoute() {

    }

    public String get_route_id() { return route_id; }
    public void set_route_id(String route_id) { this.route_id = route_id; }
    public String get_route_name() { return route_name; }
    public void set_route_name(String route_name) { this.route_name = route_name; }

} //SpecialPriceLevel
