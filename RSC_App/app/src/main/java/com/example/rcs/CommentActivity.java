package com.example.rcs;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rcs.databinding.ActivityCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommentActivity extends AppCompatActivity {

    private ActivityCommentBinding binding;
    private FirebaseDatabase database;

    private String storyID = "";
    private String chapID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        getID();

        //reference to comments
//        storyID = "story1";
//        chapterID = "chap1";
        DatabaseReference comments = database.getReference("comments").
                child(storyID).child(chapID);

        //addValueEventListener
        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //handle comments with loop through snapshot and print to logcat
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("Comments", ds.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }

    //get id from intent
    private void getID(){
        storyID = getIntent().getStringExtra("storyID");
        chapID = "chap"+ getIntent().getIntExtra("chapID", 1);
        Log.d("StoryID", storyID);
        Log.d("ChapterID", chapID);
    }
}