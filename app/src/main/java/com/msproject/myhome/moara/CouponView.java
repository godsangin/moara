package com.msproject.myhome.moara;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by user on 2017-11-16.
 */

public class CouponView extends LinearLayout {
    TextView myCoupon_name;
    ImageView myCoupon_img;
    LinearLayout linearLayout;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    public CouponView(Context context){
        super(context);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.coupon_my_coupon,this,true);

        myCoupon_img = (ImageView) findViewById(R.id.myCoupon_img);
        myCoupon_name = (TextView) findViewById(R.id.myCoupon_name);
        linearLayout = (LinearLayout) findViewById(R.id.CouponColor);
    }

    public void setName(String name){
        this.myCoupon_name.setText(name);
    }
    public void setImg(String imgSrc){
        StorageReference islandRef = storageRef.child(imgSrc);
        Log.d("imgSrc==", imgSrc);
        Glide.with(getContext()).using(new FirebaseImageLoader()).load(islandRef).into(myCoupon_img);
    }
    public void setBackground(int resId) {this.linearLayout.setBackgroundResource(resId);}
}
