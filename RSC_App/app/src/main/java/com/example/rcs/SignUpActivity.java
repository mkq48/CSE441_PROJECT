package com.example.rcs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rcs.databinding.ActivitySignUpBinding;
import com.example.rcs.fragment.DatePickerFragment;
import com.example.rcs.viewmodel.SignUpViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivitySignUpBinding binding;
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(this);

        //inflate layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        //handle date picker
        binding.ngaySinh.setOnClickListener(v -> showDatePicker());

        //provide view model
        SignUpViewModel signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        binding.setSignUpViewModel(signUpViewModel);

        //reset error message
        signUpViewModel.resetErrorMessage();
        //handle error message
        signUpViewModel.tenTaiKhoanError.observe(this, s -> binding.tenDangNhap.setError(s));
        signUpViewModel.ngaySinhError.observe(this, s -> binding.ngaySinh.setError(s));
        signUpViewModel.emailError.observe(this, s -> binding.emailDk.setError(s));
        signUpViewModel.matKhauError.observe(this, s -> binding.passwordDk.setError(s));
        signUpViewModel.xacNhanMatKhauError.observe(this, s -> binding.xacNhanMk.setError(s));

        //set lifecycle owner
        binding.setLifecycleOwner(binding.getLifecycleOwner());

        //
        binding.btnDangKi.setOnClickListener(v -> {
            if (signUpViewModel.isValidInput()) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(signUpViewModel.email, signUpViewModel.matKhau)
                        .addOnCompleteListener(this, task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //handle the selected date
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        //use the selected date
        //format: dd/MM/yyyy
        String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
        binding.ngaySinh.setText(date);
        binding.ngaySinh.setError(null);
    }
    public void showDatePicker() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
