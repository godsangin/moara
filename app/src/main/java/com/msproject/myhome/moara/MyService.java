package com.msproject.myhome.moara;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msproject.myhome.moara.Alarm;
import com.msproject.myhome.moara.MainActivity;
import com.msproject.myhome.moara.R;

import java.util.ArrayList;

public class MyService extends Service {
    NotificationManager Notifi_M;
    //ServiceThread thread;
    Notification Notifi ;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            databaseReference.child("users/" + MainActivity.uid).child("alarm").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Intent intent = new Intent(MyService.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    Alarm alarm = dataSnapshot.getValue(Alarm.class);
                    Log.d("alarm==", alarm.toString());
                    if(alarm.isBuy()){
                        databaseReference.child("users/" + MainActivity.uid).child("alarm").child("buy").setValue(false);
                        Notifi = new Notification.Builder(getApplicationContext())
                                .setContentTitle("구매")
                                .setContentText("구매가 완료되었습니다.")
                                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                                .setTicker("알림!!!")
                                .setContentIntent(pendingIntent)
                                .build();
                    }
                    else if(alarm.isGift()){
                        databaseReference.child("users/" + MainActivity.uid).child("alarm").child("gift").setValue(false);
                        Notifi = new Notification.Builder(getApplicationContext())
                                .setContentTitle("선물")
                                .setContentText("선물이 도착하었습니다.")
                                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                                .setTicker("알림!!!")
                                .setContentIntent(pendingIntent)
                                .build();
                    }
                    else if(alarm.isSave()){
                        databaseReference.child("users/" + MainActivity.uid).child("alarm").child("save").setValue(false);
                        Notifi = new Notification.Builder(getApplicationContext())
                                .setContentTitle("적립")
                                .setContentText("적립이 완료되었습니다.")
                                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                                .setTicker("알림!!!")
                                .setContentIntent(pendingIntent)
                                .build();
                    }
                    else{
                        return;
                    }

                    //소리추가
                    Notifi.defaults = Notification.DEFAULT_SOUND;
                    //알림 소리를 한번만 내도록
                    Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                    //확인하면 자동으로 알림이 제거 되도록
                    Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                    Notifi_M.notify(777 , Notifi);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {

    }

}