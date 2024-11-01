//package com.example.rcs;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.SearchView;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class FavoriteListActivity extends AppCompatActivity {
//    private SearchView searchView;
//    private TextView tv_genre, tv_author, tv_story, tv_result;
//    private RecyclerView rv_result;
//    private Button btn_search;
//    private ArrayList<Story> resultList;
//    private SearchAdapter searchAdapter;
//    private LinearLayout tab_layout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_favorite_list);
//
//        // Set up layout insets for edge-to-edge UI
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // Initialize views
//        initViews();
//    }
//
//    private void initViews() {
//        searchView = findViewById(R.id.search_view);
//        tv_genre = findViewById(R.id.tv_genre);
//        tv_author = findViewById(R.id.tv_author);
//        tv_story = findViewById(R.id.tv_story);
//        tv_result = findViewById(R.id.tv_result);
//        rv_result = findViewById(R.id.rv_result);
//        btn_search = findViewById(R.id.btn_search);
//        tab_layout = findViewById(R.id.tab_layout);
//
//        resultList = new ArrayList<>();
//        // Set default view type to STORY
//        searchAdapter = new SearchAdapter(resultList, this, SearchAdapter.VIEW_TYPE_STORY);
//        rv_result.setAdapter(searchAdapter);
//        rv_result.setLayoutManager(new GridLayoutManager(this, 2));
//
//        tv_story.setOnClickListener(view -> {
//            setActiveTab(tv_story);
//            searchAdapter = new SearchAdapter(resultList, this, SearchAdapter.VIEW_TYPE_STORY);
//            rv_result.setAdapter(searchAdapter);
//            searchByStoryName();
//        });
//
//        tv_author.setOnClickListener(view -> {
//            setActiveTab(tv_author);
//            searchAdapter = new SearchAdapter(resultList, this, SearchAdapter.VIEW_TYPE_AUTHOR);
//            rv_result.setAdapter(searchAdapter);
//            searchByAuthorName();
//        });
//
//        tv_genre.setOnClickListener(view -> {
//            setActiveTab(tv_genre);
//            searchAdapter = new SearchAdapter(resultList, this, SearchAdapter.VIEW_TYPE_GENRE);
//            rv_result.setAdapter(searchAdapter);
//            searchByGenre();
//        });
//
//        tab_layout.setVisibility(View.GONE);
//
//        // Set up the search button
//        btn_search.setOnClickListener(view -> {
//            setActiveTab(tv_story);
//            searchByStoryName();
//            tab_layout.setVisibility(View.VISIBLE);
//        });
//    }
//
//    private void setActiveTab(TextView activeTab) {
//        tv_story.setTextColor(Color.BLACK);
//        tv_author.setTextColor(Color.BLACK);
//        tv_genre.setTextColor(Color.BLACK);
//        activeTab.setTextColor(Color.BLUE); // Highlight the active tab
//    }
//
//    public void searchByStoryName() {
//        tv_result.setVisibility(View.GONE);
//        resultList.clear();
//        String keyWord = searchView.getQuery().toString().trim();
//
//        FirebaseFirestore.getInstance()
//                .collection("stories")
//                .whereArrayContainsAny("name_key", Arrays.asList(keyWord.split(" ")))
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String img = (String) document.get("imageUrl");
//                                String name = (String) document.get("name");
//                                Story story = new Story(document.getId(), name, img);
//
//                                resultList.add(story);
//                            }
//                            searchAdapter.notifyDataSetChanged();
//                            if (searchAdapter.getItemCount() == 0) {
//                                tv_result.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                });
//    }
//
//    public void searchByGenre() {
//        tv_result.setVisibility(View.GONE);
//        resultList.clear();
//        String keyWord = searchView.getQuery().toString().trim();
//
//        FirebaseFirestore.getInstance()
//                .collection("stories")
//                .whereArrayContains("categories", keyWord)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String img = (String) document.get("imageUrl");
//                                String name = (String) document.get("name");
//                                Story story = new Story(document.getId(), name, img);
//
//                                resultList.add(story);
//                            }
//                            searchAdapter.notifyDataSetChanged();
//                            if (searchAdapter.getItemCount() == 0) {
//                                tv_result.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                });
//    }
//
//    public void searchByAuthorName() {
//        tv_result.setVisibility(View.GONE);
//        resultList.clear();
//        String keyWord = searchView.getQuery().toString().trim();
//
//        FirebaseFirestore.getInstance()
//                .collection("stories")
//                .whereArrayContainsAny("author_key", Arrays.asList(keyWord.split(" ")))
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                String img = (String) document.get("imageUrl");
//                                String name = (String) document.get("name");
//                                Story story = new Story(document.getId(), name, img);
//
//                                resultList.add(story);
//                            }
//                            searchAdapter.notifyDataSetChanged();
//                            if (searchAdapter.getItemCount() == 0) {
//                                tv_result.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                });
//    }
//}



package com.example.rcs.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rcs.adapter.FavoriteStoryAdapter;
import com.example.rcs.databinding.FragmentFavoriteListBinding;
import com.example.rcs.model.Story;
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

public class FavoriteListFragment extends Fragment {
    private FavoriteStoryAdapter adapter;
    private ArrayList<Story> favoriteList;
    private RecyclerView rv;

    private FragmentFavoriteListBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteListBinding.inflate(getLayoutInflater(),container,false);
        rv = binding.rv;
        favoriteList = new ArrayList<>();
        getData();
        adapter = new FavoriteStoryAdapter(favoriteList, getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    public void getData() {
        
        FirebaseDatabase.getInstance().getReference("user_favorites/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                FirebaseFirestore.getInstance().document("stories/" + id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String author = (String)document.get("author");
                            String name = (String)document.get("name");
                            String imgUrl = (String)document.get("imageUrl");
                            List<String> categories = ( List<String>)document.get("categories");
                            favoriteList.add(new Story(id,author,categories,name,imgUrl));
                            adapter.notifyItemInserted(favoriteList.size()-1);
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index = findIndexOfItem(snapshot.getKey());
                if(index!=-1){
                    favoriteList.remove(index);
                    adapter.notifyItemRemoved(index);
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

    public int findIndexOfItem(String id){
        for (int i = 0; i < favoriteList.size(); i++) {
            if(favoriteList.get(i).getStoryId().equals(id)){
                return i;
            }
        }
        return -1;
    }
}
