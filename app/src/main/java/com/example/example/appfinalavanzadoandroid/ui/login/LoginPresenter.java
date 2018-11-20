package com.example.example.appfinalavanzadoandroid.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.example.appfinalavanzadoandroid.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class LoginPresenter {
    LoginView mView;

    public LoginPresenter(LoginView view) {
        this.mView = view;
    }
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(mView.GetLayoutContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent mainIntent = new Intent(mView.GetLayoutContext(), MainActivity.class);
                            mView.startActivity(mainIntent);
                            mView.finish();
                        }
                    }
                });
    }


}
