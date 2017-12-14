package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import static android.content.Context.MODE_PRIVATE;


public class SaveFragment extends Fragment {
    ImageView imageView;
    TextView storeName;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    LayoutInflater mLayoutInflater;
    EditText phoneNumber;
    Button saveButton;
    public SaveFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance() {
        SaveFragment fragment = new SaveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        storeName = (TextView) view.findViewById(R.id.storeName);
        phoneNumber = (EditText) view.findViewById(R.id.PhoneNumberText);
        saveButton = (Button) view.findViewById(R.id.SaveButton);

        DatabaseReference mConditionRef = mdatabase.child("/stores/" + MainActivity.uid);
        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("barcode").getValue() != null){
                    storeName.setText(dataSnapshot.child("name").getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        StorageReference islandRef = storageRef.child(MainActivity.uid + "/store/logo.jpg");
        Glide.with(getContext()).using(new FirebaseImageLoader()).load(islandRef).into(imageView);


        saveButton.setOnClickListener(new View.OnClickListener() { //적립버튼클릭
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
