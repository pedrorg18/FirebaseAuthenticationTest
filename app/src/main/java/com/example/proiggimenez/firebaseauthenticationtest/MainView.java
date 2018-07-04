package com.example.proiggimenez.firebaseauthenticationtest;

import com.google.firebase.auth.FirebaseUser;

interface MainView {

    void updateUI(FirebaseUser currentUser);

    void notifyMessage(String message);

    void log(String message);

    void logWarn(String message, Exception e);


}
