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
        }
        // Thêm xử lý cho các view type khác nếu cần

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

    // ViewHolder cho Author (Tạo các ViewHolder khác cho các loại khác)
    static class AuthorViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các view tương ứng
        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            // Tìm các view trong layout author_item_layout.xml
        }
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            // Tìm các view trong layout genre_item_layout.xml
        }
    }
}


