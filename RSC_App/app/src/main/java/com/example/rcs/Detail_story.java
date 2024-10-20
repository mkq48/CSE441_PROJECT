package com.example.rcs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Detail_story extends AppCompatActivity {
    private TextView tv_name, tv_author, tv_category, tv_content, tv_favorites,tv_views;
    private ImageView btn_favorite, img;
    private String storyId, name, imageUrl;
    private boolean isfavorited,initial_favorite_status;
    private List<String> chapterList;
    private RecyclerView chapter_rv;
    private ChapterAdapter chapterAdapter;
    private String userId = new User().getCurrentUserId();
    private Button btn_read;
    private int currentChap, currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_story);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_story), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        loadData();
    }

    private void loadData() {
//        hien thi ten truyen
        tv_name.setText(name);
//        hien thi anh
        Glide.with(this).asBitmap().load(imageUrl).into(img);
//        lay thong tin cua truyen
        FirebaseDatabase.getInstance().getReference("stories/" + storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // lay noi dung truyen gán vào tv tuong ung
                tv_content.setText("Tóm tắt nội dung : " + snapshot.child("content").getValue(String.class));

                // hien thi so luot thich
                int total_like;
                if(initial_favorite_status && !isfavorited){
                    total_like = snapshot.child("favorites").getValue(Integer.class)-1;
                }else if(!initial_favorite_status && isfavorited){
                    total_like = snapshot.child("favorites").getValue(Integer.class)+1;
                }else {
                    total_like = snapshot.child("favorites").getValue(Integer.class);
                }
                tv_favorites.setText("Yêu thích: " + total_like);
                // hien thi so luot xem
                tv_views.setText(snapshot.child("views").getValue(Integer.class)+"");


                // lay ten tac gia
                FirebaseDatabase.getInstance().getReference("authors/" + snapshot.child("author").getValue()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tv_author.setText("Tác giả: " + snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // lay ten the loai
        loadCategories();
    }

    public void initView() {
        img = findViewById(R.id.img);
        tv_name = findViewById(R.id.tv_name);
        tv_author = findViewById(R.id.tv_author);
        tv_category = findViewById(R.id.tv_category);
        tv_content = findViewById(R.id.tv_content);
        tv_favorites = findViewById(R.id.tv_favorites);
        btn_favorite = findViewById(R.id.btn_favorite);
        img = findViewById(R.id.img);
        tv_name = findViewById(R.id.tv_name);
        tv_author = findViewById(R.id.tv_author);
        tv_category = findViewById(R.id.tv_category);
        tv_content = findViewById(R.id.tv_content);
        tv_favorites = findViewById(R.id.tv_favorites);
        btn_favorite = findViewById(R.id.btn_favorite);
        tv_views = findViewById(R.id.tv_views);
        // lay du lieu tu intent
        Intent intent = getIntent();
        storyId = intent.getStringExtra("storyId");
        name = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("imageUrl");
        userId = new User().getCurrentUserId();
        chapter_rv = findViewById(R.id.chapter_rv);
        chapterList = new ArrayList<>();
        // hien thi cac chap len recycler
        chapterAdapter = new ChapterAdapter(chapterList,storyId,this);
        chapter_rv.setAdapter(chapterAdapter);
        chapter_rv.setLayoutManager(new GridLayoutManager(this,4));
        // lay danh sach cac chapter
        loadChapters();

        // kiem tra xem nguoi dung da thich truyen hien tai chua

        showIsFavorite();
        //gán sự kien cho nut yeu thich:
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long favorites = Long.parseLong(tv_favorites.getText().toString().replace("Yêu thích: ", ""));
                    if (isfavorited) {
                        // neu nguoi dung da thich truyen hien tai
                        // so luong da thich truyen = sl - 1
//                        cap nhat lại biến isfavorites va giao dien
                        isfavorited = false;
                        tv_favorites.setText("Yêu thích: " + (favorites - 1) + "");
                        btn_favorite.setImageResource(R.drawable.white_heart);

                    } else {
                        //    neu nguoi dung chua thich truyen
//                        sl da thich truyen +=1
//                        cap nhat lại biến isfavorites va giao dien
                        isfavorited = true;
                        tv_favorites.setText("Yêu thích: " + (favorites + 1) + "");
                        btn_favorite.setImageResource(R.drawable.red_heart);
                    }
                } catch (Exception e) {

                }
            }
        });
