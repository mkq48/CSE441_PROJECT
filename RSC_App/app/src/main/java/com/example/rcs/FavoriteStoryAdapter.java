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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_story_item,parent,false);
        return new FavoriteStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteStoryViewHolder holder, int position) {
        holder.tv_story_name.setText(storiesList.get(holder.getAdapterPosition()).getName());
        Glide.with(context).asBitmap().load(storiesList.get(holder.getAdapterPosition()).getImageUrl()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Detail_story.class);
                intent.putExtra("storyId",storiesList.get(holder.getAdapterPosition()).getStoryId());
//                intent.putExtra("author",storiesList.get(position).getAuthor());
//                intent.putExtra("categories",storiesList.get(position).getCategories());
//                intent.putExtra("StoryId",storiesList.get(position).getStoryId());
//                intent.putExtra("content",storiesList.get(position).getContent());
//                intent.putExtra("favorites",storiesList.get(position).getFavorites());
                intent.putExtra("imageUrl",storiesList.get(holder.getAdapterPosition()).getImageUrl());
                intent.putExtra("name",storiesList.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    class FavoriteStoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_story_name;

        public FavoriteStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_story_name = itemView.findViewById(R.id.tv_story_name);
        }
    }
}
