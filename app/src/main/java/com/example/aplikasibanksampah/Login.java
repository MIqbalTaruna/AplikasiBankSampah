package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{
    FragmentManager fm;
    FragmentTransaction ft;
    TextView to_register;
    Button btn_login;
    EditText et_email, et_pass;

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String EMAIL_KEY = "email_key";

    // key for storing password.
    public static final String PASSWORD_KEY = "password_key";

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // Tempel fragment logo
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.logo_container, new LogoFragment());
        ft.commit();

        // inisialisasi komponen
        to_register = findViewById(R.id.text_daftar);
        btn_login = findViewById(R.id.id_btn_login);
        et_email = findViewById(R.id.input_email);
        et_pass = findViewById(R.id.input_password);

        to_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        // getting the data which is stored in shard preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // in shared prefs inside the string method
        // we are passing key value as EMAIL_KEY and
        // default value is set to null
        email = sharedpreferences.getString(EMAIL_KEY, null);
        password = sharedpreferences.getString(PASSWORD_KEY, null);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_daftar:
//                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(new Intent(Login.this, SignUp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.id_btn_login:
                // cek jika field kosong atau tidak
                if(TextUtils.isEmpty(et_email.getText().toString()) && TextUtils.isEmpty(et_pass.getText().toString())){
                    // jika field kosong
                    Toast.makeText(Login.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    // simpan email dan password ke shard preferences
                    editor.putString(EMAIL_KEY, et_email.getText().toString());
                    editor.putString(PASSWORD_KEY, et_pass.getText().toString());
                    editor.apply();;

                    // start new activity
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                break;
        }
    }
}