package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.TextUtils.isEmpty;
import static com.ralph.adnu_hrmoattendancemonitoringmobileapplication.MainActivity.userStaffId;
import static com.ralph.adnu_hrmoattendancemonitoringmobileapplication.MainActivity.userToken;

public class DashBoard extends AppCompatActivity {
    private CardView buildingList;
    private CardView updateDatabase;
    private CardView uploadConfirmationNotice;
    private CardView uploadFacultyAttendance;
    private CardView downloadImages;
    private CardView updateSchedAndFaculty;

    private static AhcfamsApi ahcfamsApi;
    private static List<FacultyAttendance> attendanceItems;

    String base_url = "http://" + MainActivity.ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/assets/images/";
    String root = Environment.getExternalStorageDirectory().toString(); // deprecated in API level 29.
    public static File imageDir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        imageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        createOptionMenu();
        createRetrofitClient();

        buildingList = (CardView)findViewById(R.id.buildingList);
        updateDatabase = (CardView)findViewById(R.id.updateDatabase);
        uploadConfirmationNotice = (CardView)findViewById(R.id.uploadConfirmationNotice);
        uploadFacultyAttendance = (CardView)findViewById(R.id.uploadFacultyAttendance);
        downloadImages = (CardView)findViewById(R.id.downloadImages);
        updateSchedAndFaculty = (CardView)findViewById(R.id.updateSchedAndFaculty);

        buildingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(), BuildingList.class);
                startActivity(in);
            }
        });

        updateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (updateFacultyAttendance())
                        if(updateConfirmationNotice())
                            Log.d("Updated Database", "Done");
            }
        });

        uploadFacultyAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFacultyAttendance();

            }
        });

        uploadConfirmationNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadConfirmationNotice();
            }
        });

        downloadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImages();
            }
        });

        updateSchedAndFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateFaculty())
                    if(updateClassSchedule())
                        updateRoom();
            }
        });
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

    private boolean updateFaculty(){
        Faculty faculty = new Faculty(userStaffId, userToken);
        Call<List<Faculty>> call = ahcfamsApi.faculty(userStaffId, userToken);

        call.enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }else{
                    List<Faculty> faculties = response.body();

                    if(faculties != null){
                        for(Faculty faculty1 : faculties){
                            boolean isInserted = MainActivity.myDB.updateFaculty(faculty1.getFACULTY_ID(), faculty1.getNAME(),faculty1.getDEPARTMENT(), faculty1.getCOLLEGE());
                            if (!isInserted)
                                Toast.makeText(getApplicationContext(), "Faculty Error", Toast.LENGTH_SHORT).show();
                            else
                                MainActivity.currentFacultyCount += 1;
                        }
                        Toast.makeText(getApplicationContext(), "Faculty Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Faculty Error", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean updateClassSchedule(){
        ClassSchedule classSchedule = new ClassSchedule(userStaffId, userToken);
        Call<List<ClassSchedule>> call = ahcfamsApi.classSchedule(userStaffId,userToken);

        call.enqueue(new Callback<List<ClassSchedule>>() {
            @Override
            public void onResponse(Call<List<ClassSchedule>> call, Response<List<ClassSchedule>> response) {
                List<ClassSchedule> classSchedules = response.body();

                if(classSchedules != null){
                    for (ClassSchedule classSchedule1 : classSchedules){
                        boolean isInserted = false;
                        try {
                            Day day = new Day(classSchedule1.getCLASS_DAY());
                            isInserted = MainActivity.myDB.updateClassSchedule(classSchedule1.getCLASS_SCHEDULE_ID(), classSchedule1.getFACULTY_ID(), classSchedule1.getSEMESTER(), classSchedule1.getSCHOOL_YEAR(), MainActivity.time12HourTo24Hour(classSchedule1.getSTART_TIME()), MainActivity.time12HourTo24Hour(classSchedule1.getEND_TIME()), classSchedule1.getCLASS_SECTION(), day.parseDay(), classSchedule1.getSUBJECT_CODE(), classSchedule1.getHOURS());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(isInserted){
                            MainActivity.currentClassScheduleCount += 1;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Class Schedule Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ClassSchedule>> call, Throwable t) {
            }
        });
        return true;
    }

    private boolean updateRoom(){
        Room room = new Room(userStaffId, userToken);

        Call<List<Room>> call = ahcfamsApi.room(userStaffId,userToken);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                List<Room> rooms = response.body();

                if(rooms != null){
                    for (Room room1 : rooms){
                        boolean isInserted = MainActivity.myDB.updateRoom(room1.getROOM_ID(), room1.getROOM_NAME(), room1.getBUILDING_NAME(), room1.getROUTE());
                        if (!isInserted)
                            Toast.makeText(getApplicationContext(), "Room: Error", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Rooms Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Room: Error", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean updateConfirmationNotice(){
        ConfirmationNotice confirmationNotice = new ConfirmationNotice(userStaffId, userToken);

        Call<List<ConfirmationNotice>> call = ahcfamsApi.confirmation_notice(userStaffId,userToken);
        call.enqueue(new Callback<List<ConfirmationNotice>>() {
            @Override
            public void onResponse(Call<List<ConfirmationNotice>> call, Response<List<ConfirmationNotice>> response) {
                List<ConfirmationNotice> confirmationNotices = response.body();
                boolean isInserted;
                for (ConfirmationNotice confirmationNotice1: confirmationNotices){
                    isInserted = MainActivity.myDB.updateConfirmationNotice(confirmationNotice1.getCONFIRMATION_NOTICE_ID(), confirmationNotice1.getCONFIRMATION_NOTICE_DATE(), confirmationNotice1.getELECTRONIC_SIGNATURE(),confirmationNotice1.getREMARKS(), confirmationNotice1.getCONFIRMED());
                    if(!isInserted)
                        Toast.makeText(getApplicationContext(), "Confirmation Notice: Error", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Confirmation Notice Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ConfirmationNotice>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Confirmation Notice: Error", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean updateFacultyAttendance(){
        FacultyAttendance facultyAttendance = new FacultyAttendance(userStaffId, userToken);

        Call<List<FacultyAttendance>> call = ahcfamsApi.faculty_attendance(userStaffId, userToken);
        call.enqueue(new Callback<List<FacultyAttendance>>() {
            @Override
            public void onResponse(Call<List<FacultyAttendance>> call, Response<List<FacultyAttendance>> response) {
                List<FacultyAttendance> facultyAttendances = response.body();

                if(facultyAttendances != null){
                    for (FacultyAttendance facultyAttendance1 : facultyAttendances){
                        boolean isInserted = MainActivity.myDB.updateFacultyAttendance(facultyAttendance1.getFACULTY_ATTENDANCE_ID(), facultyAttendance1.getSTAFF_ID(), facultyAttendance1.getCLASS_SCHEDULE_ID(), facultyAttendance1.getCONFIRMATION_NOTICE_ID(), facultyAttendance1.getROOM_ID(), facultyAttendance1.getATTENDANCE_DATE(), facultyAttendance1.getFIRST_CHECK(), facultyAttendance1.getSECOND_CHECK(), facultyAttendance1.getFIRST_IMAGE_FILE(), facultyAttendance1.getSECOND_IMAGE_FILE(), facultyAttendance1.getSTATUS());
                        if(!isInserted)
                            Toast.makeText(getApplicationContext(), "Faculty Attendance: Error", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), "Faculty Attendance Updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FacultyAttendance>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Faculty Attendance: Error", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadConfirmationNotice(){
        List<ConfirmationNotice> confirmationNoticeItems = new ArrayList<>();

        final Cursor confirmationNotice = MainActivity.myDB.getAllConfirmationNotice();
        confirmationNotice.moveToFirst();

        for(int i = 0; i < confirmationNotice.getCount(); i++){
            MultipartBody.Part signature;

            if(isEmpty(confirmationNotice.getString(5))){
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"),"");

                signature = MultipartBody.Part.createFormData("esignature", "", fileReqBody);
            }else{
                File file = new File(imageDir ,confirmationNotice.getString(5));
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                signature = MultipartBody.Part.createFormData("esignature", file.getName(), fileReqBody);
            }

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userStaffId);
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), userToken);
            final RequestBody cnid = RequestBody.create(MediaType.parse("text/plain"), confirmationNotice.getString(0));
            RequestBody faid = RequestBody.create(MediaType.parse("text/plain"), confirmationNotice.getString(1));
            RequestBody remarks = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody reason = RequestBody.create(MediaType.parse("type/plain"), confirmationNotice.getString(3));
            RequestBody confirmed = RequestBody.create(MediaType.parse("type/plain"), confirmationNotice.getString(6));
            RequestBody date = RequestBody.create(MediaType.parse("type/plain"), confirmationNotice.getString(2));

            Call call = ahcfamsApi.confirmation_notice(id, token, cnid, faid, date, remarks, reason, confirmed ,signature);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    ConfirmationNotice noticeResponse = (ConfirmationNotice) response.body();
                    if(noticeResponse.getStatus().equals("201")){
                        MainActivity.myDB.changeSync("confirmation_notice_id", confirmationNotice.getString(0), "notice_synchronized", "CONFIRMATION_NOTICE");
                        Toast.makeText(getApplicationContext(), "Confirmation Notice Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                }
            });

            if(!confirmationNotice.isLast()){
                confirmationNotice.moveToNext();
            }
        }
        return true;
    }

    private boolean uploadFacultyAttendance(){
        Log.d("Faculty Attendance", "Called");
        attendanceItems = new ArrayList<>();

        final Cursor facultyAttendance = MainActivity.myDB.getFacultyAttendance();
        facultyAttendance.moveToFirst();

        Integer temp = facultyAttendance.getCount();

        Log.d("Faculty_Attendance", temp.toString());

        for (int i = 0; i < facultyAttendance.getCount(); i++){
            File file1;
            File file2;
            MultipartBody.Part firstImage;
            MultipartBody.Part secondImage;

            if(isEmpty(facultyAttendance.getString(6))){ // checks if there's no image
                /* Send an empty image*/
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), "");

                firstImage = MultipartBody.Part.createFormData("fipath", "", fileReqBody1);
            }else{
                file1 = new File(imageDir, facultyAttendance.getString(6));
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                firstImage = MultipartBody.Part.createFormData("fipath", file1.getName(), fileReqBody1);
            }

            if(isEmpty(facultyAttendance.getString(7))){
                RequestBody fileReqBody2 = RequestBody.create(MediaType.parse("image/*"), "");
                secondImage = MultipartBody.Part.createFormData("sipath", "", fileReqBody2);
            }else{
                file2 = new File(imageDir, facultyAttendance.getString(7));
                RequestBody fileReqBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                secondImage = MultipartBody.Part.createFormData("sipath", file2.getName(), fileReqBody2);
            }

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userStaffId);
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), userToken);
            RequestBody faid = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(0));
            RequestBody sid = RequestBody.create(MediaType.parse("text/plain"), userStaffId);
            RequestBody csid = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(2));
            RequestBody adate = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(3));
            RequestBody fcheck = null;
            if(isEmpty(facultyAttendance.getString(4))){
                fcheck = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(4));
            }else if(facultyAttendance.getString(4) == "Absent"){
                fcheck = RequestBody.create(MediaType.parse("text/plain"), "null");
            }else{
                try {
                    fcheck = RequestBody.create(MediaType.parse("text/plain"), MainActivity.convertTimeToDateTime(facultyAttendance.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            RequestBody scheck = null;
            if(isEmpty(facultyAttendance.getString(5))){
                scheck = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(5));
            }else if(facultyAttendance.getString(5) == "Absent"){
                scheck = RequestBody.create(MediaType.parse("text/plain"), "null");
            }else{
                try {
                    scheck = RequestBody.create(MediaType.parse("text/plain"), MainActivity.convertTimeToDateTime(facultyAttendance.getString(5)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            RequestBody status = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(8));

            Call<FacultyAttendance> call = ahcfamsApi.faculty_attendance(id, token, faid, sid, csid, adate, fcheck, scheck, firstImage, secondImage, status);

            call.enqueue(new Callback<FacultyAttendance>() {
                @Override
                public void onResponse(Call<FacultyAttendance> call, Response<FacultyAttendance> response) {
                    FacultyAttendance attendanceResponse = response.body();
                    Log.d("Faculty Attendance", attendanceResponse.getStatus());

                    if(attendanceResponse.getStatus().equals("201")){
                        MainActivity.myDB.changeSync("faculty_attendance_id", facultyAttendance.getString(0), "attendance_synchronized", "FACULTY_ATTENDANCE");

                    }
                }

                @Override
                public void onFailure(Call<FacultyAttendance> call, Throwable t) {
                    Log.d("Faculty_Attendance", t.getMessage());
                }
            });
            if(!facultyAttendance.isLast())
                facultyAttendance.moveToNext();
        }
        Toast.makeText(getApplicationContext(), "Faculty Attendance Uploaded", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean downloadImages(){
        //final Pattern pattern  = Pattern.compile("/(.*?).");\
        OkHttpClient client = new OkHttpClient();

        Request request;

        final Cursor imageFileNames = MainActivity.myDB.getImageFileNames();
        imageFileNames.moveToFirst();

        for (int i = 0; i <= 1; i ++){
            for (int j = 0; j < imageFileNames.getCount(); j++){
                final String fname = imageFileNames.getString(i);
                if(fname != null){
                    request = new Request.Builder()
                            .url(base_url + fname)
                            .build();

                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            Log.d("DownloadImage", "Fail");
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            InputStream inputStream = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            File file = new File(imageDir, fname);

                            try{
                                FileOutputStream out = new FileOutputStream(file);
                                if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out))
                                    Log.d("DownloadImage", fname + " has been downloaded and saved");
                                out.flush();
                                out.close();
                            }catch (Exception e){
                                Log.d("DownloadImage", "Error " + e.getMessage());
                            }
                        }
                    });

                    if(!imageFileNames.isLast())
                        imageFileNames.moveToNext();
                }
            }
        }
        return true;
    }
}
