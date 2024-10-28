package com.example.rcs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rcs.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private boolean passwordVisible = false;
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);

        //inflate layout
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            //startActivity(new Intent(this, SignUpActivity.class);
        });

        binding.quenMk.setOnClickListener(view ->{
            //startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
        });

        binding.btnDangNhap.setOnClickListener(view -> {
            String email = binding.dnEmail.getText().toString();
            String password = binding.dnMatKhau.getText().toString();
            if (isValidInput(email, password)) {
                //sign in with firebase
                progressDialog.show();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        //go to user activity
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean isValidInput(String email, String password) {
        boolean check=true;
        if (TextUtils.isEmpty(email)) {
            binding.dnEmail.setError("Vui lòng nhập email");
            check = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.dnEmail.setError("Email không hợp lệ");
            check = false;
        }
        if(password.isEmpty()){
            binding.dnMatKhau.setError("Vui lòng nhập mật khẩu");
            check=false;
        }
        return check;
    }

}
