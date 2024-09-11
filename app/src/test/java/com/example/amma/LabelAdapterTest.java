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

public class LabelAdapterTest {

    @Mock
    private LabelAdapter.OnItemClickListener mockListener;

    private LabelAdapter adapter;
    private List<Label> labels;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        labels = new ArrayList<>();
        labels.add(new Label("Label1"));
        labels.add(new Label("Label2"));

        adapter = new LabelAdapter(labels, mockListener);
    }

    @Test
    public void testItemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a real ViewHolder with a real View
        View mockView = mock(View.class);
        TextView mockTextView = mock(TextView.class);
        when(mockView.findViewById(R.id.text_view_label)).thenReturn(mockTextView);

        LabelAdapter.ViewHolder viewHolder = new LabelAdapter.ViewHolder(mockView, mockListener);

        // Bind the ViewHolder
        adapter.onBindViewHolder(viewHolder, 0);

        // Verify that the text is set correctly
        verify(mockTextView).setText("Label1");
    }

    @Test
    public void testOnItemClick() {
        // Create a real ViewHolder with a real View
        View mockView = mock(View.class);
        TextView mockTextView = mock(TextView.class);
        when(mockView.findViewById(R.id.text_view_label)).thenReturn(mockTextView);

        LabelAdapter.ViewHolder viewHolder = new LabelAdapter.ViewHolder(mockView, mockListener);

        // Set the ViewHolder for the adapter
        adapter.onBindViewHolder(viewHolder, 0);

        // Simulate item click
        viewHolder.itemView.performClick();

        // Verify that onItemClick was called
        verify(mockListener).onItemClick(0);
    }
}
