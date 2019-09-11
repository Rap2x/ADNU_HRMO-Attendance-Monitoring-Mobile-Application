package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.app.Activity;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner routeSpinner;
    private Button routeSaveButton;

    private ArrayList<String> routeArray;

    private Cursor routes;
    int currentPosition;

    private static ProgressBar classScheduleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        createOptionMenu();

        bindData();
        onClickListener();

        createSpinnerChoice();

    }

    private void bindData(){
        routeSpinner = (Spinner) findViewById(R.id.routeSpinner);
        routeSpinner.setOnItemSelectedListener(this);

        routeSaveButton = (Button) findViewById(R.id.routeSave);
        classScheduleProgress = (ProgressBar) findViewById(R.id.classScheduleProgress);
    }

    private void onClickListener(){
        routeSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.myDB.changeRoute(MainActivity.userStaffId, MainActivity.userRoute)){
                    Toast.makeText(getApplicationContext(), "Route Changed to " + MainActivity.userRoute, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(), "Route changing error", Toast.LENGTH_SHORT).show();
            }
        });
        classScheduleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceList.updateClassSchedule();

                /*while(MainActivity.currentClassScheduleCount != MainActivity.classScheduleCount){
                    double classSchedulePercent = (MainActivity.currentClassScheduleCount / MainActivity.classScheduleCount) * 100;
                    classScheduleProgress.setProgress((int) Math.ceil(classSchedulePercent));
                } */
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        MainActivity.userRoute = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void createOptionMenu(){
        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createSpinnerChoice(){
        routeArray = new ArrayList<>();
        routes = MainActivity.myDB.getRoutes();

        for (int i = 0; i < routes.getCount(); i++){
            routeArray.add(routes.getString(0));

            if(!routes.isLast())
                routes.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.route_spinner_item, routeArray);
        routeSpinner.setAdapter(adapter);
    }
}
