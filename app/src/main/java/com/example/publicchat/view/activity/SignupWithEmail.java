package com.example.publicchat.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.publicchat.R;
import com.example.publicchat.viewmodel.SignupEmailViewModel;

public class SignupWithEmail extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private ImageButton showPasswordBtn;
    private Button signupBtn;

    private SignupEmailViewModel signupEmailViewModel;

    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_email);

        progressBar = findViewById(R.id.signup_email_progress_bar);
        inputUsername = findViewById(R.id.input_username);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        showPasswordBtn = findViewById(R.id.show_password_btn);
        signupBtn = findViewById(R.id.signup_btn);

        progressBar.setVisibility(View.GONE);

        signupEmailViewModel = new ViewModelProvider(this).get(SignupEmailViewModel.class);

        //Show or hide password
        showPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked)
                {
                    //Show password
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPasswordBtn.setImageResource(R.drawable.icon_hide_pass);
                    isClicked = true;
                } else
                {
                    //Hide password
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPasswordBtn.setImageResource(R.drawable.icon_show_pass);
                    isClicked = false;
                }

                inputPassword.setSelection(inputPassword.getText().length());
            }
        });

        //When signUpBtn is clicked
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String username = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty())
                {
                    signupEmailViewModel.signUpEmail(username, email, password);

                    //Observe userSignupEmailLiveData
                    signupEmailViewModel.getUserSignupEmailLiveData().observe(SignupWithEmail.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean userSignUpEmail) {
                            if(userSignUpEmail)
                            {
                                Log.d("User SignUp", "Completed");
                                Toast.makeText(getApplicationContext(), "Berhasil mendaftar", Toast.LENGTH_SHORT).show();

                                //Go to Home
                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(homeIntent);
                            } else
                            {
                                Log.d("User SignUp", "Failed");
                                Toast.makeText(getApplicationContext(), "Gagal mendaftar", Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Isi data dengan lengkap!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
