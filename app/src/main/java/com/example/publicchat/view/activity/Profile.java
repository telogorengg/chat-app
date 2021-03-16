package com.example.publicchat.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.publicchat.R;
import com.example.publicchat.viewmodel.ProfileViewModel;

public class Profile extends AppCompatActivity {

    private Toolbar profileToolbar;
    private ProgressBar progressBar;
    private TextView usernameTv;
    private TextView emailTv;
    private Button logoutBtn;

    private ProfileViewModel profileViewModel;

    private String usernameFromHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileToolbar = findViewById(R.id.profile_toolbar);
        progressBar = findViewById(R.id.profile_progress_bar);
        usernameTv = findViewById(R.id.profile_username);
        emailTv = findViewById(R.id.profile_email);
        logoutBtn = findViewById(R.id.logout_btn);

        setSupportActionBar(profileToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar.setVisibility(View.VISIBLE);
        usernameTv.setVisibility(View.INVISIBLE);
        emailTv.setVisibility(View.INVISIBLE);

        usernameFromHome = getIntent().getExtras().getString("Username");

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getEmailUser();

        //Observe emailUserLiveData
        profileViewModel.getEmailUserLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String userEmail) {
                progressBar.setVisibility(View.INVISIBLE);
                usernameTv.setVisibility(View.VISIBLE);
                emailTv.setVisibility(View.VISIBLE);

                usernameTv.setText(usernameFromHome);
                emailTv.setText(userEmail);
            }
        });

        //When logoutBtn is clicked
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                profileViewModel.logoutUser(Profile.this);

                //Observe logoutUserLiveData
                profileViewModel.getLogoutUserLiveData().observe(Profile.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean logoutUser) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if(logoutUser)
                        {
                            Toast.makeText(getApplicationContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show();

                            Intent welcomePageIntent = new Intent(getApplicationContext(), WelcomePage.class);
                            startActivity(welcomePageIntent);
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "Gagal keluar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
