package com.example.publicchat.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.publicchat.R;
import com.example.publicchat.model.objects.Messages;
import com.example.publicchat.view.adapter.PublicChatAdapter;
import com.example.publicchat.viewmodel.PublicChatViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PublicChat extends AppCompatActivity {

    private Toolbar publicChatToolbar;
    private ProgressBar progressBar;
    private RecyclerView publicChatRv;
    private EditText inputMessage;
    private FloatingActionButton sendFab;
    private LinearLayoutManager linearLayoutManager;

    private PublicChatViewModel publicChatViewModel;
    private PublicChatAdapter publicChatAdapter;

    private String usernameFromHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat);

        publicChatToolbar = findViewById(R.id.public_chat_toolbar);
        progressBar = findViewById(R.id.chat_progress_bar);
        publicChatRv = findViewById(R.id.public_chat_rv);
        inputMessage = findViewById(R.id.input_message);
        sendFab = findViewById(R.id.send_message_fab);

        setSupportActionBar(publicChatToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.public_chat));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar.setVisibility(View.VISIBLE);

        usernameFromHome = getIntent().getExtras().getString("Username");

        publicChatViewModel = new ViewModelProvider(this).get(PublicChatViewModel.class);

        publicChatViewModel.setRecyclerOptions(this);

        //Observe recyclerOptionsLiveData
        publicChatViewModel.getRecyclerOptionsLiveData().observe(this, new Observer<FirebaseRecyclerOptions<Messages>>() {
            @Override
            public void onChanged(FirebaseRecyclerOptions<Messages> recyclerOptions) {
                Log.d("Options", recyclerOptions.toString());

                publicChatAdapter = new PublicChatAdapter(recyclerOptions, usernameFromHome);
                publicChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);

                        publicChatRv.smoothScrollToPosition(publicChatAdapter.getItemCount() - 1);
                    }
                });

                linearLayoutManager = new LinearLayoutManager(PublicChat.this);
                publicChatRv.setLayoutManager(linearLayoutManager);
                publicChatRv.setAdapter(publicChatAdapter);

                progressBar.setVisibility(View.GONE);
            }
        });

        //When sendFab is clicked
        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = inputMessage.getText().toString();

                if(!messageText.isEmpty())
                {
                    publicChatViewModel.sendMessage(usernameFromHome, messageText);

                    inputMessage.setText("");
                }
            }
        });
    }
}
