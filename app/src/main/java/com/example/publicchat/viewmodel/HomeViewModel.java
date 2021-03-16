package com.example.publicchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.repository.FirebaseRepository;

public class HomeViewModel extends AndroidViewModel {
    private LiveData<String> usernameLiveData;
    private FirebaseRepository firebaseRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<String> getUsernameLiveData()
    {
        return usernameLiveData;
    }

    public void getUsername()
    {
        usernameLiveData = firebaseRepository.getUsername();
    }
}
