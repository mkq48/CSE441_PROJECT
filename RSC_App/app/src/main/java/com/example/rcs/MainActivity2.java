package com.example.rcs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private StoryAdapter storyAdapter1, storyAdapter2, storyAdapter3;
    private ArrayList<Story> storyList1, storyList2, storyList3;
    FirebaseFirestore db;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        progressDialog = new ProgressDialog(MainActivity2.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        recyclerView1 = findViewById(R.id.recycleviewNewStory);
        recyclerView2 = findViewById(R.id.recycleviewRcmt);
        recyclerView3 = findViewById(R.id.recycleviewTop);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);

        storyList1 = new ArrayList<Story>();
        storyList2 = new ArrayList<Story>();
        storyList3 = new ArrayList<Story>();

        EventChangeListener();

        storyAdapter1 = new StoryAdapter(storyList1, MainActivity2.this);
        storyAdapter2 = new StoryAdapter(storyList2, MainActivity2.this);
        storyAdapter3 = new StoryAdapter(storyList3, MainActivity2.this);

        recyclerView1.setAdapter(storyAdapter1);
        recyclerView2.setAdapter(storyAdapter2);
        recyclerView3.setAdapter(storyAdapter3);
    }

    public void EventChangeListener(){
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("stories");

        db.collection("stories").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    Toast.makeText(MainActivity2.this, "Lỗi firestore", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value == null || value.isEmpty()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity2.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storyList3.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Story story = data.getValue(Story.class);

                    if (story != null) {
                        storyList3.add(story);
                    }
                    storyAdapter3.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity2.this, "Lỗi Realtime Database", Toast.LENGTH_SHORT).show();
            }
        });

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
}