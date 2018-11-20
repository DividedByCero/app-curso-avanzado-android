package com.example.example.appfinalavanzadoandroid.ui.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class LoginPresenter {
    private LoginView mLoginVIew;
    private GoogleSignInClient mGoogleClient;

    public LoginPresenter(LoginView view) {
        this.mLoginVIew = view;
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(mLoginVIew.GetLayoutContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent mainIntent = new Intent(mLoginVIew.GetLayoutContext(), MainActivity.class);
                            mLoginVIew.startActivity(mainIntent);
                            mLoginVIew.finish();
                        }
                    }
                });
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mUserName;
        private final View mView;
        private final EditText mPasswordView;

        UserLoginTask(String email, String password, String userName, View view, EditText passwordView) {
            mEmail = email;
            mPassword = password;
            mView = view;
            mPasswordView = passwordView;
            mUserName = userName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            if (mView.getId() == R.id.email_log_in_button) {
                auth.signInWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent mainIntent = new Intent(mLoginVIew.GetAuthContext(), MainActivity.class);
                        mLoginVIew.startActivity(mainIntent);
                        mLoginVIew.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // mPasswordView.setError(getString(R.string.user_does_not_exist));
                        Toast.makeText(mLoginVIew.GetLayoutContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
                return true;
            } else if (mView.getId() == R.id.email_sign_in_button) {
                auth.createUserWithEmailAndPassword(mEmail, mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mUserName)
                                .build();
                        authResult.getUser().updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent mainIntent = new Intent(mLoginVIew.GetAuthContext(), MainActivity.class);
                                mLoginVIew.startActivity(mainIntent);
                                mLoginVIew.finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mLoginVIew.GetLayoutContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            } else if (mView.getId() == R.id.google_sign_in) {
                GoogleSignInOptions options = new GoogleSignInOptions.Builder()
                        .requestEmail()
                        .requestIdToken(mLoginVIew.getString(R.string.default_web_client_id)).build();

                mGoogleClient = GoogleSignIn.getClient(mLoginVIew.GetAuthContext(), options);
                Intent googleSign = mGoogleClient.getSignInIntent();
                mLoginVIew.startActivityForResult(googleSign, LoginActivity.RC_SIGN_IN);
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoginVIew.DismissAuthtask();
            mLoginVIew.showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mLoginVIew.DismissAuthtask();
            mLoginVIew.showProgress(false);
        }
    }

    public UserLoginTask GetUserTaskInstance(String email, String password, String userName, View view, EditText passwordView){
        return new UserLoginTask(email, password,userName, view, passwordView);
    }


}
