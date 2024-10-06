package com.example.rcs.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {
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

    //check all input are valid
    public boolean isValidInput() {
        boolean check=true;
        if(tenTaiKhoan.isEmpty()){
            tenTaiKhoanError.setValue("Vui lòng nhập tên tài khoản");
            check=false;
        }
        if (ngaySinh.isEmpty()){
            ngaySinhError.setValue("Vui lòng nhập ngày sinh");
            check=false;
        }
        if(email.isEmpty()){
            emailError.setValue("Vui lòng nhập email");
            check=false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailError.setValue("Email không hợp lệ");
            check=false;
        }
        if(matKhau.isEmpty()){
            matKhauError.setValue("Vui lòng nhập mật khẩu");
            check=false;
        }else if (matKhau.length()<6){
            matKhauError.setValue("Mật khẩu phải ít nhất 6 kí tu");
            check=false;
        }
        if(xacNhanMatKhau.isEmpty()){
            xacNhanMatKhauError.setValue("Vui lòng xác nhận mật khẩu");
            check=false;
        }else if(!matKhau.equals(xacNhanMatKhau)){
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
