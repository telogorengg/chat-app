package com.example.publicchat.model.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.publicchat.R;
import com.example.publicchat.model.objects.Messages;
import com.example.publicchat.model.objects.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseRepository {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;

    public FirebaseRepository()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
    }

    //Check User Authentication
    public MutableLiveData<Boolean> checkUserAuth()
    {
        MutableLiveData<Boolean> userAuthLiveData = new MutableLiveData<>();

        if(firebaseAuth.getCurrentUser() != null)
        {
            userAuthLiveData.setValue(true);
        } else
        {
            userAuthLiveData.setValue(false);
        }

        return userAuthLiveData;
    }

    //Continue with Google Account Intent
    public MutableLiveData<Intent> continueWithGoogleIntent(Context context)
    {
        MutableLiveData<Intent> googleIntentLiveData = new MutableLiveData<>();

        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Build a GoogleSignInClient with the options specified by gso
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();

        googleIntentLiveData.setValue(signInIntent);

        return googleIntentLiveData;
    }

    //Continue with Google Account
    public MutableLiveData<Boolean> continueWithGoogle(String idToken)
    {
        final MutableLiveData<Boolean> userContinueGoogleLiveData = new MutableLiveData<>();

        Log.d("GoogleSignIn", "Credential");
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("GoogleSignIn", "Completed");
                            userContinueGoogleLiveData.setValue(true);
                        } else
                        {
                            Log.d("GoogleSignIn", "Failed");
                            userContinueGoogleLiveData.setValue(false);
                        }
                    }
                });

        return userContinueGoogleLiveData;
    }

    //Signing up user with email
    public MutableLiveData<Boolean> signUpEmail(final String username, final String email, final String password)
    {
        final MutableLiveData<Boolean> userSignupEmailLiveData = new MutableLiveData<>();

        if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                User user = new User();
                                user.setUsername(username);
                                user.setEmail(email);
                                user.setPassword(password);

                                dbReference.child("users")
                                        .child(task.getResult().getUser().getUid())
                                        .push()
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                userSignupEmailLiveData.setValue(true);
                                            }
                                        });
                            } else {
                                Log.d("User SignUp", "Fail");
                                userSignupEmailLiveData.setValue(false);
                            }
                        }
                    });
        }

        return userSignupEmailLiveData;
    }

    //Logging in user with email
    public MutableLiveData<Boolean> loginEmail(final String email, final String password)
    {
        final MutableLiveData<Boolean> userLoginEmailLiveData = new MutableLiveData<>();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            userLoginEmailLiveData.setValue(true);
                        } else
                        {
                            userLoginEmailLiveData.setValue(false);
                        }
                    }
                });

        return userLoginEmailLiveData;
    }

    //Logging out user
    public MutableLiveData<Boolean> logoutUser(Context context)
    {
        final MutableLiveData<Boolean> logoutUserLiveData = new MutableLiveData<>();

        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            logoutUserLiveData.setValue(true);
                        } else
                        {
                            logoutUserLiveData.setValue(false);
                        }
                    }
                });

        return logoutUserLiveData;
    }

    //Get the username
    public MutableLiveData<String> getUsername()
    {
        final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();

        final ArrayList<User> userData = new ArrayList<>();

        dbReference.child("users")
                .child(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot userDataSnapshot : snapshot.getChildren())
                        {
                            User user = userDataSnapshot.getValue(User.class);
                            userData.add(user);
                        }

                        if(userData.size() > 0)
                        {
                            usernameLiveData.setValue(userData.get(0).getUsername());
                        } else
                        {
                            usernameLiveData.setValue(firebaseAuth.getCurrentUser().getDisplayName().substring(0, 3).toLowerCase());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return usernameLiveData;
    }

    //Get the user email
    public MutableLiveData<String> getUserEmail()
    {
        MutableLiveData<String> userEmailLiveData = new MutableLiveData<>();

        String userEmail = firebaseAuth.getCurrentUser().getEmail();

        Log.d("User Email", userEmail);

        userEmailLiveData.setValue(userEmail);

        return userEmailLiveData;
    }

    //Set the FirebaseRecyclerOptions for PublicChatAdapter
    public MutableLiveData<FirebaseRecyclerOptions<Messages>> setRecyclerOptions(LifecycleOwner owner)
    {
        MutableLiveData<FirebaseRecyclerOptions<Messages>> recyclerOptionsLiveData = new MutableLiveData<>();

        FirebaseRecyclerOptions<Messages> recyclerOptions = new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(dbReference.child("messages"), Messages.class)
                .setLifecycleOwner(owner)
                .build();

        Log.d("Recyler Options", "Completed");

        recyclerOptionsLiveData.setValue(recyclerOptions);

        return recyclerOptionsLiveData;
    }

    //Send message
    public void sendMessage(String messageUser, String messageText)
    {
        Messages messages = new Messages();
        messages.setMessageUser(messageUser);
        messages.setMessageText(messageText);

        dbReference.child("messages")
                .push()
                .setValue(messages)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Message", "Sent");
                    }
                });
    }
}
