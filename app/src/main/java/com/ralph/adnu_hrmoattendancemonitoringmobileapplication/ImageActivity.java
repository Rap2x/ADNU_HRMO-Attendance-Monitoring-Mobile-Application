package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class ImageActivity extends AppCompatActivity {

    private String facultyAttendanceId;
    private String firstImage;
    private String secondImage;

    private ImageView firstImageView;
    private ImageView secondImageView;

    private File firstFile;
    private File secondFile;
    private String TAG = "ImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        createOptionMenu();

        facultyAttendanceId = getIntent().getStringExtra("faculty_attendance_id");
        firstImage = getIntent().getStringExtra("first_image_file");
        secondImage = getIntent().getStringExtra("second_image_file");

        dataBind();

        Toast.makeText(getApplicationContext(), firstImage, Toast.LENGTH_SHORT).show();

        displayImages();
    }

    private void displayImages(){
        if(firstImage != null){
            firstFile = new File(DashBoard.imageDir, firstImage);
            Bitmap myBitmap = BitmapFactory.decodeFile(firstFile.getAbsolutePath());
            firstImageView.setImageBitmap(myBitmap);
            firstImageView.setAdjustViewBounds(true);
        }
        Log.d(TAG, "onCreate: " + secondImage);
        if(secondImage != null){
            secondFile = new File(DashBoard.imageDir, secondImage);
            Bitmap myBitmap = BitmapFactory.decodeFile(secondFile.getAbsolutePath());
            secondImageView.setImageBitmap(myBitmap);
            secondImageView.setAdjustViewBounds(true);
        }
    }

    private void createOptionMenu(){
        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void dataBind(){
        firstImageView = (ImageView)findViewById(R.id.firstImage);
        secondImageView = (ImageView)findViewById(R.id.secondImage);
    }


}
