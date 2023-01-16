package com.example.aplikasibanksampah;

import static com.example.aplikasibanksampah.Login.EMAIL_KEY;
import static com.example.aplikasibanksampah.Login.NAMA_KEY;
import static com.example.aplikasibanksampah.Login.NO_TELP_KEY;
import static com.example.aplikasibanksampah.Login.SHARED_PREFS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, nama, no_telp;

    Button btn_logout;
    TextView textUsername, textEmail, textNoTelp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        // getting the data which is stored in shard preferences
        sharedpreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // ambil variable dari sharepref(session)
        // jika tidak ada isi null
        nama = sharedpreferences.getString(NAMA_KEY, null);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        no_telp = sharedpreferences.getString(NO_TELP_KEY, null);

        // tangkap element TextView
        textUsername = requireView().findViewById(R.id.username_profile);
        textEmail = requireView().findViewById(R.id.email_profile);
        textNoTelp = requireView().findViewById(R.id.no_telp_profile);

        // Set TextView diatas sesuai dengan data user yang sedang login
        textUsername.setText(nama);
        textEmail.setText(email);
        textNoTelp.setText(no_telp);

        btn_logout = requireView().findViewById(R.id.logout_button);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}