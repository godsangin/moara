package com.msproject.myhome.moara;

/**
 * Created by user on 2017-11-16.
 */

public class Coupon {
    String coupon_name;
    String imageSrc;
    int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Coupon(){

    }

    public Coupon(String name, int num, String img){
        this.coupon_name = name;
        this.imageSrc = img;
        this.num = num;
    }

    public String getCoupon_name(){
        return this.coupon_name;
    }
    public void setCoupon_name(String name){
        this.coupon_name = name;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrd(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
