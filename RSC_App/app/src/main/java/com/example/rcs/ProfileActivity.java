package com.example.rcs;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                edtName.setFocusable(true);
                edtName.setFocusableInTouchMode(true);
                edtName.requestFocus();
            }
        });

        edtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtEmail.setFocusable(true);
                edtEmail.setFocusableInTouchMode(true);
                edtEmail.requestFocus();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setFocusable(false);
                edtName.setFocusableInTouchMode(false);

                edtEmail.setFocusable(false);
                edtEmail.setFocusableInTouchMode(false);
                updateProfile();


            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                deleteUser();

                String newEmail = edtEmail.getText().toString().trim();
                showPasswordDialogAndReauthenticate(newEmail);

//                if (currentUser.isEmailVerified()) {
//                    Toast.makeText(ProfileActivity.this, "Tài khoản đã được xác minh.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(ProfileActivity.this, "Tài khoản chưa được xác minh.", Toast.LENGTH_SHORT).show();
//                    if (currentUser != null) {
//                        currentUser.sendEmailVerification()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(ProfileActivity.this, "Email xác minh đã được gửi. Vui lòng kiểm tra hộp thư.", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(ProfileActivity.this, "Gửi email xác minh thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                }


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


    private void updateProfile(){
        if (currentUser != null) {
            String newDisplayName = edtName.getText().toString().trim();
            String newEmail = edtEmail.getText().toString().trim();

            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Đang cập nhật ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newDisplayName)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Cập nhật thông tin tài khoản thành công.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Cập nhật thông tin tài khoản thất bại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void showPasswordDialogAndReauthenticate(String newEmail) {
        // Tạo một AlertDialog với một EditText để nhập mật khẩu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác thực lại");
        builder.setMessage("Vui lòng nhập mật khẩu để xác thực lại:");

        // Tạo EditText cho việc nhập mật khẩu
        final EditText inputPassword = new EditText(this);
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputPassword);

        // Thiết lập nút Xác nhận
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String password = inputPassword.getText().toString().trim();
            if (!password.isEmpty()) {
                updateEmail(newEmail, password);
            } else {
                Toast.makeText(ProfileActivity.this, "Mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
            }
        });

        // Thiết lập nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        // Hiển thị hộp thoại
        builder.show();
    }

    private void updateEmail(String newEmail, String password) {
        if (currentUser == null) {
            Toast.makeText(ProfileActivity.this, "User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }



        String currentEmail = currentUser.getEmail();
        if (currentEmail == null) {
            Toast.makeText(ProfileActivity.this, "Current email is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, password); // Xác thực lại với mật khẩu hiện tại

        // Bắt đầu xác thực lại người dùng
        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Xác thực lại thành công, tiến hành cập nhật email
                    currentUser.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Email updated successfully!", Toast.LENGTH_SHORT).show();
                                        edtEmail.setText(newEmail);
                                    } else {
                                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred.";
                                        Toast.makeText(ProfileActivity.this, "Failed to update email: " + errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Re-authentication failed.";
                    Toast.makeText(ProfileActivity.this, "Re-authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void deleteUser() {
        if (currentUser != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản?")
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
                            Toast.makeText(ProfileActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}