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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private StoryAdapter storyAdapter1, storyAdapter2, storyAdapter3, adapter;
    private SliderAdapter sliderAdapter;
    private ArrayList<Story> storyList1, storyList2, storyList3, stories;
    private ArrayList<Story> sliderList;
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
        sliderList = new ArrayList<Story>();

        initUI();
        initListener();

        storyAdapter1 = new StoryAdapter(storyList1, HomeActivity.this);
        storyAdapter2 = new StoryAdapter(storyList2, HomeActivity.this);
        storyAdapter3 = new StoryAdapter(storyList3, HomeActivity.this);

        recyclerView1.setAdapter(storyAdapter1);
        recyclerView2.setAdapter(storyAdapter2);
        recyclerView3.setAdapter(storyAdapter3);

        setAutoSlide();
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
        getData();

    }


    private void getData() {
        FirebaseDatabase.getInstance().getReference("stories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                FirebaseFirestore.getInstance().document("stories/" + id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = (String)document.get("name");
                                String imgUrl = (String)document.get("imageUrl");;
                                storyList1.add(new Story(id,name,imgUrl));
                                storyAdapter1.notifyItemInserted(storyList1.size()-1);
                                storyList2.add(new Story(id,name,imgUrl));
                                storyAdapter2.notifyItemInserted(storyList1.size()-1);
                                sliderList.add(new Story(id, name,imgUrl));
                                sliderAdapter = new SliderAdapter(HomeActivity.this, sliderList);
                                viewPager2.setAdapter(sliderAdapter);
                            }

                        }
                     progressDialog.dismiss();

                    }
                });
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