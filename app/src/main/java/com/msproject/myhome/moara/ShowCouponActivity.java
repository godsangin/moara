package com.msproject.myhome.moara;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowCouponActivity extends AppCompatActivity {
    ListView listView;
    ProductAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    Context mContext;
    LayoutInflater mLayoutInflater;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_coupon);

        Intent intent = getIntent();
        String storeUid = intent.getStringExtra("storeUid");

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mdatabase.child("stores/" + storeUid + "/products");

        listView = (ListView) findViewById(R.id.productList);

        adapter = new ProductAdapter();

        mContext = this;
        mLayoutInflater = getLayoutInflater();


        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    adapter.addItems(new Product(snapshot.child("name").getValue().toString(), snapshot.child("price").getValue().toString(), snapshot.child("until").getValue().toString(), snapshot.child("comment").getValue().toString(), snapshot.child("imageSrc").getValue().toString()));

                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        index = i;
                        Product selectedItem = adapter.items.get(i);
                        buyProduct(selectedItem);
                    }
                });
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);
        return d;
    }


    public void buyProduct(Product selectedProduct){
//        Product selectedProduct = (Product) adapter.getItem(index);
        final BuyProductDialog buyProductDialog = new BuyProductDialog();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year+1, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        String date = "~" + dateFormat.format(calendar.getTime());
        selectedProduct.setUntil(date);
        buyProductDialog.getInstance(mContext, mLayoutInflater, R.layout.activity_buy_product_dialog);
        buyProductDialog.show(getApplicationContext(), selectedProduct);
    }

    class ProductAdapter extends BaseAdapter {
        ArrayList<Product> items = new ArrayList<Product>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItems(Product item){items.add(item);}

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void delete(int i){
            this.items.remove(i);
        }

        @Override
        public View getView(int i, View contextView, ViewGroup viewGroup) {
            ProductView view = new ProductView(getApplicationContext());

            Product item = items.get(i);
            view.setName(item.getName());
            view.setPrice(item.getPrice());
            view.setImage(item.getImageSrc());

            return view;
        }
    }
}
