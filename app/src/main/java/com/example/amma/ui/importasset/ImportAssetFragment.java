package com.example.amma.ui.importasset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.databinding.FragmentImportassetBinding;

public class ImportAssetFragment extends Fragment {

    private ImportAssetViewModel importAssetViewModel;
    private FragmentImportassetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        importAssetViewModel = new ViewModelProvider(this).get(ImportAssetViewModel.class);

        binding = FragmentImportassetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtTitle;
        importAssetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
