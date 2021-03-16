package com.example.publicchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.objects.Messages;
import com.example.publicchat.model.repository.FirebaseRepository;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PublicChatViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LiveData<FirebaseRecyclerOptions<Messages>> recyclerOptionsLiveData;

    public PublicChatViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<FirebaseRecyclerOptions<Messages>> getRecyclerOptionsLiveData()
    {
        return recyclerOptionsLiveData;
    }

    public void setRecyclerOptions(LifecycleOwner owner)
    {
        recyclerOptionsLiveData = firebaseRepository.setRecyclerOptions(owner);
    }

    public void sendMessage(String messageUser, String messageText)
    {
        firebaseRepository.sendMessage(messageUser, messageText);
    }
}
