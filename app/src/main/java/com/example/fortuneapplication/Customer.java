package com.example.fortuneapplication;

public class Customer {
    private String id;
    private String customername;
    private String postaladdress;
    private String contactperson;
    private String telephonenumber;
    private String mobilenumber;
    private String paymenttermsid;
    private String salesrepid;
    private String pricelevelid;
    private String longitude;
    private String latitude;
    private PaymenTerm paymenTerm;

    public Customer(String id, String customername, String postaladdress, String contactperson, String telephonenumber, String mobilenumber, String paymenttermsid, String salesrepid, String pricelevelid, String longitude, String latitude) {
        this.id = id;
        this.customername = customername;
        this.postaladdress = postaladdress;
        this.contactperson = contactperson;
        this.telephonenumber = telephonenumber;
        this.mobilenumber = mobilenumber;
        this.paymenttermsid = paymenttermsid;
        this.salesrepid = salesrepid;
        this.pricelevelid = pricelevelid;
        this.paymenTerm = paymenTerm;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Customer(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getContactperson() {
        return contactperson;
    }

    public void setContactperson(String contactperson) {
        this.contactperson = contactperson;
    }

    public String getTelephonenumber() {
        return telephonenumber;
    }

    public void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getPaymenttermsid() {
        return paymenttermsid;
    }

    public void setPaymenttermsid(String paymenttermsid) {
        this.paymenttermsid = paymenttermsid;
    }

    public String getSalesrepid() {
        return salesrepid;
    }

    public void setSalesrepid(String salesrepid) {
        this.salesrepid = salesrepid;
    }

    public String getPricelevelid() {
        return pricelevelid;
    }

    public void setPricelevelid(String pricelevelid) {
        this.pricelevelid = pricelevelid;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public PaymenTerm getPaymenTerm() {
        return paymenTerm;
    }

    public void setPaymenTerm(PaymenTerm paymenTerm) {
        this.paymenTerm = paymenTerm;
    }
}




