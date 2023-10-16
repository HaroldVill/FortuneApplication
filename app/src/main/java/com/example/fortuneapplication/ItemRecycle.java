package com.example.fortuneapplication;

public class ItemRecycle {
    private String code;
    private String desccription;
    private int rate;


    public ItemRecycle() {
    }

    public ItemRecycle(String code, String desccription, int rate) {
        this.code = code;
        this.desccription = desccription;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public String getDesccription() {
        return desccription;
    }

    public int getRate() {
        return rate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesccription(String desccription) {
        this.desccription = desccription;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}

