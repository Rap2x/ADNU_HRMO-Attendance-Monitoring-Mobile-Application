package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AttendanceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AttendanceListItem> listItems;

    private AhcfamsApi ahcfamsApi;

    private String userStaffId;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ArrayList userCredential = MainActivity.myDB.getUserCredentials();
        userStaffId = userCredential.get(0).toString();
        userToken = userCredential.get(1).toString();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + MainActivity.ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_refresh:
                showList();
                break;

            case R.id.action_update_faculty_table:
                updateFaculty();
                break;

            case R.id.action_update_route_table:
                updateRoute();
                break;

            case R.id.action_class_schedule:
                updateClassSchedule();
                break;

            case R.id.action_faculty_attendance:
                MainActivity.myDB.createAttendance();
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        Cursor attendanceData = MainActivity.myDB.getFacultyAttendance();

        attendanceData.moveToFirst();

        for(int i = 0 ;i < attendanceData.getCount(); i++){
            AttendanceListItem listItem = new AttendanceListItem(
                    attendanceData.getString(3),
                    attendanceData.getString(2),
                    attendanceData.getString(1),
                    attendanceData.getString(4),
                    attendanceData.getString(0),
                    attendanceData.getString(5),
                    attendanceData.getString(6)
            );

            listItems.add(listItem);
            attendanceData.moveToNext();
        }
        adapter = new AttendanceAdapter(listItems, this);

        recyclerView.setAdapter(adapter);
    }

    private void updateFaculty(){
        Faculty faculty = new Faculty(userStaffId, userToken);
        Call<List<Faculty>> call = ahcfamsApi.faculty(userStaffId,userToken);

        call.enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }else{
                    List<Faculty> faculties = response.body();

                    Integer count =0;

                    for(Faculty faculty1 : faculties){

                        boolean isInserted = MainActivity.myDB.updateFaculty(faculty1.getFaculty_id(), faculty1.getName(), faculty1.getDesignation(), faculty1.getDepartment(), faculty1.getCollege());
                        if (isInserted){
                            count += 1;
                            Toast.makeText(getApplicationContext(),count.toString() , Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {

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

                Integer count = 0;

                for (Route route1 : routes){
                    boolean isInserted = MainActivity.myDB.updateRoute(route1.getRoute_id(), route1.getDescription());
                    if(isInserted) {
                        count += 1;
                        Toast.makeText(getApplicationContext(), "Routes: " + count.toString(), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplicationContext(),"Routes: ERROR" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {

            }
        });
    }

    private void updateClassSchedule(){
        ClassSchedule classSchedule = new ClassSchedule(userStaffId, userToken);
        Call<List<ClassSchedule>> call = ahcfamsApi.classSchedule(userStaffId,userToken);

        call.enqueue(new Callback<List<ClassSchedule>>() {
            @Override
            public void onResponse(Call<List<ClassSchedule>> call, Response<List<ClassSchedule>> response) {
                List<ClassSchedule> classSchedules = response.body();

                Integer count = 0;
                for (ClassSchedule classSchedule1 : classSchedules){
                    boolean isInserted = MainActivity.myDB.updateClassSchedule(classSchedule1.getClass_schedule_id(), classSchedule1.getRoom_id(), classSchedule1.getFaculty_id(), classSchedule1.getSemester(), classSchedule1.getSchool_year(), classSchedule1.getStart_time(), classSchedule1.getEnd_time(), classSchedule1.getClass_section(), classSchedule1.getClass_day(), classSchedule1.getSubject_code(), classSchedule1.getHalf_day(), classSchedule1.getHours());
                    if(isInserted){
                        count += 1;
                        Toast.makeText(getApplicationContext(), "Class Schedule: " + count, Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplicationContext(), "Class Schedule Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<ClassSchedule>> call, Throwable t) {

            }
        });
    }




}
