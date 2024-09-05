package com.example.amma;

import java.sql.Date;
import java.sql.Timestamp;

public class Asset {
    private String assetName;
    private String barcode;
    private int quantity;
    private String description;
    private String label;
    private Timestamp createdAt;

    public Asset(String assetName, String barcode, int quantity, String description, String label) {
        this.assetName = assetName;
        this.barcode = barcode;
        this.quantity = quantity;
        this.description = description;
        this.label = label;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

