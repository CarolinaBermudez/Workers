package com.example.login.ui.perfil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment{

    private FragmentPerfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PerfilViewModel perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView_usuario = binding.textView1;
        final TextView textView_correo = binding.textView2;
        final TextView textView_fecha = binding.textView3;
        final TextView textView_pais = binding.textView4;

        //perfilViewModel.getText().observe(getViewLifecycleOwner(), textView_usuario::setText);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("sesiones", 0);
        String usuario = sharedPref.getString("usuario", "");
        String correo = sharedPref.getString("correo", "");
        String fecha = sharedPref.getString("fecha", "");
        String pais = sharedPref.getString("pais", "");

        textView_usuario.setText(usuario);
        textView_correo.setText(correo);
        textView_fecha.setText(fecha);
        textView_pais.setText(pais);
        return root;
    }

}
