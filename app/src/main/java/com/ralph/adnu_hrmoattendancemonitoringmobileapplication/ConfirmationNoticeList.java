package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationNoticeList extends AppCompatActivity {

    private TextView fullName;
    private TextView department;
    private TextView college;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ConfirmationNoticeListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_notice_list);
        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        fullName = findViewById(R.id.name);
        department = findViewById(R.id.department);
        college = findViewById(R.id.college);

        String faculty_id = getIntent().getStringExtra("faculty_id");
        Cursor facultyDetails = MainActivity.myDB.getFacultyDetails(faculty_id);

        fullName.setText("Name: " + facultyDetails.getString(1));
        department.setText("Department: " + facultyDetails.getString(2));
        college.setText("College: " + facultyDetails.getString(3));

        Cursor confirmationNoticeData = MainActivity.myDB.getConfirmationNotice(faculty_id);
        confirmationNoticeData.moveToFirst();

        recyclerView = (RecyclerView) findViewById(R.id.confirmation_notice_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        Integer count = confirmationNoticeData.getCount();
        Toast.makeText(getApplicationContext(), count.toString(), Toast.LENGTH_SHORT).show();

        for(int i = 0; i < confirmationNoticeData.getCount(); i++){
            ConfirmationNoticeListItem listItem = new ConfirmationNoticeListItem(
                    confirmationNoticeData.getString(0),
                    confirmationNoticeData.getString(1),
                    confirmationNoticeData.getString(2),
                    confirmationNoticeData.getString(3),
                    confirmationNoticeData.getString(4)
            );
            listItems.add(listItem);
        }

        adapter = new ConfirmationNoticeAdapter(listItems,this);
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
}
