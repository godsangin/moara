package com.msproject.myhome.moara;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;
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

        actionBar = this.getSupportActionBar();
        actionBar.show();

        //actionBar.setLogo(R.drawable.ic_lightbulb_outline_black_24dp)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.AlarmButton){
            Intent intent = new Intent(MainActivity.this,AlarmActivity.class);
            startActivityForResult(intent,1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            String code = data.getStringExtra("code");
            Toast.makeText(MainActivity.this,code,Toast.LENGTH_LONG).show();
        }
    }
}
