package com.example.publicchat.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.publicchat.R;
import com.example.publicchat.model.objects.Messages;
import com.example.publicchat.viewmodel.HomeViewModel;
import com.example.publicchat.viewmodel.ProfileViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTv;
    private CarouselView carouselView;
    private ProgressBar progressBar;
    private CardView publicChatCard;
    private CardView profileCard;

    private HomeViewModel homeViewModel;
    private ProfileViewModel profileViewModel;

    private String usernameForSent;
    private int[] images = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTv = findViewById(R.id.username_tv);
        carouselView = findViewById(R.id.carousel_view);
        progressBar = findViewById(R.id.home_progress_bar);
        publicChatCard = findViewById(R.id.public_chat_card);
        profileCard = findViewById(R.id.profile_card);

        progressBar.setVisibility(View.VISIBLE);
        carouselView.setVisibility(View.INVISIBLE);
        publicChatCard.setVisibility(View.INVISIBLE);
        profileCard.setVisibility(View.INVISIBLE);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        homeViewModel.getUsername();

        //Observe usernameLiveData
        homeViewModel.getUsernameLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String username) {
                usernameTv.setText(username);
                usernameForSent = username;

                progressBar.setVisibility(View.INVISIBLE);
                carouselView.setVisibility(View.VISIBLE);
                publicChatCard.setVisibility(View.VISIBLE);
                profileCard.setVisibility(View.VISIBLE);

                Log.d("Username Finished", username);
            }
        });

        //Set carouselView
        carouselView.setPageCount(images.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
            }
        });

        //When publicChatBtn is clicked
        publicChatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent publicChatIntent = new Intent(getApplicationContext(), PublicChat.class);
                publicChatIntent.putExtra("Username", usernameForSent);
                startActivity(publicChatIntent);
            }
        });

        //When profileBtn is clicked
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                profileIntent.putExtra("Username", usernameForSent);
                startActivity(profileIntent);
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
