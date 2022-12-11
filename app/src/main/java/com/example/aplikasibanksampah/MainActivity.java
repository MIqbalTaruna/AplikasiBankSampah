package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private int waktu_loading = 3000;
    // 1000ms = 1s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // setelah loading akan langsung berpindah ke halaman login
                Intent login = new Intent(MainActivity.this, Login.class);
                startActivity(login);
                finish();
            }
        }, waktu_loading);
    }
}