package com.example.login.ui.eliminar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.login.databinding.FragmentEliminarBinding;
import com.example.login.ui.eliminar.EliminarViewModel;

public class EliminarFragment extends Fragment {

    private FragmentEliminarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EliminarViewModel eliminarViewModel =
                new ViewModelProvider(this).get(EliminarViewModel.class);

        binding = FragmentEliminarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textEliminar;
        //eliminarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}