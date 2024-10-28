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

public class AllStoryActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private ArrayList<Story> stories;
    private StoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_story);

        // Áp dụng Insets cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo RecyclerView và adapter
        recyclerview = findViewById(R.id.recyclerview);
        stories = new ArrayList<>();
        adapter = new StoryAdapter(stories, this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Firebase
        getData();

        // Test chuyển sang FavoriteListActivity
        Button btn_test = findViewById(R.id.btn_test);
        btn_test.setOnClickListener(view -> {
            Intent i = new Intent(AllStoryActivity.this, FavoriteListActivity.class);
            startActivity(i);
        });

        // Thêm sự kiện cho btn_search để chuyển sang SearchActivity
        Button btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(view -> {
            Intent intent = new Intent(AllStoryActivity.this, Search.class);
            startActivity(intent);
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
                            String author = (String) document.get("author");
                            int favorites = snapshot.child("favorites").getValue(Integer.class);

                            // Thêm truyện vào danh sách
                            stories.add(new Story(favorites, imgUrl, id, author, name));

                            // Sắp xếp theo lượt thích giảm dần và giới hạn 50 truyện
                            sortAndLimitStories();

                            // Cập nhật adapter
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

                    // Sắp xếp lại và giới hạn số lượng truyện
                    sortAndLimitStories();

                    // Cập nhật adapter
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getKey();
                int index = findStoryIndexById(id);

                if (index != -1) {
                    stories.remove(index);

                    // Cập nhật lại adapter sau khi xóa
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Không cần xử lý trong trường hợp này
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void sortAndLimitStories() {
        // Sắp xếp danh sách theo lượt yêu thích giảm dần
        stories.sort((s1, s2) -> Long.compare(s2.getFavorites(), s1.getFavorites()));

        // Giới hạn danh sách tối đa 50 truyện
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
