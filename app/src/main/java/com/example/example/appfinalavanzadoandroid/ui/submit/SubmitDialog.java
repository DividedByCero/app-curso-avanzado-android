package com.example.example.appfinalavanzadoandroid.ui.submit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.ui.main.DataInterop;
import com.example.example.appfinalavanzadoandroid.ui.main.MainView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class SubmitDialog  extends DialogFragment {
    public static final int PICK_IMAGE = 0x4343;
    private FirebaseUser mUser;
    private DataInterop mInterop;
    private Dialog mDialog;
    private View mView;
    private InputStream mInputStream;
    private StorageReference mStorageReference;
    private Context mWorkingContext;
    FirebaseStorage mStorage;

    public SubmitDialog() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void SetInterop(DataInterop interop){
        mInterop = interop;
    }

    public void SetWorkingContext(Context workingContext){
        mWorkingContext = workingContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        Activity activity = getActivity();
        mView = activity.getLayoutInflater().inflate(R.layout.submit_dialog, null);
        dialog.setTitle("Submit Image");
        dialog.setView(mView);
        final Button submitBtn = mView.findViewById(R.id.submit_btn_dialog);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        dialog.setPositiveButton("Subir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  uploadImage(mInputStream, mStorageReference);

            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return dialog.create();

    }

    public void uploadImage(InputStream stream, StorageReference storageReference) {

        if(stream != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Images/"+ UUID.randomUUID().toString());
            ref.putStream(stream)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            final String Title = ((EditText)mView.findViewById(R.id.title_submit_dialog)).getText().toString();
                            progressDialog.dismiss();
                            final FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference ref = db.getReference("Images");
                            int position = mInterop.getArrayDataLength();
                            final DatabaseReference subRef = ref.child(Integer.toString(position));
                            subRef.setValue("");
                            db.getReference("Images/" + subRef.getKey() + "/author").setValue(mUser.getDisplayName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.getReference("Images/" +  subRef.getKey() + "/file").setValue(taskSnapshot.getMetadata().getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.getReference("Images/" +  subRef.getKey() + "/title").setValue(Title).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    mInterop.InitializeProfile(mUser);
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                            Toast.makeText(mWorkingContext, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(mWorkingContext, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE){
            try {
                Button submitBtn = mView.findViewById(R.id.submit_btn_dialog);
                submitBtn.setText("Imagen Agregada");
                Uri result = data.getData();
                if(result != null){
                    mInputStream = mWorkingContext.getContentResolver().openInputStream(result);
                    mStorage = FirebaseStorage.getInstance();
                    mStorageReference = mStorage.getReference();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.submit_dialog, container, false);
//    }
}
