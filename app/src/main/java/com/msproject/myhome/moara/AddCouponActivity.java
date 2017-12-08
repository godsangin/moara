package com.msproject.myhome.moara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddCouponActivity extends AppCompatActivity {

    Button add, back;

    int REQUEST_CODE_ADD = 100;
    int REQUEST_CODE_FAIL = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        add = (Button) findViewById(R.id.add);
        back = (Button) findViewById(R.id.back);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(REQUEST_CODE_ADD,intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(REQUEST_CODE_FAIL,intent);
                finish();
            }
        });
    }


}
