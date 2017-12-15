package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class UseFragment extends Fragment {

    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    EditText couponNumber;
    Button useButton;
    char[] barcode;
    String barcodeString;
    char[] charUserName;
    String userName;
    public UseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UseFragment newInstance() {
        UseFragment fragment = new UseFragment();
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
        View view = inflater.inflate(R.layout.fragment_use, container, false);
        couponNumber = (EditText) view.findViewById(R.id.CouponNumber);
        barcode = new char[36];
        charUserName=new char[23];



        useButton = (Button) view.findViewById(R.id.UseButton);

        useButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {// 쿠폰번호 db에서 같은거 찾아서 삭제
                barcode = couponNumber.getText().toString().toCharArray();
                barcodeString=String.valueOf(barcode);

                for(int i = 0; i<charUserName.length;i++){
                    charUserName[i]=barcode[i+13];
                }
                userName=String.valueOf(charUserName);

                final DatabaseReference usersRef = mdatabase.child("/users/");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getKey().toString().substring(5).equals(userName)){ //유저이름 경로를 저장
                                DataSnapshot giftitem = dataSnapshot.child(snapshot.getKey().toString() + "/giftitem");
                                for(DataSnapshot snap : giftitem.getChildren()){
                                    for(DataSnapshot s : snap.getChildren()){
                                        if(s.child("barcode").getValue().toString().equals(barcodeString)){
                                            usersRef.child(snapshot.getKey().toString() + "/giftitem/" + snap.getKey().toString() + "/" + s.getKey().toString()).removeValue();
                                            Toast.makeText(getActivity(),"사용완료",Toast.LENGTH_SHORT).show();
                                            usersRef.child(snapshot.getKey().toString() + "/alarm/buy").setValue(true);
                                            //알림추가


                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
