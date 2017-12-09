package com.msproject.myhome.moara;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShowCouponActivity extends AppCompatActivity {
    ListView listView;
    ProductAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

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

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    adapter.addItems(new Product(snapshot.child("name").getValue().toString(), snapshot.child("price").getValue().toString(), snapshot.child("until").getValue().toString(), snapshot.child("comment").getValue().toString(), snapshot.child("imageSrc").getValue().toString()));
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                buyProduct();
            }
        });
    }
    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);
        return d;
    }


    public void buyProduct(){
        Product selectedProduct = (Product) adapter.getItem(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ImageView imageView = null;
        StorageReference islandRef = storageRef.child(selectedProduct.getImageSrc());
        Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);
        Bitmap bitmap = imageView.getDrawingCache(true);
        builder.setTitle(selectedProduct.getName());
        builder.setMessage("구매하시겠습니까? \n사용 쿠폰 : " + selectedProduct.getPrice() +" 개");
        builder.setIcon(getDrawableFromBitmap(bitmap));

        builder.setPositiveButton("예", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogI, int whichButton){
                
                Toast.makeText(getApplicationContext(), "구매 완료", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogI, int whichButton){
                Toast.makeText(getApplicationContext(), "구매 실패", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
