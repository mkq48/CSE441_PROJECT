package com.example.rcs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.Inflater;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHoder> {

    private List<Integer> chapterList;
    private int itemCountDefault;
    private String storyId;
    private Context context;

    public ChapterAdapter(List<Integer> chapterList, String storyId, Context context) {
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
                    Intent i = new Intent(context, Chaper_View_Activity.class);
                    i.putExtra("chapId", chapterList.get(holder.getAdapterPosition()));
                    i.putExtra("storyId", storyId);
                    context.startActivity(i);
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
