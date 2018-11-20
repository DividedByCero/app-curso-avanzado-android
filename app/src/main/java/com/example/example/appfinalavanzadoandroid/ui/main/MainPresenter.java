package com.example.example.appfinalavanzadoandroid.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.adapters.PostAdapter;
import com.example.example.appfinalavanzadoandroid.models.ImageFile;
import com.example.example.appfinalavanzadoandroid.models.Post;
import com.example.example.appfinalavanzadoandroid.models.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.Date;

public class MainPresenter implements MainPresenterManager {
    public static final String IMAGES_FOLDER = "Images";
    public static final String STORAGE_URL = "gs://proyectofinalavanzadoandroid.appspot.com";
    private MainView mView;
    private FirebaseStorage mStorage;
    protected DatabaseReference mUserdataRef;
    protected Usuario mUserData;

    private ArrayList<Post> mData;
    MainPresenter(MainView view) {
        mView = view;
    }


    public int getArrayDataLength(){
        return mData.size();
    }

    public PostAdapter GetPostAdapter(ArrayList<ImageFile> imageFiles) {
        mStorage = FirebaseStorage.getInstance(STORAGE_URL);
        mData = new ArrayList<>();
        QueuePostsData(imageFiles, mData);
        return new PostAdapter(mView.GetLayoutContext(), R.layout.post_item, mData);
    }

    private void QueuePostsData(ArrayList<ImageFile> imageFiles, final ArrayList<Post> posts) {
        final StorageReference ref = mStorage.getReference().child(IMAGES_FOLDER);
        for (ImageFile file : imageFiles) {
            if (file == null)
                continue;

            final StorageReference refChild = ref.child(file.file);
            final Post post = new Post();
            post.author = file.author;

            if(file.location != null){
                post.setLocation(file.location);
            }

            if(file.title != null){
                post.setTitle(file.title);
            }

            refChild.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    refChild.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            post.setImage(bmp);
                            mView.NotifyChange();
                        }
                    });

                    refChild.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            post.setDownloadUrl(uri);
                        }
                    });

                    post.setFileName(storageMetadata.getName());
                    post.setDate(new Date(storageMetadata.getCreationTimeMillis()));
                    posts.add(post);
                    mView.NotifyChange();
                }
            });
        }
    }

    public void InitializeProfile(final FirebaseUser loggedUser){
        if(loggedUser != null){
            // mUserUID = mUser.getUid();
            mUserdataRef = FirebaseDatabase.getInstance().getReference("Images");
            mUserdataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<ImageFile>> generic = new GenericTypeIndicator<ArrayList<ImageFile>>() {};

                    if(dataSnapshot.getValue() != null){
                        ArrayList<ImageFile> _values = dataSnapshot.getValue(generic);
                        mView.SetRecycleViewAdapter(_values);
                    }
                    else{
                        ArrayList<ImageFile> _values = new ArrayList<>();
                        mView.SetRecycleViewAdapter(_values);
                    }
                    mUserData = new Usuario(loggedUser.getDisplayName());
                    mView.SetUserName(mUserData.Uid);
                    mView.SetProfilePicture(loggedUser);

                    mView.DisabledProgressBar();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseDatabase", databaseError.getMessage());
                }
            });
        }

    }

}
