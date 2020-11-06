package com.example.on;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}

//login-> (성공)채널 선택
//     -> (실패) 다시 login
//     -> (회원가입 클릭) 회원가입 창