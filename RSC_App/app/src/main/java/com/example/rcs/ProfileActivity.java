package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avartar, btnBack;
    private EditText edtName, edtEmail;
    private Button btnChangepass;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        avartar = findViewById(R.id.avatar);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        btnBack = findViewById(R.id.btnBack);
        btnChangepass = findViewById(R.id.btnChangePass);

        loadPrile();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePassActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadPrile(){

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            String displayName = currentUser.getDisplayName();
            Uri photoUrl = currentUser.getPhotoUrl();

            edtName.setText(displayName);
            edtEmail.setText(email);
            if (photoUrl != null) {
                Glide.with(avartar.getContext())
                        .load(photoUrl)
                        .into(avartar);
            } else {
                avartar.setImageResource(R.drawable.avartar_default);
            }
        }
    }
}