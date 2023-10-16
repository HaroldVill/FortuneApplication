package com.example.fortuneapplication;

public class CustomerRecycle2 {

    String Cname;
    String Caddress;
    String Cstorename;
    String Ccontact;

    public CustomerRecycle2(String cname, String caddress, String cstorename, String ccontact) {
        Cname = cname;
        Caddress = caddress;
        Cstorename = cstorename;
        Ccontact = ccontact;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCaddress() {
        return Caddress;
    }

    public void setCaddress(String caddress) {
        Caddress = caddress;
    }

    public String getCstorename() {
        return Cstorename;
    }

    public void setCstorename(String cstorename) {
        Cstorename = cstorename;
    }

    public String getCcontact() {
        return Ccontact;
    }

    public void setCcontact(String ccontact) {
        Ccontact = ccontact;
    }
}
