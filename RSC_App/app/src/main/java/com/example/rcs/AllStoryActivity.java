package com.example.rcs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllStoryActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private ArrayList<Story> stories;
    private StoryAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_story);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Hiển thị 2 item mỗi dòng
        adapter = new StoryAdapter(stories, this);
        recyclerView.setAdapter(adapter);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerview = findViewById(R.id.recyclerview);
        stories = new ArrayList<>();
        adapter = new StoryAdapter(stories, this);
        recyclerview.setAdapter(adapter);
//        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Firebase
        getData();

        // Button chuyển sang FavoriteListActivity
        Button btn_test = findViewById(R.id.btn_test);
//        btn_test.setOnClickListener(view -> {
//            Intent intent = new Intent(AllStoryActivity.this, FavoriteListActivity.class);
//            startActivity(intent);
//        });


        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllStoryActivity.this, FavoriteListActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getData() {
        FirebaseDatabase.getInstance().getReference("stories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                FirebaseFirestore.getInstance().document("stories/" + id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String name = (String) document.get("name");
                            String imgUrl = (String) document.get("imageUrl");
//                            String author = (String) document.get("author");
//                            String category = (String) document.get("category");
                            int favorites = snapshot.child("favorites").getValue(Integer.class);

//                            List<String> categoryList = Collections.singletonList((String) document.get("category"));

//                            stories.add(new Story(favorites, imgUrl, id, author, name, categoryList));
                            stories.add(new Story(favorites, imgUrl, id, name));

                            sortAndLimitStories();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                int favorites = snapshot.child("favorites").getValue(Integer.class);
                int index = findStoryIndexById(id);

                if (index != -1) {
                    stories.get(index).setFavorites(favorites);

                    sortAndLimitStories();

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getKey();
                int index = findStoryIndexById(id);

                if (index != -1) {
                    stories.remove(index);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sortAndLimitStories() {
        stories.sort((s1, s2) -> Long.compare(s2.getFavorites(), s1.getFavorites()));

        if (stories.size() > 50) {
            stories = new ArrayList<>(stories.subList(0, 50));
        }
    }

    public int findStoryIndexById(String id) {
        for (int i = 0; i < stories.size(); i++) {
            if (stories.get(i).getStoryId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
