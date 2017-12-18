package com.msproject.myhome.moara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText user_id;
    EditText user_password;
    EditText user_name;
    EditText user_tel;
    Spinner user_type;
    Button button;
    FirebaseAuth mAuth;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public String[] typeList = {"user", "store"};
    public String type = "";
    //String[] emailList = {"@naver.com", "daum.net","gmail.com","직접입력"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user_id = (EditText)findViewById(R.id.user_id);
        user_password = (EditText)findViewById(R.id.user_password);
        user_name = (EditText)findViewById(R.id.user_name);
        user_tel = (EditText)findViewById(R.id.user_tel);
        user_type = (Spinner) findViewById(R.id.user_type);

        mContext = this;
        mLayoutInflater = getLayoutInflater();

        user_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput(user_id) && checkInput(user_password) && checkInput(user_name) && checkInput(user_tel) && checkEmail(user_id)){
                    final String id = user_id.getText().toString();
                    final String password = user_password.getText().toString();
                    final String name = user_name.getText().toString();
                    final String tel = user_tel.getText().toString();
                    Log.d("type==", type);

                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                String uid = user.getUid();
                                User newUser;
//                                String userEmail = emailText.getText().toString();
//                                String userPassword = passwordText.getText().toString();
//                                String userTel = phoneText.getText().toString();
//                                String userName = nameText.getText().toString();

                                Map<String, String> userValues;
                                Map<String, Object> childUpdates = new HashMap<String, Object>();

                                newUser = new User(id, password, name, tel, type);
                                userValues = newUser.toMap();
                                childUpdates.put(uid, userValues);
                                mDatabase.updateChildren(childUpdates);

//                                if(isProviderChecked) { // provider
//
//                                    newUser = new User(userEmail, userPassword, userName, userTel, userPlace);
//                                    userValues = newUser.toMap();
//                                    childUpdates.put("/provider/" + uid, userValues);
//                                } else { // consumer
//                                    newUser = new User(userEmail, userPassword, userName, userTel, null);
//                                    userValues = newUser.toMap();
//                                    childUpdates.put("/consumer/" + uid, userValues);
//                                }
//                                mDatabase.updateChildren(childUpdates);

                                SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("uid", uid);
                                editor.putString("id", id);
                                editor.putString("name", name);
                                editor.putString("tel", tel);
                                editor.putString("type", type);
//                                if (isProviderChecked) {
//                                    editor.putBoolean("place", true);
//                                }
                                editor.commit();


                                final CustomDialog customDialog = new CustomDialog();
                                customDialog.getInstance(mContext, mLayoutInflater, R.layout.submit_dialog);
                                customDialog.show("회원가입이 완료되었습니다.", "확인");
                                customDialog.dialogButton1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    });

                } else { // password incorrect
                   // Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private boolean checkInput(EditText editText){
        String str = editText.getText().toString();
        if(str.equals("")){
            return false;
        }
        return true;
    }
    private boolean checkEmail(EditText editText){
        String str = editText.getText().toString();
        char[] email;
        email = new char[30];
        email = str.toCharArray();
        for (int i=0; i<email.length;i++){
            if(email[i]=='@')
                return true;
        }
        Toast.makeText(getApplicationContext(), "ID를 email형식으로 갖춰주십시오.", Toast.LENGTH_SHORT).show();
        return false;
    }
}
