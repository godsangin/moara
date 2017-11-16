package com.msproject.myhome.moara;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_main:
                    transaction.replace(R.id.content, MainFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_myCoupon:
                    transaction.replace(R.id.content, MyCouponFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_myItem:
                    transaction.replace(R.id.content, MyItemFragment.newInstance()).commit();
                    return true;
                case R.id.navigation_setting:
                    transaction.replace(R.id.content,SettingFragment.newInstance()).commit();
                    return true;
                default:
                    break;

            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content,MainFragment.newInstance());
        transaction.commit();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
