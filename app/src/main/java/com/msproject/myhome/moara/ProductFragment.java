package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import java.io.IOException;
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

    private int SELECT_PICTURE =1;
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });



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
                    DatabaseReference mConditionRef = mdatabase.child("/stores/" + uid + "/product/" + productName.getText().toString());
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
    public void onAttach(Context context) { super.onAttach(context);  }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == SELECT_PICTURE) {
                Bitmap bm = null;
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bm=rotate(bm,270);
                imageView.setImageBitmap(bm);
                Uri selectedImageUri = data.getData();
                getPath(selectedImageUri);
            }
    }

    /**
     * 사진의 URI 경로를 받는 메소드
     */
    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }
    public Bitmap rotate(Bitmap bitmap, int degrees){
        Bitmap retBitmap = bitmap;

        if(degrees != 0 && bitmap != null){
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted) {
                    retBitmap = converted;
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            catch(OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return retBitmap;
    }
}
