package com.example.rcs.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.rcs.MainActivity;
import com.example.rcs.R;
import com.example.rcs.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileFragment extends Fragment {
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FragmentProfileBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);

        loadProfile();
        binding.btnUpdateProfile.setOnClickListener(view -> updateProfileUser());
        binding.btnChangePass.setOnClickListener(view -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_profileFragment_to_changePassFragment);
        });
        binding.btnDel.setOnClickListener(view -> deleteUser());
        binding.changeAvatar.setOnClickListener(v->{
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_profileFragment_to_updateAvatarFragment);
        });
        return binding.getRoot();
    }

    private void deleteUser() {
        if (currentUser != null) {
            new AlertDialog.Builder(getContext())
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
                            clearSharedPreferences();
                            Toast.makeText(getContext(), "Xóa tài khoản thành công.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Xóa tài khoản thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RSC", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userEmail");
        editor.remove("isLoggedIn");
        editor.apply();

        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void updateProfileUser() {
        if (currentUser != null) {
            String newDisplayName = binding.edtName.getText().toString().trim();
            String newEmail = binding.edtEmail.getText().toString().trim();

            confirmUpdate(newEmail, newDisplayName);
        }
    }

    private void confirmUpdate(String newEmail, String newDisplayName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác thực lại");
        builder.setMessage("Vui lòng nhập mật khẩu để xác thực lại:");

        final EditText inputPassword = new EditText(getContext());
        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputPassword);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String password = inputPassword.getText().toString().trim();
            if (!password.isEmpty()) {
                updateProfile(newEmail, password, newDisplayName);
            } else {
                Toast.makeText(getActivity(), "Mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void loadProfile() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            binding.edtName.setText(currentUser.getDisplayName());
            binding.edtEmail.setText(currentUser.getEmail());
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(currentUser.getPhotoUrl())
                        .override(500, 500)
                        .into(binding.avatarProfileImg);
            }
        }
    }
    private void updateProfile(String newEmail, String password, String newDisplayName) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang cập nhật ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (currentUser == null) {
            Toast.makeText(getActivity(), "Vui lòng đăng nhập tài khoản.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail = currentUser.getEmail();
        if (currentEmail == null) {
            Toast.makeText(getContext(), "Email hiện tại không có sẵn.", Toast.LENGTH_SHORT).show();
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
                                        binding.edtEmail.setText(newEmail);
                                        progressDialog.dismiss();
                                    } else {
                                        progressDialog.dismiss();
                                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred.";
                                        Toast.makeText(getContext(), "Cập nhật thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        //Log.d("onFail", "Cập nhật thất bại: " + errorMessage);
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Re-authentication lỗi.";
                    Toast.makeText(getContext(), "Re-authentication lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                            Toast.makeText(getContext(), "Cập nhật thông tin tài khoản thành công.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thông tin tài khoản thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
