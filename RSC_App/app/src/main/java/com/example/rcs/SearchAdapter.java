package com.example.rcs;

import static androidx.core.content.ContextCompat.startActivities;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Story> storiesList;
    private Context context;
    private int viewType;

    // Các viewType cụ thể
    public static final int VIEW_TYPE_STORY = 0;
    public static final int VIEW_TYPE_AUTHOR = 1;
    public static final int VIEW_TYPE_GENRE = 2;

    public SearchAdapter(List<Story> storiesList, Context context, int viewType) {
        this.storiesList = storiesList;
        this.context = context;
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_AUTHOR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.author_item_layout, parent, false);
                return new AuthorViewHolder(view);
            case VIEW_TYPE_GENRE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item_layout, parent, false);
                return new GenreViewHolder(view);
            case VIEW_TYPE_STORY:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item_layout, parent, false);
                return new StoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Story story = storiesList.get(position);

        if (holder instanceof StoryViewHolder) {
            ((StoryViewHolder) holder).tv_name.setText(story.getName());
            Glide.with(context).load(story.getImageUrl()).into(((StoryViewHolder) holder).img);
        } else if (holder instanceof AuthorViewHolder) {
            ((AuthorViewHolder) holder).tv_name.setText(story.getName());
            ((AuthorViewHolder) holder).tv_author.setText(story.getAuthor()); // Gán tên tác giả vào TextView
            Glide.with(context).load(story.getImageUrl()).into(((AuthorViewHolder) holder).img);
        } else if (holder instanceof GenreViewHolder) {
            GenreViewHolder genreHolder = (GenreViewHolder) holder;
            genreHolder.tv_name.setText(story.getName());

            // Thiết lập CategoryAdapter cho danh sách thể loại
            List<String> categories = story.getCategories();
            CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
            genreHolder.categories_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            genreHolder.categories_rv.setAdapter(categoryAdapter);

            Glide.with(context).load(story.getImageUrl()).into(genreHolder.img);
        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    // ViewHolder cho Story
    static class StoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView img;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            img = itemView.findViewById(R.id.img);
        }
    }

    // ViewHolder cho Author
    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_author;
        private ImageView img;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_author = itemView.findViewById(R.id.tv_author);
            img = itemView.findViewById(R.id.img);
        }
    }

    // ViewHolder cho Genre
    static class GenreViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView img;
        private RecyclerView categories_rv;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            img = itemView.findViewById(R.id.img);
            categories_rv = itemView.findViewById(R.id.categories_rv);
        }
    }
}
