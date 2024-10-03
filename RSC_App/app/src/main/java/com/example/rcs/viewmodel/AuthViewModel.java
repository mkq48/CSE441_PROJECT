package com.example.rcs.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModel extends ViewModel {
    private final FirebaseAuth auth;

    public AuthViewModel(FirebaseAuth auth) {
        this.auth = auth;
    }
}
