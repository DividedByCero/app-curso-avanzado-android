package com.example.example.appfinalavanzadoandroid.ui.submit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.example.appfinalavanzadoandroid.R;
import com.example.example.appfinalavanzadoandroid.models.ImageFile;
import com.example.example.appfinalavanzadoandroid.ui.main.DataInterop;
import com.example.example.appfinalavanzadoandroid.ui.main.MainView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class SubmitDialog extends DialogFragment {
    public static final int PICK_IMAGE = 0x4343;
    private FirebaseUser mUser;
    private DataInterop mInterop;
    private Dialog mDialog;
    private View mView;
    private InputStream mInputStream;
    private StorageReference mStorageReference;
    private Context mWorkingContext;
    FirebaseStorage mStorage;
    private FusedLocationProviderClient mFusedLocationClient;

    public SubmitDialog() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mWorkingContext);
    }

    public void SetInterop(DataInterop interop) {
        mInterop = interop;
    }

    public void SetWorkingContext(Context workingContext) {
        mWorkingContext = workingContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        Activity activity = getActivity();
        mView = activity.getLayoutInflater().inflate(R.layout.submit_dialog, null);
        dialog.setTitle(R.string.SubirImagen);
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

        dialog.setPositiveButton(R.string.SubitTextDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadImage(mInputStream, mStorageReference);

            }
        });

        dialog.setNegativeButton(R.string.CancelarTextDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return dialog.create();

    }

    public void uploadImage(InputStream stream, StorageReference storageReference) {

        if (stream != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Images/" + UUID.randomUUID().toString());
            ref.putStream(stream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    final String Title = ((EditText) mView.findViewById(R.id.title_submit_dialog)).getText().toString();
                    progressDialog.dismiss();
                    final FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref = db.getReference("Images");
                    int position = mInterop.getArrayDataLength();
                    final DatabaseReference subRef = ref.child(Integer.toString(position));
                    subRef.setValue("");
                    final ImageFile file = new ImageFile();
                    file.setFile(taskSnapshot.getMetadata().getName());
                    file.setAuthor(mUser.getDisplayName());
                    file.setTitle(Title);

                    if (ActivityCompat.checkSelfPermission(mWorkingContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mWorkingContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null)
                                    file.setLocation(location.getLatitude() + ", " + location.getLongitude());

                                CreateFirebaseImageFile(db, subRef, file);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CreateFirebaseImageFile(db, subRef, file);
                            }
                        });
                    } else {
                        CreateFirebaseImageFile(db, subRef, file);
                    }

                    Toast.makeText(mWorkingContext, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(mWorkingContext, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            try {
                Button submitBtn = mView.findViewById(R.id.submit_btn_dialog);
                submitBtn.setText(R.string.ImagenAgregada);
                Uri result = data.getData();
                if (result != null) {
                    mInputStream = mWorkingContext.getContentResolver().openInputStream(result);
                    mStorage = FirebaseStorage.getInstance();
                    mStorageReference = mStorage.getReference();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void CreateFirebaseImageFile(FirebaseDatabase db, DatabaseReference subRef, ImageFile file) {
        db.getReference("Images/" + subRef.getKey()).setValue(file).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mInterop.InitializeProfile(mUser);
            }
        });

    }
}
