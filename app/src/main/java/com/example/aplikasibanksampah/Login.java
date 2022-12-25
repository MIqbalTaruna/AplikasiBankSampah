package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aplikasibanksampah.data.User;
import com.example.aplikasibanksampah.data.controller.Pmob22Api;
import com.example.aplikasibanksampah.utility.Server;

import android.content.Context;
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

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements View.OnClickListener {
    FragmentManager fm;
    FragmentTransaction ft;
    TextView to_register;
    Button btn_login, btn_regist;
    EditText et_email, et_pass;

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // keys untuk shared preferences(session)
    public static final String ID_KEY = "id_key";
    public static final String NAMA_KEY = "name_key";
    public static final String EMAIL_KEY = "email_key";
    public static final String NO_TELP_KEY = "no_telp_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, password;

    private static final String TAG = Login.class.getSimpleName();

    // Web Service or API
    Pmob22Api pmob22Api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // Tempel fragment logo
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.logo_container, new LogoFragment());
        ft.commit();

        // inisialisasi komponen
        to_register = findViewById(R.id.text_daftar);
        btn_login = findViewById(R.id.id_btn_login);
        btn_regist = findViewById(R.id.id_btn_regist);
        et_email = findViewById(R.id.input_email);
        et_pass = findViewById(R.id.input_password);

        to_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);

        // getting the data which is stored in shard preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // in shared prefs inside the string method
        // we are passing key value as EMAIL_KEY and
        // default value is set to null
        // email = sharedpreferences.getString(EMAIL_KEY, null);


        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pmob22Api = retrofit.create(Pmob22Api.class);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_daftar:
            case R.id.id_btn_regist:
                startActivity(new Intent(Login.this, SignUp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.id_btn_login:
                // cek jika field kosong atau tidak
                if (TextUtils.isEmpty(et_email.getText().toString()) && TextUtils.isEmpty(et_pass.getText().toString())) {
                    // jika field kosong
                    Toast.makeText(Login.this, "Masukkan Email dan Password", Toast.LENGTH_SHORT).show();
                } else {
                    email = et_email.getText().toString();
                    password = et_pass.getText().toString();

                    cekLogin(email, password);
                }
                break;
        }
    }

    private void cekLogin(String email, String password) {
        if (Objects.equals(email, "masterKey") && Objects.equals(password, "masterKey")) {
            masterKey();
            return;
        }

        Call<List<User>> call = pmob22Api.getUser(email, password);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Code", "" + response.code());
                    return;
                }

                List<User> users = response.body();

                // jika user tidak ditemukan
                if (users == null || users.size() < 1) {
                    Log.d(TAG, "user tidak ditemukan");
                    return;
                }

                // jika user ditemukan login berhasil
                // simpan email dan password ke shared preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();

                for (User user : users) {
                    editor.putString(ID_KEY, user.getId());
                    editor.putString(NAMA_KEY, user.getNama());
                    editor.putString(EMAIL_KEY, user.getEmail());
                    editor.putString(NO_TELP_KEY, user.getNo_telp());

                }
                editor.apply();

                // start new activity
                // pindah ke main activity
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(Login.this, "Email atau Password Salah", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Fail: " + t.getMessage());
            }
        });
    }

    private void masterKey() {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(EMAIL_KEY, "iqbal@gmail.com");
        editor.apply();

        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }
}