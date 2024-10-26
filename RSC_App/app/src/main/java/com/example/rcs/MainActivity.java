package com.example.rcs;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private List<String> chapterList, categoryList;
//    private RecyclerView chapter_rv, categories_rv;
//    private ChapterAdapter chapterAdapter;
//    private CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(MainActivity.this,AllStoryActivity.class);
        startActivity(i);
//        categories_rv = findViewById(R.id.categories_rv);
//        categoryList = new ArrayList<>();
//        categoryList.add("Huyn hu");
//        categoryList.add("Tu tien");
//        categoryAdapter = new CategoryAdapter(categoryList);
//        categories_rv.setAdapter(categoryAdapter);
//        categories_rv.setLayoutManager(new GridLayoutManager(this,2));
    }
}