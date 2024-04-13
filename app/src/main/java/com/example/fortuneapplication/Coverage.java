package com.example.fortuneapplication;

public class Coverage {
    private String id;
    private String customer_id;
    private String salesrep_id;
    private String frequency;
    private String frequency_week_schedule;
    private String day;
    public Coverage(String id,String customer_id,String salesrep_id,String day,String frequency,String frequency_week_schedule){
        this.id = id;
        this.customer_id = customer_id;
        this.salesrep_id = salesrep_id;
        this.frequency = frequency;
        this.frequency_week_schedule = frequency_week_schedule;
        this.day = day;
    }


    public String get_id(){
        return id;
    }
    public void set_id(String id){
        this.id = id;
    }

    public void set_customer_id(String customer_id){
        this.customer_id = customer_id;
    }
    public String get_customer_id(){
        return customer_id;
    }

    public void set_salesrep_id(String salesrep_id){
        this.salesrep_id = salesrep_id;
    }
    public String get_salesrep_id(){
        return salesrep_id;
    }

    public void set_frequency(String frequency){
        this.frequency = frequency;
    }
    public String get_frequency(){
        return frequency;
    }

    public void set_day(String day){
        this.day = day;
    }
    public String get_day(){
        return day;
    }

    public void set_frequency_week_schedule(String frequency_week_schedule){
        this.frequency_week_schedule = frequency_week_schedule;
    }
    public String get_frequency_week_schedule(){
        return frequency_week_schedule;
    }
}
