package com.example.amma.ui.addasset;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.AssetSQL;
import com.example.amma.LoginActivity;
import com.example.amma.UserSQL;
import com.example.amma.databinding.FragmentAddassetBinding;

import java.util.List;

public class AddAssetFragment extends Fragment {

    private AddAssetViewModel addAssetViewModel;
    private FragmentAddassetBinding binding;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

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

        btnCapturePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                dispatchTakePictureIntent();
            }
        });

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

    private class BtnClearClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            txtAssetName.setText("");
            txtBarcode.setText("");
            txtQuantity.setText("");
            txtDescription.setText("");
            photoView.setImageDrawable(null);
        }
    }

    private class BtnSaveClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            AssetSQL assetSQL = new AssetSQL();

            String assetName = txtAssetName.getText().toString().trim();
            String barcode = txtBarcode.getText().toString().trim();
            int quantity = Integer.parseInt(txtQuantity.getText().toString().trim());
            String description = txtDescription.getText().toString().trim();
            String label = spinnerLabels.getSelectedItem().toString();

            Bitmap photo = null;
            if (photoView.getDrawable() != null) {
                photo = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
            }

            // Call the method to save the asset
            assetSQL.saveAsset(assetName, barcode, quantity, description.isEmpty() ? null : description, label, photo);

            // Clear the fields after saving
            txtAssetName.setText("");
            txtBarcode.setText("");
            txtQuantity.setText("");
            txtDescription.setText("");
            spinnerLabels.setSelection(0); // or set to default item if needed
            photoView.setImageDrawable(null); // Clear the image
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // Permission denied, show a message to the user
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
