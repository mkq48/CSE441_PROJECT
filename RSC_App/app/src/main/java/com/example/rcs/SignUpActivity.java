package com.example.rcs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rcs.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    //create auth
    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);

        //inflate layout
        ActivitySignUpBinding binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        //event handler
        binding.btnDangKi.setOnClickListener(view -> {
            String email = binding.tenDangNhap.getText().toString();
            String password = binding.matKhau.getText().toString();
            progressDialog.setMessage("Creating account...");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("status", "createUserWithEmail:success");
                            startActivity(new Intent(this, SignInActivity.class));
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("status", "createUserWithEmail:failure", task.getException());
                        }
                    });
        });

        //set layout
        setContentView(binding.getRoot());


    }
}
