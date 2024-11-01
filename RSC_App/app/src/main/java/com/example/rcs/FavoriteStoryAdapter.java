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

import java.util.List;

public class FavoriteStoryAdapter extends RecyclerView.Adapter<FavoriteStoryAdapter.FavoriteViewHolder> {

    private final Context context;
    private final List<Story> favoriteStoryList;

    public FavoriteStoryAdapter(Context context, List<Story> favoriteStoryList) {
        this.context = context;
        this.favoriteStoryList = favoriteStoryList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_story_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.tv_name.setText(favoriteStoryList.get(holder.getAdapterPosition()).getName());
        holder.tv_author.setText(favoriteStoryList.get(holder.getAdapterPosition()).getAuthor());
        holder.tv_like.setText(String.valueOf(favoriteStoryList.get(holder.getAdapterPosition()).getFavorites()));
        Glide.with(context).asBitmap().load(favoriteStoryList.get(holder.getAdapterPosition()).getImageUrl()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Detail_story.class);
                intent.putExtra("storyId",favoriteStoryList.get(holder.getAdapterPosition()).getStoryId());
                intent.putExtra("author",favoriteStoryList.get(holder.getAdapterPosition()).getAuthor());
//                intent.putExtra("categories",storiesList.get(position).getCategories());
//                intent.putExtra("StoryId",storiesList.get(position).getStoryId());
//                intent.putExtra("content",storiesList.get(position).getContent());
                intent.putExtra("favorites",favoriteStoryList.get(holder.getAdapterPosition()).getFavorites());
                intent.putExtra("imageUrl",favoriteStoryList.get(holder.getAdapterPosition()).getImageUrl());
                intent.putExtra("name",favoriteStoryList.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteStoryList.size();
    }


    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_name, tv_author, tv_like;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_like = itemView.findViewById(R.id.tv_like);
        }
    }
}
