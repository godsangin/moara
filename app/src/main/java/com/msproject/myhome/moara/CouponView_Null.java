package com.msproject.myhome.moara;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CouponView_Null extends LinearLayout {
    ImageView myCoupon_img;
    LinearLayout linearLayout;

    public CouponView_Null(Context context){
        super(context);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.null_coupon_view,this,true);

        myCoupon_img = (ImageView) findViewById(R.id.NullCoupon_img);
        linearLayout = (LinearLayout) findViewById(R.id.NullCouponColor);
    }

    public void setImg(int img){
        this.myCoupon_img.setImageResource(img);
    }
    public void setBackground(int resId) {this.linearLayout.setBackgroundResource(resId);}
}
