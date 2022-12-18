package com.example.aplikasibanksampah;

import static com.example.aplikasibanksampah.Login.EMAIL_KEY;
import static com.example.aplikasibanksampah.Login.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.aplikasibanksampah.utility.Server;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email;

    // Bottom Navigation
    BottomNavigationView bottomNavigation;

    // Web Service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // Ambil variable shared preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        Log.d("EMAIL_KEY", "" + email);

        // Bottom navigation
        bottomNavigation = findViewById(R.id.bottom_nav);

        // fragment default yaitu home
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        // switch case untuk pindah fragment
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        Log.d("Fragment", "Ini Fragment Home");
                        break;
                    case R.id.nav_notif:
                        selectedFragment = new NotificationsFragment();
                        Log.d("Fragment", "Ini Fragment Notifikasi");
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        Log.d("Fragment", "Ini Fragment Profile");
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(email == null){
            startActivity(new Intent(MainActivity.this, SplashScreen.class));
            finish();
        }
    }
}