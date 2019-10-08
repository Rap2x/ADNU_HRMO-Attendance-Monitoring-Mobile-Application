package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    static DatabaseHelper myDB;

    public final static String ip = "192.168.137.1"; //Change this accordingly

    EditText eUsername;
    EditText ePassword;

    private String username = "sample";
    private String password = "sample";

    public static  String userStaffId;
    public static String userToken;
    public static String userRoute;

    private AhcfamsApi ahcfamsApi;

    public static int absenceAppealCount;
    public static double classScheduleCount;
    public static int confirmationNoticeCount;
    public static int facultyCount;
    public static int facultyAttendanceCount;
    public static int roomCount;
    public static int routeCount;
    public static int staffCount;

    public static int currentAbsenceAppealCount = 0;
    public static double currentClassScheduleCount = 0;
    public static int currentConfirmationNoticeCount = 0;
    public static int currentFacultyCount = 0;
    public static int currentFacultyAttendanceCount = 0;
    public static int currentRoomCount = 0;
    public static int currentRouteCount = 0;
    public static int currentStaffCount = 0;

    public static String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        createRetrofitClient();

        onClickListener();

        currentDay = getDay();

        myDB.clearUser();

    }

    private void onClickListener(){
        final Button signIn = findViewById(R.id.SignIn);
        signIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                login();
            }
        });
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) //check if camera permission is not yet granted
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100); // grant camera permission

    }

    private void createRetrofitClient(){
        myDB = new DatabaseHelper(this);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);

        addEditTextListener();
    }

    public void addEditTextListener(){
        eUsername = (EditText) findViewById(R.id.editTextUsername);
        ePassword = (EditText) findViewById(R.id.editTextPassword);

        eUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();

                if(username == ""){
                    username = "empty";
                }
                Log.d("Username: ", username);
            }
        });

        ePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();

                if(password == ""){
                    password = "empty";
                }
                Log.d("Password: ", password);
            }
        });
    }

    private void login(){
        Log.d("username & password", username + " " + password);
        Login login = new Login(username, password);
        Call<Login> call = ahcfamsApi.login(username, password);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Login loginResponse = response.body();
                    Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if(loginResponse.getStatus().equals("200")){
                        boolean isInserted = myDB.loginStaff(loginResponse.getUsername(), loginResponse.getToken(), loginResponse.getDateTime(), loginResponse.getRoute_id());

                        if(isInserted) { //Move to the next activity if inserted
                            Intent in = new Intent(MainActivity.this, BuildingList.class);
                            startActivity(in);
                            Toast.makeText(getApplicationContext(), "Database Updated", Toast.LENGTH_SHORT).show();

                            ArrayList userCredentials = myDB.getUserCredentials();
                            userStaffId = userCredentials.get(0).toString();
                            userToken = userCredentials.get(1).toString();
                            userRoute = userCredentials.get(2).toString();
                            Toast.makeText(getApplicationContext(), userStaffId + userToken, Toast.LENGTH_SHORT).show();

                            //getTableRowCounts();
                        }
                        else
                            Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        String currentTime = dateFormat.format(calendar.getTime());

        return currentTime;
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");

        String currentDate = dateFormat.format(calendar.getTime());

        return currentDate;
    }

    public static String getCurrentDateDash(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        String currentDate = dateFormat.format(calendar.getTime());

        return currentDate;
    }

    public static String getCurrentTime12Hours(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

        String currentTime = dateFormat.format(calendar.getTime());

        return currentTime;

    }

    public static String time12HourTo24Hour(String time) throws ParseException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String result = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mma", Locale.US)).format(DateTimeFormatter.ofPattern("HH:mm"));
            return result;
        }else{
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");
            Date date = parseFormat.parse(time);
            return displayFormat.format(date);
        }
    }

    public static String time24HourTo12Hour(String time) throws ParseException{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String result = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm", Locale.US)).format(DateTimeFormatter.ofPattern("hh:mma"));
            return result;
        }else{
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mma");
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = parseFormat.parse(time);
            return displayFormat.format(date);
        }
    }

    public static String convertTimeToDateTime(String time) throws ParseException {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String result = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US)).format(DateTimeFormatter.ofPattern("hh.mm.ss a"));
            return getCurrentDate() + " " + result;
        }else{
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh.mm.ss a");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
            Date date = parseFormat.parse(time);
            return getCurrentDateDash()+ " " + displayFormat.format(date);
        }
    }

    public void getTableRowCounts(){
        TableRowCounts counts1 = new TableRowCounts(userStaffId, userToken);
        Call<TableRowCounts> call = ahcfamsApi.table_row_counts(userStaffId, userToken);

        call.enqueue(new Callback<TableRowCounts>() {
            @Override
            public void onResponse(Call<TableRowCounts> call, Response<TableRowCounts> response) {
                TableRowCounts counts = response.body();

                absenceAppealCount = Integer.parseInt(counts.getABSENCE_APPEAL());
                classScheduleCount = Integer.parseInt(counts.getCLASS_SCHEDULE());
                confirmationNoticeCount = Integer.parseInt(counts.getCONFIRMATION_NOTICE());
                facultyCount = Integer.parseInt(counts.getFACULTY());
                facultyAttendanceCount = Integer.parseInt(counts.getFACULTY_ATTENDANCE());
                roomCount = Integer.parseInt(counts.getROOM());
                routeCount = Integer.parseInt(counts.getROUTE());
                staffCount = Integer.parseInt(counts.getSTAFF());
            }

            @Override
            public void onFailure(Call<TableRowCounts> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String getDay(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("E");
        String currentDay = dateFormat.format(calendar.getTime());

        return currentDay.toUpperCase();
    }
}
