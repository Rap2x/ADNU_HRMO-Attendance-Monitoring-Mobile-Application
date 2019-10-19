package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Color.RED;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AttendanceList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;

    private List<AttendanceListItem> listItems;

    private static AhcfamsApi ahcfamsApi;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    private String buildingName;

    private static String info;

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
            attendanceData.moveToNext();
        }
        adapter = new AttendanceAdapter(listItems, this);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AttendanceAdapter.OnItemClickListener() {
            Integer set = 0;

            @Override
            public void setAbsent(int position) {

                if(set == 0){
                    listItems.get(position).setFirst("Absent");
                    boolean isUpdated = MainActivity.myDB.checkFirstAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getFirst());
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        dispatchTakePictureIntent();

                        info = "Name: " + listItems.get(position).getName() + " Subject Code: " + listItems.get(position).getSubjectCode() + " Room: " + listItems.get(position).getRoomNumber() + " Class Schedule: " + listItems.get(position).getClassTime();
                        MainActivity.myDB.saveImage(listItems.get(position).getFacultyAttendance_Id(), "first_image_file",currentPhotoPath,"first_check", MainActivity.getCurrentTime12Hours());
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else if(set == 1){
                    listItems.get(position).setSecond("Absent");
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        dispatchTakePictureIntent();
                        info = "Name: " + listItems.get(position).getName() + " Subject Code: " + listItems.get(position).getSubjectCode() + " Room: " + listItems.get(position).getRoomNumber() + " Class Schedule: " + listItems.get(position).getClassTime();
                        MainActivity.myDB.saveImage(listItems.get(position).getFacultyAttendance_Id(), "second_image_file", currentPhotoPath, "second_check", MainActivity.getCurrentTime12Hours());
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                //listItems.remove(position);
                //adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(position);
                if(adapter.getItemCount() == 0){
                    finish();
                }
            }

            @Override
            public void setPresent(int position) {
                if(set == 0){
                    listItems.get(position).setFirst(MainActivity.getCurrentTime12Hours());
                    boolean isUpdated = MainActivity.myDB.checkFirstAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getFirst());
                    boolean isCleared = MainActivity.myDB.clearFirstImage(listItems.get(position).getFacultyAttendance_Id());
                    if(isUpdated && isCleared) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }else if(set == 1){
                    listItems.get(position).setSecond(MainActivity.getCurrentTime12Hours());
                    boolean isUpdated = MainActivity.myDB.checkSecondAttendance(listItems.get(position).getFacultyAttendance_Id(),listItems.get(position).getSecond());
                    boolean isCleared = MainActivity.myDB.clearSecondImage(listItems.get(position).getFacultyAttendance_Id());
                    if(isUpdated && isCleared) {
                        Toast.makeText(getApplicationContext(), "Local Database Update", Toast.LENGTH_SHORT).show();
                        MainActivity.myDB.changeAttendanceStatus(listItems.get(position).getFacultyAttendance_Id());
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Error: Local Database not Updated", Toast.LENGTH_SHORT).show();
                }
                //listItems.remove(position);
                //adapter.notifyItemRemoved(position);
                adapter.notifyItemChanged(position);
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

            @Override
            public void onRadioButtonClicked(int position, View view) {
                boolean checked = ((RadioButton)view).isChecked();

                switch (view.getId()){
                    case R.id.firstCheck:
                        if(checked){
                            set = 0;
                        }
                        break;

                    case R.id.secondCheck:
                        if(checked){
                            set = 1;
                        }
                        break;
                }
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
            compressImage(currentPhotoPath);
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + MainActivity.userRoute + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void compressImage(String photoPath){
        File dir = new File(photoPath);
        if(dir.exists()){
            Toast.makeText(getApplicationContext(), "Current Photo Path", Toast.LENGTH_SHORT).show();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap image = BitmapFactory.decodeFile(photoPath, options);
            image = addWaterMark(image);

            try(FileOutputStream out = new FileOutputStream(photoPath)){
                image.compress(Bitmap.CompressFormat.JPEG, 20, out);
            }catch(IOException e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static Bitmap addWaterMark(Bitmap source){
        int w = source.getWidth();
        int h = source.getHeight();

        Bitmap result = Bitmap.createBitmap(w, h, source.getConfig());

        //Location of the watermark in the photo
        float x = (float) (w * 0.6);
        float y = (float) (h * 0.95);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(source, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(RED);
        paint.setTextSize(50);
        paint.setAntiAlias(true);
        canvas.drawText("Date: " + MainActivity.getCurrentDate() + " Time: " + MainActivity.getCurrentTime12Hours() + " " + info,x, y, paint);

        return result;
    }
}
