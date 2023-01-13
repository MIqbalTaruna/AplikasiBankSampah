package com.example.aplikasibanksampah.data;

public class Pesanan {
    String id, id_barang, id_pemesan, jumlah, total_harga, loc_lat, loc_long;

    public Pesanan(String id, String id_barang, String id_pemesan, String jumlah, String total_harga, String loc_lat, String loc_long) {
        this.id = id;
        this.id_barang = id_barang;
        this.id_pemesan = id_pemesan;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
        this.loc_lat = loc_lat;
        this.loc_long = loc_long;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getId_pemesan() {
        return id_pemesan;
    }

    public void setId_pemesan(String id_pemesan) {
        this.id_pemesan = id_pemesan;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getLoc_lat() {
        return loc_lat;
    }

    public void setLoc_lat(String loc_lat) {
        this.loc_lat = loc_lat;
    }

    public String getLoc_long() {
        return loc_long;
    }

    public void setLoc_long(String loc_long) {
        this.loc_long = loc_long;
    }
}
