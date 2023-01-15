package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasibanksampah.data.Barang;
import com.example.aplikasibanksampah.data.controller.Pmob22Api;
import com.example.aplikasibanksampah.utility.Server;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TambahBarangActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    EditText etNamaBarang, etHarga, etDeskripsi, etKategori;
    String strNamaBarang, strHarga, strDeksripsi, strKategori;
    Button btnBatal, btnTambah;

    SharedPreferences sharedPreferences;
    String id_user;

    Pmob22Api pmob22Api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        if(getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // Retrofit untuk API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pmob22Api = retrofit.create(Pmob22Api.class);

        // Tempel fragment logo
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.logo_container, new LogoFragment());
        ft.commit();

        // SharedPreferences
        sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString(Login.ID_KEY, "");

        etNamaBarang = findViewById(R.id.input_nama_barang);
        etHarga = findViewById(R.id.input_harga);
        etDeskripsi = findViewById(R.id.input_deskripsi);
        spinner = findViewById(R.id.pilih_kategori);
        btnBatal = findViewById(R.id.btn_batal);
        btnTambah = findViewById(R.id.btn_tambah);

        // Dropdown
        adapter = ArrayAdapter.createFromResource(this, R.array.kategori, R.layout.my_selected_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strNamaBarang = etNamaBarang.getText().toString();
                strHarga = etHarga.getText().toString();
                strDeksripsi = etDeskripsi.getText().toString();
                strKategori = spinner.getSelectedItem().toString();

                if(isFormEmpty(new String[]{strNamaBarang, strHarga, strDeksripsi, strKategori})){
                    Toast.makeText(TambahBarangActivity.this, "Terdapat field kosong", Toast.LENGTH_SHORT).show();
                } else {
                    tambahBarang(strNamaBarang, id_user, strKategori, strHarga, strDeksripsi);
                }


            }
        });
    }

    private void tambahBarang(String namaBarang, String idPemilik, String kategori, String harga, String deskripsi){
        Call<Barang> call = pmob22Api.createBarang(namaBarang, idPemilik, kategori, harga, deskripsi);

        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(TambahBarangActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Barang barang = response.body();
                assert barang != null;
                Log.d("Tambah Barang", "ID Pemilik->" + barang.getId_pemilik());
                Toast.makeText(TambahBarangActivity.this, "Barang berhasil ditambah", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                Toast.makeText(TambahBarangActivity.this, "Barang gagal ditambah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFormEmpty(String[] inputs){
        for(String input : inputs){
            if(TextUtils.isEmpty(input)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void finish() {
        Intent intent = new Intent(TambahBarangActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }
}