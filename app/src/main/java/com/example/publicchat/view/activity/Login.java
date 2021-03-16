package com.example.publicchat.view.activity;

import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.publicchat.R;
import com.example.publicchat.viewmodel.ContinueGoogleViewModel;
import com.example.publicchat.viewmodel.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText inputEmail;
    private EditText inputPassword;
    private ImageButton showPasswordBtn;
    private Button loginBtn;
    private TextView continueGoogleTv;

    private LoginViewModel loginViewModel;
    private ContinueGoogleViewModel continueGoogleViewModel;

    private boolean isClicked = false;
    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.login_email_progress_bar);
        inputEmail = findViewById(R.id.input_login_email);
        inputPassword = findViewById(R.id.input_login_password);
        showPasswordBtn = findViewById(R.id.show_password_login_btn);
        loginBtn = findViewById(R.id.login_btn);
        continueGoogleTv = findViewById(R.id.google_account_tv);

        progressBar.setVisibility(View.GONE);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        continueGoogleViewModel = new ViewModelProvider(this).get(ContinueGoogleViewModel.class);

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

        //When loginBtn is clicked
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty())
                {
                    loginViewModel.loginEmail(email, password);

                    //Observe userLoginEmailLiveData
                    loginViewModel.getUserLoginEmailLiveData().observe(Login.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean userLoginEmail) {
                            if(userLoginEmail)
                            {
                                Log.d("User Login", "Completed");
                                Toast.makeText(getApplicationContext(), "Berhasil masuk", Toast.LENGTH_SHORT).show();

                                //Go to Home
                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(homeIntent);
                            } else
                            {
                                Log.d("User Login", "Failed");
                                Toast.makeText(getApplicationContext(), "Gagal masuk", Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        //When continueGoogleTv is clicked
        continueGoogleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                continueGoogleViewModel.continueWithGoogleIntent(Login.this);

                //Observe googleIntentLiveData
                continueGoogleViewModel.getGoogleIntentLiveData().observe(Login.this, new Observer<Intent>() {
                    @Override
                    public void onChanged(Intent userContinueGoogle) {
                        startActivityForResult(userContinueGoogle, RC_SIGN_IN);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            progressBar.setVisibility(View.INVISIBLE);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try
            {
                //Google Sign In was successful, auth with Firebase
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                continueGoogleViewModel.continueWithGoogle(googleSignInAccount.getIdToken());

                //Observe userContinueGoogleLiveData
                continueGoogleViewModel.getUserContinueGoogleLiveData().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean userContinueGoogle) {
                        if(userContinueGoogle)
                        {
                            Log.d("User SignUp", "Completed");
                            Toast.makeText(getApplicationContext(), "Berhasil masuk", Toast.LENGTH_SHORT).show();

                            //Go to Home
                            Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(homeIntent);
                        } else
                        {
                            Log.d("User SignUp", "Failed");
                            Toast.makeText(getApplicationContext(), "Gagal masuk", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch(ApiException ex)
            {
                Log.w("User SignUp", ex);
                Toast.makeText(getApplicationContext(), "Gagal masuk", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
