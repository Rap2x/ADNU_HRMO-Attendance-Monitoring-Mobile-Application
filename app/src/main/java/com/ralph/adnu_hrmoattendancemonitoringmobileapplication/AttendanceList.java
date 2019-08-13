package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceList extends FragmentActivity {
    DatabaseHelper myDB;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<AttendanceListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

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



}
