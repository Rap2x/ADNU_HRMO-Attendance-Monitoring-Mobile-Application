package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
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
import static com.ralph.adnu_hrmoattendancemonitoringmobileapplication.MainActivity.userStaffId;
import static com.ralph.adnu_hrmoattendancemonitoringmobileapplication.MainActivity.userToken;

public class BuildingList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BuildingAdapter adapter;

    private List<BuildingListItem> listItems;

    private static AhcfamsApi ahcfamsApi;

    private static List<FacultyAttendance> attendanceItems;

    private String buildingName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        createRetrofitClient();

        createOptionMenu();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_refresh:
                showList();
                break;
            case R.id.action_faculty_attendance:
                MainActivity.myDB.createAttendance();
                break;
            case R.id.settings:
                openSettings();
                break;
            case R.id.update_database:
                updateFaculty();
                updateClassSchedule();
                updateRoom();
                updateFacultyAttendance();
                updateConfirmationNotice();
                break;
            case R.id.upload_confirmation_notice:
                uploadConfirmationNotice();
                break;
            case R.id.upload_faculty_attendance:
                uploadFacultyAttendance();
                break;
            case R.id.logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showList(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        Cursor buildingData = MainActivity.myDB.getBuildings();

        Integer buildingCount = buildingData.getCount(); // remove
        Toast.makeText(getApplicationContext(), buildingData.getString(0), Toast.LENGTH_SHORT).show();

        for(int i = 0; i < buildingData.getCount(); i++){
            BuildingListItem listItem = new BuildingListItem(buildingData.getString(0));
            if(MainActivity.myDB.isClassAvailable(buildingData.getString(0))){
                listItems.add(listItem);
            }else
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

            if(!buildingData.isLast()){
                buildingData.moveToNext();
            }
        }

        adapter = new BuildingAdapter(listItems,this);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BuildingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getBaseContext(), AttendanceList.class);
                intent.putExtra("building_name", listItems.get(position).getBuildingName());

                startActivity(intent);
            }
        });
    }

    private void openSettings(){

        Intent intent = new Intent(getBaseContext(), Settings.class);
        startActivity(intent);
    }

    private void updateFaculty(){
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
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Faculty Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRoute(){
        Route route = new Route(userStaffId, userToken);
        Call<List<Route>> call = ahcfamsApi.route(userStaffId, userToken);

        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                List<Route> routes = response.body();

                for (Route route1 : routes){
                    boolean isInserted = MainActivity.myDB.updateRoute(route1.getROUTE_ID(), route1.getDESCRIPTION());
                    if(!isInserted)
                        Toast.makeText(getApplicationContext(),"Routes: Error" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Routes: Error" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void updateClassSchedule(){
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
                }
            }

            @Override
            public void onFailure(Call<List<ClassSchedule>> call, Throwable t) {
            }
        });
    }

    private void updateRoom(){
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
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Room: Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateConfirmationNotice(){
        ConfirmationNotice confirmationNotice = new ConfirmationNotice(userStaffId, userToken);

        Call<List<ConfirmationNotice>> call = ahcfamsApi.confirmation_notice(userStaffId,userToken);
        call.enqueue(new Callback<List<ConfirmationNotice>>() {
            @Override
            public void onResponse(Call<List<ConfirmationNotice>> call, Response<List<ConfirmationNotice>> response) {
                List<ConfirmationNotice> confirmationNotices = response.body();
                for (ConfirmationNotice confirmationNotice1: confirmationNotices){
                    boolean isInserted = MainActivity.myDB.updateConfirmationNotice(confirmationNotice1.getCONFIRMATION_NOTICE_ID(), confirmationNotice1.getCONFIRMATION_NOTICE_DATE(), confirmationNotice1.getELECTRONIC_SIGNATURE(),confirmationNotice1.getREMARKS(), confirmationNotice1.getCONFIRMED());
                    if(!isInserted)
                        Toast.makeText(getApplicationContext(), "Confirmation Notice: Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ConfirmationNotice>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Confirmation Notice: Errora", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFacultyAttendance(){
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
                }
            }

            @Override
            public void onFailure(Call<List<FacultyAttendance>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Faculty Attendance: Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void uploadConfirmationNotice(){
        List<ConfirmationNotice> confirmationNoticeItems = new ArrayList<>();

        final Cursor confirmationNotice = MainActivity.myDB.getAllConfirmationNotice();
        confirmationNotice.moveToFirst();

        /*Integer count = confirmationNotice.getCount();

        Toast.makeText(getApplicationContext(), count.toString(), Toast.LENGTH_SHORT).show();*/

        for(int i = 0; i < confirmationNotice.getCount(); i++){
            File file = new File(confirmationNotice.getString(5));
            MultipartBody.Part signature;

            if(isEmpty(confirmationNotice.getString(5))){
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"),"");

                signature = MultipartBody.Part.createFormData("esignature", "", fileReqBody);
            }else{
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
    }

    public static void uploadFacultyAttendance(){
        Log.d("Faculty Attendance", "Called");
        attendanceItems = new ArrayList<>();

        final Cursor facultyAttendance = MainActivity.myDB.getFacultyAttendance();
        facultyAttendance.moveToFirst();

        Integer temp = facultyAttendance.getCount();

        Log.d("Faculty_Attendance", temp.toString());

        for (int i = 0; i < facultyAttendance.getCount(); i++){
            File file1 = new File(facultyAttendance.getString(6));
            File file2 = new File(facultyAttendance.getString(7));
            MultipartBody.Part firstImage;
            MultipartBody.Part secondImage;

            if(isEmpty(facultyAttendance.getString(6))){ // checks if there's no image
                /* Send an empty image*/
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), "");

                firstImage = MultipartBody.Part.createFormData("fipath", "", fileReqBody1);
            }else{
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                firstImage = MultipartBody.Part.createFormData("fipath", file1.getName(), fileReqBody1);
            }

            if(isEmpty(facultyAttendance.getString(7))){
                RequestBody fileReqBody2 = RequestBody.create(MediaType.parse("image/*"), "");
                secondImage = MultipartBody.Part.createFormData("sipath", "", fileReqBody2);
            }else{
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
                    Log.d("Faculty_Attenadnce", t.getMessage());
                }
            });

            if(!facultyAttendance.isLast())
                facultyAttendance.moveToNext();
        }

    }

    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Intent in = new Intent(getBaseContext(), MainActivity.class);
        startActivity(in);
    }
}
