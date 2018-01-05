package com.example.user.uploadimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonBrowse, buttonUpload;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBrowse = (Button) findViewById(R.id.buttonBrowse);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonBrowse.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
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

        }else if(v == buttonUpload){
            //upload image to firebase
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //display image in the ImageView
            Bitmap bitmap = MediaStore.Images.Media.getBitmap();
        }
    }
}
