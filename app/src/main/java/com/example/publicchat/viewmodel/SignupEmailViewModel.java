package com.example.publicchat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.publicchat.model.repository.FirebaseRepository;

public class SignupEmailViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LiveData<Boolean> userSignupEmailLiveData;

    public SignupEmailViewModel(@NonNull Application application) {
        super(application);

        firebaseRepository = new FirebaseRepository();
    }

    public LiveData<Boolean> getUserSignupEmailLiveData()
    {
        return userSignupEmailLiveData;
    }

    //Signing Up user
    public void signUpEmail(String username, String email, String password)
    {
        userSignupEmailLiveData = firebaseRepository.signUpEmail(username, email, password);
    }
}
