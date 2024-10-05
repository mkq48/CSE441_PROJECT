package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.rcs.databinding.ActivitySignInBinding;
import com.example.rcs.viewmodel.SignInViewModel;

public class SignInActivity extends AppCompatActivity {

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        ActivitySignInBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        //create view model
        SignInViewModel viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        binding.setSignInViewModel(viewModel);
        binding.setLifecycleOwner(this);

        //set lifecycle owner
        binding.setLifecycleOwner(this);

        //set error message
        viewModel.emailError.observe(this, s -> {
            binding.dnEmail.requestFocus();
            binding.dnEmail.setError(s);
        });
        viewModel.passwordError.observe(this, s ->{
            binding.dnMatKhau.requestFocus();
            binding.dnMatKhau.setError(s);
        });
        viewModel.isLoginSuccess.observe(this, aBoolean -> {
            if(aBoolean){
                //go to main activity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        });

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

        binding.quenMk.setOnClickListener(view ->{
            //go to reset password activity
            Intent intent = new Intent(this, ResetPasswordActivity.class);
            startActivity(intent);
        });


    }

}
