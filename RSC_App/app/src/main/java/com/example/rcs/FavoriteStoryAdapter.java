package com.example.rcs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;


public class FavoriteStoryAdapter extends RecyclerView.Adapter<FavoriteStoryAdapter.FavoriteStoryViewHolder> {
    private List<Story> storiesList;
    private Context context;

    public FavoriteStoryAdapter(List<Story> storiesList, Context context) {
        this.storiesList = storiesList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_story_item, parent, false);
        return new FavoriteStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteStoryViewHolder holder, int position) {
        Story story = storiesList.get(holder.getAdapterPosition());
        holder.tv_story_name.setText(story.getName());
        Glide.with(context).asBitmap().load(story.getImageUrl()).into(holder.img);


        CategoryAdapter categoryAdapter = new CategoryAdapter(story.getCategories());
        holder.categories_rv.setAdapter(categoryAdapter);
        holder.categories_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, Detail_story.class);
            intent.putExtra("storyId", story.getStoryId());
            intent.putExtra("imageUrl", story.getImageUrl());
            intent.putExtra("name", story.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    class FavoriteStoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_story_name;
        private RecyclerView categories_rv;

        public FavoriteStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_story_name = itemView.findViewById(R.id.tv_story_name);
            categories_rv = itemView.findViewById(R.id.categories_rv);
        }
    }
}

