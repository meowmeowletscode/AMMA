package com.example.amma.ui.exportasset;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.Asset;
import com.example.amma.AssetSQL;
import com.example.amma.R;
import com.example.amma.User;
import com.example.amma.UserManager;
import com.example.amma.util.ExcelExportUtil;

import java.util.List;

import com.example.amma.databinding.FragmentExportassetBinding;

public class ExportAssetFragment extends Fragment {

    private ExportAssetViewModel exportAssetViewModel;
    private FragmentExportassetBinding binding;

    private static final int REQUEST_CODE_PERMISSION = 100;

    private Button buttonExport;
    private AssetSQL assetSQL;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exportAssetViewModel = new ViewModelProvider(this).get(ExportAssetViewModel.class);

        binding = FragmentExportassetBinding.inflate(inflater, container, false);
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
        exportAssetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        buttonExport = root.findViewById(R.id.buttonExport);
        assetSQL = new AssetSQL();

        buttonExport.setOnClickListener(v -> exportAssets());

        checkAndRequestPermissions();

        return root;
    }

    private void exportAssets() {
        List<Asset> assets = assetSQL.getAllAssets(); // Fetch assets to be exported
        ExcelExportUtil.exportAssetsToExcel(getContext(), assets);
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // For API levels below 33
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Permission denied to write to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
