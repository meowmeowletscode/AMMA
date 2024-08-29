package com.example.amma.ui.CustomizeLabel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amma.Label;
import com.example.amma.LabelAdapter;
import com.example.amma.LabelSQL;
import com.example.amma.databinding.FragmentCustomizelabelBinding;

import java.util.List;

public class CustomizeLabelFragment extends Fragment {

    private CustomizeLabelViewModel customizeLabelViewModel;
    private FragmentCustomizelabelBinding binding;

    private RecyclerView recyclerView;
    private LabelAdapter adapter;
    private List<Label> labels;
    private LabelSQL labelSQL;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        customizeLabelViewModel = new ViewModelProvider(this).get(CustomizeLabelViewModel.class);

        binding = FragmentCustomizelabelBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize LabelSQL and RecyclerView
        labelSQL = new LabelSQL();
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch labels and set up RecyclerView
        fetchLabels();

        final TextView textView = binding.textSlideshow;
        customizeLabelViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    private void fetchLabels() {
        // Retrieve labels using LabelSQL
        labels = labelSQL.getLabels();
        adapter = new LabelAdapter(labels, position -> {
            // Handle item click if needed
            // For example, show a dialog or navigate to an edit screen
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}