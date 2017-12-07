package com.msproject.myhome.moara;

/**
 * Created by sanginLee on 2017-12-06.
 */

public class Store {
    String name;
    String image_src;
    Product product;
    String local;

    public Store(String name, String image_src, Product product, String local){
        this.name = name;
        this.image_src = image_src;
        this.product = product;
        this.local = local;
    }

    public Store(String name, String image_src, String local){
        this.name = name;
        this.image_src = image_src;
        this.product = null;
        this.local = local;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_src() {
        return image_src;
    }

    public void setImage_src(String image_src) {
        this.image_src = image_src;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

}
