package com.msproject.myhome.moara;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;


public class GiftItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView imageView;
    LinearLayout background;
    int[] color = {R.drawable.round_top_00,R.drawable.round_top_01,R.drawable.round_top_02,R.drawable.round_top_03,R.drawable.round_top_04,R.drawable.round_top_05};

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public GiftItemView(Context context){
        super(context);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_gift_item,this, true);

        textView =(TextView)view.findViewById(R.id.productName);
        textView2 =(TextView)view.findViewById(R.id.productDate);
        textView3 =(TextView)view.findViewById(R.id.from);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        background = (LinearLayout) view.findViewById(R.id.GiftItemColor);

    }
    public void setBackground(int position){
        background.setBackgroundResource(color[position%6]);
    }
    public void setName(String name){textView.setText(name);}
    public void setDate(String date){textView2.setText(date);}
    public void setFrom(String from){textView3.setText(from);}
    public void setImage(String imageSrc, Context context){
        Log.d("imgSrc", imageSrc);
        StorageReference islandRef = storageRef.child(imageSrc);
        Glide.with(context).using(new FirebaseImageLoader()).load(islandRef).into(imageView);
    }
}