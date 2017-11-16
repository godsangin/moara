package com.msproject.myhome.moara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class AlarmActivity extends AppCompatActivity {
    Button button;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

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
    }
}
