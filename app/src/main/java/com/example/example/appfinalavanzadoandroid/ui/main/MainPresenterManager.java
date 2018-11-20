package com.example.example.appfinalavanzadoandroid.ui.main;

import com.google.firebase.auth.FirebaseUser;

public interface MainPresenterManager {
    void InitializeProfile(final FirebaseUser loggedUser);
    int getArrayDataLength();
}
