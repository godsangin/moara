package com.msproject.myhome.moara;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sanginLee on 2017-12-15.
 */

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;
    DatabaseReference mDatamaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mDatamaseReference.child(MainActivity.uid + "/alarm");

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {}
        }
    }

}
