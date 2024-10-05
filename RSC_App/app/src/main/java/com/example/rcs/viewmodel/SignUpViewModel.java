package com.example.rcs.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class SignUpViewModel extends ViewModel {
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    public String tenTaiKhoan="";
    public String ngaySinh="";
    public String email="";
    public String matKhau="";
    public String xacNhanMatKhau="";

    //declare MutableLiveData for error message
    public MutableLiveData<String> tenTaiKhoanError= new MutableLiveData<>();
    public MutableLiveData<String> ngaySinhError= new MutableLiveData<>();
    public MutableLiveData<String> emailError= new MutableLiveData<>();
    public MutableLiveData<String> matKhauError= new MutableLiveData<>();
    public MutableLiveData<String> xacNhanMatKhauError= new MutableLiveData<>();
    public MutableLiveData<Boolean> isSignUpSuccess= new MutableLiveData<>();

    //check all input are valid
    public boolean isValidInput() {
        boolean check=true;
        if(tenTaiKhoan.isEmpty()){
            tenTaiKhoanError.setValue("Vui lòng nhập tên tài khoản");
            check=false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailError.setValue("Email không hợp lệ");
            check=false;
        }
        if (ngaySinh.isEmpty()){
            ngaySinhError.setValue("Vui lòng nhập ngày sinh");
            check=false;
        }
        if(email.isEmpty()){
            emailError.setValue("Vui lòng nhập email");
            check=false;
        }
        if(matKhau.isEmpty()){
            matKhauError.setValue("Vui lòng nhập mật khẩu");
            check=false;
        }
        if(xacNhanMatKhau.isEmpty()){
            xacNhanMatKhauError.setValue("Vui lòng xác nhận mật khẩu");
            check=false;
        }
        if(!matKhau.equals(xacNhanMatKhau)){
            xacNhanMatKhauError.setValue("Mật khẩu không khớp");
            check=false;
        }
        return check;
    }

    //reset error message
    public void resetErrorMessage(){
        tenTaiKhoanError.setValue(null);
        ngaySinhError.setValue(null);
        emailError.setValue(null);
        matKhauError.setValue(null);
        xacNhanMatKhauError.setValue(null);
    }
}
