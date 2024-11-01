package com.example.rcs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

    private EditText edtPassOld, edtPassNew, edtConfirm;
    private Button btnChange;
    private ImageView btnBack, btnShowOld, btnHideOld, btnShowNew, btnHideNew, btnShowConfirm, btnHideConfirm;
    private ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initUi();

        initListener();

    }

    private void initUi() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        edtPassOld = findViewById(R.id.edtPassOld);
        edtPassNew = findViewById(R.id.edtPassNew);
        edtConfirm = findViewById(R.id.edtConfirm);
        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.btnBack);
        btnShowOld = findViewById(R.id.btnShowOld);
        btnHideOld = findViewById(R.id.btnHideOld);
        btnShowNew = findViewById(R.id.btnShowNew);
        btnHideNew = findViewById(R.id.btnHideNew);
        btnShowConfirm = findViewById(R.id.btnShowConfirm);
        btnHideConfirm = findViewById(R.id.btnHideConfirm);
    }

    private void initListener() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigatetoProfile();
            }
        });

        btnShowOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = edtPassOld.getSelectionStart();
                edtPassOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnShowOld.setVisibility(View.GONE);
                btnHideOld.setVisibility(View.VISIBLE);
                edtPassOld.setSelection(cursorPosition);
            }
        });

        btnHideOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = edtPassOld.getSelectionStart();
                edtPassOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShowOld.setVisibility(View.VISIBLE);
                btnHideOld.setVisibility(View.GONE);
                edtPassOld.setSelection(cursorPosition);
            }
        });

        btnShowNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = edtPassNew.getSelectionStart();
                edtPassNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnShowNew.setVisibility(View.GONE);
                btnHideNew.setVisibility(View.VISIBLE);
                edtPassNew.setSelection(cursorPosition);
            }
        });

        btnHideNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = edtPassNew.getSelectionStart();
                edtPassNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShowNew.setVisibility(View.VISIBLE);
                btnHideNew.setVisibility(View.GONE);
                edtPassNew.setSelection(cursorPosition);
            }
        });

        btnShowConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = edtConfirm.getSelectionStart();
                edtConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnShowConfirm.setVisibility(View.GONE);
                btnHideConfirm.setVisibility(View.VISIBLE);
                edtConfirm.setSelection(cursorPosition);
            }
        });

        btnHideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursorPosition = edtConfirm.getSelectionStart();
                edtConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnShowConfirm.setVisibility(View.VISIBLE);
                btnHideConfirm.setVisibility(View.GONE);
                edtConfirm.setSelection(cursorPosition);
            }
        });
    }

    private void changePass(){
        String oldPass = edtPassOld.getText().toString().trim();
        String newPass = edtPassNew.getText().toString().trim();
        String confirmPass = edtConfirm.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu mới và mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.equals(oldPass)) {
            Toast.makeText(this, "Mật khẩu mới và mật khẩu cũ không được trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }


        if (currentUser != null) {
            progressDialog = new ProgressDialog(ChangePassActivity.this);
            progressDialog.setMessage("Đang cập nhật mật khẩu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(currentUser.getEmail(), oldPass);

            currentUser.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(newPass)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            navigatetoProfile();
                                        } else {
                                            Toast.makeText(this, "Đổi mật khẩu thất bại: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "Xác thực lại thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigatetoProfile() {
        Intent intent = new Intent(ChangePassActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }


}