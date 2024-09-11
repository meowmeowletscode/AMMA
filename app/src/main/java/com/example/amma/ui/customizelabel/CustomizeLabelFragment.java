package com.example.amma.ui.customizelabel;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.amma.Label;
import com.example.amma.LabelAdapter;
import com.example.amma.LabelSQL;
import com.example.amma.databinding.FragmentCustomizelabelBinding;

import java.util.ArrayList;
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

        // Set up buttons
        setupButtons();

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

    private void setupButtons() {
        binding.buttonAdd.setOnClickListener(v -> showAddLabelDialog());
        binding.buttonEdit.setOnClickListener(v -> showEditLabelDialog());
        binding.buttonDelete.setOnClickListener(v -> showDeleteLabelDialog());
    }

    private void showAddLabelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New Label");

        final EditText input = new EditText(getContext());
        input.setHint("Label Name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String labelName = input.getText().toString().trim();
            if (!labelName.isEmpty()) {
                boolean isAdded = labelSQL.addLabel(labelName); // Add this method to LabelSQL
                if (isAdded) {
                    fetchLabels();
                    Toast.makeText(getContext(), "Label added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add label. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Label name cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditLabelDialog() {
        // Get current labels
        List<Label> labelObjects = labelSQL.getLabels();
        List<String> labelNames = new ArrayList<>();
        for (Label label : labelObjects) {
            labelNames.add(label.getLabelName());
        }

        // Create a dialog to select a label to edit
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Label to Edit");

        // Convert list of labels to a CharSequence array
        CharSequence[] labelsArray = labelNames.toArray(new CharSequence[0]);

        builder.setItems(labelsArray, (dialog, which) -> {
            String selectedLabel = labelNames.get(which);
            showEditLabelInputDialog(selectedLabel);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditLabelInputDialog(String oldLabelName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Label");

        // Create an EditText for the new label name
        final EditText input = new EditText(getContext());
        input.setHint("New Label Name");
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newLabelName = input.getText().toString().trim();
            if (!newLabelName.isEmpty()) {
                boolean isUpdated = labelSQL.updateLabel(oldLabelName, newLabelName); // Ensure this method is implemented
                if (isUpdated) {
                    fetchLabels();
                    Toast.makeText(getContext(), "Label updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update label. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Label name cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteLabelDialog() {
        // Get current labels
        List<Label> labelObjects = labelSQL.getLabels();
        List<String> labelNames = new ArrayList<>();
        for (Label label : labelObjects) {
            labelNames.add(label.getLabelName());
        }

        // Create a dialog to select a label to delete
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Label to Delete");

        // Convert list of labels to a CharSequence array
        CharSequence[] labelsArray = labelNames.toArray(new CharSequence[0]);

        builder.setItems(labelsArray, (dialog, which) -> {
            String selectedLabel = labelNames.get(which);
            showConfirmDeleteDialog(selectedLabel);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showConfirmDeleteDialog(String labelName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this label: " + labelName + "?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            boolean isDeleted = labelSQL.deleteLabel(labelName); // Ensure this method is implemented
            if (isDeleted) {
                fetchLabels();
                Toast.makeText(getContext(), "Label deleted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to delete label. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}