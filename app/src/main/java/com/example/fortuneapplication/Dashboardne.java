package com.example.fortuneapplication;

public class Dashboardne {
    private String dcode;
    private String dcustomername;
    private double dtotal;
    private String ddate;

    public Dashboardne(String dcode, String dcustomername, double dtotal, String ddate) {
        this.dcode = dcode;
        this.dcustomername = dcustomername;
        this.dtotal = dtotal;
        this.ddate = ddate;
    }

    public Dashboardne (){

    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getDcustomername() {
        return dcustomername;
    }

    public void setDcustomername(String dcustomername) {
        this.dcustomername = dcustomername;
    }

    public double getDtotal() {
        return dtotal;
    }

    public void setDtotal(double dtotal) {
        this.dtotal = dtotal;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
    }
}
