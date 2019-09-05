package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import java.net.URI;
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

import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.text.TextUtils.isEmpty;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AttendanceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;

    private List<AttendanceListItem> listItems;
    private List<FacultyAttendance> attendanceItems;

    private AhcfamsApi ahcfamsApi;

    private String userStaffId;
    private String userToken;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        android.support.v7.widget.Toolbar toolbar =findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ArrayList userCredential = MainActivity.myDB.getUserCredentials();
        userStaffId = userCredential.get(0).toString();
        userToken = userCredential.get(1).toString();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + MainActivity.ip + "/ADNU_HRMO-College-Faculty-Attendance-Monitoring-System/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ahcfamsApi = retrofit.create(AhcfamsApi.class);

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

            case R.id.update_database:
                updateFaculty();
                updateRoute();
                updateRoom();
                updateClassSchedule();
                break;
            case R.id.upload_faculty_attendance:
                uploadFacultyAttendance();
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        Cursor attendanceData = MainActivity.myDB.getAttendanceList();

        attendanceData.moveToFirst();

        for(int i = 0 ;i < attendanceData.getCount(); i++){
            AttendanceListItem listItem = new AttendanceListItem(
                    attendanceData.getString(3),
                    attendanceData.getString(2),
                    attendanceData.getString(1),
                    attendanceData.getString(4),
                    attendanceData.getString(0),
                    attendanceData.getString(5),
                    attendanceData.getString(6)
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
                        MainActivity.myDB.saveImage(listItems.get(position).getFacultyAttendance_Id(), "first_image_file", currentPhotoPath);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else{
                    listItems.get(position).setSecond("Absent");
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        dispatchTakePictureIntent();
                        MainActivity.myDB.saveImage(listItems.get(position).getFacultyAttendance_Id(), "second_image_file", currentPhotoPath);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void setPresent(int position) {
                if (isEmpty(listItems.get(position).getFirst())){
                    listItems.get(position).setFirst(MainActivity.getCurrentTime12Hours());
                    boolean isUpdated = MainActivity.myDB.checkFirstAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getFirst());
                    if(isUpdated)
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else{
                    listItems.get(position).setSecond(MainActivity.getCurrentTime12Hours());
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated)
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                listItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
    }

    private void updateFaculty(){
        Faculty faculty = new Faculty(userStaffId, userToken);
        Call<List<Faculty>> call = ahcfamsApi.faculty(userStaffId,userToken);

        call.enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }else{
                    List<Faculty> faculties = response.body();

                    Integer count = 0;

                    for(Faculty faculty1 : faculties){

                        boolean isInserted = MainActivity.myDB.updateFaculty(faculty1.getFACULTY_ID(), faculty1.getNAME(),faculty1.getDEPARTMENT(), faculty1.getCOLLEGE());
                        if (isInserted){
                            count += 1;
                            Toast.makeText(getApplicationContext(),count.toString() , Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {

            }
        });
    }

    private void updateRoute(){
        Route route = new Route(userStaffId, userToken);
        Call<List<Route>> call = ahcfamsApi.route(userStaffId, userToken);

        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                List<Route> routes = response.body();

                Integer count = 0;

                for (Route route1 : routes){
                    boolean isInserted = MainActivity.myDB.updateRoute(route1.getROUTE_ID(), route1.getDESCRIPTION());
                    if(isInserted) {
                        count += 1;
                        Toast.makeText(getApplicationContext(), "Routes: " + count.toString(), Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplicationContext(),"Routes: ERROR" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {

            }
        });
    }

    private void updateClassSchedule(){
        ClassSchedule classSchedule = new ClassSchedule(userStaffId, userToken);
        Call<List<ClassSchedule>> call = ahcfamsApi.classSchedule(userStaffId,userToken);

        call.enqueue(new Callback<List<ClassSchedule>>() {
            @Override
            public void onResponse(Call<List<ClassSchedule>> call, Response<List<ClassSchedule>> response) {
                List<ClassSchedule> classSchedules = response.body();

                Integer count = 0;
                for (ClassSchedule classSchedule1 : classSchedules){
                    boolean isInserted = false;
                    try {
                        isInserted = MainActivity.myDB.updateClassSchedule(classSchedule1.getCLASS_SCHEDULE_ID(), classSchedule1.getROOM_ID(), classSchedule1.getFACULTY_ID(), classSchedule1.getSEMESTER(), classSchedule1.getSCHOOL_YEAR(), MainActivity.time12HourTo24Hour(classSchedule1.getSTART_TIME()), MainActivity.time12HourTo24Hour(classSchedule1.getEND_TIME()), classSchedule1.getCLASS_SECTION(), classSchedule1.getCLASS_DAY(), classSchedule1.getSUBJECT_CODE(), classSchedule1.getHALF_DAY(), classSchedule1.getHOURS());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(isInserted){
                        count += 1;
                        Toast.makeText(getApplicationContext(), "Class Schedule: " + count, Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplicationContext(), "Class Schedule Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<ClassSchedule>> call, Throwable t) {

            }
        });
    }

    private void updateRoom(){
        Room room = new Room(userStaffId, userToken);

        Call<List<Room>> call = ahcfamsApi.room(userStaffId,userToken);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                List<Room> rooms = response.body();
                Integer count = 0;

                for (Room room1 : rooms){
                    boolean isInserted = MainActivity.myDB.updateRoom(room1.getROOM_ID(), room1.getROUTE_ID(), room1.getBUILDING_NAME(), room1.getROOM_ORDER());
                    if (isInserted){
                        count += 1;
                    }else
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {

            }
        });

    }

    private void uploadFacultyAttendance(){
        attendanceItems = new ArrayList<>();

        final Cursor facultyAttendance = MainActivity.myDB.getFacultyAttendance();

        facultyAttendance.moveToFirst();

        for (int i = 0; i < facultyAttendance.getCount(); i++){
            File file1 = new File(facultyAttendance.getString(6));
            File file2 = new File(facultyAttendance.getString(7));
            MultipartBody.Part firstImage;
            MultipartBody.Part secondImage;

            if(isEmpty(facultyAttendance.getString(6))){ // checks if there's no image
                /* Send an empty image*/
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), "");

                firstImage = MultipartBody.Part.createFormData("fipath", "", fileReqBody1);
            }else{
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                firstImage = MultipartBody.Part.createFormData("fipath", file1.getName(), fileReqBody1);
            }

            if(isEmpty(facultyAttendance.getString(7))){
                RequestBody fileReqBody2 = RequestBody.create(MediaType.parse("image/*"), "");
                secondImage = MultipartBody.Part.createFormData("sipath", "", fileReqBody2);
            }else{
                RequestBody fileReqBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                secondImage = MultipartBody.Part.createFormData("sipath", file2.getName(), fileReqBody2);
            }

            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userStaffId);
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), userToken);
            RequestBody faid = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(0));
            RequestBody sid = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(1));
            RequestBody csid = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(2));
            RequestBody adate = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(3));
            RequestBody fcheck = null;
            if(isEmpty(facultyAttendance.getString(4))){
                fcheck = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(4));
                Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            }else if(facultyAttendance.getString(4) == "Absent"){
                fcheck = RequestBody.create(MediaType.parse("text/plain"), "null");
            }else{
                try {
                    fcheck = RequestBody.create(MediaType.parse("text/plain"), MainActivity.convertTimeToDateTime(facultyAttendance.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            RequestBody scheck = null;
            if(isEmpty(facultyAttendance.getString(5))){
                scheck = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(5));
            }else if(facultyAttendance.getString(5) == "Absent"){
                scheck = RequestBody.create(MediaType.parse("text/plain"), "null");
            }else{
                try {
                    scheck = RequestBody.create(MediaType.parse("text/plain"), MainActivity.convertTimeToDateTime(facultyAttendance.getString(5)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            RequestBody sdeduct = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(8));
            RequestBody status = RequestBody.create(MediaType.parse("text/plain"), facultyAttendance.getString(9));

            Call call = ahcfamsApi.faculty_attendance(id, token, faid, sid, csid, adate, fcheck, scheck, firstImage, secondImage, sdeduct, status);

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            //if(MainActivity.myDB.changeSync(facultyAttendance.getString(0))){}
            if(!facultyAttendance.isLast())
                facultyAttendance.moveToNext();
        }



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

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
