package com.example.rcs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity {
    private SearchView searchView;
    private TextView tv_genre, tv_author, tv_story, tv_result;
    private RecyclerView rv_result;
    private Button btn_search;
    private ArrayList<Story> resultList;
    private SearchAdapter searchAdapter;
    private LinearLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_list);

        // Set up layout insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initViews();
    }

    private void initViews() {
        searchView = findViewById(R.id.search_view);
        tv_genre = findViewById(R.id.tv_genre);
        tv_author = findViewById(R.id.tv_author);
        tv_story = findViewById(R.id.tv_story);
        tv_result = findViewById(R.id.tv_result);
        rv_result = findViewById(R.id.rv_result);
        btn_search = findViewById(R.id.btn_search);
        tab_layout = findViewById(R.id.tab_layout);

        tv_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(tv_story);
                searchByStoryName();
            }
        });
        tv_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(tv_author);
                searchByAuthorName();
            }
        });
        tv_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(tv_genre);
                searchByGenre();
            }
        });
        tab_layout.setVisibility(View.GONE);
        // hien thi danh sach truyen tim thay len recyclerView
        resultList = new ArrayList<>();
        searchAdapter = new SearchAdapter(resultList,this);
        rv_result.setAdapter(searchAdapter);
        rv_result.setLayoutManager(new GridLayoutManager(this,2));

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveTab(tv_story);
                searchByStoryName();
                tab_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setActiveTab(TextView activeTab) {
        tv_story.setTextColor(Color.BLACK);
        tv_author.setTextColor(Color.BLACK);
        tv_genre.setTextColor(Color.BLACK);
        activeTab.setTextColor(Color.RED); // Highlight the active tab
    }

    public void searchByStoryName(){
        tv_result.setVisibility(View.GONE);
        resultList.clear();
        // lấy chuỗi keyWord của searchview
        String keyWord = searchView.getQuery().toString().trim();
        FirebaseFirestore.getInstance().collection("stories").whereArrayContainsAny("name_key", Arrays.asList(keyWord.split(" "))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String img = (String) document.get("imageUrl");
                        String author = (String) document.get("author");
                        List<String> categories = (List<String>) document.get("categories");
                        String name = (String) document.get("name");
                        Story story = new Story(document.getId(),author,categories,name,img);
                        resultList.add(story);
                        searchAdapter.notifyItemInserted(resultList.size()-1);
                    }
                    if(searchAdapter.getItemCount()==0){
                        // hien thi tv khong tim thay
                        tv_result.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void searchByGenre(){
        tv_result.setVisibility(View.GONE);
        resultList.clear();
        // lấy chuỗi keyWord của searchview
        String keyWord = searchView.getQuery().toString().trim();
        FirebaseFirestore.getInstance().collection("stories").whereArrayContains("categories",keyWord).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String img = (String) document.get("imageUrl");
                        String author = (String) document.get("author");
                        List<String> categories = (List<String>) document.get("categories");
                        String name = (String) document.get("name");
                        Story story = new Story(document.getId(),author,categories,name,img);
                        resultList.add(story);
                        searchAdapter.notifyItemInserted(resultList.size()-1);
                    }
                    if(searchAdapter.getItemCount()==0){
                        // hien thi tv khong tim thay
                        tv_result.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void searchByAuthorName(){
        tv_result.setVisibility(View.GONE);
        resultList.clear();
        // lấy chuỗi keyWord của searchview
        String keyWord = searchView.getQuery().toString().trim();
        FirebaseFirestore.getInstance().collection("stories").whereArrayContainsAny("author_key", Arrays.asList(keyWord.split(" "))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String img = (String) document.get("imageUrl");
                        String author = (String) document.get("author");
                        List<String> categories = (List<String>) document.get("categories");
                        String name = (String) document.get("name");
                        Story story = new Story(document.getId(),author,categories,name,img);
                        resultList.add(story);
                        searchAdapter.notifyItemInserted(resultList.size()-1);
                    }
                    if(searchAdapter.getItemCount()==0){
                        // hien thi tv khong tim thay
                        tv_result.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
