package com.example.proiggimenez.firebaseauthenticationtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = "FirebaseAuthTest";

    private TextView textMessage;
    private Button multiPurposeButton;
    private EditText editTextName;

    private Mode currentMode;

    private Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMessage = findViewById(R.id.textMessage);
        multiPurposeButton = findViewById(R.id.buttonSignIn);
        editTextName = findViewById(R.id.editTextName);

        multiPurposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMode.equals(Mode.SIGN_IN))
                    presenter.signInUser();
                else
                    presenter.changeUserName(editTextName.getText().toString());
            }
        });

        presenter = new Presenter(this, this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        presenter.loadCurrentUser();
    }

    @Override
    public void updateUI(FirebaseUser currentUser) {
        if(currentUser == null) {
            textMessage.setText(R.string.user_not_logged);
            editTextName.setVisibility(View.INVISIBLE);
            multiPurposeButton.setText(R.string.sign_in);
            currentMode = Mode.SIGN_IN;
        } else {
            textMessage.setText(String.format("%s %s", getString(R.string.user_logged), currentUser.getDisplayName()));
            editTextName.setVisibility(View.VISIBLE);
            multiPurposeButton.setText(R.string.set_name);
            currentMode = Mode.CHANGE_NAME;
        }
    }

    @Override
    public void notifyMessage(String message) {
        Toast.makeText(MainActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void log(String message) {
        Log.d(TAG, message);
    }

    @Override
    public void logWarn(String message, Exception e) {
        Log.w(TAG, message, e);
    }


    private enum Mode {
        SIGN_IN,
        CHANGE_NAME
    }
}
