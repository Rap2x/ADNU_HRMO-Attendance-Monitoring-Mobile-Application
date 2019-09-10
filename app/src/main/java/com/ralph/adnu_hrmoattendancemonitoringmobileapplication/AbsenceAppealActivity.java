package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AbsenceAppealActivity extends AppCompatActivity {

    private String confirmationNoticeId;
    private String facultyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_appeal);

        confirmationNoticeId = getIntent().getStringExtra("confirmation_notice_id");
        facultyId = getIntent().getStringExtra("faculty_id");


    }
}
