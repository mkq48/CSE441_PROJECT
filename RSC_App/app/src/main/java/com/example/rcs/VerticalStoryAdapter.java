package com.example.rcs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class VerticalStoryAdapter extends RecyclerView.Adapter<VerticalStoryAdapter.VerticalStoryViewHolder> {
    private List<Story> storiesList;
    private Context context;

    public VerticalStoryAdapter(List<Story> storiesList, Context context) {
        this.storiesList = storiesList;
        this.context = context;
    }

    @NonNull
    @Override
    public VerticalStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_story_item_layout,parent,false);
        return new VerticalStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalStoryViewHolder holder, int position) {
        holder.tv_author.setText("Tác giả: "+storiesList.get(holder.getAdapterPosition()).getAuthor());
        holder.tv_story_name.setText(storiesList.get(holder.getAdapterPosition()).getName());
        Glide.with(context).asBitmap().load(storiesList.get(holder.getAdapterPosition()).getImageUrl()).into(holder.img);
        Story story = storiesList.get(holder.getAdapterPosition());
        CategoryAdapter categoryAdapter = new CategoryAdapter(story.getCategories());
        holder.categories_rv.setAdapter(categoryAdapter);
        holder.categories_rv.setLayoutManager(new GridLayoutManager(context,2));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Detail_story.class);
                intent.putExtra("storyId",storiesList.get(holder.getAdapterPosition()).getStoryId());
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

    class VerticalStoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_story_name,tv_author;
        private RecyclerView categories_rv;

        public VerticalStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_story_name = itemView.findViewById(R.id.tv_story_name);
            tv_author = itemView.findViewById(R.id.tv_author);
            categories_rv = itemView.findViewById(R.id.categories_rv);
        }
    }
}