//        nut doc tiep
        btn_read = findViewById(R.id.btn_read);
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }
    @Override
    protected void onPause() {
        super.onPause();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user_favorites/" + userId + "/" + storyId);
        int favorites;
        FirebaseDatabase.getInstance().getReference("stories/" + storyId).child("favorites").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                int favorites;
                if (isfavorited&&!initial_favorite_status) {
                    ref.setValue(true);
                    favorites = currentData.getValue(Integer.class)+1;
                    currentData.setValue(favorites);
                    initial_favorite_status = isfavorited;
                    return Transaction.success(currentData);
                } else if(!isfavorited&&initial_favorite_status) {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ref.removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    favorites = currentData.getValue(Integer.class)-1;
                    currentData.setValue(favorites);
                    initial_favorite_status = isfavorited;
                    return Transaction.success(currentData);
                }else{
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }
    public void showDialog(){
        // lay chap dang doc trong lịch sử nếu co
        FirebaseDatabase.getInstance().getReference("history/"+userId+"/"+storyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentChap = snapshot.child("currentChap").getValue(Integer.class);
                    currentPage = snapshot.child("currentPage").getValue(Integer.class);
//                    positionOffSet = snapshot.child("positionOffSet").getValue(Float.class);
//                    Toast.makeText(Detail_story.this, positionOffSet+"", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Detail_story.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Chúng tôi ghi nhận lần gần nhất bạn đang đọc Chap "+currentChap+". Bạn có muốn đọc tiếp Chap đang đọc hay không ?");
                    builder.setPositiveButton("Đọc tiếp", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Detail_story.this, Chaper_View_Activity.class);
                            i.putExtra("chapId",currentChap);
                            i.putExtra("storyId",storyId);
                            i.putExtra("chapCount",chapterAdapter.getItemCount());
                            i.putExtra("currentPage",currentPage);
//                            i.putExtra("positionOffSet",positionOffSet);
                            startActivity(i);
                        }
                    });

                    builder.setNegativeButton("Đọc từ đầu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            read_from_the_beginning();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    read_from_the_beginning();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void read_from_the_beginning(){
        Intent i = new Intent(Detail_story.this, Chaper_View_Activity.class);
        i.putExtra("chapId",1);
        i.putExtra("storyId",storyId);
        i.putExtra("chapCount",chapterAdapter.getItemCount());
        startActivity(i);
    }
    public void loadCategories(){
        FirebaseDatabase.getInstance().getReference("stories/" + storyId + "/categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_category.setText("");
                for (DataSnapshot snap : snapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference("categories/" + snap.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String text = tv_category.getText().toString();
                            if (!text.equals("")) {
                                text += ", ";
                            } else {
                                text = "Thể loại: ";
                            }
                            tv_category.setText(text + snapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loadChapters(){
        FirebaseDatabase.getInstance().getReference("story_chapters/"+storyId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String url = snapshot.getValue(String.class);
                chapterList.add(url);
                chapterAdapter.notifyItemInserted(chapterList.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void showIsFavorite(){
        // lay id cua nguoi dung hien tai de truy van trong bang user_favorites
        FirebaseDatabase.getInstance().getReference("user_favorites/" + userId).child(storyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    initial_favorite_status = true;
                    isfavorited = true;
                    btn_favorite.setImageResource(R.drawable.red_heart);
                } else {
                    initial_favorite_status = false;
                    isfavorited = false;
                    btn_favorite.setImageResource(R.drawable.white_heart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}