package com.example.rcs;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rcs.databinding.CommentItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private static List<Comment> comments;
    public CommentAdapter(List<Comment> comments) {
        CommentAdapter.comments = comments;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentItemBinding binding = CommentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<Comment> commentList) {
        comments=commentList;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        private final CommentItemBinding binding;
        public CommentViewHolder(CommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.likeTv.setOnClickListener(v->{
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    Comment comment = comments.get(position);
                    comment.setNumberOfLike(comment.getNumberOfLike()+1);
                    CommentActivity.commentsRef.child(comment.getId()).setValue(comment, (error, ref) -> {
                        if (error!=null){
                            Log.d("err", "loi gi do");
                        }
                    });
                }
            });
        }
        public void bind(Comment comment) {
            binding.user.setText(comment.getUserID());
            binding.content.setText(comment.getContent());
            binding.likeTv.setText(String.valueOf(comment.getNumberOfLike()));
        }
    }
}
