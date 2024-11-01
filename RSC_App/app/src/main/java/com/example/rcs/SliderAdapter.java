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

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private final Context context;
    private final List<Story> sliderList;

    public SliderAdapter(Context context, List<Story> imageUrls) {
        this.context = context;
        this.sliderList = imageUrls;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(sliderList.get(holder.getAdapterPosition()).getImageUrl()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Detail_story.class);
                intent.putExtra("storyId",sliderList.get(holder.getAdapterPosition()).getStoryId());
//                intent.putExtra("author",storiesList.get(position).getAuthor());
//                intent.putExtra("categories",storiesList.get(position).getCategories());
//                intent.putExtra("StoryId",storiesList.get(position).getStoryId());
//                intent.putExtra("content",storiesList.get(position).getContent());
//                intent.putExtra("favorites",storiesList.get(position).getFavorites());
                intent.putExtra("imageUrl",sliderList.get(holder.getAdapterPosition()).getImageUrl());
                intent.putExtra("name",sliderList.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_title;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewSlider);
        }
    }
}
