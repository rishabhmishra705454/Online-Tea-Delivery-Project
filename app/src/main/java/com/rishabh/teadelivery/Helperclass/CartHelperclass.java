package com.rishabh.teadelivery.Helperclass;

public class CartHelperclass {

    private String  chaiTitle , chaiPrice , chaiQuanty , totalPrice;

    public CartHelperclass() {
    }

    public CartHelperclass(String chaiTitle, String chaiPrice, String chaiQuanty, String totalPrice) {
        this.chaiTitle = chaiTitle;
        this.chaiPrice = chaiPrice;
        this.chaiQuanty = chaiQuanty;
        this.totalPrice = totalPrice;
    }

    public String getChaiTitle() {
        return chaiTitle;
    }

    public void setChaiTitle(String chaiTitle) {
        this.chaiTitle = chaiTitle;
    }

    public String getChaiPrice() {
        return chaiPrice;
    }

    public void setChaiPrice(String chaiPrice) {
        this.chaiPrice = chaiPrice;
    }

    public String getChaiQuanty() {
        return chaiQuanty;
    }

    public void setChaiQuanty(String chaiQuanty) {
        this.chaiQuanty = chaiQuanty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
