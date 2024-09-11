package com.example.amma.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.amma.R;
import com.example.amma.User;
import com.example.amma.UserManager;
import com.example.amma.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        final ImageButton btnAddAsset = binding.btnAddAsset;
        final ImageButton btnEditAsset = binding.btnEditAsset;
        final ImageButton btnCustomizeLabel = binding.btnCustomizeLabel;

        User currentUser = UserManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            textView.setText("Welcome " + currentUser.getRole() + ", "+ currentUser.getUserName());
        } else {
            textView.setText("No user logged in");
        }

        btnAddAsset.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_addAsset);
        });

        btnEditAsset.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_editAsset);
        });

        btnCustomizeLabel.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_customizeLabel);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}