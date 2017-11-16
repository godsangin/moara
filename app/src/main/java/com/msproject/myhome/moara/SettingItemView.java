package com.msproject.myhome.moara;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by SEUNGHYUK SHIN on 2017-11-17.
 */

public class SettingItemView extends LinearLayout {
    public SettingItemView(Context context){
        super(context);
        init(context);
    }
    TextView textView;
    ImageView imageView;
    public SettingItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_item_setting,this, true);

        textView =(TextView)findViewById(R.id.SettingText);
        imageView = (ImageView) findViewById(R.id.SettingImage);
    }
    public void setName(String name){textView.setText(name);}
    public void setImage(int resId){imageView.setImageResource(resId);}
}
