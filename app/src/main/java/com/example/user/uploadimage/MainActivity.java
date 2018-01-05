package com.example.user.uploadimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonBrowse, buttonUpload;
    ImageView imageView;

    private StorageReference mStorageRef;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBrowse = (Button) findViewById(R.id.buttonBrowse);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonBrowse.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    //fcn to browse for image
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonBrowse) {
            //open file chooser
            showFileChooser();

        }else if(v == buttonUpload){
            //upload image to firebase
            uploadFile();
        }
    }

    //fcn to upload file into firebase
    private void uploadFile() {

        if(filePath != null) {
            ProgressDialog pg = new ProgressDialog(getApplicationContext());
            pg.setTitle("Upload in progress");
            pg.show();

            StorageReference riversRef = mStorageRef.child("images/profile.jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            pg.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pg.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Unsuccessful",Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //display percentage of upload
                            double p = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            pg.setMessage((int)p + "% uploaded");
                        }
                    });
        }else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //display image in the ImageView
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
