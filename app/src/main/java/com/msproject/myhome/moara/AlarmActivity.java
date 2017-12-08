package com.msproject.myhome.moara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

public class AlarmActivity extends AppCompatActivity {
    Button button;
    SeekBar seekBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        button = (Button) findViewById(R.id.ApplyButton);
        seekBar1=(SeekBar) findViewById(R.id.SeekBar1);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("code","Apply Success");
                setResult(1,intent);
                finish();
            }
        });

    }
}
