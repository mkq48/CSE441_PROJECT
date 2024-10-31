package com.example.rcs.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rcs.adapter.FavoriteStoryAdapter;
import com.example.rcs.model.Story;
import com.example.rcs.databinding.FragmentHistoryStoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private FavoriteStoryAdapter adapter;
    private ArrayList<Story> historyList;
    private RecyclerView rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHistoryStoryBinding binding = FragmentHistoryStoryBinding.inflate(inflater, container, false);
        historyList = new ArrayList<>();
        getData();
        adapter = new FavoriteStoryAdapter(historyList, getContext());
        rv = binding.rv;
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference("history/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                FirebaseFirestore.getInstance().document("stories/" + id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String author = (String)document.get("author");
                                String name = (String)document.get("name");
                                String imgUrl = (String)document.get("imageUrl");
                                List<String> categories = ( List<String>)document.get("categories");
                                historyList.add(new Story(id,author,categories,name,imgUrl));
                                adapter.notifyItemInserted(historyList.size()-1);
                            }
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}