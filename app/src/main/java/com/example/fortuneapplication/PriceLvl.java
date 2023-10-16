package com.example.fortuneapplication;

public class PriceLvl {

    private String pricelvl_id;
    private String item_id;
    private String custom_price;
    private String code;
    private String description;

    public PriceLvl(String pricelvl_id, String item_id, String custom_price, String code, String description) {
        this.pricelvl_id = pricelvl_id;
        this.item_id = item_id;
        this.custom_price = custom_price;
        this.code = code;
        this.description = description;
    }

    public PriceLvl(){

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

    public String getCustom_price() {
        return custom_price;
    }

    public void setCustom_price(String custom_price) {
        this.custom_price = custom_price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
