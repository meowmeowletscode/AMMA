package com.example.amma;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class AssetAdapterTest {

    @Mock
    private View mockView;

    @Mock
    private TextView mockTextAssetName;

    @Mock
    private TextView mockTextBarcode;

    @Mock
    private TextView mockTextQuantity;

    @Mock
    private TextView mockTextDescription;

    @Mock
    private TextView mockTextLabel;

    private AssetAdapter adapter;
    private List<Asset> assets;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock view and TextViews
        when(mockView.findViewById(R.id.textAssetName)).thenReturn(mockTextAssetName);
        when(mockView.findViewById(R.id.textBarcode)).thenReturn(mockTextBarcode);
        when(mockView.findViewById(R.id.textQuantity)).thenReturn(mockTextQuantity);
        when(mockView.findViewById(R.id.textDescription)).thenReturn(mockTextDescription);
        when(mockView.findViewById(R.id.textLabel)).thenReturn(mockTextLabel);

        assets = new ArrayList<>();
        assets.add(new Asset("Asset1", "123456", 10, "Description1", "Label1"));
        assets.add(new Asset("Asset2", "789012", 20, "Description2", "Label2"));

        adapter = new AssetAdapter(assets);
    }

    @Test
    public void testItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a ViewHolder with mock view
        AssetAdapter.ViewHolder viewHolder = new AssetAdapter.ViewHolder(mockView);

        // Bind the ViewHolder
        adapter.onBindViewHolder(viewHolder, 0);

        // Verify that the TextViews are set correctly
        verify(mockTextAssetName).setText("Asset1");
        verify(mockTextBarcode).setText("123456");
        verify(mockTextQuantity).setText("10");
        verify(mockTextDescription).setText("Description1");
        verify(mockTextLabel).setText("Label1");
    }
}
