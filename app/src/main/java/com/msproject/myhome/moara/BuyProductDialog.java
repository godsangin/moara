package com.msproject.myhome.moara;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.w3c.dom.Text;

import java.util.Calendar;

public class BuyProductDialog extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View dialogView;
    public ImageView imageView;
    public TextView productName;
    public TextView cost;
    public TextView comment;
    public Button buy;
    public Button gift;
    public LinearLayout giftLayout;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    public AlertDialog getInstance(Context context, LayoutInflater inflater, int layout) {
        dialogView = inflater.inflate(layout, null);

        builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    public void show(final Context context, final Product product) {

        imageView = (ImageView) dialogView.findViewById(R.id.imageView);
        productName = (TextView) dialogView.findViewById(R.id.productName);
        cost = (TextView)dialogView.findViewById(R.id.cost);
        comment = (TextView)dialogView.findViewById(R.id.comment);
        buy = (Button)dialogView.findViewById(R.id.buy);
        gift = (Button)dialogView.findViewById(R.id.gift);
        giftLayout = (LinearLayout) dialogView.findViewById(R.id.gift_layout);

        productName.setText(product.getName());
        cost.setText(product.getPrice());
        comment.setText(product.getComment());

        StorageReference islandRef = storageRef.child(product.getImageSrc());
        Glide.with(context).using(new FirebaseImageLoader()).load(islandRef).into(imageView);
        final String storeUid = product.getImageSrc().split("/")[0];

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상품구매 stamp차감
                DatabaseReference mdataReference = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference mConditionRef = mdataReference.child("users/" + MainActivity.uid);

                mConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int remain_num = Integer.parseInt(dataSnapshot.child("/stamps/" + storeUid +"/num").getValue().toString());
                        int product_price = Integer.parseInt(product.getPrice());
                        if(remain_num >= product_price) {
                            remain_num -= product_price;
                            Toast.makeText(context, "구매가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            mConditionRef.child("stamps/" + storeUid + "/num").setValue(remain_num);
                            String myName = dataSnapshot.child("name").getValue().toString();
                            Log.d("until", product.getUntil());
                            String uid_half = MainActivity.uid.substring(5);
                            long barcodeString = System.currentTimeMillis();
                            GiftItem giftItem = new GiftItem(product.getName(), product.getUntil(), myName, storeUid, String.valueOf(barcodeString) + uid_half);
                            Alarm alarm = new Alarm(false, true, false);
                            mConditionRef.child("alarm").setValue(alarm.toMap());
                            mConditionRef.child("giftitem/" + storeUid + "/" + product.getName()).setValue(giftItem.toMap());
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(context, "보유한 스탬프가 부족합니다.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //상품 선물 stamp차감
                giftLayout.setVisibility(View.VISIBLE);
                final EditText input = (EditText) dialogView.findViewById(R.id.gift_phone_number);
                Button verifyButton = (Button) dialogView.findViewById(R.id.gift_verify_bt);
                verifyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(input.getText().toString().equals("")) {
                            Toast.makeText(context, "전화번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference ref = database.getReference("users");

                            final String fromName = ref.child(MainActivity.uid).child("name").toString();

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean exist = false;
                                    String key = null;

                                    int remain_num = Integer.parseInt(dataSnapshot.child(MainActivity.uid + "/stamps/" + storeUid +"/num").getValue().toString());
                                    int product_price = Integer.parseInt(product.getPrice());
                                    if(remain_num >= product_price) {
                                        remain_num -= product_price;
                                        ref.child(MainActivity.uid + "/stamps/" + storeUid + "/num").setValue(remain_num);

                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String tel = snapshot.child("tel").getValue().toString() == null ? "" : snapshot.child("tel").getValue().toString();
                                            if(tel.equals(input.getText().toString())) {
                                                key = snapshot.getKey();
                                                exist = true;

                                                break;
                                            }
                                        }

                                        if(exist) {
                                            DatabaseReference alarm = database.getReference("users/" + key + "/alarm");
                                            Alarm alarm1 = new Alarm(true, false, false);
                                            alarm.setValue(alarm1);
                                            String from = dataSnapshot.child(MainActivity.uid + "/name").getValue().toString();
                                            DatabaseReference giftRef = database.getReference("users/" + key + "/giftitem/" + storeUid + "/");
                                            String barcode = String.valueOf(System.currentTimeMillis()) + MainActivity.uid.substring(5);
                                            giftRef.child(productName.getText().toString()).setValue(new GiftItem(product.getName(), product.getUntil(), from, storeUid, barcode));

                                            dialog.dismiss();
                                            Toast.makeText(context, "선물이 완료되었습니다.", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "회원이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    else {
                                        Toast.makeText(context, "보유한 스탬프가 부족합니다.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }



    public void dismiss() {
        dialog.dismiss();
    }


}