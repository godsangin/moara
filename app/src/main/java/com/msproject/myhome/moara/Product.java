package com.msproject.myhome.moara;

import java.text.NumberFormat;

/**
 * Created by user on 2017-11-17.
 */

public class Product {
    String name;
    String price;
    int image;

    public Product(String productName, int productPrice, int image){
        this.name = productName;
        this.price = String.valueOf(productPrice);
        this.image = image;
    }
    public String getName(){return this.name;}
    public void setName(String name){ this.name = name; }

    public String getPrice(){return this.price;}
    public void setPrice(String price){this.price = price;}

    public int getImage(){return this.image;}
    public void setImage(int image){this.image = image;}
}
