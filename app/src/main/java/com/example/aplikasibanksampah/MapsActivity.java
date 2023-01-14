package com.example.aplikasibanksampah;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.example.aplikasibanksampah.utility.Server;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.aplikasibanksampah.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    Bundle bundle;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMapsBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        bundle = getIntent().getExtras();
        latitude = bundle.getString(Server.LATITUDE_KEY);
        longitude = bundle.getString(Server.LONGITUDE_KEY);

        double dLatitude = Double.parseDouble(latitude);
        double dLongitude = Double.parseDouble(longitude);

        // Add a marker in Sydney and move the camera
        float zoomLevel = 18.0f;
        LatLng lokasi = new LatLng(dLatitude, dLongitude);
        mMap.addMarker(new MarkerOptions().position(lokasi).title("Lokasi Pemesan"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, zoomLevel));

    }
}