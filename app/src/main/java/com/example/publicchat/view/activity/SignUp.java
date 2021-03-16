package com.example.publicchat.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.publicchat.R;
import com.example.publicchat.viewmodel.ContinueGoogleViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignUp extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button continueWithGoogleBtn;
    private Button signupEmailBtn;

    private ContinueGoogleViewModel continueGoogleViewModel;

    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressBar = findViewById(R.id.signup_google_progress_bar);
        continueWithGoogleBtn = findViewById(R.id.continue_google_btn);
        signupEmailBtn = findViewById(R.id.signup_email_btn);

        progressBar.setVisibility(View.INVISIBLE);

        continueGoogleViewModel = new ViewModelProvider(this).get(ContinueGoogleViewModel.class);

        //When continueWithGoogleBtn is clicked
        continueWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                continueGoogleViewModel.continueWithGoogleIntent(SignUp.this);

                //Observe googleIntentLiveData
                continueGoogleViewModel.getGoogleIntentLiveData().observe(SignUp.this, new Observer<Intent>() {
                    @Override
                    public void onChanged(Intent googleIntent) {
                        startActivityForResult(googleIntent, RC_SIGN_IN);
                    }
                });
            }
        });

        //When signupEmailBtn is clicked
        signupEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupEmailIntent = new Intent(getApplicationContext(), SignupWithEmail.class);
                startActivity(signupEmailIntent);
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
                            Toast.makeText(getApplicationContext(), "Berhasil mendaftar", Toast.LENGTH_SHORT).show();

                            //Go to Home
                            Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(homeIntent);
                        } else
                        {
                            Log.d("User SignUp", "Failed");
                            Toast.makeText(getApplicationContext(), "Gagal mendaftar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch(ApiException ex)
            {
                Log.w("User SignUp", ex);
                Toast.makeText(getApplicationContext(), "Gagal mendaftar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
