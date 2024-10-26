package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(this::checkUser,2000);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void checkUser() {
        if(auth.getCurrentUser() != null){
            //user is signed in
            startActivity(new Intent(this, CommentActivity.class));
        }else{
            //user is not signed in
            startActivity(new Intent(this, SignInActivity.class));
        }
        finish();
    }
}
