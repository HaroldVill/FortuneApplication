package com.example.fortuneapplication;

public class Item {
    private String id;
    private String code;
    private String description;
    private String rate;
    private String group;
    private String quantity;
    private String unitquant;
    private Unit unit;
    private NewPriceLvl newPriceLvl;
    private String vendor;
    private String inactive;
    public String wsr;

    public Item(String id, String code, String description, String rate, String group, String quantity, String unitquant, String vendor, String inactive,String wsr) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.rate = rate;
        this.group = group;
        this.quantity = quantity;
        this.unitquant = unitquant;
        this.unit = unit;
        this.newPriceLvl = newPriceLvl;
        this.vendor = vendor;
        this.inactive = inactive;
        this.wsr = wsr;
    }
    public Item(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitquant() {
        return unitquant;
    }

    public void setUnitquant(String unitquant) {
        this.unitquant = unitquant;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    public NewPriceLvl getNewPriceLvl() {
        return newPriceLvl;
    }

    public void setNewPriceLvl(NewPriceLvl newPriceLvl) {
        this.newPriceLvl = newPriceLvl;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getWsr() {
        return wsr;
    }

    public void setWsr(String wsr) {
        this.wsr = wsr;
    }
}


