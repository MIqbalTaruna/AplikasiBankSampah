package com.example.aplikasibanksampah;

import static com.example.aplikasibanksampah.Login.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Kategori extends AppCompatActivity implements View.OnClickListener{
    ImageView arrow_left;
    LinearLayout kategori_cloth, kategori_ewaste;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String KATEGORI_KEY = "kategori_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // getting the data which is stored in shard preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // tombol kembali ke halaman home
        arrow_left = findViewById(R.id.arrow_left);
        arrow_left.setOnClickListener(this);

        // tombol untuk menampilkan daftar barang sesuai kategori
        kategori_cloth = findViewById(R.id.cloth_waste);
        kategori_ewaste = findViewById(R.id.e_waste);

        kategori_cloth.setOnClickListener(this);
        kategori_ewaste.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        editor = sharedPreferences.edit();

        switch(view.getId()){
            case R.id.arrow_left:
                finish();
                break;
            case R.id.cloth_waste:
                editor.putString(KATEGORI_KEY, "2"); // 2 adalah id_kategori untuk sampah kain
                editor.commit();
                Intent intent = new Intent(Kategori.this, DaftarBarang.class);
                startActivity(intent);
                break;
            case R.id.e_waste:
                editor.putString(KATEGORI_KEY, "1"); // 1 adalah id_kategori untuk sampah elektronik
                editor.commit();
                startActivity(new Intent(Kategori.this, DaftarBarang.class));
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent(Kategori.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }
}