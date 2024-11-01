package com.example.rcs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Chaper_View_Activity extends AppCompatActivity {
    private PDFView pdfView;
    private String pdfUrl,storyId;
    private int currentPage,chapId;
    private long chapCount;
    private Button previous_btn,next_btn;
    private ImageView img_comment;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chaper_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pdfView = findViewById(R.id.pdfView);
        // set su kien chuyen den man hinh binh luan cua chap
        img_comment = findViewById(R.id.img_comment);
        img_comment.setVisibility(View.GONE);
        img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chuyen den man hinh binh luan
                Intent i = new Intent(Chaper_View_Activity.this, CommentActivity.class);
                i.putExtra("chapId",chapId);
                i.putExtra("storyId",storyId);
                startActivity(i);
            }
        });
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        next_btn = findViewById(R.id.next_btn);
        next_btn.setVisibility(View.GONE);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chapId<chapCount){
                    Intent i = new Intent(Chaper_View_Activity.this, Chaper_View_Activity.class);
                    i.putExtra("chapId",chapId+1);
                    i.putExtra("storyId",storyId);
                    startActivity(i);
                }
            }
        });
        previous_btn = findViewById(R.id.previous_btn);
        previous_btn.setVisibility(View.GONE);
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chapId>1){
                    Intent i = new Intent(Chaper_View_Activity.this, Chaper_View_Activity.class);
                    i.putExtra("chapId",chapId-1);
                    i.putExtra("storyId",storyId);
                    startActivity(i);
                }
            }
        });
        Intent i = getIntent();
        chapId = i.getIntExtra("chapId",0);
        storyId = i.getStringExtra("storyId");
        currentPage = i.getIntExtra("currentPage",0);
        // get chapCount
        FirebaseDatabase.getInstance().getReference("story_chapters/"+storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chapCount = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // lay pdfUrl de tai len pdfViewer
        FirebaseDatabase.getInstance().getReference("story_chapters/"+storyId+"/chap"+chapId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfUrl = snapshot.getValue(String.class);
                new DownloadPdf().execute(pdfUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        new DownloadPdf().execute(pdfUrl);
    }
    class DownloadPdf extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(pdfUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == 200) {
                    return new BufferedInputStream(connection.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).spacing(10).defaultPage(currentPage).enableSwipe(true)// Cho phép lướt
                    .swipeHorizontal(false)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    showFunctionButton();
                }
            }).onTap(new OnTapListener() {
                @Override
                public boolean onTap(MotionEvent e) {
                    showFunctionButton();
                    return true;
                }
            }).load();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // them so view
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("history/"+userID+"/"+storyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    FirebaseDatabase.getInstance().getReference("stories/" + storyId).child("views").runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                            long views = currentData.getValue(Long.class)+1;
                            currentData.setValue(views);
                                return Transaction.success(currentData);
                        }

                        @Override
                        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                            FirebaseDatabase.getInstance().getReference("history/"+userID+"/"+storyId+"/currentChap").setValue(chapId);
                            FirebaseDatabase.getInstance().getReference("history/"+userID+"/"+storyId+"/currentPage").setValue(pdfView.getCurrentPage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void showFunctionButton(){
        if(chapId<chapCount){
            next_btn.setVisibility(View.VISIBLE);

            // Sử dụng Handler để ẩn nút sau 3 giây (3000 ms)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    next_btn.setVisibility(View.GONE); // Ẩn nút
                }
            }, 3000); // Thời gian hiển thị là 3 giây
        }

        if(chapId>1){
            previous_btn.setVisibility(View.VISIBLE);

            // Sử dụng Handler để ẩn nút sau 3 giây (3000 ms)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    previous_btn.setVisibility(View.GONE); // Ẩn nút
                }
            }, 3000); // Thời gian hiển thị là 3 giây
        }
        img_comment.setVisibility(View.VISIBLE);

        // Sử dụng Handler để ẩn nút sau 3 giây (3000 ms)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img_comment.setVisibility(View.GONE); // Ẩn nút
            }
        }, 3000); // Thời gian hiển thị là 3 giây
    }
}