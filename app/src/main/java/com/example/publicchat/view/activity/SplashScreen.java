package com.example.publicchat.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.publicchat.R;
import com.example.publicchat.model.repository.FirebaseRepository;
import com.example.publicchat.viewmodel.SplashViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashScreen extends AppCompatActivity {

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Temporary
        /*AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });*/

        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        splashViewModel.checkUserAuth();

        //Observe userAuthLiveData
        splashViewModel.getUserAuthLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUserAuth) {
                Log.d("User Auth", String.valueOf(isUserAuth));

                if(isUserAuth)
                {
                    //Go to Home
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);
                } else
                {
                    Intent welcomeIntent = new Intent(getApplicationContext(), WelcomePage.class);
                    startActivity(welcomeIntent);
                }
            }
        });
    }
}
