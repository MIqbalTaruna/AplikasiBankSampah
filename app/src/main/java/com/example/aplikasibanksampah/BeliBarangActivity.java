package com.example.aplikasibanksampah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasibanksampah.data.Pesanan;
import com.example.aplikasibanksampah.data.controller.Pmob22Api;
import com.example.aplikasibanksampah.utility.Server;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeliBarangActivity extends AppCompatActivity implements LocationListener {
    ImageView arrowBack;

    TextView textNamaBarang, textHargaBarang, textDeskripsiBarang, textTagihan, textAlamat;
    EditText inputJumlah;

    Button btnHitung, btnBeli, btnLokasi;

    Bundle bundle;

    Pmob22Api api;

    String sIdUser, sIdBarang, sIdPemilik, sKategori, sNamaBarang, sHarga, sDeskripsi;
    String latitude, longitude;

    Integer intTotalHarga;

    LocationManager locationManager;

    SharedPreferences sharedPreferences;

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
        textAlamat = findViewById(R.id.text_alamat);

        inputJumlah = findViewById(R.id.input_jumlah);

        btnHitung = findViewById(R.id.btn_hitung_tagihan);
        btnBeli = findViewById(R.id.btn_beli);
        btnLokasi = findViewById(R.id.btn_lokasi);

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Pmob22Api.class);

        sharedPreferences = getSharedPreferences(Login.SHARED_PREFS, Context.MODE_PRIVATE);
        sIdUser = sharedPreferences.getString(Login.ID_KEY, "");

        // Ambil data dari intent
        bundle = getIntent().getExtras();
        sIdBarang = bundle.getString(Server.ID_BARANG_KEY);
        sIdPemilik = bundle.getString(Server.ID_PEMILIK_KEY);
        sKategori = bundle.getString(Server.ID_KATEGORI_KEY);
        sNamaBarang = bundle.getString(Server.NAMA_BARANG_KEY);
        sHarga = bundle.getString(Server.HARGA_KEY);
        sDeskripsi = bundle.getString(Server.DESKRIPSI_KEY);

        String stringHarga = "Harga: Rp " + sHarga;
        String stringDeskripsi = "Deskripsi: " + sDeskripsi;

        // Ubah text pada halaman pembelian
        textNamaBarang.setText(sNamaBarang);
        textHargaBarang.setText(stringHarga);
        textDeskripsiBarang.setText(stringDeskripsi);

        intTotalHarga = 0;

        // Button hitung tagihan
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlah = inputJumlah.getText().toString();

                if(TextUtils.isEmpty(jumlah))
                    jumlah = "0";

                intTotalHarga = Integer.parseInt(sHarga) * Integer.parseInt(jumlah);
                String stringTagihan = "Total tagihan: Rp " + intTotalHarga;

                textTagihan.setText(stringTagihan);
            }
        });

        // Button beli barang
        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumlah = inputJumlah.getText().toString();

                if(TextUtils.isEmpty(jumlah))
                    jumlah = "0";

                if(TextUtils.isEmpty(jumlah) || jumlah.equals("0") || TextUtils.isEmpty(latitude)) {
                    Toast.makeText(BeliBarangActivity.this, "Input jumlah barang dan lokasi", Toast.LENGTH_SHORT).show();
                }
                else{
                    intTotalHarga = Integer.parseInt(sHarga) * Integer.parseInt(jumlah);

                    String sTotalHarga = String.valueOf(intTotalHarga);
//                    buatPesanan(sIdBarang, sIdUser, jumlah, sTotalHarga, latitude, longitude);

                    Call<Pesanan> call = api.createPesanan(sIdBarang, sIdUser, jumlah, sTotalHarga, latitude, longitude);


                    call.enqueue(new Callback<Pesanan>() {
                        @Override
                        public void onResponse(Call<Pesanan> call, Response<Pesanan> response) {
                            if(!response.isSuccessful()){
                                Log.d("Code", "" + response.code());
                                return;
                            }

                            Pesanan pesanan = response.body();

                            Log.d("Pesanan", "" + pesanan.getId_barang());
                            Toast.makeText(BeliBarangActivity.this, "Pemesanan Berhasil", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BeliBarangActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Pesanan> call, Throwable t) {
                            Log.d("Pemesanan", "Fail: " + t.getMessage());
                        }
                    });
                }
            }
        });

        // Runtime permission untuk akses lokasi saat ini
        if(ContextCompat.checkSelfPermission(BeliBarangActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BeliBarangActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        // Button get lokasi terkini
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLocation(){

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, BeliBarangActivity.this);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(this, "" + location.getLatitude() + "|" + location.getLongitude(), Toast.LENGTH_SHORT).show();

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        try{
            Geocoder geocoder = new Geocoder(BeliBarangActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);

            textAlamat.setText(address);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

//    private void buatPesanan(String idBarang, String idPemesan, String jumlah, String totalHarga, String locLat, String locLong){
//        Call<Pesanan> call = api.createPesanan(idBarang, idPemesan, jumlah, totalHarga, locLat, locLong);
//
//
//        call.enqueue(new Callback<Pesanan>() {
//            @Override
//            public void onResponse(Call<Pesanan> call, Response<Pesanan> response) {
//                if(!response.isSuccessful()){
//                    Log.d("Code", "" + response.code());
//                    return;
//                }
//
//                Pesanan pesanan = response.body();
//
//                Log.d("Pesanan", "" + pesanan.getId_barang());
//                Toast.makeText(BeliBarangActivity.this, "Pemesanan Berhasil", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(BeliBarangActivity.this, MainActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<Pesanan> call, Throwable t) {
//                Log.d("Pemesanan", "Fail: " + t.getMessage());
//            }
//        });
//    }
}