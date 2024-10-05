package com.example.rcs.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class SignInViewModel extends ViewModel {
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    public String email="";
    public String password="";

    public MutableLiveData<String> emailError= new MutableLiveData<>();
    public MutableLiveData<String> passwordError= new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoginSuccess= new MutableLiveData<>();

    public void dangNhap(){
        if(isValidEmail(email)){
            //sign in with firebase
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener((Executor) this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    isLoginSuccess.setValue(true);
                } else {
                    // If sign in fails, display a message to the user.
                    isLoginSuccess.setValue(false);
                }
            });
        }
    }
    public boolean isValidEmail(CharSequence target) {
        Boolean check=true;
        if (TextUtils.isEmpty(target)) {
            emailError.setValue("Vui lòng nhập email");
            check = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
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
