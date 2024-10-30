package com.example.rcs.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rcs.databinding.FragmentChangePassBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassFragment extends Fragment {

    private FragmentChangePassBinding binding;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangePassBinding.inflate(getLayoutInflater(),container,false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        binding.btnChange.setOnClickListener(view -> changePass());
        binding.btnShowOld.setOnClickListener(view -> {
            int cursorPosition = binding.edtPassOld.getSelectionStart();
            binding.edtPassOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.btnShowOld.setVisibility(View.GONE);
            binding.btnHideOld.setVisibility(View.VISIBLE);
            binding.edtPassOld.setSelection(cursorPosition);
        });
        binding.btnHideOld.setOnClickListener(view -> {
            int cursorPosition = binding.edtPassOld.getSelectionStart();
            binding.edtPassOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.btnShowOld.setVisibility(View.VISIBLE);
            binding.btnHideOld.setVisibility(View.GONE);
            binding.edtPassOld.setSelection(cursorPosition);
        });
        binding.btnShowNew.setOnClickListener(view -> {
            int cursorPosition = binding.edtPassNew.getSelectionStart();
            binding.edtPassNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.btnShowNew.setVisibility(View.GONE);
            binding.btnHideNew.setVisibility(View.VISIBLE);
            binding.edtPassNew.setSelection(cursorPosition);
        });
        binding.btnHideNew.setOnClickListener(view -> {
            int cursorPosition = binding.edtPassNew.getSelectionStart();
            binding.edtPassNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.btnShowNew.setVisibility(View.VISIBLE);
            binding.btnHideNew.setVisibility(View.GONE);
            binding.edtPassNew.setSelection(cursorPosition);
        });
        binding.btnShowConfirm.setOnClickListener(view -> {
            int cursorPosition = binding.edtConfirm.getSelectionStart();
            binding.edtConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.btnShowConfirm.setVisibility(View.GONE);
            binding.btnHideConfirm.setVisibility(View.VISIBLE);
            binding.edtConfirm.setSelection(cursorPosition);
        });
        binding.btnHideConfirm.setOnClickListener(view -> {
            int cursorPosition = binding.edtConfirm.getSelectionStart();
            binding.edtConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.btnShowConfirm.setVisibility(View.VISIBLE);
            binding.btnHideConfirm.setVisibility(View.GONE);
            binding.edtConfirm.setSelection(cursorPosition);
        });
        return binding.getRoot();
    }
    private void changePass(){
        String passOld = binding.edtPassOld.getText().toString().trim();
        String newPass = binding.edtPassNew.getText().toString().trim();
        String confirmPass = binding.edtConfirm.getText().toString().trim();

        if (passOld.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(getContext(), "Mật khẩu mới và mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPass.equals(passOld)) {
            Toast.makeText(getContext(), "Mật khẩu mới và mật khẩu cũ không được trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang cập nhật mật khẩu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(currentUser.getEmail(), passOld);

            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(newPass)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            NavController navController = NavHostFragment.findNavController(this);
                                            //back to previous fragment
                                            navController.popBackStack();
                                        } else {
                                            Toast.makeText(getContext(), "Đổi mật khẩu thất bại: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Xác thực lại thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }
}