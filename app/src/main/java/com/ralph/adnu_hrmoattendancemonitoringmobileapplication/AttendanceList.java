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

import static android.text.TextUtils.isEmpty;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AttendanceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;

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
                Integer schedSize = MainActivity.myDB.getScheduleSize();
                Integer attenSize = MainActivity.myDB.getAttendanceSize();


                Toast.makeText(getApplicationContext(),"Class Schedule: " + schedSize.toString() , Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Attendance Size: " + attenSize.toString() , Toast.LENGTH_SHORT).show();
                showList();
                break;

            case R.id.action_faculty_attendance:
                MainActivity.myDB.createAttendance();
                break;

            case R.id.update_database:
                updateFaculty();
                updateRoute();
                updateRoom();
                updateClassSchedule();
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
            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
            attendanceData.moveToNext();
        }
        adapter = new AttendanceAdapter(listItems, this);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AttendanceAdapter.OnItemClickListener() {
            @Override
            public void setAbsent(int position) {
                if (isEmpty(listItems.get(position).getFirst())){
                    listItems.get(position).setFirst("Absent");
                    boolean isUpdated = MainActivity.myDB.checkFirstAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getFirst());
                    if(isUpdated)
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else{
                    listItems.get(position).setSecond("Absent");
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated)
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void setPresent(int position) {
                if (isEmpty(listItems.get(position).getFirst())){
                    listItems.get(position).setFirst(MainActivity.getCurrentTime());
                    boolean isUpdated = MainActivity.myDB.checkFirstAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getFirst());
                    if(isUpdated)
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else{
                    listItems.get(position).setSecond(MainActivity.getCurrentTime());
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated)
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
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

                    Integer count = 0;

                    for(Faculty faculty1 : faculties){

                        boolean isInserted = MainActivity.myDB.updateFaculty(faculty1.getFACULTY_ID(), faculty1.getNAME(),faculty1.getDEPARTMENT(), faculty1.getCOLLEGE());
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
                    boolean isInserted = MainActivity.myDB.updateRoute(route1.getROUTE_ID(), route1.getDESCRIPTION());
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
                    boolean isInserted = MainActivity.myDB.updateClassSchedule(classSchedule1.getCLASS_SCHEDULE_ID(), classSchedule1.getROOM_ID(), classSchedule1.getFACULTY_ID(), classSchedule1.getSEMESTER(), classSchedule1.getSCHOOL_YEAR(), classSchedule1.getSTART_TIME(), classSchedule1.getEND_TIME(), classSchedule1.getCLASS_SECTION(), classSchedule1.getCLASS_DAY(), classSchedule1.getSUBJECT_CODE(), classSchedule1.getHALF_DAY(), classSchedule1.getHOURS());
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

    private void updateRoom(){
        Room room = new Room(userStaffId, userToken);

        Call<List<Room>> call = ahcfamsApi.room(userStaffId,userToken);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                List<Room> rooms = response.body();
                Integer count = 0;

                for (Room room1 : rooms){
                    boolean isInserted = MainActivity.myDB.updateRoom(room1.getROOM_ID(), room1.getROUTE_ID(), room1.getBUILDING_NAME(), room1.getROOM_ORDER());
                    if (isInserted){
                        count += 1;
                    }else
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {

            }
        });

    }
}
