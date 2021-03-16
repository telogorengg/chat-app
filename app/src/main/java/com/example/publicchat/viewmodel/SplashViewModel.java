package com.example.publicchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.repository.FirebaseRepository;

public class SplashViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LiveData<Boolean> userAuthLiveData;

    public SplashViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<Boolean> getUserAuthLiveData()
    {
        return userAuthLiveData;
    }

    //Check if user is authenticated or not
    public void checkUserAuth()
    {
        userAuthLiveData = firebaseRepository.checkUserAuth();
    }
}
