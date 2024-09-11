package com.example.amma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserAdapterTest {

    @Mock
    Context mockContext;

    @Mock
    LayoutInflater mockInflater;

    @Mock
    ViewGroup mockParent;

    @Mock
    UserAdapter.OnUserClickListener mockListener;

    private UserAdapter userAdapter;
    private List<User> userList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userList = Arrays.asList(
                new User("user1", "password1", "Admin"),
                new User("user2", "password2", "Staff")
        );
        when(mockContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(mockInflater);
        userAdapter = new UserAdapter(mockContext, userList);
    }

    @Test
    public void testOnCreateViewHolder() {
        // Create a mock view and set up expectations
        View mockView = mock(View.class);
        when(mockInflater.inflate(anyInt(), any(ViewGroup.class), anyBoolean())).thenReturn(mockView);

        UserAdapter.UserViewHolder viewHolder = userAdapter.onCreateViewHolder(mockParent, 0);

        assertEquals(mockView, viewHolder.itemView);
    }

    @Test
    public void testOnBindViewHolder() {
        // Create a mock view
        View mockView = mock(View.class);
        TextView mockUserNameTextView = mock(TextView.class);
        TextView mockRoleTextView = mock(TextView.class);

        // Set up the UserViewHolder with mock views
        UserAdapter.UserViewHolder viewHolder = new UserAdapter.UserViewHolder(mockView);
        viewHolder.userNameTextView = mockUserNameTextView;
        viewHolder.roleTextView = mockRoleTextView;

        // Bind the view holder
        userAdapter.onBindViewHolder(viewHolder, 0);

        // Verify that the data is bound to the TextViews
        verify(mockUserNameTextView).setText("user1");
        verify(mockRoleTextView).setText("Admin");
    }

    @Test
    public void testItemClickListener() {
        UserAdapter.OnUserClickListener mockListener = mock(UserAdapter.OnUserClickListener.class);
        userAdapter.setOnUserClickListener(mockListener);

        LayoutInflater inflater = LayoutInflater.from(RuntimeEnvironment.application);
        View view = inflater.inflate(R.layout.user_item, null);
        TextView userNameTextView = view.findViewById(R.id.userNameTextView);
        TextView roleTextView = view.findViewById(R.id.roleTextView);

        UserAdapter.UserViewHolder viewHolder = new UserAdapter.UserViewHolder(view);
        userAdapter.onBindViewHolder(viewHolder, 0);

        // Simulate click
        view.performClick();

        // Verify that the click listener was triggered
        verify(mockListener).onUserClick(userList.get(0));
    }

}
