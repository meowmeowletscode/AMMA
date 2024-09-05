package com.example.amma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {
    private List<Asset> assetList;

    public AssetAdapter(List<Asset> assetList) {
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Asset asset = assetList.get(position);

        holder.textAssetName.setText(asset.getAssetName());
        holder.textBarcode.setText(asset.getBarcode());
        holder.textQuantity.setText(String.valueOf(asset.getQuantity()));
        holder.textDescription.setText(asset.getDescription());
        holder.textLabel.setText(asset.getLabel());
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textAssetName;
        TextView textBarcode;
        TextView textQuantity;
        TextView textDescription;
        TextView textLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textAssetName = itemView.findViewById(R.id.textAssetName);
            textBarcode = itemView.findViewById(R.id.textBarcode);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLabel = itemView.findViewById(R.id.textLabel);
        }
    }
}

