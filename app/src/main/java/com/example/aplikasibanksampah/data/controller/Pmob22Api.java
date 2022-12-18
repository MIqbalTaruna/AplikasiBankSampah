package com.example.aplikasibanksampah.data.controller;

import com.example.aplikasibanksampah.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Pmob22Api {

    @GET("user/{fileName}")
    Call<List<User>> getUser(
            @Path("fileName") String fileName,
            @Query("email") String email,
            @Query("password") String password
    );

}
