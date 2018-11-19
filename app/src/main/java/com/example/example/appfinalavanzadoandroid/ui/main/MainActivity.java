package com.example.example.appfinalavanzadoandroid.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.adapters.PostAdapter;
import com.example.example.appfinalavanzadoandroid.models.ImageFile;
import com.example.example.appfinalavanzadoandroid.models.Post;
import com.example.example.appfinalavanzadoandroid.models.Usuario;
import com.example.example.appfinalavanzadoandroid.ui.base.BaseActivity;
import com.example.example.appfinalavanzadoandroid.ui.submit.SubmitDialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {
    public static final int RC_SIGN_IN = 1;
    protected RecyclerView.Adapter mAdapter;

    MainPresenter mPresenter;
    RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.post_recycleView) RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this);
        SignIn();
    }

    public Context GetLayoutContext() {
        return MainActivity.this;
    }

    public void SetRecycleViewAdapter(ArrayList<ImageFile> imagesReference) {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(mLayoutManager);
        mAdapter = mPresenter.GetPostAdapter(imagesReference);
        mRecycleView.setAdapter(mAdapter);
    }

    public void NotifyChange(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            mPresenter.InitializeProfile(mUser);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void SetUserName(String userName){
        TextView userNameView = findViewById(R.id.user_name);
        userNameView.setText(userName);
    }

    public void SetProfilePicture(FirebaseUser loggedUser){
        ImageView profilePicture = findViewById(R.id.profile_picture);
        Glide.with(this).load(loggedUser.getPhotoUrl()).into(profilePicture);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                mPresenter.InitializeProfile(mUser);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.submit_pic_btn){
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                SubmitDialog dialog = new SubmitDialog();
                dialog.SetInterop(mPresenter);
                dialog.SetWorkingContext(this.getApplicationContext());
                dialog.show(fragmentManager, "submit_dialog");

        }
        return super.onOptionsItemSelected(item);
    }
}
