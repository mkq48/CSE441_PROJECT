package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.rcs.databinding.ActivitySignInBinding;
import com.example.rcs.viewmodel.AuthViewModel;

public class SignInActivity extends AppCompatActivity {

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        ActivitySignInBinding binding = ActivitySignInBinding.inflate(getLayoutInflater());

        //create view model
        AuthViewModel viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        binding.setAuthViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //event handler
        binding.toggle.setOnClickListener(view -> {
            passwordVisible=!passwordVisible;
            if(passwordVisible){
                //password visible
                binding.dnMatKhau.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.toggle.setText(R.string.toggle_off);
            }else{
                //password invisible
                binding.dnMatKhau.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.toggle.setText(R.string.toggle_on);
            }
            //move cursor to end of text
            binding.dnMatKhau.setSelection(binding.dnMatKhau.getText().length());
        });

        binding.goToDk.setOnClickListener(view ->{
            //go to sign up activity
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.btnDangNhap.setOnClickListener(view ->{

        });

        //set layout
        setContentView(binding.getRoot());
    }

}
