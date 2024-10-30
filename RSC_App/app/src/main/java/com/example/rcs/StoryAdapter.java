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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private ArrayList<Story> storiesList;
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
        Story story = storiesList.get(position);
        holder.tv_name.setText(story.getName());
        //holder.tv_author.setText(storiesList.get(holder.getAdapterPosition()).getAuthor());
        //holder.tv_like_count.setText(storiesList.get(holder.getAdapterPosition()).getFavorites()+"");
        Glide.with(context).load(storiesList.get(holder.getAdapterPosition()).getImageUrl()).into(holder.img);

    }


    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name, tv_author, tv_like_count;
        private ImageView img, img_heart;
//        private CardView cardview;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            img = itemView.findViewById(R.id.img);
//            tv_author=itemView.findViewById(R.id.tv_author);
//            tv_like_count=itemView.findViewById(R.id.tv_like_count);
        }
    }
}

