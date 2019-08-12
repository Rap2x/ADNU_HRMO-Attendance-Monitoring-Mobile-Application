package com.icst241.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;

    public final String ip = "192.168.1.2";

    private EditText eusername;
    private EditText epassword;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        eusername = (EditText) findViewById(R.id.editTextUsername);
        epassword = (EditText) findViewById(R.id.editTextPassword);

        eusername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }
        });

        epassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });

        final String url = "http://"+ip+"/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/api/test";

        final Button signIn = findViewById(R.id.SignIn);
        signIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            }
        });
    }

    public void signIn(View view){
        Intent intent = new Intent(this, AttendanceList.class);
        startActivity(intent);
    }


}
