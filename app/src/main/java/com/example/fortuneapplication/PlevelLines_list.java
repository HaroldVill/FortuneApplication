package com.example.fortuneapplication;

public class PlevelLines_list {

    private String id;
    private String pricelvl_id;
    private String item_id;
    private String customprice;

    public PlevelLines_list(String id, String pricelvl_id, String item_id, String customprice) {
        this.id = id;
        this.pricelvl_id = pricelvl_id;
        this.item_id = item_id;
        this.customprice = customprice;
    }
    public PlevelLines_list (){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPricelvl_id() {
        return pricelvl_id;
    }

    public void setPricelvl_id(String pricelvl_id) {
        this.pricelvl_id = pricelvl_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCustomprice() {
        return customprice;
    }

    public void setCustomprice(String customprice) {
        this.customprice = customprice;
    }
}
