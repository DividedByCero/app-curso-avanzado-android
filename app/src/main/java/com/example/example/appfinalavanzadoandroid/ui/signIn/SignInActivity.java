package com.example.example.appfinalavanzadoandroid.ui.signIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.example.appfinalavanzadoandroid.R;
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
            Intent returnIntent = new Intent();
            String test = mUserName.getText().toString();
            returnIntent.putExtra(LoginActivity.USER_NAME, mUserName.getText().toString());
            returnIntent.putExtra(LoginActivity.USER_EMAIL, mEmail.getText().toString());
            returnIntent.putExtra(LoginActivity.USER_PASSWORD, mPassword.getText().toString());
            setResult(LoginActivity.SIGN_IN_USER, returnIntent);
            finish();
        }
    }
}
