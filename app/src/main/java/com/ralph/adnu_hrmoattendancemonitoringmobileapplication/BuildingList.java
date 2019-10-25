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

        showList();
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

        for(int i = 0; i < buildingData.getCount(); i++){
            BuildingListItem listItem = new BuildingListItem(buildingData.getString(0));
            if(MainActivity.myDB.isClassAvailable(buildingData.getString(0))){
                listItems.add(listItem);
            }

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

    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        Intent in = new Intent(getBaseContext(), MainActivity.class);
        startActivity(in);
    }
}
