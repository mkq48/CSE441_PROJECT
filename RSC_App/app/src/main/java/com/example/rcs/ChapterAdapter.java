package com.example.rcs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHoder>{
    private long chapterCount;
    private List<Integer> chapterList;
    private String storyId;
    private Context context;


    public ChapterAdapter(long chaptersCount, String storyId, Context context) {
        chapterList = new ArrayList<>();
        for (int i = 0; i < chaptersCount; i++) {
            chapterList.add(i+1);
        }
        this.storyId = storyId;
        this.context = context;
    }
    public void addNewChap(){
        chapterList.add(getItemCount()+1);
        notifyItemInserted(getItemCount()-1);
    }

    @NonNull
    @Override
    public ChapterViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item,parent,false);
        return new ChapterViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHoder holder, int position) {
        holder.btn_chap.setText("Chap "+(position+1));
        holder.btn_chap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Chaper_View_Activity.class);
                i.putExtra("chapId",holder.getAdapterPosition()+1);
                i.putExtra("storyId",storyId);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterViewHoder extends RecyclerView.ViewHolder{
        private Button btn_chap;
        public ChapterViewHoder(@NonNull View itemView) {
            super(itemView);
            btn_chap = itemView.findViewById(R.id.btn_chap);
        }
    }
}
