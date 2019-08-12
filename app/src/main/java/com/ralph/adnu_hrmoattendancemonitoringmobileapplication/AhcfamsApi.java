package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

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
}
