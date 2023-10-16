package com.example.fortuneapplication;

public class PaymenTerm {
    private String id ;
    private String code;
    private String description;
    private String netdue;

    public PaymenTerm(String id, String code, String description, String netdue) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.netdue = netdue;
    }

    public PaymenTerm(){

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

    public String getNetdue() {
        return netdue;
    }

    public void setNetdue(String netdue) {
        this.netdue = netdue;
    }
}
