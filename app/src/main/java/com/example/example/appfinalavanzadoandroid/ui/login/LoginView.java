package com.example.example.appfinalavanzadoandroid.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public interface LoginView {
    void startActivity(Intent i);
    AppCompatActivity GetLayoutContext();
    void DismissAuthtask();
    void showProgress(boolean progress);
    void finish();
    Context GetAuthContext();
    void startActivityForResult(Intent i, int requestCode);
    String getString(int ResId);
}
