package com.example.rcs;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rcs.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //inflate layout
        ActivityForgetPasswordBinding binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        Log.d("status", "onCreate:");
        //handle event
        binding.btnXacNhan.setOnClickListener(view -> {
            //send email to reset password
            String email = binding.resetEmail.getText().toString().trim();
            if (email.isEmpty()) {
                binding.resetEmail.setError("Vui lòng nhập địa chỉ email");
                binding.resetEmail.requestFocus();
            } else {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Kiểm tra mail của bạn để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        setContentView(binding.getRoot());
    }
}
