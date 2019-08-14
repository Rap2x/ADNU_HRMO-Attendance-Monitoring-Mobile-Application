package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class AttendanceList extends AppCompatActivity {
    DatabaseHelper myDB;

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


        myDB = new DatabaseHelper(this);


        ArrayList userCredential = myDB.getUserCredentials();
        userStaffId = userCredential.get(0).toString();
        userToken = userCredential.get(1).toString();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + MainActivity.ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/auth/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        for(int i = 0 ;i <=100; i++){
            AttendanceListItem listItem = new AttendanceListItem(
                    "name " + i+1,
                    "subjectCode" + i+1,
                    "roomNumber" + i+1
            );

            listItems.add(listItem);
        }
        adapter = new AttendanceAdapter(listItems, this);

        recyclerView.setAdapter(adapter);
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

    private void updateFaculty(){
        Faculty faculty = new Faculty(userStaffId, userToken);
        Call<Faculty> call = ahcfamsApi.faculty(userStaffId,userToken);

        call.enqueue(new Callback<Faculty>() {
            @Override
            public void onResponse(Call<Faculty> call, Response<Faculty> response) {
                
            }

            @Override
            public void onFailure(Call<Faculty> call, Throwable t) {

            }
        });
    }




}
