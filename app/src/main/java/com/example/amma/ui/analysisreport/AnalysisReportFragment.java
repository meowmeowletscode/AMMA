package com.example.amma.ui.analysisreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amma.Asset;
import com.example.amma.AssetAdapter;
import com.example.amma.AssetSQL;
import com.example.amma.R;
import com.example.amma.databinding.FragmentAnalysisreportBinding;

import java.util.ArrayList;
import java.util.List;

public class AnalysisReportFragment extends Fragment {

    private AnalysisReportViewModel analysisReportViewModel;
    private FragmentAnalysisreportBinding binding;

    private RecyclerView recyclerView;
    private AssetAdapter adapter;
    private List<Asset> assetList;
    private EditText editTextFromDate, editTextToDate;
    private Spinner spinnerLabelFilter;
    private Button buttonApplyFilters;
    private AssetSQL assetSQL;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        analysisReportViewModel = new ViewModelProvider(this).get(AnalysisReportViewModel.class);

        binding = FragmentAnalysisreportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtTitle;
        analysisReportViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerView = root.findViewById(R.id.recyclerViewAssets);
        editTextFromDate = root.findViewById(R.id.editTextFromDate);
        editTextToDate = root.findViewById(R.id.editTextToDate);
        spinnerLabelFilter = root.findViewById(R.id.spinnerLabelFilter);
        buttonApplyFilters = root.findViewById(R.id.buttonApplyFilters);

        assetSQL = new AssetSQL();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assetList = new ArrayList<>();
        adapter = new AssetAdapter(assetList);
        recyclerView.setAdapter(adapter);

        // Load initial data
        loadAssets();

        // Set up filter button click listener
        buttonApplyFilters.setOnClickListener(v -> applyFilters());

        return root;
    }

    private void loadAssets() {
        // Load all assets initially
        assetList.clear();
        assetList.addAll(assetSQL.getAllAssets());
        adapter.notifyDataSetChanged();
    }

    private void applyFilters() {
        String fromDate = editTextFromDate.getText().toString().trim();
        String toDate = editTextToDate.getText().toString().trim();
        String selectedLabel = spinnerLabelFilter.getSelectedItem().toString();

        // Apply filters based on user input
        assetList.clear();
        assetList.addAll(assetSQL.getFilteredAssets(fromDate, toDate, selectedLabel));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
