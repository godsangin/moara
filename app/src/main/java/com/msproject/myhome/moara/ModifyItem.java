package com.msproject.myhome.moara;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ModifyItem extends AppCompatActivity {
    ImageView imageView;
    EditText productName;
    EditText cost;
    EditText comment;
    Button submit;
    Button cancel;
    Button modify;
    Button modufy_cancel;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    LayoutInflater mLayoutInflater;
    Context mContext;

    int REQUEST_CODE_ADD = 100;
    int REQUEST_CODE_FAIL = 101;
    final int SELECT_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);


        imageView = (ImageView) findViewById(R.id.imageView);
        productName = (EditText) findViewById(R.id.productName);
        cost = (EditText) findViewById(R.id.cost);
        comment = (EditText)findViewById(R.id.comment);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cencel);
        mLayoutInflater = getLayoutInflater();
        mContext = this;

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
            public void onClick(View v) {
                if(productName.getText().toString().equals("") || cost.getText().toString().equals("")){
                    final CustomDialog customDialog = new CustomDialog();
                    mLayoutInflater = getLayoutInflater();
                    customDialog.getInstance(getApplicationContext(), mLayoutInflater, R.layout.submit_dialog);
                    customDialog.show("상품명과 비용을 입력해주세요.", "확인");
                    customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customDialog.dismiss();
                        }
                    });

                }
                else{
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("Account", MODE_PRIVATE);
                    String uid = preferences.getString("uid", null); ;
                    Store store = new Store(productName.getText().toString(), cost.getText().toString(), comment.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    int year = calendar.get(Calendar.YEAR);
                    calendar.set(year+1, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                    String date = "~" + dateFormat.format(calendar.getTime());
                    DatabaseReference mConditionRef = mdatabase.child("/stores/" + uid + "/products/" + productName.getText().toString());
                    mConditionRef.child("name").setValue(productName.getText().toString());
                    mConditionRef.child("price").setValue(cost.getText().toString());
                    mConditionRef.child("comment").setValue(comment.getText().toString());
                    mConditionRef.child("until").setValue(date);

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

                            customDialog.getInstance(mContext, mLayoutInflater, R.layout.submit_dialog);
                            customDialog.show("등록이 완료되었습니다.", "확인");
                            customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialog.dismiss();
                                    Intent intent = new Intent();
                                    setResult(REQUEST_CODE_ADD,intent);
                                    finish();
                                }
                            });

                        }
                    });
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(REQUEST_CODE_FAIL,intent);
                finish();
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
