package com.msproject.myhome.moara;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StampGridItem extends LinearLayout {
    ImageView imageView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    public StampGridItem(Context context) {
        super(context);

        init(context);
    }

    public StampGridItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_stamp_grid_item,this, true);
        imageView = (ImageView) view.findViewById(R.id.imageView);
    }

    public void setImageView(String imgSrc){
        StorageReference islandRef = storageRef.child(imgSrc);
        Glide.with(getContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);
    }
}
