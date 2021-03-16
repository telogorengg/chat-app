package com.example.publicchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.repository.FirebaseRepository;

public class LoginViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LiveData<Boolean> userLoginEmailLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<Boolean> getUserLoginEmailLiveData()
    {
        return userLoginEmailLiveData;
    }

    public void loginEmail(String email, String password)
    {
        userLoginEmailLiveData = firebaseRepository.loginEmail(email, password);
    }
}
