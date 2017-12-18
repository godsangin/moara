package com.msproject.myhome.moara;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmActivity extends AppCompatActivity {
    Button alert;
    Button button;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alert = (Button)findViewById(R.id.alert);

        button = (Button) findViewById(R.id.ApplyButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this,MainActivity.class);
                intent.putExtra("code","Apply Success");
                setResult(1,intent);
                finish();
            }
        });
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Alarm alarm = new Alarm(false, false, false);
                mDatabase.child("users/" + MainActivity.uid + "/alarm").setValue(alarm.toMap());
                Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AlarmActivity.this, MyService.class);
                startService(intent);


//                NotificationSomethings();
            }
        });
    }


}
