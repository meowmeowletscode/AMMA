package com.example.amma.ui.importasset;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.AssetSQL;
import com.example.amma.LabelSQL;
import com.example.amma.User;
import com.example.amma.UserManager;
import com.example.amma.databinding.FragmentImportassetBinding;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

public class ImportAssetFragment extends Fragment {

    private ImportAssetViewModel importAssetViewModel;
    private FragmentImportassetBinding binding;

    private static final int IMPORT_REQUEST_CODE = 1;
    private AssetSQL assetSQL;
    private LabelSQL labelSQL;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        importAssetViewModel = new ViewModelProvider(this).get(ImportAssetViewModel.class);

        binding = FragmentImportassetBinding.inflate(inflater, container, false);
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
        importAssetViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        assetSQL = new AssetSQL();
        labelSQL = new LabelSQL();

        binding.buttonImport.setOnClickListener(v -> openFileChooser());

        return root;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(intent, IMPORT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMPORT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            importExcel(uri);
        }
    }

    private void importExcel(Uri uri) {
        try (InputStream inputStream = requireContext().getContentResolver().openInputStream(uri)) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from row 1 to skip header
                Row row = sheet.getRow(i);
                if (row == null) continue; // Skip if the row is null

                String assetName = getCellStringValue(row, 0);
                String barcode = getCellStringValue(row, 1);
                Integer quantity = getCellIntegerValue(row, 2);
                String description = getCellStringValue(row, 3);
                String labelName = getCellStringValue(row, 4);

                // Check if the barcode exists
                if (!assetSQL.isImportBarcodeExists(barcode)) {
                    // Add new label if it does not exist
                    if (!labelSQL.isLabelExists(labelName)) {
                        labelSQL.addLabel(labelName);
                    }

                    // Save asset data
                    boolean isSaved = assetSQL.saveAsset(assetName, barcode, quantity != null ? quantity : 0, description, labelName, null);
                    if (!isSaved) {
                        Toast.makeText(requireContext(), "Failed to save asset: " + assetName, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            Toast.makeText(requireContext(), "Import completed.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error reading Excel file.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper methods to safely retrieve cell values
    private String getCellStringValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null || cell.getCellType() != CellType.STRING) {
            return ""; // Return empty string or handle null as needed
        }
        return cell.getStringCellValue().trim();
    }

    private Integer getCellIntegerValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return null; // Return null or handle missing integer values
        }
        return (int) cell.getNumericCellValue();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
