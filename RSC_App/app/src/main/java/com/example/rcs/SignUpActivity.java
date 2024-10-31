package com.example.rcs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.rcs.databinding.ActivitySignUpBinding;
import com.example.rcs.fragment.DatePickerFragment;
import com.example.rcs.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivitySignUpBinding binding;
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);

        //inflate layout
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //handle date picker
        binding.ngaySinh.setOnClickListener(v -> showDatePicker());

        //
        binding.btnDangKi.setOnClickListener(v -> {
            String tenDangNhap = binding.tenDangNhap.getText().toString();
            String email = binding.emailDk.getText().toString();
            String matKhau = binding.passwordDk.getText().toString();
            String ngaySinh = binding.ngaySinh.getText().toString();
            String xacNhanMatKhau = binding.xacNhanMk.getText().toString();

            if (isValidInput(tenDangNhap, email, matKhau, ngaySinh, xacNhanMatKhau)) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(email, matKhau)
                        .addOnCompleteListener(this, task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(tenDangNhap)
                                            .setPhotoUri(Uri.parse("https://icons.iconarchive.com/icons/martin-berube/character/128/Devil-icon.png"))
                                            .build();
                                    user.updateProfile(profileUpdates).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("onUpdate", "user profile updated");
                                        }else Log.d("onUpdate", "user profile not updated");
                                    });
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    CollectionReference usersRef = db.collection("users");
                                    UserProfile userProfile = new UserProfile(tenDangNhap, "https://icons.iconarchive.com/icons/martin-berube/character/128/Devil-icon.png");
                                    usersRef.document(user.getUid()).set(userProfile)
                                            .addOnSuccessListener(aVoid -> {
                                                // Data written successfully
                                                Log.d("onPush", "User profile stored in Firestore.");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle error
                                                Log.w("onPush", "Error storing user profile", e);
                                            });
                                }else Log.d("isNull", "user null");
                                finish();
                            } else {
                                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }

                        });
            }
        });
    }

    private boolean isValidInput(String tenDangNhap, String email, String matKhau, String ngaySinh, String xacNhanMatKhau) {
        boolean check=true;
        if(tenDangNhap.isEmpty()){
            binding.tenDangNhap.setError("Vui lòng nhập tên đăng nhập");
            check=false;
        }
        if (ngaySinh.isEmpty()){
            binding.ngaySinh.setError("Vui lòng nhập ngày sinh");
            check=false;
        }
        if(email.isEmpty()){
            binding.emailDk.setError("Vui lòng nhập email");
            check=false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailDk.setError("Email không hợp lệ");
            check=false;
        }
        if(matKhau.isEmpty()){
            binding.passwordDk.setError("Vui lòng nhập mật khẩu");
            check=false;
        }else if (matKhau.length()<6){
            binding.passwordDk.setError("Mật khẩu phải có ít nhất 6 ký tự");
            check=false;
        }
        if(xacNhanMatKhau.isEmpty()){
            binding.xacNhanMk.setError("Vui lòng nhập lại mật khẩu");
            check=false;
        }else if(!matKhau.equals(xacNhanMatKhau)){
            binding.xacNhanMk.setError("Mật khẩu không khớp");
            check=false;
        }
        return check;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //handle the selected date
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        //use the selected date
        //format: dd/MM/yyyy
        @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
        binding.ngaySinh.setText(date);
        binding.ngaySinh.setError(null);
    }
    public void showDatePicker() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
