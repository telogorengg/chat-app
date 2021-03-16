package com.example.publicchat.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.repository.FirebaseRepository;

public class ProfileViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LiveData<String> emailUserLiveData;
    private LiveData<Boolean> logoutUserLiveData;

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<String> getEmailUserLiveData()
    {
        return emailUserLiveData;
    }

    public LiveData<Boolean> getLogoutUserLiveData()
    {
        return logoutUserLiveData;
    }

    public void getEmailUser()
    {
        emailUserLiveData = firebaseRepository.getUserEmail();
    }

    public void logoutUser(Context context)
    {
        logoutUserLiveData = firebaseRepository.logoutUser(context);
    }
}
