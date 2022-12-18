package com.example.aplikasibanksampah.data;

public class User {
    private String id, nama, email, no_telp, password;

    public User(){}

    public User(String id, String nama, String email, String no_telp, String password) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.no_telp = no_telp;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
