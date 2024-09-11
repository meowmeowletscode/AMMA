package com.example.amma;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LabelTest {

    private Label label;

    // Setup method to initialize the Label object before each test
    @Before
    public void setUp() {
        label = new Label("Electronics");
    }

    // Test the constructor for the Label class
    @Test
    public void testLabelConstructor() {
        assertEquals("Electronics", label.getLabelName());
    }

    // Test the getter for Label Name
    @Test
    public void testGetLabelName() {
        label = new Label("Office Supplies");
        assertEquals("Office Supplies", label.getLabelName());
    }
}

