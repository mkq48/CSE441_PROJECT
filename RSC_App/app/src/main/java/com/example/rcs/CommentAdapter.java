package com.example.rcs;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rcs.databinding.CommentItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private static List<Comment> comments;
    //private ArrayList<UserProfile> userProfiles;

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
                    Boolean hasLiked = comment.getLikes().getOrDefault(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                    if(hasLiked){
                        comment.getLikes().put(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
                        comment.setNumberOfLike(comment.getNumberOfLike()-1);
                    }else{
                        comment.getLikes().put(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                        comment.setNumberOfLike(comment.getNumberOfLike()+1);
                    }
                    CommentActivity.commentsRef.child(comment.getId()).setValue(comment, (error, ref) -> {
                        if (error!=null){
                            Log.d("err", "loi gi do");
                        }
                    });
                }
            });
        }
        public void bind(Comment comment) {
            //UserProfile userProfile = new UserProfile();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(comment.getUserID());
            docRef.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("user", "DocumentSnapshot data: " + document.getData());
                    binding.userNameTv.setText(document.getString("name"));
                    Glide.with(binding.getRoot().getContext()).load(document.getString("imageUrl")).error(R.drawable.ic_passworddoc).into(binding.userAvatarImage);
                } else {
                    Log.d("user", "No such document");
                }
            });
            binding.contentTv.setText(comment.getContent());
            binding.likeTv.setText(String.valueOf(comment.getNumberOfLike()));
            Boolean hasLiked = comment.getLikes().getOrDefault(FirebaseAuth.getInstance().getCurrentUser().getUid(), false);
            if (hasLiked) {
                binding.likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up, 0, 0, 0);
            }else binding.likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thumb_up_outline, 0, 0, 0);
        }
    }
}
