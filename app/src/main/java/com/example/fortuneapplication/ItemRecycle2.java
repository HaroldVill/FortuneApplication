package com.example.fortuneapplication;

public class ItemRecycle2 {
    String codes;
    String descriptions;
    String Prices;
    String Stocks;

    public ItemRecycle2(String codes, String descriptions, String prices, String stocks) {
        this.codes = codes;
        this.descriptions = descriptions;
        Prices = prices;
        Stocks = stocks;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getPrices() {
        return Prices;
    }

    public void setPrices(String prices) {
        Prices = prices;
    }

    public String getStocks() {
        return Stocks;
    }

    public void setStocks(String stocks) {
        Stocks = stocks;
    }
}
