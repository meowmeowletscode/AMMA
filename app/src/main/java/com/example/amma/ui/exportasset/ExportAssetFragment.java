package com.example.amma.ui.exportasset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.databinding.FragmentExportassetBinding;

public class ExportAssetFragment extends Fragment {

    private ExportAssetViewModel exportAssetViewModel;
    private FragmentExportassetBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exportAssetViewModel = new ViewModelProvider(this).get(ExportAssetViewModel.class);

        binding = FragmentExportassetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtTitle;
        exportAssetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
