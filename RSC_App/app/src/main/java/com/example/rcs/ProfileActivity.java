package com.example.rcs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.UserProfileChangeRequest;


public class ProfileActivity extends AppCompatActivity {

    private ImageView avartar, btnBack;
    private EditText edtName, edtEmail;
    private Button btnChangepass, btnUpdateProfile, btnDel;

    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initUI();
        initListener();
        loadProfile();

    }

    private void initUI(){
        avartar = findViewById(R.id.changeAvatar);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnBack = findViewById(R.id.btnBack);
        btnChangepass = findViewById(R.id.btnChangePass);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnDel = findViewById(R.id.btnDel);
    }

    private void initListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        avartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateAvatar.class);
                startActivity(intent);
                finish();
            }
        });

        edtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setFocusableInTouchMode(true);
                edtName.requestFocus();
            }
        });

        edtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEmail.setFocusableInTouchMode(true);
                edtEmail.requestFocus();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setFocusableInTouchMode(false);

                edtEmail.setFocusableInTouchMode(false);
                updateProfileUser();


            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();

            }
        });


    }

    private void loadProfile(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();
            Uri photoUrl = currentUser.getPhotoUrl();

            edtName.setText(displayName);
            edtEmail.setText(email);
            if (photoUrl != null) {
                Glide.with(avartar.getContext())
                        .load(photoUrl)
                        .into(avartar);
            } else {
                avartar.setImageResource(R.drawable.avartar_default);
            }
        }
    }


    private void updateProfileUser(){
    if (currentUser != null) {
        String newDisplayName = edtName.getText().toString().trim();
        String newEmail = edtEmail.getText().toString().trim();

        confirmUpdate(newEmail, newDisplayName);

    }
}

    private void confirmUpdate(String newEmail, String newDisplayName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác thực lại");
        builder.setMessage("Vui lòng nhập mật khẩu để xác thực lại:");

        final EditText inputPassword = new EditText(this);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputPassword);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String password = inputPassword.getText().toString().trim();
            if (!password.isEmpty()) {
                updateProfile(newEmail, password, newDisplayName);
            } else {
                Toast.makeText(ProfileActivity.this, "Mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateDisplayName(String newDisplayName){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newDisplayName)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Cập nhật thông tin tài khoản thành công.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Cập nhật thông tin tài khoản thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateProfile(String newEmail, String password, String newDisplayName) {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Đang cập nhật ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (currentUser == null) {
            Toast.makeText(ProfileActivity.this, "Vui lòng đăng nhập tài khoản.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail = currentUser.getEmail();
        if (currentEmail == null) {
            Toast.makeText(ProfileActivity.this, "Email hiện tại không có sẵn.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, password);


        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    currentUser.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updateDisplayName(newDisplayName);
                                        edtEmail.setText(newEmail);
                                        progressDialog.dismiss();
                                    } else {
                                        progressDialog.dismiss();
                                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred.";
                                        Toast.makeText(ProfileActivity.this, "Cập nhật thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Re-authentication lỗi.";
                    Toast.makeText(ProfileActivity.this, "Re-authentication lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void deleteUser() {
        if (currentUser != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản? Tất cả thông tin sẽ bị xóa vĩnh viễn và không thể khôi phục.")
                    .setIcon(R.drawable.alert)
                    .setPositiveButton("Xác nhận", (dialog, which) -> {
                        deleteAccount();
                    })
                    .setNegativeButton("Không", null)
                    .show();
        }
    }



    private void deleteAccount() {
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            clearSharedPreferences();
                            Toast.makeText(ProfileActivity.this, "Xóa tài khoản thành công.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(ProfileActivity.this, "Xóa tài khoản thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("RSC", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userEmail");
        editor.remove("isLoggedIn");
        editor.apply();

        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}