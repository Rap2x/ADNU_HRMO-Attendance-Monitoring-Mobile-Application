package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AhcfamsApi {

    @FormUrlEncoded
    @POST("login")
    Call<Login>login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("faculty")
    Call<List<Faculty>> faculty(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("route")
    Call<List<Route>> route(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("class_schedule")
    Call<List<ClassSchedule>> classSchedule(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("room")
    Call<List<Room>> room(
            @Field("id") String id,
            @Field("token") String token
    );

    @Multipart
    @POST("post_faculty_attendance")
    Call<FacultyAttendance> faculty_attendance(
            @Part("id")RequestBody id,
            @Part("token")RequestBody token,
            @Part("faid")RequestBody faid,
            @Part("sid")RequestBody sid,
            @Part("csid")RequestBody csid,
            @Part("adate")RequestBody adate,
            @Part("fcheck")RequestBody fcheck,
            @Part("scheck")RequestBody scheck,
            @Part MultipartBody.Part fipath,
            @Part MultipartBody.Part sipath,
            @Part("sdeduct")RequestBody sdeduct,
            @Part("status")RequestBody status
            );

    @Multipart
    @POST("post_confirmation_notice")
    Call<ConfirmationNotice> confirmation_notice(
            @Part("id")RequestBody id,
            @Part("token")RequestBody token,
            @Part("cid")RequestBody cid,
            @Part("remarks")RequestBody remarks,
            @Part("reason") RequestBody reason,
            @Part MultipartBody.Part spath
    );

    @FormUrlEncoded
    @POST("get_faculty_attendance")
    Call<List<FacultyAttendance>> faculty_attendance(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("absence_appeal")
    Call<List<AbsenceAppeal>> absence_appeal(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("confirmation_notice")
    Call<List<ConfirmationNotice>> confirmation_notice(
            @Field("id") String id,
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("post_absence_appeal")
    Call<AbsenceAppeal> absence_appeal(
            @Field("id") String id,
            @Field("token") String token,
            @Field("aid") String aid,
            @Field("cid") String cid,
            @Field("staff_id") String staff_id,
            @Field("chair_id") String chair_id,
            @Field("absence_reason") String absence_reason,
            @Field("validated") String validated,
            @Field("remarks") String remarks
    );

    @FormUrlEncoded
    @POST("get_all_count")
    Call<TableRowCounts> table_row_counts(
            @Field("id") String id,
            @Field("token") String token
    );
}
