package com.example.rcs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private StoryAdapter storyAdapter1, storyAdapter2, storyAdapter3;
    private SliderAdapter sliderAdapter;
    private ArrayList<Story> storyList1, storyList2, storyList3;
    private ArrayList<String> sliderList;
    private ViewPager2 viewPager2;
    private Handler sliderHandler;
    private Runnable sliderRunnable;

    FirebaseFirestore db;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        storyList1 = new ArrayList<Story>();
        storyList2 = new ArrayList<Story>();
        storyList3 = new ArrayList<Story>();
        sliderList = new ArrayList<String>();

        initUI();
        initListener();

        storyAdapter1 = new StoryAdapter(storyList1, HomeActivity.this);
        storyAdapter2 = new StoryAdapter(storyList2, HomeActivity.this);
        storyAdapter3 = new StoryAdapter(storyList3, HomeActivity.this);

        recyclerView1.setAdapter(storyAdapter1);
        recyclerView2.setAdapter(storyAdapter2);
        recyclerView3.setAdapter(storyAdapter3);

        viewPager2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stopAutoSlide();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                startAutoSlide();
            }
            return false;
        });
    }

    private void initUI(){
        recyclerView1 = findViewById(R.id.recycleviewNewStory);
        recyclerView2 = findViewById(R.id.recycleviewRcmt);
        recyclerView3 = findViewById(R.id.recycleviewTop);
        viewPager2 = findViewById(R.id.viewPager);
        sliderHandler = new Handler(Looper.getMainLooper());

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);
    }


    private void initListener(){
        EventChangeListener();
        loadFavories();
        loadSlider();


    }

    public void loadSlider(){
        db.collection("stories")
                .limit(6)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("imageUrl");
                        if (imageUrl != null) {
                            sliderList.add(imageUrl);
                        }
                    }

                    sliderAdapter = new SliderAdapter(HomeActivity.this, sliderList);
                    viewPager2.setAdapter(sliderAdapter);

                    setAutoSlide();
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });

        viewPager2.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                stopAutoSlide();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                startAutoSlide();
            }
            return false;
        });

    }

    public void EventChangeListener(){
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
//        dbRef = FirebaseDatabase.getInstance().getReference("stories").child("favories");

        db.collection("stories").limit(6).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    for (DocumentChange dc : value.getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED){
                            Story story = dc.getDocument().toObject(Story.class);
                            if (!storyList1.contains(story)) {
                                storyList1.add(story);
                            }
                            if (!storyList2.contains(story)) {
                                storyList2.add(story);
                            }
                        }
                    }
                    storyAdapter1.notifyDataSetChanged();
                    storyAdapter2.notifyDataSetChanged();

                    progressDialog.dismiss();
                }


                if(error != null){
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Lỗi firestore", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value == null || value.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });


//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                storyList3.clear();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Story story = data.getValue(Story.class);
//
//                    if (story != null) {
//                        storyList3.add(story);
//                    }
//                    storyAdapter3.notifyDataSetChanged();
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressDialog.dismiss();
//                Toast.makeText(MainActivity2.this, "Lỗi Realtime Database", Toast.LENGTH_SHORT).show();
//            }
//        });

//        db.collection("stories").orderBy("favorites").limit(3)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (value != null) {
//                    for (DocumentChange dc : value.getDocumentChanges()){
//                        if(dc.getType() == DocumentChange.Type.ADDED){
//                            Story story = dc.getDocument().toObject(Story.class);
//                            if (!storyList3.contains(story)) {
//                                storyList3.add(story);
//                            }
//
//                        }
//                    }
//                    storyAdapter3.notifyDataSetChanged();
//
//                    progressDialog.dismiss();
//                }
//
//
//                if(error != null){
//                    progressDialog.dismiss();
//                    Toast.makeText(MainActivity2.this, "Lỗi firestore", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (value == null || value.isEmpty()) {
//                    progressDialog.dismiss();
//                    Toast.makeText(MainActivity2.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//        });

    }


    public void loadFavories(){
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("stories");

        List<String> favoriteIds = new ArrayList<>();


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteIds.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String storyId = data.getKey();
                    if (storyId != null) {
                        favoriteIds.add(storyId);
                    }
                }

                if (!favoriteIds.isEmpty()) {
                    db.collection("stories")
                            .whereIn("id", favoriteIds) // Truy vấn các truyện có ID trong favoriteIds
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value != null) {
                                        storyList3.clear();
                                        for (DocumentChange dc : value.getDocumentChanges()) {
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                Story story = dc.getDocument().toObject(Story.class);
                                                storyList3.add(story);
                                            }
                                        }
                                        storyAdapter3.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }

                                    if (error != null) {
                                        progressDialog.dismiss();
                                        Toast.makeText(HomeActivity.this, "Lỗi Firestore", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (value == null || value.isEmpty()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(HomeActivity.this, "Không có dữ liệu yêu thích", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Không có dữ liệu yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Lỗi Realtime Database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setAutoSlide() {
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager2.getCurrentItem() + 1) % sliderList.size();
                viewPager2.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 4000);
            }
        };
        startAutoSlide();
    }

    private void startAutoSlide() {
        sliderHandler.postDelayed(sliderRunnable, 4000);
    }

    private void stopAutoSlide() {
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoSlide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoSlide();
    }


}