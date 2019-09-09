package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.TextUtils.isEmpty;

public class Signature extends AppCompatActivity {

    private SignaturePad signaturePad;
    private Button clearButton;
    private Button saveButton;
    private Button appealButton;
    private TextView name;
    private TextView time;
    private TextView schedule;
    private TextView room;
    private TextView timeChecked;
    private TextView remarks;

    private String currentPhotoPath;

    private AhcfamsApi ahcfamsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + MainActivity.ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);

        String faculty_id = getIntent().getStringExtra("faculty_id");
        Toast.makeText(getApplicationContext(), faculty_id, Toast.LENGTH_SHORT).show();
        final String confirmation_notice_id = getIntent().getStringExtra("confirmation_notice_id");

        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        clearButton = (Button) findViewById(R.id.clearButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        appealButton = (Button) findViewById(R.id.appealButton);

        name = (TextView) findViewById(R.id.confirmation_notice_name);
        time = (TextView) findViewById(R.id.signature_pad_time);
        schedule = (TextView) findViewById(R.id.schedule);
        room = (TextView)findViewById(R.id.signature_pad_room_id);
        timeChecked = (TextView) findViewById(R.id.timeChecked);
        remarks = (TextView) findViewById(R.id.remarks);

        Cursor confirmationNoticeData = MainActivity.myDB.getConfirmationNotice(faculty_id, confirmation_notice_id);
        Cursor facultyData = MainActivity.myDB.getFacultyDetails(faculty_id);
        Integer count = facultyData.getCount();

        Toast.makeText(getApplicationContext(), count.toString(), Toast.LENGTH_SHORT).show();

        name.setText("Name: "+facultyData.getString(1));
        time.setText("Time Schedule: " + confirmationNoticeData.getString(4));
        schedule.setText("Subject Code and Section: " + confirmationNoticeData.getString(3)+ "." + confirmationNoticeData.getString(5));
        room.setText("Room: " + confirmationNoticeData.getString(7));
        remarks.setText("Remarks: " + confirmationNoticeData.getString(6));

        //~~~~~~~~~~~~~~~~signaturePad Settings~~~~~~~~~~~~~~~~~~
        //signaturePad.setMaxWidth();
        //signaturePad.setMinWidth(2);

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {
                Toast.makeText(getApplicationContext(), "Cleared.", Toast.LENGTH_SHORT).show();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save the signature to the local database
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();

                try {
                    Boolean isInserted = MainActivity.myDB.saveSignature(confirmation_notice_id, saveSignature(signatureBitmap));

                    if(isInserted) {
                        Toast.makeText(getApplicationContext(), "Signature Saved.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), currentPhotoPath, Toast.LENGTH_SHORT).show();
                        signaturePad.clear();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        appealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open another activity for absence appeal
                Intent intent = new Intent(getBaseContext(), AbsenceAppealActivity.class);
                startActivity(intent);
            }
        });
    }


    private String saveSignature(Bitmap bitMapImage) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" ;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        FileOutputStream fos = null;

        try{
            fos = new FileOutputStream(image);
            bitMapImage.compress(Bitmap.CompressFormat.JPEG, 50, fos);

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try{
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        currentPhotoPath = image.getAbsolutePath();
        return currentPhotoPath;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

}
