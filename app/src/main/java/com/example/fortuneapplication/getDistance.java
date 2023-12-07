package com.example.fortuneapplication;

public class getDistance
{
    private final double lat1;
    private final double lat2;
    private final double lon1;
    private final double lon2;
    private final double el1;
    private final double el2;
    public getDistance( double lon1,double lon2,double lat1, double lat2, double el1, double el2){
        this.lat1 = lat1;
        this.lat2 = lat2;
        this.lon1 = lon1;
        this.lon2 = lon2;
        this.el2  = el2;
        this.el1  = el1;
    }
    public double get_distance() {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        double height = el1 - el2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        return Math.sqrt(distance);
    }
}
