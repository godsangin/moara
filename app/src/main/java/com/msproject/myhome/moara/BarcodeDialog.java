package com.msproject.myhome.moara;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
public class BarcodeDialog extends AppCompatActivity {
    ImageView barcodeImage;
    String barcodeString;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View dialogView;

    public AlertDialog getInstance(Context context, LayoutInflater inflater, int layout) {
        dialogView = inflater.inflate(layout, null);

        builder = new AlertDialog.Builder(context);

        barcodeImage =(ImageView) dialogView.findViewById(R.id.BarcodeImage);

        DatabaseReference mdataReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference mConditionRef = mdataReference.child("users/" + MainActivity.uid);

        mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String barcodString = dataSnapshot.child("tel").getValue().toString();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    for(DataSnapshot s : snapshot.getChildren()){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        builder.setView(dialogView);


        Bitmap barcode = createBarcode("1234567890123ABCDEFGHIJKLM");
        barcodeImage.setImageBitmap(barcode);
        barcodeImage.invalidate();

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_dialog);


        //DB에서 유저정보,STROE정보,ITEM정보 가져오자.

    }

    public Bitmap createBarcode(String code){

        Bitmap bitmap =null;
        MultiFormatWriter gen = new MultiFormatWriter();
        try {
            final int WIDTH = 840;
            final int HEIGHT = 160;
            BitMatrix bytemap = gen.encode(code, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0 ; i < WIDTH ; ++i)
                for (int j = 0 ; j < HEIGHT ; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}