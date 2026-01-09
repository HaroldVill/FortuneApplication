package com.example.fortuneapplication;

public class Locationing {

    private String locid;
    private String locname;

    public Locationing(String locid, String locname) {
        this.locid = locid;
        this.locname = locname;
    }
    public Locationing(){

    }

    public String getLocid() {
        return locid;
    }

    public void setLocid(String locid) {
        this.locid = locid;
    }

    public String getLocname() {
        return locname;
    }

    public void setLocname(String locname) {
        this.locname = locname;
    }
}
