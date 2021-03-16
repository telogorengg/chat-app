package com.example.publicchat.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.repository.FirebaseRepository;

public class ContinueGoogleViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LiveData<Intent> googleIntentLiveData;

    private LiveData<Boolean> userContinueGoogleLiveData;

    public ContinueGoogleViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<Intent> getGoogleIntentLiveData()
    {
        return googleIntentLiveData;
    }

    public LiveData<Boolean> getUserContinueGoogleLiveData() {
        return userContinueGoogleLiveData;
    }

    //Continue with Google Account Intent
    public void continueWithGoogleIntent(Context context)
    {
        googleIntentLiveData = firebaseRepository.continueWithGoogleIntent(context);
    }

    //Continue with Google Account
    public void continueWithGoogle(String idToken)
    {
        userContinueGoogleLiveData = firebaseRepository.continueWithGoogle(idToken);
    }
}
