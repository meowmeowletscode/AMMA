package com.example.amma.ui.deleteasset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.databinding.FragmentAddassetBinding;
import com.example.amma.databinding.FragmentDeleteassetBinding;
import com.example.amma.ui.addasset.AddAssetViewModel;

public class DeleteAssetFragment extends Fragment {

    private FragmentDeleteassetBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DeleteAssetViewModel deleteAssetViewModel = new ViewModelProvider(this).get(DeleteAssetViewModel.class);

        binding = FragmentDeleteassetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        deleteAssetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}