package com.msproject.myhome.moara;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class StoreDetailActivity extends AppCompatActivity {
    TextView storeName;
    TextView local;
    TextView comment;
    ImageView imageView;
    Button add;
    Button back;
    ListView productView;
    StoreProductAdapter adapter;
    LayoutInflater mLayoutInflater;
    int RESULT_CODE_ADD = 101;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        storeName = (TextView) findViewById(R.id.storeName);
        local = (TextView) findViewById(R.id.local);
        comment = (TextView) findViewById(R.id.comment);
        imageView = (ImageView) findViewById(R.id.imageView);
        add = (Button) findViewById(R.id.addStore);
        back = (Button) findViewById(R.id.back);
        productView = (ListView) findViewById(R.id.itemlist);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final String storeUid = bundle.getString("storeUid");
        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

        adapter = new StoreProductAdapter(getLayoutInflater());

        productView.setAdapter(adapter);

        Log.d("storeUid==", storeUid);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mConditionRef = mdatabase.child("users/" + MainActivity.uid + "/stamps/" + storeUid + "/");
                mConditionRef.child("name").setValue(storeName.getText().toString());
                mConditionRef.child("num").setValue("0");
                mConditionRef.child("storeUid").setValue(storeUid + "/store/logo.jpg");
                mLayoutInflater = getLayoutInflater();

                final CustomDialog customDialog = new CustomDialog();

                customDialog.getInstance(StoreDetailActivity.this, mLayoutInflater, R.layout.submit_dialog);
                customDialog.show("등록이 완료되었습니다.", "확인");
                customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        Intent intent = new Intent();
                        setResult(RESULT_CODE_ADD, intent);
                        finish();
                    }
                });
            }
        });


        DatabaseReference mConditionRef = mdatabase.child("stores/" + storeUid);

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeName.setText(dataSnapshot.child("name").getValue().toString());
                local.setText(dataSnapshot.child("local").getValue().toString());
                comment.setText(dataSnapshot.child("comment").getValue().toString());
                StorageReference islandRef = storageRef.child(storeUid + "/store/logo.jpg");
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);
                dataSnapshot = dataSnapshot.child("products");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   StoreProduct item = new StoreProduct(snapshot.child("name").getValue().toString(), snapshot.child("price").getValue().toString(), snapshot.child("until").getValue().toString(), snapshot.child("comment").getValue().toString());
                   item.setImage(storeUid + "/product/" + snapshot.child("name").getValue().toString() + ".jpg");
                   adapter.addItems(item);
                   productView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class StoreProduct{
        String name;
        String cost;
        String validity;
        String info;
        String imageSrc;

        public StoreProduct(String productName, String productCost, String productValidity, String productInfo){
            this.name = productName;
            this.cost = productCost;
            this.validity = productValidity;
            this.info = productInfo;
            //this.image = image;
        }
        public String getName(){return this.name;}
        public void setName(String name){ this.name = name; }

        public String getCost(){return this.cost;}
        public void setCost(String cost){this.cost = cost;}

        public String getValidity(){return this.validity;}
        public void setValidity(String validity){this.validity = validity;}

        public String getInfo(){return this.info;}
        public void setInfo(String info){this.info = info;}

        public String getImage(){return this.imageSrc;}
        public void setImage(String imageSrc){this.imageSrc = imageSrc;}
    }

    class StoreProductAdapter extends BaseAdapter {
        ArrayList<StoreProduct> items;
        LayoutInflater layoutInflater;

        public StoreProductAdapter(LayoutInflater layoutInflater) {
            this.items = new ArrayList<>();
            this.layoutInflater = layoutInflater;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItems(StoreProduct item) {
            items.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.store_item, null, false);
            TextView name = (TextView) view.findViewById(R.id.store_product_name);
            TextView cost = (TextView) view.findViewById(R.id.store_product_cost);
            TextView validity = (TextView) view.findViewById(R.id.store_product_validity);
            TextView info = (TextView) view.findViewById(R.id.store_product_info);
            ImageView imageView = (ImageView) view.findViewById(R.id.store_product_image);

            name.setText(items.get(position).getName());
            cost.setText(items.get(position).getCost());
            validity.setText(items.get(position).getValidity());
            info.setText(items.get(position).getInfo());
            StorageReference islandRef = storageRef.child(items.get(position).getImage());
            Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);

            return view;
        }
    }

}
