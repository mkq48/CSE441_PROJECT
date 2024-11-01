package com.example.rcs.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rcs.R;
import com.example.rcs.adapter.CommentAdapter;
import com.example.rcs.databinding.FragmentCommentBinding;
import com.example.rcs.model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;
    ArrayList<Comment> commentList;
    private String storyID = "";
    private String chapID = "";
    private CommentAdapter adapter;
    public static DatabaseReference commentsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentBinding.inflate(inflater, container, false);
        getID();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        commentsRef = database.getReference("comments").
                child(storyID).child(chapID);

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        binding.commentSectionRe.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.commentSectionRe.setAdapter(adapter);

        //retrieval firebase data
        getComments(false);

        binding.pushBtn.setOnClickListener(v->{
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (binding.commentEdt.getText().toString().isEmpty()) Toast.makeText(getContext(), getString(R.string.empty), Toast.LENGTH_SHORT).show();
            else{
                Map<String, Boolean> likes = new HashMap<>();
                likes.put(userID, false);
                Comment comment = new Comment(userID,binding.commentEdt.getText().toString(),0,likes,System.currentTimeMillis());
                commentsRef.push().setValue(comment, (error, ref) -> {
                    if (error!=null){
                        Log.d("err", "loi gi do");
                    }else Toast.makeText(getContext(), getString(R.string.ok), Toast.LENGTH_SHORT).show();
                });
            }
        });
        //Toast.makeText(getContext(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        binding.spinner.setAdapter(adapter);
        //register listener for spinner
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((parent.getItemAtPosition(position).toString().equals("Moi xep truoc"))){
                    getComments(true);
                }else{
                    getComments(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }
    private void getComments(Boolean moiXepTrc) {
        commentsRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    Objects.requireNonNull(comment).setId(ds.getKey());
                    commentList.add(comment);
                }
                for(Comment c:commentList) Log.d("c",c.toString());
                if (moiXepTrc) Collections.reverse(commentList);
                adapter.updateData(commentList);
                binding.numberOfCommentTv.setText(commentList.size()+" "+getContext().getString(R.string.binh_luan));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });
    }
    private void getID(){
        chapID = "chap"+ getArguments().getInt("chapId",0);
        storyID = getArguments().getString("storyId");
        Log.d("chapID",chapID);
        Log.d("storyID",storyID);
    }
}