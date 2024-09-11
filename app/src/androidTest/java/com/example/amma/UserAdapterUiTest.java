package com.example.amma;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class UserAdapterUiTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testRecyclerViewDisplaysUsers() {
        // Check if the RecyclerView is displayed
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));

        // Verify that the first user is displayed
        onView(withText("user1")).check(matches(isDisplayed()));
        onView(withText("Admin")).check(matches(isDisplayed()));

        // Verify that the second user is displayed
        onView(withText("user2")).check(matches(isDisplayed()));
        onView(withText("Staff")).check(matches(isDisplayed()));
    }
}
