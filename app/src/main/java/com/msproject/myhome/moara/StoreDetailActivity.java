package com.msproject.myhome.moara;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class StoreDetailActivity extends AppCompatActivity {
    TextView storeName;
    TextView local;
    TextView comment;
    ImageView imageView;
    Button add;
    Button back;
    ListView itemList;

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
        itemList = (ListView) findViewById(R.id.itemlist);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final String storeUid = bundle.getString("storeUid");
        Log.d("storeUid==", storeUid);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mdatabase.child("stores/" + storeUid);

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeName.setText(dataSnapshot.child("name").getValue().toString());
                local.setText(dataSnapshot.child("local").getValue().toString());
                comment.setText(dataSnapshot.child("comment").getValue().toString());
                StorageReference islandRef = storageRef.child(storeUid + "/store/logo.jpg");
                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
