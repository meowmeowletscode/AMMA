package com.example.amma.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.amma.Asset;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExcelExportUtil {
    public static void exportAssetsToExcel(Context context, List<Asset> assets) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Assets");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"AssetID", "AssetName", "Barcode", "Quantity", "Description", "LabelName", "Photo", "EditedAt", "CreatedAt"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Add asset data
        int rowNum = 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Asset asset : assets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(asset.getAssetID()); // Ensure AssetID is set correctly
            row.createCell(1).setCellValue(asset.getAssetName() != null ? asset.getAssetName() : ""); // Handle null values
            row.createCell(2).setCellValue(asset.getBarcode() != null ? asset.getBarcode() : ""); // Handle null values
            row.createCell(3).setCellValue(asset.getQuantity());
            row.createCell(4).setCellValue(asset.getDescription() != null ? asset.getDescription() : ""); // Handle null values
            row.createCell(5).setCellValue(asset.getLabel() != null ? asset.getLabel() : ""); // Handle null values
            row.createCell(6).setCellValue(asset.getPhoto() != null ? "Photo Data" : ""); // Placeholder for binary data
            row.createCell(7).setCellValue(asset.getEditedAt() != null ? sdf.format(asset.getEditedAt()) : ""); // Handle null values
            row.createCell(8).setCellValue(asset.getCreatedAt() != null ? sdf.format(asset.getCreatedAt()) : ""); // Handle null values
        }

        // Write to file
        File file = new File(context.getExternalFilesDir(null), "AssetsData.xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            Toast.makeText(context, "Excel file created successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to create Excel file.", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Notify user and open file
        notifyAndOpenFile(context, file);
    }

    private static void notifyAndOpenFile(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Open Excel File"));
    }
}
