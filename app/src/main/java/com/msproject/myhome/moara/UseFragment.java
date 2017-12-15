package com.msproject.myhome.moara;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class UseFragment extends Fragment {

    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    EditText couponNumber;
    Button useButton;
    char[] barcode;
    String barcodeString;
    char[] charUserName;
    String userName;

    private CompoundBarcodeView barcodeView;

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

        barcodeView = (CompoundBarcodeView) view.findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);

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

    private BarcodeCallback callback = new BarcodeCallback() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/");

        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() != null) {
                final String barcode = result.getText();

                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().substring(5).equals(barcode.substring(13))) {
                            DataSnapshot snapshot = dataSnapshot.child("giftitem").child(MainActivity.uid);
                            boolean exist = false;
                            GiftItem item = null;
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                item = ds.getValue(GiftItem.class);
                                exist = true;
                                break;
                            }

                            if(exist) {
                                onPause();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                final AlertDialog dialog = builder.create();

                                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_detail, null);
                                TextView name = (TextView) dialogView.findViewById(R.id.product_detail_name);
                                TextView receiver = (TextView) dialogView.findViewById(R.id.product_detail_receiver);
                                TextView until = (TextView) dialogView.findViewById(R.id.product_detail_until);
                                ImageView imageView = (ImageView) dialogView.findViewById(R.id.product_detail_img);

                                Button useButton = (Button) dialogView.findViewById(R.id.product_detail_use);
                                useButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });

                                Button cancelButton = (Button) dialogView.findViewById(R.id.product_detail_cancel);
                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onResume();
                                        dialog.dismiss();
                                    }
                                });


                                name.setText(item.getName());
                                receiver.setText(item.getFrom());
                                until.setText(item.getDate());

                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference ref = storage.getReference();

                                StorageReference downloadRef = ref.child(item.getStoreUid()).child("product").child(item.getName() + ".jpg");
                                Glide.with(getContext()).using(new FirebaseImageLoader()).load(downloadRef).into(imageView);

                                dialog.setView(dialogView);
                                dialog.show();

                            }

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
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