package com.msproject.myhome.moara;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by user on 2017-11-17.
 */

public class ProductView extends LinearLayout{
    TextView name;
    TextView price;
    ImageView img;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    public ProductView(Context context){
        super(context);

        init(context);
    }

    public ProductView(Context context, AttributeSet attrs){
        super(context, attrs);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.product,this,true);

        name = (TextView) findViewById(R.id.product_name);
        price = (TextView) findViewById(R.id.product_price);
        img = (ImageView) findViewById(R.id.product_img);
    }

    public void setName(String product_name){ name.setText(product_name);}
    public void setPrice(String product_price){ price.setText(product_price + " 개");}
    public void setImage(String imageSrc) {
        StorageReference islandRef = storageRef.child(imageSrc);
        Glide.with(getContext()).using(new FirebaseImageLoader()).load(islandRef).into(img);

    }
}
