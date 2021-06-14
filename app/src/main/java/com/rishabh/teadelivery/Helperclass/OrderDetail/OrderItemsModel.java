package com.rishabh.teadelivery.Helperclass.OrderDetail;

public class OrderItemsModel {

    private String chaiPrice, chaiQuanty ,chaiTitle , totalPrice ;

    public OrderItemsModel() {
    }

    public OrderItemsModel(String chaiPrice, String chaiQuanty, String chaiTitle, String totalPrice) {
        this.chaiPrice = chaiPrice;
        this.chaiQuanty = chaiQuanty;
        this.chaiTitle = chaiTitle;
        this.totalPrice = totalPrice;
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

    public String getChaiTitle() {
        return chaiTitle;
    }

    public void setChaiTitle(String chaiTitle) {
        this.chaiTitle = chaiTitle;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
