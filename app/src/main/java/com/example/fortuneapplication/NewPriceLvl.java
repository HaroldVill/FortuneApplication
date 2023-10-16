package com.example.fortuneapplication;

public class NewPriceLvl {

    private String pid;
    private String pcode;
    private String pdescription;


    public NewPriceLvl(String pid, String pcode, String pdescription) {
        this.pid = pid;
        this.pcode = pcode;
        this.pdescription = pdescription;
    }

    public NewPriceLvl(){

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }
}
