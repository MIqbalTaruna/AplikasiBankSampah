package com.example.aplikasibanksampah;

import static com.example.aplikasibanksampah.Kategori.KATEGORI_KEY;
import static com.example.aplikasibanksampah.Login.ID_KEY;
import static com.example.aplikasibanksampah.Login.SHARED_PREFS;
import static com.example.aplikasibanksampah.utility.Server.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplikasibanksampah.data.Barang;
import com.example.aplikasibanksampah.data.controller.Pmob22Api;
import com.example.aplikasibanksampah.utility.Server;

import java.util.List;

import javax.xml.transform.sax.SAXTransformerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaftarBarang extends AppCompatActivity {
    // Web Service or API
    Pmob22Api pmob22Api;

    // Variable shared preferences
    SharedPreferences sharedPreferences;
    String id_user, id_kategori;

    LinearLayout linearLayout;

    ImageView arrowBack;

    Button btnBeliBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_barang);

        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // getting the data which is stored in shard preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString(ID_KEY, "");
        id_kategori = sharedPreferences.getString(KATEGORI_KEY, "");

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pmob22Api = retrofit.create(Pmob22Api.class);

        linearLayout = findViewById(R.id.container_daftar_barang);

        // Tampilkan daftar barang sesuai kategori
        showBarang(id_user, id_kategori);

        arrowBack= findViewById(R.id.arrow_left);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void showBarang(String id_pemilik, String id_kategori){
        Call<List<Barang>> call = pmob22Api.getBarang(id_pemilik, id_kategori);

        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Code", "" + response.code());
                    return;
                }

                List<Barang> barangs = response.body();

                for(Barang barang : barangs){
                    View view = getLayoutInflater().inflate(R.layout.card_barang, null);
                    TextView nama_sampah = view.findViewById(R.id.nama_sampah);
                    TextView harga_sampah = view.findViewById(R.id.harga_sampah);
                    ImageView gambar_sampah = view.findViewById(R.id.gambar_sampah);


                    nama_sampah.setText(barang.getNama_barang());
                    String harga = "Harga : Rp." + barang.getHarga();
                    harga_sampah.setText(harga);

                    // ambil gambar dari website
                    String url_gambar = Server.URL + "img/" + barang.getGambar();
                    Log.d("Gambar Barang", url_gambar);

                    Glide.with(DaftarBarang.this)
                            .load(url_gambar)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(gambar_sampah);

                    String sId = barang.getId();
                    String sIdPemilik = barang.getId_pemilik();
                    String sIdKategori = barang.getId_kategori();
                    String sNama = barang.getNama_barang();
                    String sHarga = barang.getHarga();
                    String sDeskripsi = barang.getDeskripsi();

                    btnBeliBarang = view.findViewById(R.id.btn_detail_barang);
                    btnBeliBarang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DaftarBarang.this, BeliBarangActivity.class);

                            intent.putExtra(ID_BARANG_KEY, sId);
                            intent.putExtra(ID_PEMILIK_KEY, sIdPemilik);
                            intent.putExtra(ID_KATEGORI_KEY, sIdKategori);
                            intent.putExtra(NAMA_BARANG_KEY, sNama);
                            intent.putExtra(HARGA_KEY, sHarga);
                            intent.putExtra(DESKRIPSI_KEY, sDeskripsi);

                            startActivity(intent);
                        }
                    });

                    linearLayout.addView(view);
                }
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                Log.d("Barang", "Fail: " + t.getMessage());
            }
        });
    }
}