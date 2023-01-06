package com.example.aplikasibanksampah.data.controller;

import com.example.aplikasibanksampah.data.Barang;
import com.example.aplikasibanksampah.data.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Pmob22Api {

    // ambil data user sesuai email dan password
    // SELECT * FROM user WHERE email = '$email' and password = '$password';
    @GET("user/select.php")
    Call<List<User>> getUser(
            @Query("email") String email,
            @Query("password") String password
    );

    // kirim data user
    @FormUrlEncoded
    @POST("user/sign_up.php")
    Call<User> createUser(
            @Field("nama") String nama,
            @Field("no_telp") String no_telp,
            @Field("email") String email,
            @Field("password") String password
    );

    // ambil data barang sesuai kategori
    // jangan tampilkan untuk pemilik barang
    // SELECT * FROM barang WHERE id_pemilik != '$id_user' and id_kategori = '$id_kategori';
    @GET("user/barang.php")
    Call<List<Barang>> getBarang(
            @Query("id_pemilik") String id_pemilik,
            @Query("id_kategori") String id_kategori
    );
}
