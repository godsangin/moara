package com.msproject.myhome.moara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String uid;
    public static FragmentTransaction transaction;

    public static MenuItem second;
    public static MenuItem third;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            transaction = getSupportFragmentManager().beginTransaction();
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
                    transaction.replace(R.id.content, SettingFragment.newInstance()).commit();
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

        SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
        String id = preferences.getString("id", null);
        uid = preferences.getString("uid", null);
        String user_type = preferences.getString("type", null);


        if(id == null) {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("email==", id);
            if(user_type.equals("store")){
                BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navigation);
                Menu menu = navigationView.getMenu();
                MenuItem first = menu.findItem(R.id.navigation_main);
                first.setTitle("적립");

                //MenuItem second = menu.findItem(R.id.navigation_myCoupon);
                second = menu.findItem(R.id.navigation_myCoupon);
                second.setTitle("사용");

                //MenuItem third = menu.findItem(R.id.nuseravigation_myItem);
                third = menu.findItem(R.id.navigation_myItem);
                third.setTitle("상품");

                MenuItem forth = menu.findItem(R.id.navigation_setting);
                forth.setTitle("설정");

                navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        return false;
                    }
                });

            navigationView.setSelectedItemId(R.id.navigation_myItem);
            }
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content,MainFragment.newInstance());
        transaction.commit();

        actionBar = this.getSupportActionBar();
        actionBar.show();
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
