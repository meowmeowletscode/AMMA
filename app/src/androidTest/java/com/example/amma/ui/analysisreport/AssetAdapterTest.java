package com.example.amma.ui.analysisreport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.amma.Asset;
import com.example.amma.AssetAdapter;
import com.example.amma.MainActivity;
import com.example.amma.R;

@RunWith(AndroidJUnit4.class)
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

    private AssetAdapter assetAdapter;
    private List<Asset> assetList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        assetList = Arrays.asList(
                new Asset("Asset 1", "1234567890", 10, "Description 1", "Label 1"),
                new Asset("Asset 2", "0987654321", 20, "Description 2", "Label 2")
        );

        assetAdapter = new AssetAdapter(assetList);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, assetAdapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a mock ViewHolder
        AssetAdapter.ViewHolder viewHolder = new AssetAdapter.ViewHolder(mockView);

        // Set the mock TextViews in the ViewHolder
        viewHolder.textAssetName = mockTextAssetName;
        viewHolder.textBarcode = mockTextBarcode;
        viewHolder.textQuantity = mockTextQuantity;
        viewHolder.textDescription = mockTextDescription;
        viewHolder.textLabel = mockTextLabel;

        // Bind the first asset to the ViewHolder
        assetAdapter.onBindViewHolder(viewHolder, 0);

        // Verify that the TextViews have been set correctly
        verify(mockTextAssetName).setText("Asset 1");
        verify(mockTextBarcode).setText("1234567890");
        verify(mockTextQuantity).setText("10");
        verify(mockTextDescription).setText("Description 1");
        verify(mockTextLabel).setText("Label 1");
    }

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testOnCreateViewHolder() {
        Context context = activityRule.getActivity();

        // Create a mock ViewGroup
        ViewGroup mockParent = mock(ViewGroup.class);
        when(mockParent.getContext()).thenReturn(context);

        // Inflate the view
        View view = LayoutInflater.from(context).inflate(R.layout.asset_item, mockParent, false);

        // Create a new ViewHolder
        AssetAdapter.ViewHolder viewHolder = assetAdapter.onCreateViewHolder(mockParent, 0);

        // Check that the ViewHolder is not null and contains the correct views
        assertEquals(view, viewHolder.itemView);
    }
}