package com.example.fortuneapplication;

public class Unit {
    private String item_id;
    private String name;
    private String quantity;
    private String unit_id;
    private Item item;

    public Unit(String item_id, String name, String quantity, String unit_id) {
        this.item_id = item_id;
        this.name = name;
        this.quantity = quantity;
        this.unit_id = unit_id;
        this.item = item;
    }
    public  Unit(){

    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

//    public Unit(String item_id, String name, String quantity, String unit_id) {
//        this.item_id = item_id;
//        this.name = name;
//        this.quantity = quantity;
//        this.unit_id = unit_id;
//    }
//
//    public Unit(){
//
//    }
//
//    public String getItem_id() {
//        return item_id;
//    }
//
//    public void setItem_id(String item_id) {
//        this.item_id = item_id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(String quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getUnit_id() {
//        return unit_id;
//    }
//
//    public void setUnit_id(String unit_id) {
//        this.unit_id = unit_id;
//    }
//}
