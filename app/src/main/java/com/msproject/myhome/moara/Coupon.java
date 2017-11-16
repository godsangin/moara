package com.msproject.myhome.moara;

/**
 * Created by user on 2017-11-16.
 */

public class Coupon {
    String coupon_name;
    int coupon_img;

    public Coupon(){

    }

    public Coupon(String name, int img){
        this.coupon_name = name;
        this.coupon_img = img;
    }

    public String getCoupon_name(){
        return this.coupon_name;
    }
    public void setCoupon_name(String name){
        this.coupon_name = name;
    }

    public int getCoupon_img(){
        return this.coupon_img;
    }
    public void setCoupon_img(int img){
        this.coupon_img = img;
    }


}
