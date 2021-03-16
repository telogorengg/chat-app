package com.example.publicchat.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.publicchat.R;

public class WelcomePage extends AppCompatActivity {

    private Button registerBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        registerBtn = findViewById(R.id.signup_home_btn);
        loginBtn = findViewById(R.id.login_home_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(signupIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        finishAffinity();
    }
}
