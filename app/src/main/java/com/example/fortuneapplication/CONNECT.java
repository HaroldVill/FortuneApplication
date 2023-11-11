package com.example.fortuneapplication;

public class CONNECT {
    private int id;
    private String name;
    private String ip;
    private String sales_type;

    public CONNECT(int id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
    }

    public CONNECT(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String get_sales_type() {
        return sales_type;
    }
    public void set_sales_type(String sales_type) {
        this.sales_type = sales_type;
    }
}