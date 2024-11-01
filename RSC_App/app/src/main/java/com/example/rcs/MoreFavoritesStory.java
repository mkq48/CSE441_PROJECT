package com.example.rcs;

import android.os.Bundle;

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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoreFavoritesStory extends AppCompatActivity {
    private RecyclerView rv;
    private ArrayList<Story> storyList;
    private FavoriteStoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_more_favorites_story);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        loadData();

    }
    public void initViews(){
        rv = findViewById(R.id.rv);
        storyList = new ArrayList<>();
        adapter = new FavoriteStoryAdapter(MoreFavoritesStory.this, storyList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
    public void loadData(){
//        FirebaseFirestore.getInstance().collection("stories")
//                .orderBy("favorites", Query.Direction.DESCENDING)
//                .limit(3)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (DocumentSnapshot document : task.getResult()) {
//                            if (document.exists()) {
//                                String id = document.getId();
//                                String name = document.getString("name");
//                                String imgUrl = document.getString("imageUrl");
//                                String author = document.getString("author");
//                                long favorites = document.getLong("favorites");
//
//
//                                storyList.add(new Story(id, name, imgUrl, author, favorites));
//
//                            }
//
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });


        FirebaseFirestore.getInstance().collection("stories").orderBy("favorites", Query.Direction.DESCENDING).limit(2)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            storyList.clear();
                            adapter.notifyDataSetChanged();
                            for (QueryDocumentSnapshot document : value){
                                String id = document.getId();
                                String name = document.getString("name");
                                String imgUrl = document.getString("imageUrl");
                                String author = document.getString("author");
                                long favorites = document.getLong("favorites");

                                storyList.add(new Story(id, name, imgUrl, author, favorites));

                            }
                            adapter.notifyItemInserted(storyList.size() - 1);
                        }


                    }
                });
    }
}
