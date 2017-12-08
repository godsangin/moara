package com.msproject.myhome.moara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ModifyItem extends AppCompatActivity {
    ImageView imageView;
    EditText productName;
    EditText cost;
    Button submit;
    Button cancel;
    Button modify;
    Button modufy_cancel;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    int REQUEST_CODE_ADD = 100;
    int REQUEST_CODE_FAIL = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);


        imageView = (ImageView) findViewById(R.id.imageView);
        productName = (EditText) findViewById(R.id.productName);
        cost = (EditText) findViewById(R.id.cost);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cencel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*StorageReference logoRef = storageReference.child(mAuth.getCurrentUser().getUid() + "/logo.jpg");
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
                });*/
                Intent intent = new Intent();
                setResult(REQUEST_CODE_ADD,intent);
                finish();
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
}
