package com.example.amma.ui.analysisreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.databinding.FragmentAnalysisreportBinding;

public class AnalysisReportFragment extends Fragment {

    private AnalysisReportViewModel analysisReportViewModel;
    private FragmentAnalysisreportBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        analysisReportViewModel = new ViewModelProvider(this).get(AnalysisReportViewModel.class);

        binding = FragmentAnalysisreportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtTitle;
        analysisReportViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
