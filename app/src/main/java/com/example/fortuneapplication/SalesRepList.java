package com.example.fortuneapplication;

public class SalesRepList {

    private String srid;
    private String srname;
    private String sraddress;
    private String srmoble;

    public SalesRepList(String srid, String srname, String sraddress, String srmoble) {
        this.srid = srid;
        this.srname = srname;
        this.sraddress = sraddress;
        this.srmoble = srmoble;
    }

    public SalesRepList(){

    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    public String getSrname() {
        return srname;
    }

    public void setSrname(String srname) {
        this.srname = srname;
    }

    public String getSraddress() {
        return sraddress;
    }

    public void setSraddress(String sraddress) {
        this.sraddress = sraddress;
    }

    public String getSrmoble() {
        return srmoble;
    }

    public void setSrmoble(String srmoble) {
        this.srmoble = srmoble;
    }
}

