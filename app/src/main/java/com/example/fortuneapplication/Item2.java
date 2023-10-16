package com.example.fortuneapplication;

public class Item2  {
    private String unitbase;
    private String pricelvlid;
    private String id;
    private String code;
    private String description;
    private String unitmeasure;
    private String onhand;
    private String price;
    private String quantity;
    private double total;

    public Item2(String unitbase, String pricelvlid, String id, String code, String description, String unitmeasure, String onhand, String price, String quantity, String total) {
        this.unitbase = unitbase;
        this.pricelvlid = pricelvlid;
        this.id =id;
        this.code = code;
        this.description = description;
        this.unitmeasure = unitmeasure;
        this.onhand = onhand;
        this.price = price;
        this.quantity = quantity;
        total = total.replace(",", "");
        this.total = Double.parseDouble(total);
    }
    public Item2(){

    }
    public String getUnitbase(){
        return unitbase;
    }
    public void setUnitbase(String unitbase){
        this.unitbase = unitbase;
    }
    public String getPricelvlid(){
        return pricelvlid;
    }
    public void setPricelvlid(String pricelvlid){
        this.pricelvlid = pricelvlid;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
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

    public String getUnitmeasure() {
        return unitmeasure;
    }

    public void setUnitmeasure(String unitmeasure) {
        this.unitmeasure = unitmeasure;
    }

    public String getOnhand() {
        return onhand;
    }

    public void setOnhand(String onhand) {
        this.onhand = onhand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
