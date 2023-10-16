package com.example.fortuneapplication;

public class ProductList {

     int Id;
     String Code;
     String Description;

    public ProductList(int id, String code, String description) {
        Id = id;
        Code = code;
        Description = description;
    }

    public ProductList(){

    }


    public int getId() {
        return Id;
    }

    public String getCode() {
        return Code;
    }

    public String getDescription() {
        return Description;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
