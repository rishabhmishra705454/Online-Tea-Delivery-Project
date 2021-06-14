package com.rishabh.teadelivery.Helperclass;

public class HomeChaiHelperclass {

    private int chaiimage;
    private String chaiTitle;
    private String chaiPrice;
    private  String chaiQuantity;

    public HomeChaiHelperclass(int chaiimage, String chaiTitle, String chaiPrice, String chaiQuantity) {
        this.chaiimage = chaiimage;
        this.chaiTitle = chaiTitle;
        this.chaiPrice = chaiPrice;
        this.chaiQuantity = chaiQuantity;
    }

    public int getChaiimage() {
        return chaiimage;
    }

    public void setChaiimage(int chaiimage) {
        this.chaiimage = chaiimage;
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

    public String getChaiQuantity() {
        return chaiQuantity;
    }

    public void setChaiQuantity(String chaiQuantity) {
        this.chaiQuantity = chaiQuantity;
    }
}
