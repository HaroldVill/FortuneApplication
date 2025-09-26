package com.example.fortuneapplication;

public class SpecialPriceLevel {
    private String id;
    private String recorded_on;
    private String customer_id;
    private String item_id;
    private String sales_rep_id;
    private String price_level_id;
    private String approved;
    private String approved_by;
    private String approved_on;
    private String custom_price;

    public SpecialPriceLevel(String id,
                             String recorded_on,
                             String customer_id,
                             String item_id,
                             String sales_rep_id,
                             String price_level_id,
                             String approved,
                             String approved_by,
                             String approved_on,
                             String custom_price) {
        this.id = id;
        this.recorded_on = recorded_on;
        this.customer_id = customer_id;
        this.item_id = item_id;
        this.sales_rep_id = sales_rep_id;
        this.price_level_id = price_level_id;
        this.approved = approved;
        this.approved_by = approved_by;
        this.approved_on = approved_on;
        this.custom_price = custom_price;
    }

    public String get_id() { return id; }
    public void set_id(String id) { this.id = id; }
    public String get_recorded_on() { return recorded_on; }
    public void set_recorded_on(String recorded_on) { this.recorded_on = recorded_on; }
    public String get_customer_id() { return customer_id; }
    public void set_customer_id(String customer_id) { this.customer_id = customer_id; }
    public String get_item_id() { return item_id; }
    public void set_item_id(String item_id) { this.item_id = item_id; }
    public String get_sales_rep_id() { return sales_rep_id; }
    public void set_sales_rep_id(String sales_rep_id) { this.sales_rep_id = sales_rep_id; }
    public String get_price_level_id() { return price_level_id; }
    public void set_price_level_id(String price_level_id) { this.price_level_id = price_level_id; }
    public String get_approved() { return approved; }
    public void set_approved(String approved) { this.approved = approved; }
    public String get_approved_by() { return approved_by; }
    public void set_approved_by(String approved_by) { this.approved_by = approved_by; }
    public String get_approved_on() { return approved_on; }
    public void set_approved_on(String approved_on) { this.approved_on = approved_on; }
    public String get_custom_price() { return custom_price; }
    public void set_custom_price(String custom_price) { this.custom_price = custom_price; }

} //SpecialPriceLevel
