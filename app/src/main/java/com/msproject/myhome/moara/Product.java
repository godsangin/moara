package com.msproject.myhome.moara;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.text.NumberFormat;

/**
 * Created by user on 2017-11-17.
 */

public class Product {
    String name;
    String price;
    String imageSrc;
    String until;
    String comment;

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Product(String productName, String productPrice, String until, String comment, String imageSrc){
        this.comment = comment;
        this.name = productName;
        this.price = productPrice;
        this.imageSrc = imageSrc;
    }
    public String getName(){return this.name;}
    public void setName(String name){ this.name = name; }

    public String getPrice(){return this.price;}
    public void setPrice(String price){this.price = price;}

    public String getImageSrc(){return this.imageSrc;}
    public void setImageSrc(String imageSrc){this.imageSrc = imageSrc;}

}
