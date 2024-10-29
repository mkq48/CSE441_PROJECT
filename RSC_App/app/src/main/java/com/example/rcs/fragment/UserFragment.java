package com.example.rcs.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rcs.R;
import com.example.rcs.SignInActivity;
import com.example.rcs.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //binding
        FragmentUserBinding binding = FragmentUserBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            navigateToLogin();
        }
        binding.btnLogout.setOnClickListener(v->{
            logoutUser();
        });
        binding.btnProfile.setOnClickListener(v->{
            //use navController
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_userFragment2_to_profileFragment);
        });
        return binding.getRoot();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void logoutUser() {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setIcon(R.drawable.alert)
                .setPositiveButton("Có", (dialog, which) -> {
                    mAuth.signOut();
                    Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    clearSharedPreferences();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RSC", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userEmail");
        editor.remove("isLoggedIn");
        editor.apply();
        navigateToLogin();
    }
}