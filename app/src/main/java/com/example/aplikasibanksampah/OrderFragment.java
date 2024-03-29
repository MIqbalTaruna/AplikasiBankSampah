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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikasibanksampah.data.Pesanan;
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
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
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

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // SharedPref
        sharedPreferences = requireActivity().getSharedPreferences(Login.SHARED_PREFS, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getString(Login.ID_KEY, "");

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        pmob22Api = retrofit.create(Pmob22Api.class);

        linearLayout = requireView().findViewById(R.id.container_daftar_pesanan);

        Call<List<Pesanan>> call = pmob22Api.getMyPesanan(id_user);

        call.enqueue(new Callback<List<Pesanan>>() {
            @Override
            public void onResponse(Call<List<Pesanan>> call, Response<List<Pesanan>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Code", "" + response.code());
                    return;
                }
                
                List<Pesanan> pesananList = response.body();
                
                for(Pesanan pesanan : pesananList){
                    View view = getLayoutInflater().inflate(R.layout.card_my_pesanan, null);
                    
                    TextView textNamaBarang = view.findViewById(R.id.nama_barang);
                    TextView textJumlah = view.findViewById(R.id.jumlah);
                    TextView textTotalHarga = view.findViewById(R.id.tagihan);
                    
                    String strJumlah = "Jumlah: " + pesanan.getJumlah();
                    String strTotalHarga = "Tagihan: Rp" + pesanan.getTotal_harga();
                    
                    textNamaBarang.setText(pesanan.getNama_barang());
                    textJumlah.setText(strJumlah);
                    textTotalHarga.setText(strTotalHarga);
                    
                    linearLayout.addView(view);
                }
            }

            @Override
            public void onFailure(Call<List<Pesanan>> call, Throwable t) {
                Toast.makeText(getActivity(), "Belum melakukan pemesanan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}