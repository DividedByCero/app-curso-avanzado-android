package com.example.example.appfinalavanzadoandroid.ui.signIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.helpers.UserHelpers;
import com.example.example.appfinalavanzadoandroid.ui.login.LoginActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSignInButton;
    private EditText mUserName, mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSignInButton = findViewById(R.id.sign_in_user_btn);
        mUserName = findViewById(R.id.sign_in_username);
        mEmail = findViewById(R.id.sign_in_email);
        mPassword = findViewById(R.id.sign_in_password);
        mSignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_in_user_btn){

            String password = mPassword.getText().toString();
            String userName = mUserName.getText().toString();
            String email = mEmail.getText().toString();

            if(email.isEmpty() || !UserHelpers.isEmailValid(email)){
                mEmail.setError(getString(R.string.error_field_required));
                mEmail.requestFocus();
                return;
            }

            if(password.isEmpty() || !UserHelpers.isPasswordValid(password)){
                mPassword.setError(getString(R.string.error_field_required));
                mPassword.requestFocus();
                return;
            }

            if(userName.isEmpty()){
                mUserName.setError(getString(R.string.error_field_required));
                mUserName.requestFocus();
                return;
            }

            Intent returnIntent = new Intent();
            String test = mUserName.getText().toString();
            returnIntent.putExtra(LoginActivity.USER_NAME, userName);
            returnIntent.putExtra(LoginActivity.USER_EMAIL, email);
            returnIntent.putExtra(LoginActivity.USER_PASSWORD, password);
            setResult(LoginActivity.SIGN_IN_USER, returnIntent);
            finish();
        }
    }
}
