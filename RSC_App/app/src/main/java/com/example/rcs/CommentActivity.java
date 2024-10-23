package com.example.rcs;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rcs.databinding.ActivityCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    private ActivityCommentBinding binding;
    ArrayList<Comment> commentList;
    private String storyID = "";
    private String chapID = "";
    private CommentAdapter adapter;
    protected static DatabaseReference commentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        getID();
        commentsRef = database.getReference("comments").
                child(storyID).child(chapID);

        //
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        binding.commentSectionRe.setLayoutManager(new LinearLayoutManager(this));
        binding.commentSectionRe.setAdapter(adapter);

        //retrieval firebase data
        getComments();

        binding.pushBtn.setOnClickListener(v->{
            if (binding.commentEdt.getText().toString().isEmpty()) Toast.makeText(this, getString(R.string.empty), Toast.LENGTH_SHORT).show();
            else{
                Comment comment = new Comment("dat",binding.commentEdt.getText().toString(),0);
                commentsRef.push().setValue(comment, (error, ref) -> {
                    if (error!=null){
                        Log.d("err", "loi gi do");
                    }else Toast.makeText(CommentActivity.this, getString(R.string.ok), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void getComments() {
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    Objects.requireNonNull(comment).setId(ds.getKey());
                    commentList.add(comment);
                }
                for(Comment c:commentList) Log.d("c",c.toString());
                adapter.updateData(commentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }

    private void getID(){
        storyID = getIntent().getStringExtra("storyID");
        chapID = "chap"+ getIntent().getIntExtra("chapID", 1);
    }
}