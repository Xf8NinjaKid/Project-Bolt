package com.ivy.green.bolt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void user(View view){
        Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
        startActivity(intent);
    }

    public void provider(View view){
        Intent intent = new Intent(RegisterActivity.this, CompanyActivity.class);
        startActivity(intent);

    }

    public void login(View view){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void back(View view){
        Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }
}
