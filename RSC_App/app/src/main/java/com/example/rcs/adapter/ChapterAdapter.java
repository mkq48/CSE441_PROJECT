package com.example.rcs.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rcs.R;
import com.example.rcs.fragment.FragmentChaperView;
import com.example.rcs.fragment.FragmentDetailStory;

import java.util.Comparator;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHoder> {

    private List<Integer> chapterList;
    private int itemCountDefault;
    private String storyId;
    private FragmentDetailStory context;

    public ChapterAdapter(List<Integer> chapterList, String storyId, FragmentDetailStory context) {
        this.chapterList = chapterList;
        this.storyId = storyId;
        this.context = context;
        itemCountDefault=5;
    }
    public void sortDesc(){
        chapterList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                notifyDataSetChanged();
                return t1.compareTo(integer);
            }
        });
    }
    public void sortIncrease(){
        chapterList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                notifyDataSetChanged();
                return integer.compareTo(t1);
            }
        });
    }
    @NonNull
    @Override
    public ChapterViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new ChapterViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHoder holder, int position) {
        if(position<itemCountDefault){
            holder.tv_chap.setText(chapterList.get(holder.getAdapterPosition())+ "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("chapId", chapterList.get(holder.getAdapterPosition()));
                    bundle.putInt("currentPage", 1);
                    bundle.putString("storyId", storyId);

                    NavController navController = NavHostFragment.findNavController(context);
                    navController.navigate(R.id.action_fragmentDetailStory_to_fragmentChaperView, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
         return Math.min(itemCountDefault, chapterList.size());
    }
    public void expand() {
        itemCountDefault = chapterList.size(); // Hiển thị tất cả item
        notifyDataSetChanged();
    }
    public void collapse() {
        itemCountDefault = 5; // Giới hạn về 5 item
        notifyDataSetChanged();
    }
    public class ChapterViewHoder extends RecyclerView.ViewHolder {
        private TextView tv_chap;

        public ChapterViewHoder(@NonNull View itemView) {
            super(itemView);
            tv_chap = itemView.findViewById(R.id.tv_chap);
        }
    }
}
