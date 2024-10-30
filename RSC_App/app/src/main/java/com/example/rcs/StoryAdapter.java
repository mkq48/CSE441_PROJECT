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

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private List<Story> storiesList;
    private Context context;

    public StoryAdapter(ArrayList<Story> storiesList, Context context) {
        this.storiesList = storiesList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item,parent,false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.tv_name.setText(storiesList.get(holder.getAdapterPosition()).getName());
//        holder.tv_author.setText(storiesList.get(holder.getAdapterPosition()).getAuthor());
        Story story = storiesList.get(position);
        holder.tv_like_count.setText(storiesList.get(holder.getAdapterPosition()).getFavorites()+"");
        Glide.with(context).asBitmap().load(storiesList.get(holder.getAdapterPosition()).getImageUrl()).into(holder.img);

//        if (story.getCategories() != null && !story.getCategories().isEmpty()) {
//            CategoryAdapter categoryAdapter = new CategoryAdapter(story.getCategories());
//            holder.categories_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            holder.categories_rv.setAdapter(categoryAdapter);
//        }

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

    static class StoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name, tv_author, tv_like_count;
        private ImageView img, img_heart;
        private RecyclerView categories_rv;

        //        private CardView cardview;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            img=itemView.findViewById(R.id.img);
//            tv_author=itemView.findViewById(R.id.tv_author);
//            categories_rv = itemView.findViewById(R.id.categories_rv);
            tv_like_count=itemView.findViewById(R.id.tv_like_count);
        }
    }
}

