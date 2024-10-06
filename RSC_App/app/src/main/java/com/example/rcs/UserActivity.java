package com.example.rcs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rcs.databinding.ActivityUserBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        ActivityUserBinding binding = ActivityUserBinding.inflate(getLayoutInflater());

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            //get firebase user info
            String email = auth.getCurrentUser().getEmail();
            String name = auth.getCurrentUser().getDisplayName();
            Uri photoUrl = auth.getCurrentUser().getPhotoUrl();
            Log.d("status", "onCreate: " + email + " " + name + " " + photoUrl);
            //set user info
            if (name==null) {
                binding.userName.setText("No name");
            }else binding.userName.setText(name);
            binding.userEmail.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.ic_passworddoc).into(binding.imageView);
        }
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });

    }
}
