package com.example.amma.ui.usercontrol;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amma.R;
import com.example.amma.User;
import com.example.amma.UserSQL;
import com.example.amma.UserAdapter;
import com.example.amma.databinding.FragmentUsercontrolBinding;

import java.util.ArrayList;
import java.util.List;

public class UserControlFragment extends Fragment {

    private UserControlViewModel userControlViewModel;
    private FragmentUsercontrolBinding binding;

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> users;
    private UserSQL userSQL;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userControlViewModel = new ViewModelProvider(this).get(UserControlViewModel.class);

        binding = FragmentUsercontrolBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UserSQL and RecyclerView
        userSQL = new UserSQL();
        recyclerView = binding.recyclerViewUsers;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch users and set up RecyclerView
        fetchUsers();

        // Set up buttons
        setupButtons();

        final TextView textView = binding.txtTitle;
        userControlViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    private void fetchUsers() {
        // Retrieve users using UserSQL
        users = userSQL.getUsers();
        adapter = new UserAdapter(getContext(), users);
        recyclerView.setAdapter(adapter);
    }

    private void setupButtons() {
        binding.btnAddUser.setOnClickListener(v -> showAddUserDialog());
        binding.btnEditUser.setOnClickListener(v -> showEditUserDialog());
        binding.btnDeleteUser.setOnClickListener(v -> showDeleteUserDialog());
    }

    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add New User");

        // Create input fields programmatically
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20); // Add some padding to look nice

        // Username input
        final EditText inputUsername = new EditText(getContext());
        inputUsername.setHint("Username");
        layout.addView(inputUsername);

        // Password input
        final EditText inputPassword = new EditText(getContext());
        inputPassword.setHint("Password");
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(inputPassword);

        // Role selection spinner
        Spinner roleSpinner = new Spinner(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{"Admin", "Staff"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        layout.addView(roleSpinner);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String username = inputUsername.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String selectedRole = roleSpinner.getSelectedItem().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                boolean isAdded = userSQL.addUser(username, password, selectedRole);
                if (isAdded) {
                    fetchUsers();
                    Toast.makeText(getContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to add user. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Username and password cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditUserDialog() {
        // Get current users
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getUserName());
        }

        // Create a dialog to select a user to edit
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select User to Edit");

        // Convert list of users to a CharSequence array
        CharSequence[] usersArray = userNames.toArray(new CharSequence[0]);

        builder.setItems(usersArray, (dialog, which) -> {
            String selectedUser = userNames.get(which);
            showEditPasswordDialog(selectedUser);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditPasswordDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit User Password");

        // Create input fields for old and new passwords
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputOldPassword = new EditText(getContext());
        inputOldPassword.setHint("Enter old password");
        layout.addView(inputOldPassword);

        final EditText inputNewPassword = new EditText(getContext());
        inputNewPassword.setHint("Enter new password");
        layout.addView(inputNewPassword);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String oldPassword = inputOldPassword.getText().toString().trim();
            String newPassword = inputNewPassword.getText().toString().trim();
            if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                boolean isUpdated = userSQL.editUserPassword(username, oldPassword, newPassword);
                if (isUpdated) {
                    fetchUsers();
                    Toast.makeText(getContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update password. Please check your old password and try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Passwords cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteUserDialog() {
        // Get current users
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getUserName());
        }

        // Create a dialog to select a user to delete
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select User to Delete");

        // Convert list of users to a CharSequence array
        CharSequence[] usersArray = userNames.toArray(new CharSequence[0]);

        builder.setItems(usersArray, (dialog, which) -> {
            String selectedUser = userNames.get(which);
            showConfirmDeleteUserDialog(selectedUser);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showConfirmDeleteUserDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete User");

        // Create an EditText for entering the password
        final EditText inputPassword = new EditText(getContext());
        inputPassword.setHint("Enter password to confirm deletion");
        builder.setView(inputPassword);

        builder.setPositiveButton("Delete", (dialog, which) -> {
            String password = inputPassword.getText().toString().trim();
            if (!password.isEmpty()) {
                boolean isDeleted = userSQL.deleteUser(username, password); // Ensure this method is implemented
                if (isDeleted) {
                    fetchUsers();
                    Toast.makeText(getContext(), "User deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to delete user. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
