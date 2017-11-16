package com.msproject.myhome.moara;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by user on 2017-11-16.
 */

public class CouponView extends LinearLayout {
    TextView myCoupon_name;
    ImageView myCoupon_img;

    public CouponView(Context context){
        super(context);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.coupon_my_coupon,this,true);

        myCoupon_img = (ImageView) findViewById(R.id.myCoupon_img);
        myCoupon_name = (TextView) findViewById(R.id.myCoupon_name);
    }

    public void setName(String name){
        this.myCoupon_name.setText(name);
    }
    public void setImg(int img){
        this.myCoupon_img.setImageResource(img);
    }

}
