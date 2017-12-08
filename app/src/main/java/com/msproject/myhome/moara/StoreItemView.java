package com.msproject.myhome.moara;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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


public class StoreItemView extends LinearLayout {
    TextView storeName;
    TextView local;
    ImageView imageView;

    public StoreItemView(Context context){
        super(context);
        init(context);
    }

    public StoreItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_gift_item,this, true);

        storeName = (EditText)view.findViewById(R.id.storeName);
        local = (EditText)view.findViewById(R.id.local);
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }
    public void setName(String name){storeName.setText(name);}
    public void setLocal(String date){local.setText(date);}
    public void setImage(int resId){imageView.setImageResource(resId);}
    public TextView getStoreName() {
        return storeName;
    }

    public TextView getLocal() {
        return local;
    }

    public ImageView getImageView() {
        return imageView;
    }

}