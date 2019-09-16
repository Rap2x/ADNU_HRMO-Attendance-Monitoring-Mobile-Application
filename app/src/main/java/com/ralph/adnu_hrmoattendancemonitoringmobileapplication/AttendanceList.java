package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

@RequiresApi(api = Build.VERSION_CODES.N)
public class AttendanceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;

    private List<AttendanceListItem> listItems;

    private static AhcfamsApi ahcfamsApi;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    private String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        buildingName = getIntent().getStringExtra("building_name");

        createOptionMenu();

        showList();
        createRetrofitClient();

        showList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_refresh:
                showList();
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

    private void showList(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        Cursor attendanceData = MainActivity.myDB.getAttendanceList(buildingName);

        attendanceData.moveToFirst();

        for(int i = 0 ;i < attendanceData.getCount(); i++){
            String noticeCount = MainActivity.myDB.getConfirmationNoticeCount(attendanceData.getString(7));
            AttendanceListItem listItem = new AttendanceListItem(
                    attendanceData.getString(3),
                    attendanceData.getString(2),
                    attendanceData.getString(1),
                    attendanceData.getString(4),
                    attendanceData.getString(0),
                    attendanceData.getString(5),
                    attendanceData.getString(6),
                    attendanceData.getString(7),
                    noticeCount
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
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        dispatchTakePictureIntent();
                        MainActivity.myDB.saveImage(listItems.get(position).getFacultyAttendance_Id(), "first_image_file",currentPhotoPath,"first_check", MainActivity.getCurrentTime12Hours());
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else{
                    listItems.get(position).setSecond("Absent");
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        dispatchTakePictureIntent();
                        MainActivity.myDB.saveImage(listItems.get(position).getFacultyAttendance_Id(), "second_image_file", currentPhotoPath, "second_check", MainActivity.getCurrentTime12Hours());
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
                if(adapter.getItemCount() == 0){
                    finish();
                }
            }

            @Override
            public void setPresent(int position) {
                if (isEmpty(listItems.get(position).getFirst())){
                    listItems.get(position).setFirst(MainActivity.getCurrentTime12Hours());
                    boolean isUpdated = MainActivity.myDB.checkFirstAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getFirst());
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else{
                    listItems.get(position).setSecond(MainActivity.getCurrentTime12Hours());
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
                if(adapter.getItemCount() == 0){
                    finish();
                }
            }

            @Override
            public void viewConfirmationNotice(int position) {
                Intent intent = new Intent(getBaseContext(), ConfirmationNoticeList.class);
                String faculty_id = listItems.get(position).getFaculty_id();
                intent.putExtra("faculty_id", faculty_id);
                startActivity(intent);
            }
        });
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException ex){
                Toast.makeText(getApplicationContext(), "Image Not Created", Toast.LENGTH_SHORT).show();
            }
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(AttendanceList.this, "com.ralph.adnu_hrmoattendancemonitoringmobileapplication.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }else
                Toast.makeText(getApplicationContext(), "ERROR!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" ;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
