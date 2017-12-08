package com.msproject.myhome.moara;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

/**
 * Created by sanginLee on 2017-12-06.
 */

public class Store {
    String name;
    String comment;
    Product product;
    String local;
    String imageSrc;
    String storeUid;

    public String getStoreUid() {
        return storeUid;
    }

    public void setStoreUid(String storeUid) {
        this.storeUid = storeUid;
    }

    public Store(String name, String local, String comment, Product product, String imageSrc){
        this.name = name;
        this.product = product;
        this.local = local;
        this.comment = comment;
        this.imageSrc = imageSrc;
    }

    public Store(String name, String local, String comment){
        this.name = name;
        this.comment = comment;
        this.product = null;
        this.local = local;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String image_src) {
        this.comment = comment;
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
    public String getImageView() {
        return imageSrc;
    }
    public void setImageView(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
