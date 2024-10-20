package com.example.rcs;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rcs.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ActivityForgetPasswordBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());

        //handle event
        binding.btnXacNhan.setOnClickListener(view -> {
            //send email to reset password
            String email = binding.resetEmail.getText().toString().trim();
            if (email.isEmpty()) {
                binding.resetEmail.setError("Vui lòng nhập địa chỉ email");
                binding.resetEmail.requestFocus();
            } else {
                auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Kiểm tra mail của bạn để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
                        });
            }
        });
        setContentView(binding.getRoot());
    }
}