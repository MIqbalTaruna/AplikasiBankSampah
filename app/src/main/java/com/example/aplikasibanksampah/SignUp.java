package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasibanksampah.data.User;
import com.example.aplikasibanksampah.data.controller.Pmob22Api;
import com.example.aplikasibanksampah.utility.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    // component xml
    EditText et_nama, et_no_telp, et_email, et_password, et_password_ulang;
    Button signup_button, success_button;

    // variable
    String nama, no_telp, email, password, password_ulang;

    // Web Service or API
    Pmob22Api pmob22Api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // Tempel fragment logo
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.logo_container, new LogoFragment());
        ft.commit();

        // Link ke halaman login
        TextView to_login = findViewById(R.id.text_login);
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pmob22Api = retrofit.create(Pmob22Api.class);

        // Hubungkan dengan xml
        et_nama = findViewById(R.id.input_nama);
        et_no_telp = findViewById(R.id.input_no_telp);
        et_email = findViewById(R.id.input_email);
        et_password = findViewById(R.id.input_password1);
        et_password_ulang = findViewById(R.id.input_password2);
        signup_button = findViewById(R.id.btn_daftar);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ambil inputan user
                nama = et_nama.getText().toString();
                no_telp = et_no_telp.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                password_ulang = et_password_ulang.getText().toString();

                if(isFormEmpty(new String[]{nama, email, no_telp, password, password_ulang})){
                    // jika ada field kosong
                    Toast.makeText(SignUp.this, "Terdapat Field Kosong", Toast.LENGTH_SHORT).show();
                } else if(!TextUtils.equals(password, password_ulang)){
                    // jika password dan password ulang berbeda
                    Toast.makeText(SignUp.this, "Password yang diinput berbeda", Toast.LENGTH_SHORT).show();
                }else{
                    // registrasi user
                    Registrasi(nama, no_telp, email, password);
                }
            }
        });

    }

    @Override
    public void finish(){
        Intent login = new Intent(SignUp.this, Login.class);
        startActivity(login);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }

    private boolean isFormEmpty(String[] inputs){
        for(String input : inputs){
            if(TextUtils.isEmpty(input)){
                return true;
            }
        }
        return false;
    }

    private void Registrasi(String nama, String no_telp, String email, String password){
        Call<User> call = pmob22Api.createUser(nama, no_telp, email, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if(!response.isSuccessful()){
                    Log.d("Code", "" + response.code());
                    return;
                }

                User userResponse = response.body();
                String TAG = "Registrasi";
                Log.d(TAG, "Code -> " + response.code());
                Log.d(TAG, "nama -> " + userResponse.getNama());
                Log.d(TAG, "no_telp -> " + userResponse.getNo_telp());
                Log.d(TAG, "email -> " + userResponse.getEmail());
                Log.d(TAG, "password -> " + userResponse.getPassword());

                Toast.makeText(SignUp.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.regist_berhasil);
                success_button = findViewById(R.id.btn_daftar_success);
                success_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUp.this, "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                Log.d(SignUp.class.getSimpleName(), "Fail: " + t.getMessage());
            }
        });
    }
}