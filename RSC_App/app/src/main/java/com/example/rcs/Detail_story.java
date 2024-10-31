package com.example.rcs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Detail_story extends AppCompatActivity {
    private TextView tv_name, tv_author, tv_category, tv_content, tv_favorites,
            tv_views, tv_chap_count, tv_show_more, tv_collapse, tv_show_more_chap,
            tv_collapse_chap,
            tv_latest, tv_oldest;
    private ImageView btn_favorite, img;
    private String storyId, name, imageUrl;
    private boolean isfavorited, initial_favorite_status;
    private List<String> categoryList;
    private List<Integer> chapList;
    private boolean isCollapseChapterList, isLastestChapterList;
    private RecyclerView chapter_rv, categories_rv;
    private ChapterAdapter chapterAdapter;
    private CategoryAdapter categoryAdapter;
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
        //        lay thong tin cua truyen
        loadStaticIfo();
//        hien thi ten truyen
        tv_name.setText(name);
//        hien thi anh
        Glide.with(this).asBitmap().load(imageUrl).into(img);
        // lay danh sach cac chapter
        loadChapters();

        // kiem tra xem nguoi dung da thich truyen hien tai chua
        // neu chua: chuyen trang thai btn yeu thich thanh chua thich
        // neu da thich: chuyen trang thai btn yeu thich thanh da thich
        showIsFavorite();
        // hien thi so luot thich, views dong thoi cap nhat realtime khi co thay doi trong db
        FirebaseDatabase.getInstance().getReference("stories/" + storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // hien thi so luot thich
                int total_like;
                if (initial_favorite_status && !isfavorited) {
                    total_like = snapshot.child("favorites").getValue(Integer.class) - 1;
                } else if (!initial_favorite_status && isfavorited) {
                    total_like = snapshot.child("favorites").getValue(Integer.class) + 1;
                } else {
                    total_like = snapshot.child("favorites").getValue(Integer.class);
                }
                tv_favorites.setText(total_like + "");
                // hien thi so luot xem
                tv_views.setText(snapshot.child("views").getValue(Integer.class) + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initView() {
        img = findViewById(R.id.img);
        tv_chap_count = findViewById(R.id.tv_chap_count);
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
        categories_rv = findViewById(R.id.categories_rv);
        // lay du lieu tu intent
        Intent intent = getIntent();
        storyId = intent.getStringExtra("storyId");
        name = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("imageUrl");
        userId = new User().getCurrentUserId();
        chapter_rv = findViewById(R.id.chapter_rv);
        // hien thi cac chap len recycler
        chapList = new ArrayList<>();

//        isCollapseChapterList = true;
        isLastestChapterList = true;
        chapterAdapter = new ChapterAdapter(chapList, storyId, this);
        chapter_rv.setAdapter(chapterAdapter);
        chapter_rv.setLayoutManager(new GridLayoutManager(this, 5));
        // hien thi the loai truyen len recycler
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        categoryAdapter.notifyDataSetChanged();
        categories_rv.setAdapter(categoryAdapter);
        categories_rv.setLayoutManager(new GridLayoutManager(this, 2));
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
                        tv_favorites.setText((favorites - 1) + "");
                        btn_favorite.setImageResource(R.drawable.white_heart);

                    } else {
                        //    neu nguoi dung chua thich truyen
//                        sl da thich truyen +=1
//                        cap nhat lại biến isfavorites va giao dien
                        isfavorited = true;
                        tv_favorites.setText((favorites + 1) + "");
                        btn_favorite.setImageResource(R.drawable.red_heart);
                    }
                } catch (Exception e) {

                }
            }
        });
        tv_show_more = findViewById(R.id.tv_show_more);
        tv_collapse = findViewById(R.id.tv_collapse);
        tv_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_show_more.setVisibility(View.GONE);
                tv_content.setMaxLines(Integer.MAX_VALUE);
                tv_collapse.setVisibility(View.VISIBLE);
            }
        });
        tv_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_collapse.setVisibility(View.GONE);
                tv_content.setMaxLines(1);
                tv_show_more.setVisibility(View.VISIBLE);
            }
        });
        // su kien xu ly cac btn voi chapList
        tv_show_more_chap = findViewById(R.id.tv_show_more_chap);
        tv_collapse_chap = findViewById(R.id.tv_collapse_chap);
        tv_latest = findViewById(R.id.tv_latest);
        tv_oldest = findViewById(R.id.tv_oldest);
        tv_show_more_chap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_show_more_chap.setVisibility(View.GONE);
                chapterAdapter.expand();
                tv_collapse_chap.setVisibility(View.VISIBLE);
            }
        });
        tv_collapse_chap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_collapse_chap.setVisibility(View.GONE);
                chapterAdapter.collapse();
                tv_show_more_chap.setVisibility(View.VISIBLE);
            }
        });
        tv_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_latest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                tv_latest.setTextColor(getResources().getColor(R.color.white));
                tv_oldest.setTextColor(getResources().getColor(R.color.black));
                tv_oldest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                chapterAdapter.sortDesc();
            }
        });
        tv_oldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_oldest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
                tv_oldest.setTextColor(getResources().getColor(R.color.white));
                tv_latest.setTextColor(getResources().getColor(R.color.black));
                tv_latest.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                chapterAdapter.sortIncrease();
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
        show_read_and_show_more_chap();
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
                if (isfavorited && !initial_favorite_status) {
                    ref.setValue(true);
                    favorites = currentData.getValue(Integer.class) + 1;
                    currentData.setValue(favorites);
                    initial_favorite_status = isfavorited;
                    return Transaction.success(currentData);
                } else if (!isfavorited && initial_favorite_status) {
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
                    favorites = currentData.getValue(Integer.class) - 1;
                    currentData.setValue(favorites);
                    initial_favorite_status = isfavorited;
                    return Transaction.success(currentData);
                } else {
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }

    public void showDialog() {
        // lay chap dang doc trong lịch sử nếu co
        FirebaseDatabase.getInstance().getReference("history/" + userId + "/" + storyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentChap = snapshot.child("currentChap").getValue(Integer.class);
                    currentPage = snapshot.child("currentPage").getValue(Integer.class);
//                    positionOffSet = snapshot.child("positionOffSet").getValue(Float.class);
//                    Toast.makeText(Detail_story.this, positionOffSet+"", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Detail_story.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Chúng tôi ghi nhận lần gần nhất bạn đang đọc Chap " + currentChap + ". Bạn có muốn đọc tiếp Chap đang đọc hay không ?");
                    builder.setPositiveButton("Đọc tiếp", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Detail_story.this, Chaper_View_Activity.class);
                            i.putExtra("chapId", currentChap);
                            i.putExtra("storyId", storyId);
                            i.putExtra("currentPage", currentPage);
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
                } else {
                    read_from_the_beginning();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void read_from_the_beginning() {
        Intent i = new Intent(Detail_story.this, Chaper_View_Activity.class);
        i.putExtra("chapId", 1);
        i.putExtra("storyId", storyId);
        startActivity(i);
    }

    public void loadChapters() {
        FirebaseDatabase.getInstance().getReference("story_chapters/" + storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapList.clear();
                for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                    chapList.add(chapList.size()+1);
                }
                if(isLastestChapterList){
                    chapterAdapter.sortDesc();
                }else {
                    chapterAdapter.sortIncrease();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showIsFavorite() {
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

    public void loadStaticIfo() {
        FirebaseFirestore.getInstance().document("stories/" + storyId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String author = (String) document.get("author");
                        List<String> categories = (List<String>) document.get("categories");
                        String content = (String) document.get("content");
                        tv_author.setText("Tác giả: " + author);
                        tv_content.setText("Tóm tắt nội dung: " + content);
                        categoryList.addAll(categories);
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void show_read_and_show_more_chap() {
        FirebaseDatabase.getInstance().getReference("story_chapters/" + storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long chapCount = snapshot.getChildrenCount();
                tv_chap_count.setText(chapCount + " Chapter");
                if (chapCount > 0) {
                    btn_read.setVisibility(View.VISIBLE);
                    tv_oldest.setVisibility(View.VISIBLE);
                    tv_latest.setVisibility(View.VISIBLE);
                    if (chapCount > 5) {
                        tv_show_more_chap.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_read.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}