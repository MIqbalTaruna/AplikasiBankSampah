package com.example.aplikasibanksampah;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyItemsFragment extends Fragment {
    Pmob22Api pmob22Api;

    SharedPreferences sharedPreferences;
    String id_user;

    LinearLayout linearLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyItemsFragment newInstance(String param1, String param2) {
        MyItemsFragment fragment = new MyItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = requireActivity().getSharedPreferences(Login.SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString(Login.ID_KEY, "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pmob22Api = retrofit.create(Pmob22Api.class);

        linearLayout = requireView().findViewById(R.id.container_daftar_barang);

        Call<List<Barang>> call = pmob22Api.getAllBarang(id_user);

        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                List<Barang> barangList = response.body();

                for (Barang barang : barangList){
                    View view = getLayoutInflater().inflate(R.layout.card_items, null);

                    TextView namaSampah = view.findViewById(R.id.nama_sampah);
                    TextView hargaSampah = view.findViewById(R.id.harga_sampah);
                    ImageView gambarSampah = view.findViewById(R.id.gambar_sampah);

                    Button btnHapusBarang = view.findViewById(R.id.btn_hapus);

                    String strHarga = "Harga: Rp " + barang.getHarga();

                    // Set textview
                    namaSampah.setText(barang.getNama_barang());
                    hargaSampah.setText(strHarga);

                    // ambil gambar dari website
                    String urlGambar = Server.URL + "img/" + barang.getGambar();

                    Glide.with(requireActivity())
                            .load(urlGambar)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .into(gambarSampah);

                    btnHapusBarang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hapusBarang(barang.getId());
                        }
                    });

                    linearLayout.addView(view);
                }
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {

            }
        });
    }

    private void hapusBarang(String idBarang){
        Call<Barang> call = pmob22Api.deleteBarang(idBarang);

        call.enqueue(new Callback<Barang>() {
            @Override
            public void onResponse(Call<Barang> call, Response<Barang> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getActivity(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                Barang barang = response.body();

                Toast.makeText(getActivity(), barang.getId() + " berhasil dihapus", Toast.LENGTH_SHORT).show();

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }

            @Override
            public void onFailure(Call<Barang> call, Throwable t) {
                Toast.makeText(getActivity(), "gagal hapus", Toast.LENGTH_SHORT).show();
                Log.d("Hapus", "Fail: " + t.getMessage());
            }
        });
    }
}