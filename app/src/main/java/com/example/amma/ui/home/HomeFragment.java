package com.example.amma.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.amma.R;
import com.example.amma.User;
import com.example.amma.UserManager;
import com.example.amma.databinding.FragmentHomeBinding;
import com.example.amma.ui.addasset.AddAssetFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private User currentUser;

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
            textView.setText("Welcome " + currentUser.getUserName());
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

//        btnAddAsset.setOnClickListener(new BtnAddAssetClickListener());
//        btnEditAsset.setOnClickListener(new BtnEditAssetClickListener());
//        btnCustomizeLabel.setOnClickListener(new BtnCustomizeLabelClickListener());

        return root;
    }

    public class BtnAddAssetClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_addAsset);
        }
    }

    public class BtnEditAssetClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_editAsset);
        }
    }

    public class BtnCustomizeLabelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_customizeLabel);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}