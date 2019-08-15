package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;

    public final static String ip = "192.168.1.4"; //Change this accordingly

    EditText eUsername;
    EditText ePassword;

    private String username = "sample";
    private String password = "sample";

    private AhcfamsApi ahcfamsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/auth/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);

        addEditTextListener();

        final Button signIn = findViewById(R.id.SignIn);
        signIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                login();
            }
        });
    }

    public void addEditTextListener(){
        eUsername = (EditText) findViewById(R.id.editTextUsername);
        ePassword = (EditText) findViewById(R.id.editTextPassword);

        eUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();

                if(username == ""){
                    username = "empty";
                }
                Log.d("Username: ", username);
            }
        });

        ePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();

                if(password == ""){
                    password = "empty";
                }
                Log.d("Password: ", password);
            }
        });
    }

    private void login(){
        Log.d("username & password", username + " " + password);
        Login login = new Login(username, password);
        Call<Login> call = ahcfamsApi.login(username, password);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Login loginResponse = response.body();
                    Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if(loginResponse.getStatus().equals("200")){
                        boolean isInserted = myDB.loginStaff(loginResponse.getUsername(), loginResponse.getToken(), loginResponse.getDateTime());

                        if(isInserted == true) {
                            Intent in = new Intent(MainActivity.this, AttendanceList.class);
                            startActivity(in);
                            Toast.makeText(getApplicationContext(), "Database Updated", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Error: Database not Updated", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }


}
