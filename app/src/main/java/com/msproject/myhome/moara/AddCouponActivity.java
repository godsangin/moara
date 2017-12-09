package com.msproject.myhome.moara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class AddCouponActivity extends AppCompatActivity {
    ListView store_item_list;
    StoreItemAdapter store_item_adapter;
    Button add, back;
    Button search;
    EditText search_bar;
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    int REQUEST_CODE_ADD = 100;
    int RESULT_CODE_ADD = 101;
    int REQUEST_CODE_FAIL = 102;
    class StoreItemAdapter extends BaseAdapter {
        ArrayList<Store> items;


        public StoreItemAdapter() {
            this.items = new ArrayList<>();
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.activity_store_item, null, false);
            TextView store_name =(TextView)view.findViewById(R.id.storeName);
            TextView local =(TextView)view.findViewById(R.id.local);
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);

            store_name.setText(items.get(position).getName());
            local.setText(items.get(position).getLocal());
            StorageReference islandRef = storageRef.child(items.get(position).getImageView());
            Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
        String uid = preferences.getString("uid", null);

        add = (Button) findViewById(R.id.add);
        back = (Button) findViewById(R.id.back);
        search = (Button) findViewById(R.id.search);
        search_bar = (EditText) findViewById(R.id.search_bar);

        store_item_list = (ListView) findViewById(R.id.list_item);
        store_item_adapter =  new StoreItemAdapter();

        store_item_list.setAdapter(store_item_adapter);

        store_item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        store_item_adapter.notifyDataSetChanged();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(REQUEST_CODE_ADD,intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(REQUEST_CODE_FAIL,intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input_name = search_bar.getText().toString();
                store_item_adapter.items.clear();
                DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mConditionRef = mdatabase.child("stores");

                mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("snapshot==", dataSnapshot.getValue().toString());
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(input_name.equals(snapshot.child("name").getValue().toString())){//seaquence alignment를 적용해보자
//                                Store item = snapshot.getValue(Store.class);
                                String uid = snapshot.getKey().toString();
                                Log.d("uid==", uid);

                                Store item = new Store(snapshot.child("name").getValue().toString(), snapshot.child("local").getValue().toString(), snapshot.child("comment").getValue().toString());
                                item.setImageView(uid + "/store/logo.jpg");
                                item.setStoreUid(uid);

                                store_item_adapter.items.add(item);
                                store_item_adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("error==", databaseError.getMessage());
                    }
                });
            }
        });

        store_item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Store store = (Store)store_item_adapter.getItem(position);
                Intent intent = new Intent(AddCouponActivity.this, StoreDetailActivity.class);
                intent.putExtra("storeUid", store.getStoreUid());
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD){
            if(resultCode == RESULT_CODE_ADD){
                Intent intent = new Intent();
                setResult(RESULT_CODE_ADD, intent);
                finish();
            }
        }
    }
}
