package com.rishabh.teadelivery.Helperclass;

public class UserProfileHelperClass {

    String phoneNo , fullName , userId , email;

    public UserProfileHelperClass() {
    }



    public UserProfileHelperClass(String phoneNo, String fullName, String userId , String email) {
        this.phoneNo = phoneNo;
        this.fullName = fullName;
        this.userId = userId;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
