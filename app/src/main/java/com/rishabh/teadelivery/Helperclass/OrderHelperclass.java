package com.rishabh.teadelivery.Helperclass;

import java.util.Map;

public class OrderHelperclass {

   private String id , phoneNo, uid, address, locality, pincode, latitude, longitude, orderPrice , orderQuantity, orderStatus , orderDate , orderTime , houseFlatBlockNo , landmark;

    public OrderHelperclass() {
    }

    public OrderHelperclass(String id, String phoneNo, String uid, String address, String locality, String pincode, String latitude, String longitude, String orderPrice, String orderQuantity, String orderStatus, String orderDate, String orderTime, String houseFlatBlockNo, String landmark) {
        this.id = id;
        this.phoneNo = phoneNo;
        this.uid = uid;
        this.address = address;
        this.locality = locality;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderPrice = orderPrice;
        this.orderQuantity = orderQuantity;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.houseFlatBlockNo = houseFlatBlockNo;
        this.landmark = landmark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getHouseFlatBlockNo() {
        return houseFlatBlockNo;
    }

    public void setHouseFlatBlockNo(String houseFlatBlockNo) {
        this.houseFlatBlockNo = houseFlatBlockNo;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}