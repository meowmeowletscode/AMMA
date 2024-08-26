package com.example.amma.ui.addasset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.AssetSQL;
import com.example.amma.LoginActivity;
import com.example.amma.UserSQL;
import com.example.amma.databinding.FragmentAddassetBinding;

import java.io.IOException;
import java.util.List;

public class AddAssetFragment extends Fragment {

    private AddAssetViewModel addAssetViewModel;
    private FragmentAddassetBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private static final int REQUEST_FILE_IMAGE = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private EditText txtAssetName;
    private EditText txtBarcode;
    private EditText txtQuantity;
    private EditText txtDescription;
    private Spinner spinnerLabels;
    private ImageView photoView;
    private ImageButton btnCapturePhoto;
    private Button btnClear;
    private Button btnSave;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addAssetViewModel = new ViewModelProvider(this).get(AddAssetViewModel.class);

        binding = FragmentAddassetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtTitle;
        addAssetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //binding the id
        txtAssetName = binding.txtAssetName;
        txtBarcode = binding.txtBarcode;
        txtQuantity = binding.txtQuantity;
        txtDescription = binding.txtDescription;
        spinnerLabels = binding.spinnerLabels;
        photoView = binding.photoView;
        btnCapturePhoto = binding.btnCapturePhoto;
        btnClear = binding.btnClear;
        btnSave = binding.btnSave;

        btnCapturePhoto.setOnClickListener(v -> showImageSourceDialog());

        loadLabels();

        btnClear.setOnClickListener(new BtnClearClickListener());
        btnSave.setOnClickListener(new BtnSaveClickListener());

        return root;
    }

    private void loadLabels() {
        List<String> labels = addAssetViewModel.getLabels(); // Fetch labels from database
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLabels.setAdapter(adapter);
    }

    private void showImageSourceDialog() {
        // Show a dialog to choose between Camera, Gallery, or File
        String[] options = {"Camera", "Gallery", "File"};
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            } else {
                                dispatchTakePictureIntent();
                            }
                            break;
                        case 1:
                            dispatchSelectGalleryIntent();
                            break;
                        case 2:
                            dispatchSelectFileIntent();
                            break;
                    }
                })
                .show();
    }



    private class BtnClearClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            txtAssetName.setText("");
            txtBarcode.setText("");
            txtQuantity.setText("");
            txtDescription.setText("");
            spinnerLabels.setSelection(0);
            photoView.setImageDrawable(null);
        }
    }

    private class BtnSaveClickListener implements View.OnClickListener {
        AssetSQL assetSQL = new AssetSQL();

        @Override
        public void onClick(View view) {
            // Get the input values
            String assetName = txtAssetName.getText().toString().trim();
            String barcode = txtBarcode.getText().toString().trim();
            String quantityStr = txtQuantity.getText().toString().trim();
            String description = txtDescription.getText().toString().trim();
            String label = spinnerLabels.getSelectedItem().toString();

            // Check if required fields are empty
            if (assetName.isEmpty() || barcode.isEmpty() || quantityStr.isEmpty() || label.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert quantity to integer
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid quantity entered.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the photo if available
            Bitmap photo = null;
            if (photoView.getDrawable() != null) {
                photo = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
            }

            // Save the asset
            boolean isSaved = assetSQL.saveAsset(
                    assetName,
                    barcode,
                    quantity,
                    description.isEmpty() ? null : description,
                    label,
                    photo
            );

            // Show appropriate toast based on save operation result
            if (isSaved) {
                Toast.makeText(getContext(), "Asset saved successfully!", Toast.LENGTH_SHORT).show();

                // Clear the fields after saving
                txtAssetName.setText("");
                txtBarcode.setText("");
                txtQuantity.setText("");
                txtDescription.setText("");
                spinnerLabels.setSelection(0); // Reset spinner to the first item
                photoView.setImageDrawable(null); // Clear the photo view
            } else {
                Toast.makeText(getContext(), "Failed to save asset. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectGalleryIntent() {
        Intent selectGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectGalleryIntent, REQUEST_GALLERY_IMAGE);
    }

    private void dispatchSelectFileIntent() {
        Intent selectFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        selectFileIntent.setType("image/*");
        startActivityForResult(selectFileIntent, REQUEST_FILE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Uri imageUri = null;
            Bitmap imageBitmap = null;

            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    break;

                case REQUEST_GALLERY_IMAGE:
                    imageUri = data.getData();
                    break;

                case REQUEST_FILE_IMAGE:
                    imageUri = data.getData();
                    break;
            }

            if (imageUri != null) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (imageBitmap != null) {
                photoView.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
