package com.example.rcs;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rcs.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            //get firebase user info
            String email = auth.getCurrentUser().getEmail();
            String name = auth.getCurrentUser().getDisplayName();
            Uri photoUrl = auth.getCurrentUser().getPhotoUrl();
            Log.d("status", "onCreate: " + email + " " + name + " " + photoUrl);
            //set user info
            if (name.isEmpty()) {
                binding.userName.setText("No name");
            }else binding.userName.setText(name);
            binding.userEmail.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.ic_passworddoc).into(binding.imageView);
        }
        setContentView(binding.getRoot());

    }
}
