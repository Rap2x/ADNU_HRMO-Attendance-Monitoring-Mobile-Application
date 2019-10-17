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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

    private String currentPhotoPath;

    private AhcfamsApi ahcfamsApi;

    private String faculty_attendance_id;

    private Cursor confirmationNoticeData;
    private Cursor facultyData;

    private String faculty_id;

    private EditText absentReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        createOptionMenu();

        createRetrofitClient();

        faculty_id = getIntent().getStringExtra("faculty_id");
        faculty_attendance_id = getIntent().getStringExtra("faculty_attendance_id");

        Toast.makeText(getApplicationContext(), faculty_attendance_id, Toast.LENGTH_SHORT).show();
        bindData();

        showConfirmationNoticeDetails();

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

        onClickListener();

        absentReason.setEnabled(true);
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

    private String saveSignature(Bitmap bitMapImage) throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + MainActivity.userRoute + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        FileOutputStream fos = null;

        try{
            fos = new FileOutputStream(image);
            bitMapImage.compress(Bitmap.CompressFormat.JPEG, 10, fos);

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

    private void createRetrofitClient(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + MainActivity.ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);
    }

    private void createOptionMenu(){
        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    private void bindData(){
        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        clearButton = (Button) findViewById(R.id.clearButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        name = (TextView) findViewById(R.id.confirmation_notice_name);
        time = (TextView) findViewById(R.id.signature_pad_time);
        schedule = (TextView) findViewById(R.id.schedule);
        room = (TextView)findViewById(R.id.signature_pad_room_id);
        timeChecked = (TextView) findViewById(R.id.timeChecked);

        absentReason = (EditText) findViewById(R.id.reasonTextView);
    }

    private void onClickListener(){
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
                    String confirmationNoticeId = MainActivity.myDB.getConfirmationNoticeId(faculty_attendance_id);
                    Boolean isInserted = MainActivity.myDB.saveSignature(confirmationNoticeId, saveSignature(signatureBitmap));

                    if(isInserted) {
                        signaturePad.clear();
                        MainActivity.myDB.changeSync("confirmation_notice_id", confirmationNoticeId, "confirmed", "CONFIRMATION_NOTICE");
                        Toast.makeText(getApplicationContext(), "Absence Appeal Created", Toast.LENGTH_SHORT).show();
                        MainActivity.myDB.addReason(confirmationNoticeId, absentReason.getText().toString());
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showConfirmationNoticeDetails(){
        confirmationNoticeData = MainActivity.myDB.getConfirmationNotice(faculty_id, faculty_attendance_id);
        facultyData = MainActivity.myDB.getFacultyDetails(faculty_id);

        name.setText("Name: "+ confirmationNoticeData.getString(0));
        time.setText("Time Schedule: " + confirmationNoticeData.getString(1));
        schedule.setText("Subject Code and Section: " + confirmationNoticeData.getString(3)+ "." + confirmationNoticeData.getString(2));
        room.setText("Room: " + confirmationNoticeData.getString(4));
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()){
            case R.id.radioPresent:
                if(checked){
                    Toast.makeText(getApplicationContext(), "Present", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.radioAbsent:
                if(checked){
                    Toast.makeText(getApplicationContext(), "Absent", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
