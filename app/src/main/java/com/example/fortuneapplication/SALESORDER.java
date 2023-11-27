package com.example.fortuneapplication;

public class SALESORDER {
    private int salesorderid;
    private String code;
    private String date;
    private int customerid;
    private int locationid;
    private int salesrepid;
    private String dateneeded;
    private int ponumber;
    private int shipvia;
    private String amount;
    private String notes;
    private String custom1;
    private String custom2;
    private String custom3;
    private String custom4;
    private String custom5;
    private Customer customer;
    private SALESORDERITEMS salesorderitems;
    private Item item;
    private Location location;
    private SalesRepList salesRepList;
    private String sales_rep_name;
    private String item_group;
    private String customer_name;


    public SALESORDER(int salesorderid, String code, String date, int customerid, int locationid, int salesrepid, String dateneeded, int ponumber, int shipvia, String amount, String notes, String custom1, String custom2, String custom3, String custom4, String custom5, Customer customer, SALESORDERITEMS salesorderitems, Item item, Location location, SalesRepList salesRepList, String sales_rep_name,String item_group, String customer_name) {
        this.salesorderid = salesorderid;
        this.code = code;
        this.date = date;
        this.customerid = customerid;
        this.locationid = locationid;
        this.salesrepid = salesrepid;
        this.dateneeded = dateneeded;
        this.ponumber = ponumber;
        this.shipvia = shipvia;
        this.amount = amount;
        this.notes = notes;
        this.custom1 = custom1;
        this.custom2 = custom2;
        this.custom3 = custom3;
        this.custom4 = custom4;
        this.custom5 = custom5;
        this.customer = customer;
        this.salesorderitems = salesorderitems;
        this.item = item;
        this.location = location;
        this.salesRepList = salesRepList;
        this.item_group = item_group;
        this.customer_name=customer_name;
    }
    public  SALESORDER(){

    }

    public int getSalesorderid() {
        return salesorderid;
    }

    public void setSalesorderid(int salesorderid) {
        this.salesorderid = salesorderid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getLocationid() {
        return locationid;
    }

    public void setLocationid(int locationid) {
        this.locationid = locationid;
    }

    public int getSalesrepid() {
        return salesrepid;
    }

    public void setSalesrepid(int salesrepid) {
        this.salesrepid = salesrepid;
    }

    public String getDateneeded() {
        return dateneeded;
    }

    public void setDateneeded(String dateneeded) {
        this.dateneeded = dateneeded;
    }

    public int getPonumber() {
        return ponumber;
    }

    public void setPonumber(int ponumber) {
        this.ponumber = ponumber;
    }

    public int getShipvia() {
        return shipvia;
    }

    public void setShipvia(int shipvia) {
        this.shipvia = shipvia;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public String getCustom5() {
        return custom5;
    }

    public void setCustom5(String custom5) {
        this.custom5 = custom5;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SALESORDERITEMS getSalesorderitems() {
        return salesorderitems;
    }

    public void setSalesorderitems(SALESORDERITEMS salesorderitems) {
        this.salesorderitems = salesorderitems;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void set_sales_rep_name(String sales_rep_name){this.sales_rep_name=sales_rep_name;}
    public String get_sales_rep_name(){return sales_rep_name;}

    public void set_customer_name(String customer_name){this.customer_name=customer_name;}
    public String get_customer_name(){return customer_name;}

    public SalesRepList getSalesRepList() {
        return salesRepList;
    }

    public void setSalesRepList(SalesRepList salesRepList) {
        this.salesRepList = salesRepList;
    }
    public void set_item_group(String item_group){this.item_group=item_group;}
    public String get_item_group(){return item_group;}


}

