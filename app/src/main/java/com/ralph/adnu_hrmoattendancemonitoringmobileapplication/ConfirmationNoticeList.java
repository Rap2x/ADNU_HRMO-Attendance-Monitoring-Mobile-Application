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
    private ConfirmationNoticeAdapter adapter;

    private List<ConfirmationNoticeListItem> listItems;

    private String faculty_id = null;

    private Cursor confirmationNoticeData = null;
    private Cursor facultyDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_notice_list);

        createOptionMenu();

        showConfirmationNoticeList();

        showFacultyDetails();

        onClickListener();
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

    private void createOptionMenu(){
        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    private void onClickListener(){
        adapter.setOnItemClickListener(new ConfirmationNoticeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getBaseContext(), Signature.class);
                String confirmationNoticeId = listItems.get(position).getConfirmationNoticeId();
                intent.putExtra("faculty_id", faculty_id);
                intent.putExtra("confirmation_notice_id", confirmationNoticeId);
                startActivity(intent);
            }
        });
    }

    private void showConfirmationNoticeList(){ //Call this function before showFacultyDetails()
        faculty_id = getIntent().getStringExtra("faculty_id");
        facultyDetails = MainActivity.myDB.getFacultyDetails(faculty_id);

        confirmationNoticeData = MainActivity.myDB.getAllConfirmationNoticeOfAFaculty(faculty_id);
        confirmationNoticeData.moveToFirst();

        recyclerView = (RecyclerView) findViewById(R.id.confirmation_notice_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        for(int i = 0; i < confirmationNoticeData.getCount(); i++){
            ConfirmationNoticeListItem listItem = new ConfirmationNoticeListItem(
                    confirmationNoticeData.getString(0),
                    confirmationNoticeData.getString(1),
                    confirmationNoticeData.getString(2),
                    confirmationNoticeData.getString(3)+ "." + confirmationNoticeData.getString(5),
                    confirmationNoticeData.getString(4)
            );
            listItems.add(listItem);
        }

        adapter = new ConfirmationNoticeAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
    }

    private void showFacultyDetails(){
        fullName = findViewById(R.id.name);
        department = findViewById(R.id.department);
        college = findViewById(R.id.college);

        fullName.setText("Name: " + facultyDetails.getString(1));
        department.setText("Department: " + facultyDetails.getString(2));
        college.setText("College: " + facultyDetails.getString(3));
    }
}
