package com.example.amma.ui.usercontrol;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amma.databinding.FragmentCustomizelabelBinding;
import com.example.amma.databinding.FragmentUsercontrolBinding;


public class UserControlFragment extends Fragment {

    private com.example.amma.ui.usercontrol.UserControlViewModel userControlViewModel;
    private FragmentUsercontrolBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userControlViewModel = new ViewModelProvider(this).get(com.example.amma.ui.usercontrol.UserControlViewModel.class);

        binding = FragmentUsercontrolBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.txtTitle;
        userControlViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
