package com.example.rcs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.rcs.databinding.ActivitySignUpBinding;
import com.example.rcs.fragment.DatePickerFragment;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        //handle date picker
        binding.ngaySinh.setOnClickListener(v -> showDatePicker());
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
    }
    public void showDatePicker() {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
