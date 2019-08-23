package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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


}
