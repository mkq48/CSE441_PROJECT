package com.example.rcs.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rcs.R;
import com.example.rcs.adapter.SearchAdapter;
import com.example.rcs.databinding.FragmentSearchBinding;
import com.example.rcs.model.Story;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private TextView tv_genre, tv_author, tv_story, tv_result;
    private RecyclerView rv_result;
    private Button btn_search;
    private ArrayList<Story> resultList;
    private SearchAdapter searchAdapter;
    private LinearLayout tab_layout;
    private @NonNull FragmentSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        searchView = binding.searchView;
        tv_genre = binding.tvGenre;
        tv_author = binding.tvAuthor;
        tv_story = binding.tvStory;
        tv_result = binding.tvResult;
        rv_result = binding.rvRessult;
        btn_search = binding.btnSearch;
        tab_layout = binding.tabLayout;

        resultList = new ArrayList<>();
        searchAdapter = new SearchAdapter(resultList, getContext(), SearchAdapter.VIEW_TYPE_STORY);
        rv_result.setAdapter(searchAdapter);
        rv_result.setLayoutManager(new GridLayoutManager(getContext(), 2));

        tv_story.setOnClickListener(view -> {
            setActiveTab(tv_story);
            searchAdapter = new SearchAdapter(resultList, getContext(), SearchAdapter.VIEW_TYPE_STORY);
            rv_result.setAdapter(searchAdapter);
            searchByStoryName();
        });

        tv_author.setOnClickListener(view -> {
            setActiveTab(tv_author);
            searchAdapter = new SearchAdapter(resultList, getContext(), SearchAdapter.VIEW_TYPE_AUTHOR);
            rv_result.setAdapter(searchAdapter);
            searchByAuthorName();
        });

        tv_genre.setOnClickListener(view -> {
            setActiveTab(tv_genre);
            searchAdapter = new SearchAdapter(resultList, getContext(), SearchAdapter.VIEW_TYPE_GENRE);
            rv_result.setAdapter(searchAdapter);
            searchByGenre();
        });

        tab_layout.setVisibility(View.GONE);

        btn_search.setOnClickListener(view -> {
            setActiveTab(tv_story);
            searchByStoryName();
            tab_layout.setVisibility(View.VISIBLE);
        });
    }

    private void setActiveTab(TextView activeTab) {
        tv_story.setTextColor(Color.BLACK);
        tv_author.setTextColor(Color.BLACK);
        tv_genre.setTextColor(Color.BLACK);
        activeTab.setTextColor(Color.BLUE);
    }

    public void searchByStoryName() {
        tv_result.setVisibility(View.GONE);
        resultList.clear();
        String keyWord = searchView.getQuery().toString().trim();

        FirebaseFirestore.getInstance()
                .collection("stories")
                .whereArrayContainsAny("name_key", Arrays.asList(keyWord.split(" ")))
                .get()
                .addOnCompleteListener(task -> handleSearchResult(task, SearchAdapter.VIEW_TYPE_STORY));
    }

    public void searchByGenre() {
        tv_result.setVisibility(View.GONE);
        resultList.clear();
        String keyWord = searchView.getQuery().toString().trim();

        FirebaseFirestore.getInstance()
                .collection("stories")
                .whereArrayContains("categories", keyWord)
                .get()
                .addOnCompleteListener(task -> handleSearchResult(task, SearchAdapter.VIEW_TYPE_GENRE));
    }

    public void searchByAuthorName() {
        tv_result.setVisibility(View.GONE);
        resultList.clear();
        String keyWord = searchView.getQuery().toString().trim();

        FirebaseFirestore.getInstance()
                .collection("stories")
                .whereArrayContainsAny("author_key", Arrays.asList(keyWord.split(" ")))
                .get()
                .addOnCompleteListener(task -> handleSearchResult(task, SearchAdapter.VIEW_TYPE_AUTHOR));
    }

    private void handleSearchResult(Task<QuerySnapshot> task, int viewType) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                String img = (String) document.get("imageUrl");
                String name = (String) document.get("name");
                String author = (String) document.get("author");
                List<String> categories = (List<String>) document.get("categories");
                Story story = new Story(document.getId(), author, categories, name, img);

                resultList.add(story);
            }
            searchAdapter.notifyDataSetChanged();
            if (searchAdapter.getItemCount() == 0) {
                tv_result.setVisibility(View.VISIBLE);
            }
        }
    }
}
