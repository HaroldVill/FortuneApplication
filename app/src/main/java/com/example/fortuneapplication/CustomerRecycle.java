package com.example.fortuneapplication;

public class CustomerRecycle {

    String name;
    String Addres;
    String StoreName;
    String contactnumber;

    public CustomerRecycle(String name, String addres, String storeName, String contactnumber) {
        this.name = name;
        Addres = addres;
        StoreName = storeName;
        this.contactnumber = contactnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddres() {
        return Addres;
    }

    public void setAddres(String addres) {
        Addres = addres;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
