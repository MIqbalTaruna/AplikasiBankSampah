package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasibanksampah.data.controller.Pmob22Api;
import com.example.aplikasibanksampah.utility.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeliBarangActivity extends AppCompatActivity {
    ImageView arrowBack;

    TextView textNamaBarang, textHargaBarang, textDeskripsiBarang, textTagihan;
    EditText inputJumlah;

    Button btnHitung, btnBeli;

    Bundle bundle;

    Pmob22Api api;

    String sId, sIdPemilik, sKategori, sNamaBarang, sHarga, sDeskripsi;

    Integer intTagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_barang);

        if(getSupportActionBar() !=  null)
            this.getSupportActionBar().hide();

        arrowBack = findViewById(R.id.arrow_left);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textNamaBarang = findViewById(R.id.detail_nama_barang);
        textHargaBarang = findViewById(R.id.detail_harga);
        textDeskripsiBarang = findViewById(R.id.deskripsi);
        textTagihan = findViewById(R.id.total_tagihan);

        inputJumlah = findViewById(R.id.input_jumlah);

        btnHitung = findViewById(R.id.btn_hitung_tagihan);
        btnBeli = findViewById(R.id.btn_beli);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Pmob22Api.class);

        // Ambil data dari intent
        bundle = getIntent().getExtras();
        sId = bundle.getString(Server.ID_BARANG_KEY);
        sIdPemilik = bundle.getString(Server.ID_PEMILIK_KEY);
        sKategori = bundle.getString(Server.ID_KATEGORI_KEY);
        sNamaBarang = bundle.getString(Server.NAMA_BARANG_KEY);
        sHarga = bundle.getString(Server.HARGA_KEY);
        sDeskripsi = bundle.getString(Server.DESKRIPSI_KEY);

        String stringHarga = "Harga: Rp " + sHarga;

        // Ubah text pada halaman pembelian
        textNamaBarang.setText(sNamaBarang);
        textHargaBarang.setText(stringHarga);
        textDeskripsiBarang.setText(sDeskripsi);

        intTagihan = 0;

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlah = inputJumlah.getText().toString();

                if(TextUtils.isEmpty(jumlah))
                    jumlah = "0";

                intTagihan = Integer.parseInt(sHarga) * Integer.parseInt(jumlah);
                String stringTagihan = "Total tagihan: Rp " + intTagihan;

                textTagihan.setText(stringTagihan);
            }
        });


        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlah = inputJumlah.getText().toString();

                if(TextUtils.isEmpty(jumlah))
                    jumlah = "0";

                if(TextUtils.isEmpty(jumlah) || jumlah.equals("0")) {
                    Toast.makeText(BeliBarangActivity.this, "Input jumlah barang", Toast.LENGTH_SHORT).show();
                }
                else{
                    intTagihan = Integer.parseInt(sHarga) * Integer.parseInt(jumlah);
                    Toast.makeText(BeliBarangActivity.this, "Pembelian berhasil", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}