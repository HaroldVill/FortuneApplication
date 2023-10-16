package com.example.fortuneapplication;

public class SOlist {
    private String soname;
    private String soaddress;
    private String socontact;
    private String soitemcode;
    private String soitemdescription;
    private String soitemprice;
    private String soquantity;
    private String total;

    public SOlist(String soname, String soaddress, String socontact, String soitemcode, String soitemdescription, String soitemprice, String soquantity, String total) {
        this.soname = soname;
        this.soaddress = soaddress;
        this.socontact = socontact;
        this.soitemcode = soitemcode;
        this.soitemdescription = soitemdescription;
        this.soitemprice = soitemprice;
        this.soquantity = soquantity;
        this.total = total;
    }

    public String getSoname() {
        return soname;
    }

    public void setSoname(String soname) {
        this.soname = soname;
    }

    public String getSoaddress() {
        return soaddress;
    }

    public void setSoaddress(String soaddress) {
        this.soaddress = soaddress;
    }

    public String getSocontact() {
        return socontact;
    }

    public void setSocontact(String socontact) {
        this.socontact = socontact;
    }

    public String getSoitemcode() {
        return soitemcode;
    }

    public void setSoitemcode(String soitemcode) {
        this.soitemcode = soitemcode;
    }

    public String getSoitemdescription() {
        return soitemdescription;
    }

    public void setSoitemdescription(String soitemdescription) {
        this.soitemdescription = soitemdescription;
    }

    public String getSoitemprice() {
        return soitemprice;
    }

    public void setSoitemprice(String soitemprice) {
        this.soitemprice = soitemprice;
    }

    public String getSoquantity() {
        return soquantity;
    }

    public void setSoquantity(String soquantity) {
        this.soquantity = soquantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
