package com.example.on;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText etID, etPwd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //데이터베이스 authentication
        mAuth = FirebaseAuth.getInstance();

        //아이디, 비밀번호 받기
        etID = findViewById(R.id.etID);
        etPwd = findViewById(R.id.etPwd);
        progressBar = findViewById(R.id.progressBar);

        //로그인 버튼 클릭
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stPwd = etPwd.getText().toString();
                String stID = etID.getText().toString();

                if(stID.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(stPwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //진행바 동그라미 나타남.
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(stID, stPwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //진행바 동그라미 사라짐.
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String stID = user.getEmail();
                                    String stPwd = user.getDisplayName();
                                    Log.d(TAG, "stID: "+stID+", stPwd: "+stPwd);
                                    Intent in = new Intent(LoginActivity.this, CategoryActivity.class);
                                    //ChatActivity로 이메일 넘기기
                                    in.putExtra("email", stID);
                                    startActivity(in);
                        //            updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "로그인 실패",
                                            Toast.LENGTH_SHORT).show();
                          //          updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });


        //회원가입 버튼 클릭
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stPwd = etPwd.getText().toString();
                String stID = etID.getText().toString();
                Toast.makeText(LoginActivity.this, "Email: " + stID + ", password: " + stPwd, Toast.LENGTH_LONG).show();
                //아이디, 비밀번호 비었으면 토스트로 입력하라고 뜸.
                if (stID.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (stPwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                //회원가입
                mAuth.createUserWithEmailAndPassword(stID, stPwd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                   // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                               //     updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }

    @Override
    public void onStart () {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }
}
