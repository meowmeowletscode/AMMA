package com.example.amma.ui.analysisreport;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amma.Asset;
import com.example.amma.AssetAdapter;
import com.example.amma.AssetSQL;
import com.example.amma.R;
import com.example.amma.User;
import com.example.amma.UserManager;
import com.example.amma.databinding.FragmentAnalysisreportBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnalysisReportFragment extends Fragment {

    private AnalysisReportViewModel analysisReportViewModel;
    private FragmentAnalysisreportBinding binding;

    private RecyclerView recyclerView;
    private AssetAdapter adapter;
    private List<Asset> assetList;
    private EditText editTextFromDate, editTextToDate;
    private Spinner spinnerLabelFilter;
    private Button buttonApplyFilters, buttonSelectFromDate, buttonSelectToDate;
    private AssetSQL assetSQL;

    private Calendar calendar;
    private int year, month, day;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        analysisReportViewModel = new ViewModelProvider(this).get(AnalysisReportViewModel.class);

        binding = FragmentAnalysisreportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Check the current user's role
        User currentUser = UserManager.getInstance().getCurrentUser();

        // Check if no user is logged in or if the user is not an Admin
        if (currentUser == null || !"Admin".equals(currentUser.getRole())) {
            // Display a toast message and navigate back after a short delay to avoid FragmentManager issues
            Toast.makeText(getContext(), "Access denied. Only Admins can view this page.", Toast.LENGTH_SHORT).show();

            // Use Handler to safely navigate back to the previous fragment
            new Handler(Looper.getMainLooper()).postDelayed(() -> requireActivity().onBackPressed(), 100);

            // Return early to prevent the rest of the fragment from being set up
            return null;
        }

        final TextView textView = binding.txtTitle;
        analysisReportViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerView = root.findViewById(R.id.recyclerViewAssets);
        editTextFromDate = root.findViewById(R.id.editTextFromDate);
        editTextToDate = root.findViewById(R.id.editTextToDate);
        spinnerLabelFilter = root.findViewById(R.id.spinnerLabelFilter);
        buttonApplyFilters = root.findViewById(R.id.buttonApplyFilters);
        buttonSelectFromDate = root.findViewById(R.id.buttonSelectFromDate);
        buttonSelectToDate = root.findViewById(R.id.buttonSelectToDate);

        assetSQL = new AssetSQL();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assetList = new ArrayList<>();
        adapter = new AssetAdapter(assetList);
        recyclerView.setAdapter(adapter);

        // Load initial data
        loadAssets();
        loadLabels();

        // Set up filter button click listener
        buttonApplyFilters.setOnClickListener(v -> applyFilters());

        // Set up date pickers
        buttonSelectFromDate.setOnClickListener(v -> showDatePickerDialog(true));
        buttonSelectToDate.setOnClickListener(v -> showDatePickerDialog(false));

        return root;
    }

    private void loadAssets() {
        // Load all assets initially
        assetList.clear();
        assetList.addAll(assetSQL.getAllAssets());
        adapter.notifyDataSetChanged();
    }

    private void loadLabels() {
        List<String> labels = analysisReportViewModel.getLabels(); // Fetch labels from database
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLabelFilter.setAdapter(adapter);
    }

    private void applyFilters() {
        String fromDateStr = editTextFromDate.getText().toString().trim();
        String toDateStr = editTextToDate.getText().toString().trim();
        String selectedLabel = spinnerLabelFilter.getSelectedItem().toString();

        if (fromDateStr.isEmpty() || toDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both start and end dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fromDate = "";
        String toDate = "";

        try {
            // Convert fromDateStr to yyyy-MM-dd
            Date date = inputFormat.parse(fromDateStr);
            fromDate = outputFormat.format(date);

            // Convert toDateStr to yyyy-MM-dd
            date = inputFormat.parse(toDateStr);
            toDate = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Date format is incorrect. Please use MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Apply filters based on user input
        assetList.clear();
        assetList.addAll(assetSQL.getFilteredAssets(fromDate, toDate, selectedLabel));
        adapter.notifyDataSetChanged();
    }

    private void showDatePickerDialog(boolean isStartDate) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year;
            if (isStartDate) {
                editTextFromDate.setText(date);
            } else {
                editTextToDate.setText(date);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
