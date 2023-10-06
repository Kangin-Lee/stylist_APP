package com.example.stylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private Button loginSubmit;
    private TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginSubmit = findViewById(R.id.loginSubmit);
        register = findViewById(R.id.register);

        //메인 페이지로 넘어가기------------------------------------------------------------------
        loginSubmit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
        });

        //회원가입 페이지로 넘어가기---------------------------------------------------------------
        register.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        });
    }
}