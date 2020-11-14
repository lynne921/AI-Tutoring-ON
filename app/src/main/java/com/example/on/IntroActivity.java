package com.example.on;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(()->{
            Intent intent = new Intent(com.example.on.IntroActivity.this, com.example.on.LoginActivity.class);//main액티비티로 넘어감.
            startActivity(intent);
            finish();
        }, 3000);
    }
}