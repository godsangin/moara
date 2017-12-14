package com.msproject.myhome.moara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingAlarmActivity extends AppCompatActivity {
    Switch aSwitch1;
    Switch aSwitch2;
    Switch aSwitch3;
    Switch aSwitch4;
    Switch aSwitch5;
    Button button;
    final int SETTING_OK = 2;

    //선언
    //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        aSwitch1 = (Switch) findViewById(R.id.Switch1);
        aSwitch2 = (Switch) findViewById(R.id.Switch2);
        aSwitch3 = (Switch) findViewById(R.id.Switch3);
        aSwitch4 = (Switch) findViewById(R.id.Switch4);
        aSwitch5 = (Switch) findViewById(R.id.Switch5);

        //데이터 호출
        //String callValue = pref.getString("key","defaultvalue");


        button = (Button) findViewById(R.id.ApplyButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingFragment.class); //->수정필요
                intent.putExtra("code","Apply Success");
                setResult(SETTING_OK,intent);
                if(aSwitch1.isChecked()){// 받은 선물에 대한 알림(소켓통신)
                }
                if(aSwitch2.isChecked()){ // 쿠폰 사용에 대한 알림 (소켓통신)
                }
                if(aSwitch3.isChecked()){ // 스탬프 적립시 알림 (소켓통신)

                }if(aSwitch4.isChecked()){ // 아이템 만료 기간 알림

                }if(aSwitch5.isChecked()){ // 구매가능한 상품 알림

                }
                //데이터저장
             /*   SharedPreferences.Editor editor = pref.edit();
                editor.putString("key","value");
                editor.commit();
             */   finish();
            }
        });
    }

}
