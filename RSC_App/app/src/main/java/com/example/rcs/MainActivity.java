package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.rcs.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadAvatar();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.searchFragment, R.id.favoriteFragment, R.id.historyFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.avatarImg.setOnClickListener(v-> startActivity(new Intent(MainActivity.this, UserActivity1.class)));
    }

    private void loadAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //load avatar
        Glide.with(this).load(user.getPhotoUrl()).
                error(R.drawable.account).
                into(binding.avatarImg);
        //Log.d("user-img-mainac",user.getPhotoUrl().toString());
    }
}