package com.twulfz.ecualert.database;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.twulfz.ecualert.fragments.HomeFragment;

public class AuthManager {
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    public AuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerUser(String email, String password, final AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            callback.onSuccess(user);
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public void updateUserProfile(FirebaseUser user,String username, String urlPicture) {
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(Uri.parse(urlPicture))
                    .build();
            user.updateProfile(profileUpdates);
        }
    }

    public void loginUser(String email, String password, final AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(task.getResult().getUser());
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }

    public FirebaseUser getCurrentUser() {
        currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public void signOut() {
        mAuth.signOut();
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser authUser);
        void onFailure(Exception e);
    }

}
