//package com.example.rcs;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//
//public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
//
//    private List<MyItem> itemList;
//
//    public HomeAdapter(List<MyItem> itemList) {
//        this.itemList = itemList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        MyItem item = itemList.get(position);
//        holder.textViewTitle.setText(item.getTitle());
//        holder.textViewDescription.setText(item.getDescription());
//    }
//
//    @Override
//    public int getItemCount() {
//        return itemList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView textViewTitle, textViewDescription;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewTitle = itemView.findViewById(R.id.textViewTitle);
//            textViewDescription = itemView.findViewById(R.id.textViewDescription);
//        }
//    }
//}
//
//
