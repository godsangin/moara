package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SubmitStoreActivity extends AppCompatActivity {
    ImageView imageView;
    EditText store_name;
    EditText where;
    EditText comment;
    Button submit;
    Button cancel;
    Button back;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    LayoutInflater mLayoutInflater;

    final int SELECT_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_store);

        imageView = (ImageView) findViewById(R.id.imageView);
        store_name = (EditText) findViewById(R.id.storeName);
        where = (EditText) findViewById(R.id.where);
        comment = (EditText) findViewById(R.id.comment);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cencel);
        back = (Button) findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지 업로드
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//이미지 업로드 // storage
                if(store_name.getText().toString().equals("") || where.getText().toString().equals("")){
                    CustomDialog customDialog = new CustomDialog();
                    mLayoutInflater = getLayoutInflater();
                    customDialog.getInstance(SubmitStoreActivity.this, mLayoutInflater, R.layout.submit_dialog);
                    customDialog.show("매장명과 위치를 입력해주세요.", "확인");
                    customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                }
                else{
                    SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
                    String uid = preferences.getString("uid", null); ;
                    Store store = new Store(store_name.getText().toString(), where.getText().toString(), comment.getText().toString());
                    DatabaseReference mConditionRef = mdatabase.child("/stores/" + uid);
                    mConditionRef.child("name").setValue(store_name.getText().toString());
                    mConditionRef.child("local").setValue(where.getText().toString());
                    mConditionRef.child("comment").setValue(comment.getText().toString());

                    StorageReference logoRef = storageReference.child(mAuth.getCurrentUser().getUid() + "/store/logo.jpg");
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
                            CustomDialog customDialog = new CustomDialog();
                            mLayoutInflater = getLayoutInflater();
                            customDialog.getInstance(SubmitStoreActivity.this, mLayoutInflater, R.layout.submit_dialog);
                            customDialog.show("등록이 완료되었습니다.", "확인");
                            customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

                        }
                    });
                }

            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setBackgroundColor(Color.parseColor("#000000"));
            imageView.setImageBitmap(bm);
            Uri selectedImageUri = data.getData();
            getPath(selectedImageUri);
        }
    }
    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor =getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }
}
