package com.example.rcs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;


import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private HomeStoryAdapter storyAdapter1, storyAdapter2;
    private SliderAdapter sliderAdapter;
    private FavoriteStoryAdapter favoriteStoryAdapter;
    private ArrayList<Story> storyList1, storyList2, storyList3;
    private ArrayList<Story> sliderList;
    private ViewPager2 viewPager2;
    private Handler sliderHandler;
    private Runnable sliderRunnable;
    private ImageView btnUser;
    private TextView btnMoreFavorites, btnMoreViews, btnMoreNew;

    FirebaseFirestore db;
    ProgressDialog progressDialog;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        initUI();
        initListener();

    }

    private void initUI(){
        btnMoreNew = findViewById(R.id.btnMoreNew);
        btnMoreFavorites = findViewById(R.id.btnMoreFavorites);
        btnMoreViews = findViewById(R.id.btnMoreViews);
        btnUser = findViewById(R.id.btnUser);
        recyclerView1 = findViewById(R.id.recycleviewNewStory);
        recyclerView2 = findViewById(R.id.recycleviewRcmt);
        recyclerView3 = findViewById(R.id.recycleviewTop);
        viewPager2 = findViewById(R.id.viewPager);
        sliderHandler = new Handler(Looper.getMainLooper());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);

        storyList1 = new ArrayList<Story>();
        storyList2 = new ArrayList<Story>();
        storyList3 = new ArrayList<Story>();
        sliderList = new ArrayList<Story>();

        storyAdapter1 = new HomeStoryAdapter(storyList1, HomeActivity.this);
        storyAdapter2 = new HomeStoryAdapter(storyList2, HomeActivity.this);
        favoriteStoryAdapter = new FavoriteStoryAdapter(HomeActivity.this, storyList3);

        recyclerView1.setAdapter(storyAdapter1);
        recyclerView2.setAdapter(storyAdapter2);
        recyclerView3.setAdapter(favoriteStoryAdapter);

    }


    private void initListener(){
        getData();

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        btnMoreNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewStoryActivity.class);
                startActivity(intent);
            }
        });

        btnMoreFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MoreFavoritesStory.class);
                startActivity(intent);
            }
        });

        btnMoreViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ViewActivity.class);
                startActivity(intent);
            }
        });
    }



    private void getData() {
        db = FirebaseFirestore.getInstance();

        db.collection("stories")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(6)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                String imgUrl = document.getString("imageUrl");

                                storyList1.add(new Story(id, name, imgUrl));


                                sliderList.add(new Story(id, name, imgUrl));
                                sliderAdapter = new SliderAdapter(HomeActivity.this, sliderList);
                                viewPager2.setAdapter(sliderAdapter);
                            }
                        }
                        storyAdapter1.notifyDataSetChanged();
                        setAutoSlide();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                    progressDialog.dismiss();
                });

        db.collection("stories")
                .orderBy("views", Query.Direction.DESCENDING)
                .limit(6)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                String imgUrl = document.getString("imageUrl");

                                storyList2.add(new Story(id, name, imgUrl));
                            }
                        }
                        storyAdapter2.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                    progressDialog.dismiss();
                });

        db.collection("stories")
                .orderBy("favorites", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                String imgUrl = document.getString("imageUrl");
                                String author = document.getString("author");
                                long favorites = document.getLong("favorites");


                                storyList3.add(new Story(id, name, imgUrl, author, favorites));

                            }

                        }
                        favoriteStoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                    progressDialog.dismiss();
                });
    }




    private void setAutoSlide() {
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager2.getCurrentItem() + 1) % sliderList.size();
                viewPager2.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 4000);
            }
        };
        startAutoSlide();
    }

    private void startAutoSlide() {
        sliderHandler.postDelayed(sliderRunnable, 4000);
    }

    private void stopAutoSlide() {
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoSlide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoSlide();
    }


}

