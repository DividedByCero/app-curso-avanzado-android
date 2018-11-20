package com.example.example.appfinalavanzadoandroid.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    //public static final int NO_USER = 0x5000;
    //public static final int LOGGED = 0x5001;
    //protected int mUserState = NO_USER;
    protected FirebaseUser mUser;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());
        Log.e("MyCustom", "12345678");

    }



    protected void SignIn(){
        //List<AuthUI.IdpConfig> providers = Arrays.asList(
        //        new AuthUI.IdpConfig.EmailBuilder().build(),
        //        new AuthUI.IdpConfig.GoogleBuilder().build()
        //);
        //startActivityForResult(
        //        AuthUI.getInstance()
        //                .createSignInIntentBuilder()
        //                .setAvailableProviders(providers)
        //                .build(),
        //        RC_SIGN_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_btn:
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions options = new  GoogleSignInOptions.Builder()
                        .requestEmail()
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build();

                GoogleSignIn.getClient(getApplicationContext(), options).signOut();
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
