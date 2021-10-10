package com.example.covid19trackerapp;

public class userInfo_Adapter {


    private String usernum;
    private String userdistrict;

    public userInfo_Adapter(){}

    public userInfo_Adapter(String usernum, String userdistrict) {
        this.usernum = usernum;
        this.userdistrict = userdistrict;
    }


    public String getUsernum() {
        return usernum;
    }

    public void setUsernum(String usernum) {
        this.usernum = usernum;
    }

    public String getUserdistrict() {
        return userdistrict;
    }

    public void setUserdistrict(String userdistrict) {
        this.userdistrict = userdistrict;
    }


}
