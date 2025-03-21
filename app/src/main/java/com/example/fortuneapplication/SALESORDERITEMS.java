package com.example.fortuneapplication;

public class SALESORDERITEMS {
     private int id ;
     private int salesorderid;
     private int soiitemid;
     private int soiquantity;
     private int soiunitbasequantity;
     private String uom;
     private double soirate;
     private double soiamount;
     private double taxable;
     private double taxableamount;
     private double taxamount;
     private double soipricelevelid;
     private String soicustomfield1;
     private String soicustomfield2;
     private String soicustomfield3;
     private String soicustomfield4;
     private String soicustomfield5;
     private SALESORDER salesorder ;
     private Item item;
     private String Itemdesc;
     private int location_id;
     private double inventory;
     private double wsr;
     private double suggested;

     public SALESORDERITEMS(int id, int salesorderid, int soiitemid, int soiquantity, int soiunitbasequantity, String uom, double soirate, double soiamount, double taxable, double taxableamount, double taxamount, double soipricelevelid, String soicustomfield1, String soicustomfield2, String soicustomfield3, String soicustomfield4, String soicustomfield5, SALESORDER salesorder, Item item, String Itemdesc, int location_id) {
          this.id = id;
          this.salesorderid = salesorderid;
          this.soiitemid = soiitemid;
          this.soiquantity = soiquantity;
          this.soiunitbasequantity = soiunitbasequantity;
          this.uom = uom;
          this.soirate = soirate;
          this.soiamount = soiamount;
          this.taxable = taxable;
          this.taxableamount = taxableamount;
          this.taxamount = taxamount;
          this.soipricelevelid = soipricelevelid;
          this.soicustomfield1 = soicustomfield1;
          this.soicustomfield2 = soicustomfield2;
          this.soicustomfield3 = soicustomfield3;
          this.soicustomfield4 = soicustomfield4;
          this.soicustomfield5 = soicustomfield5;
          this.salesorder = salesorder;
          this.item = item;
          this.location_id = location_id;
     }

   public SALESORDERITEMS(){

     }

     public int getId() {
          return id;
     }

     public void setId(int id) {
          this.id = id;
     }

     public int getSalesorderid() {
          return salesorderid;
     }

     public void setSalesorderid(int salesorderid) {
          this.salesorderid = salesorderid;
     }

     public int getSoiitemid() {
          return soiitemid;
     }

     public void setSoiitemid(int soiitemid) {
          this.soiitemid = soiitemid;
     }

     public int getSoiquantity() {
          return soiquantity;
     }

     public void setSoiquantity(int soiquantity) {
          this.soiquantity = soiquantity;
     }

     public int getSoiunitbasequantity() {
          return soiunitbasequantity;
     }

     public void setSoiunitbasequantity(int soiunitbasequantity) {
          this.soiunitbasequantity = soiunitbasequantity;
     }

     public String getUom() {
          return uom;
     }

     public void setUom(String uom) {
          this.uom = uom;
     }

     public double getSoirate() {
          return soirate;
     }

     public void setSoirate(double soirate) {
          this.soirate = soirate;
     }

     public double getSoiamount() {
          return soiamount;
     }

     public void setSoiamount(double soiamount) {
          this.soiamount = soiamount;
     }

     public double getTaxable() {
          return taxable;
     }

     public void setTaxable(double taxable) {
          this.taxable = taxable;
     }

     public double getTaxableamount() {
          return taxableamount;
     }

     public void setTaxableamount(double taxableamount) {
          this.taxableamount = taxableamount;
     }

     public double getTaxamount() {
          return taxamount;
     }

     public void setTaxamount(double taxamount) {
          this.taxamount = taxamount;
     }

     public double getSoipricelevelid() {
          return soipricelevelid;
     }

     public void setSoipricelevelid(double soipricelevelid) {
          this.soipricelevelid = soipricelevelid;
     }

     public String getSoicustomfield1() {
          return soicustomfield1;
     }

     public void setSoicustomfield1(String soicustomfield1) {
          this.soicustomfield1 = soicustomfield1;
     }

     public String getSoicustomfield2() {
          return soicustomfield2;
     }

     public void setSoicustomfield2(String soicustomfield2) {
          this.soicustomfield2 = soicustomfield2;
     }

     public String getSoicustomfield3() {
          return soicustomfield3;
     }

     public void setSoicustomfield3(String soicustomfield3) {
          this.soicustomfield3 = soicustomfield3;
     }

     public String getSoicustomfield4() {
          return soicustomfield4;
     }

     public void setSoicustomfield4(String soicustomfield4) {
          this.soicustomfield4 = soicustomfield4;
     }

     public String getSoicustomfield5() {
          return soicustomfield5;
     }

     public void setSoicustomfield5(String soicustomfield5) {
          this.soicustomfield5 = soicustomfield5;
     }

     public SALESORDER getSalesorder() {
          return salesorder;
     }

     public void setSalesorder(SALESORDER salesorder) {
          this.salesorder = salesorder;
     }

     public Item getItem() {
          return item;
     }

     public void setItem(Item item) {
          this.item = item;
     }

     public String getitemdesc(){
         return Itemdesc;
     }
    public void setitemdesc(String Itemdesc) {
        this.Itemdesc = Itemdesc;
    }
    public int getLocationId() {
        return location_id;
    }

    public void setLocationId(int location_id) {
        this.location_id = location_id;
    }
}
