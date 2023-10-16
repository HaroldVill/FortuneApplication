package com.example.fortuneapplication;

public class HistoryGetSet {

    String Reference;
    String CustomerName;
    String Totalsales;
    String date;

    public HistoryGetSet(String reference, String customerName, String totalsales, String date) {
        Reference = reference;
        CustomerName = customerName;
        Totalsales = totalsales;
        this.date = date;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getTotalsales() {
        return Totalsales;
    }

    public void setTotalsales(String totalsales) {
        Totalsales = totalsales;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
