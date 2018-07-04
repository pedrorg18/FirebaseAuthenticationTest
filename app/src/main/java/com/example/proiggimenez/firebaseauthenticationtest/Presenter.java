package com.example.proiggimenez.firebaseauthenticationtest;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Presenter {

    private static final String TAG = "FirebaseAuthTest";

    private FirebaseAuth mAuth;
    private MainView view;
    private Activity activity;
    FirebaseUser currentUser;

    public Presenter(MainView view, Activity activity) {
        this.view = view;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public void loadCurrentUser() {
        currentUser = mAuth.getCurrentUser();
        view.updateUI(currentUser);
    }

    void signInUser() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            view.log("signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            view.updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            view.logWarn("signInAnonymously:failure", task.getException());
                            view.notifyMessage("Authentication failed.");
                            view.updateUI(null);
                        }
                    }
                });
    }

    void changeUserName(String userName) {
        Log.d(TAG, "changeUserName");
        final FirebaseUser currentUser = mAuth.getCurrentUser();
//        currentUser.updateEmail(userName);

        UserProfileChangeRequest userProfileChangeRequest
                = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
        Task<Void> voidTask = currentUser.updateProfile(userProfileChangeRequest);
        voidTask.addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                view.log("Firebase Name change Completed");
            }
        });

        voidTask.addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                view.notifyMessage("Firebase Name changed successfully");
                view.log("Firebase Name changed successfully");
                view.updateUI(currentUser);
            }
        });

        voidTask.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.notifyMessage("Firebase Name changed successfully");
                view.log("Firebase Name changed successfully");
                e.printStackTrace();
            }
        });
    }

}
