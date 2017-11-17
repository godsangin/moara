package com.msproject.myhome.moara;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText user_id;
    EditText user_password;
    Button login_button;
    Button signup_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_id = (EditText)findViewById(R.id.user_id);
        user_password = (EditText)findViewById((R.id.user_password));

        login_button = (Button)findViewById(R.id.login_button);
        signup_button = (Button)findViewById(R.id.signup_button);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();//뭐가끝날까
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user_id.getText().toString().equals("") && !user_password.getText().toString().equals("")) {
                    mAuth.signInWithEmailAndPassword(user_id.getText().toString(), user_password.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String uid = mAuth.getCurrentUser().getUid();
                                Log.d("Uid==", uid);
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users/" + uid+  "/");
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.getValue() == null) {

                                        } else {
                                            User userData = null;
                                            String userKey = "";


                                            userData = dataSnapshot.getValue(User.class);
//                                                Log.d("snapshot==", snapshot.getValue().toString());
//                                                userData = new User(snapshot.child("id").getValue().toString(), snapshot.child("password").getValue().toString(), snapshot.child("name").getValue().toString(), snapshot.child("tel").getValue().toString());



                                            SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("id", userData.id);
                                            editor.putString("name", userData.name);
                                            editor.putString("tel", userData.tel);

                                            editor.commit();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                };

                                mDatabase.addListenerForSingleValueEvent(valueEventListener);
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
