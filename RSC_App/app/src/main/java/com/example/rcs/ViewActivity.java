package com.example.rcs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;

import android.os.Bundle;

import android.util.Log;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;


import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ViewStoryAdapter storyAdapter;
    private ArrayList<Story> storyList;

    FirebaseFirestore db;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(ViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        initUI();
        initListener();

    }

    private void initUI(){

        recyclerView = findViewById(R.id.recycleviewView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        storyList = new ArrayList<Story>();

        storyAdapter = new ViewStoryAdapter(storyList, ViewActivity.this);


        recyclerView.setAdapter(storyAdapter);

    }


    private void initListener(){
        getData();
    }



    private void getData() {
        db = FirebaseFirestore.getInstance();


        db.collection("stories")
                .orderBy("views", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                String imgUrl = document.getString("imageUrl");
                                long views = document.getLong("views");
                                String author = document.getString("author");

                                storyList.add(new Story(id, name, imgUrl, views, author));
                            }
                        }
                        storyAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                    progressDialog.dismiss();
                });

    }




}

