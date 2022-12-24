package com.example.aplikasibanksampah.data;

public class Barang {
    private String id, nama, gambar;
    private Integer harga;

    public Barang(String id, String nama, String gambar, Integer harga) {
        this.id = id;
        this.nama = nama;
        this.gambar = gambar;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }
}
