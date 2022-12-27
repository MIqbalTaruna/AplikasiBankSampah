package com.example.aplikasibanksampah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Kategori extends AppCompatActivity implements View.OnClickListener{
    ImageView arrow_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        // tombol kembali ke halaman home
        arrow_left = findViewById(R.id.arrow_left);
        arrow_left.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.arrow_left:
                finish();
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