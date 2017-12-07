package com.msproject.myhome.moara;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SubmitStoreActivity extends AppCompatActivity {
    ImageView imageView;
    EditText store_name;
    EditText where;
    EditText comment;
    Button submit;
    Button cencel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_store);

        imageView = (ImageView) findViewById(R.id.imageView);
        store_name = (EditText) findViewById(R.id.storeName);
        where = (EditText) findViewById(R.id.where);
        comment = (EditText) findViewById(R.id.comment);
        submit = (Button) findViewById(R.id.submit);
        cencel = (Button) findViewById(R.id.cencel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//이미지 업로드

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//이미지 업로드 // storage
                if(store_name.getText().toString().equals("") || where.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());     // 여기서 this는 Activity의 this

// 여기서 부터는 알림창의 속성 설정
                    builder.setTitle("등록 오류")        // 제목 설정
                            .setMessage("매장명과 장소를 입력해주세요.")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                // 취소 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton){
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("확인", new DialogInterface.OnClickListener(){
                                // 취소 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton){
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();    // 알림창 띄우기

                }
                else{
                    SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
                    String uid = preferences.getString("uid", null); ;
                    Store store = new Store(store_name.getText().toString(), where.getText().toString(), comment.getText().toString());
                    DatabaseReference mConditionRef = mdatabase.child("/stores/" + uid);
                    mConditionRef.child("name").setValue(store_name.getText().toString());
                    mConditionRef.child("local").setValue(where.getText().toString());
                    mConditionRef.child("comment").setValue(comment.getText().toString());

                    StorageReference logoRef = storageReference.child(store_name.getText().toString() + "/" + mAuth.getCurrentUser().getUid() + "/logo.jpg");
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
                        }
                    });
                }

            }
        });
    }
}
