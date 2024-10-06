package com.example.rcs.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {

    public String email="";
    public String password="";

    public MutableLiveData<String> emailError= new MutableLiveData<>();
    public MutableLiveData<String> passwordError= new MutableLiveData<>();
    public boolean isValidEmail() {
        boolean check=true;
        if (TextUtils.isEmpty(email)) {
            emailError.setValue("Vui lòng nhập email");
            check = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setValue("Email không hợp lệ");
            check = false;
        }
        if(password.isEmpty()){
            passwordError.setValue("Vui lòng nhập mật khẩu");
            check=false;
        }
        return check;
    }
}
