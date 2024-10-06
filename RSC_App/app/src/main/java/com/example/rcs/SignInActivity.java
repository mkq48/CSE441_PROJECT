package com.example.rcs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.rcs.databinding.ActivitySignInBinding;
import com.example.rcs.viewmodel.SignInViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private boolean passwordVisible = false;
    private final FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);

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

        binding.btnDangNhap.setOnClickListener(view -> {
            if (viewModel.isValidEmail()) {
                //sign in with firebase
                progressDialog.show();
                auth.signInWithEmailAndPassword(viewModel.email, viewModel.password).addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        //go to user activity
                        Intent intent = new Intent(SignInActivity.this, UserActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(SignInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
