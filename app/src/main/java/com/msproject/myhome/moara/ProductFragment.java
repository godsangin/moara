package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ProductFragment extends Fragment {
    ImageView imageView;
    EditText productName;
    EditText cost;
    EditText comment;
    Button submit;
    Button cencel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    LayoutInflater mLayoutInflater;

    public ProductFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        productName = (EditText)view.findViewById(R.id.productName);
        comment = (EditText)view.findViewById(R.id.comment);
        cost = (EditText)view.findViewById(R.id.cost);
        submit = (Button)view.findViewById(R.id.submit);
        cencel = (Button)view.findViewById(R.id.cencel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//이미지 업로드 // storage
                if(productName.getText().toString().equals("") || cost.getText().toString().equals("")){
                    final CustomDialog customDialog = new CustomDialog();
                    mLayoutInflater = getActivity().getLayoutInflater();
                    customDialog.getInstance(getActivity(), mLayoutInflater, R.layout.submit_dialog);
                    customDialog.show("상품명과 비용을 입력해주세요.", "확인");
                    customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialog.dismiss();
                        }
                    });

                }
                else{
                    SharedPreferences preferences = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
                    String uid = preferences.getString("uid", null); ;
                    Store store = new Store(productName.getText().toString(), cost.getText().toString(), comment.getText().toString());
                    DatabaseReference mConditionRef = mdatabase.child("/stores/" + uid + "/" + productName.getText().toString());
                    mConditionRef.child("name").setValue(productName.getText().toString());
                    mConditionRef.child("price").setValue(cost.getText().toString());
                    mConditionRef.child("comment").setValue(comment.getText().toString());

                    StorageReference logoRef = storageReference.child(mAuth.getCurrentUser().getUid() + "/product/" + productName.getText().toString() + ".jpg");
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = logoRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            final CustomDialog customDialog = new CustomDialog();
                            mLayoutInflater = getActivity().getLayoutInflater();
                            customDialog.getInstance(getActivity(), mLayoutInflater, R.layout.submit_dialog);
                            customDialog.show("등록이 완료되었습니다.", "확인");
                            customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.add(R.id.content,SaveFragment.newInstance());
                                    transaction.commit();
                                    customDialog.dismiss();
                                }
                            });

                        }
                    });
                }

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
