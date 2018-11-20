package com.example.example.appfinalavanzadoandroid.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v7.widget.RecyclerView;

import com.example.example.appfinalavanzadoandroid.models.ImageFile;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public interface MainView {
    Context GetLayoutContext();
    void SetRecycleViewAdapter(ArrayList<ImageFile> imagesReference);
    void NotifyChange();
    void SetUserName(String userName);
    void SetProfilePicture(FirebaseUser loggedUser);
    void EnableProgressBar();
    void DisabledProgressBar();
}
