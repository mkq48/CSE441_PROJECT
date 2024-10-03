package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rcs.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        ActivitySignInBinding binding = ActivitySignInBinding.inflate(getLayoutInflater());

        //event handler
        binding.btnDangNhap.setOnClickListener(view -> {

        });

        binding.btnDangKi.setOnClickListener(view ->{
            //go to sign up activity
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        //set layout
        setContentView(binding.getRoot());
    }

}
