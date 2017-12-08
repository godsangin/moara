package com.msproject.myhome.moara;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by sanginLee on 2017-11-01.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GiftItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView imageView;

    public GiftItemView(Context context){
        super(context);
        init(context);
    }

    public GiftItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_gift_item,this, true);

        textView =(TextView)view.findViewById(R.id.productName);
        textView2 =(TextView)view.findViewById(R.id.productDate);
        textView3 =(TextView)view.findViewById(R.id.from);
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }
    public void setName(String name){textView.setText(name);}
    public void setDate(String date){textView2.setText(date);}
    public void setFrom(String from){textView3.setText(from);}
    public void setImage(int resId){imageView.setImageResource(resId);}
}