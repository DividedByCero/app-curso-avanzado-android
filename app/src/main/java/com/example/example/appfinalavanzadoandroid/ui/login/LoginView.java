package com.example.example.appfinalavanzadoandroid.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public interface LoginView {
    void startActivity(Intent i);
    AppCompatActivity GetLayoutContext();
    void finish();
}
