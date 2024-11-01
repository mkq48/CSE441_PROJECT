package com.example.rcs.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rcs.R;
import com.example.rcs.databinding.FragmentChaperViewBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FragmentChaperView extends Fragment {
    private PDFView pdfView;
    private String pdfUrl,storyId;
    private int currentPage,chapId;
    private long chapCount;
    private Button previous_btn,next_btn;
    private ImageView img_comment;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentChaperViewBinding binding = FragmentChaperViewBinding.inflate(inflater, container, false);
        pdfView = binding.pdfView;
        // set su kien chuyen den man hinh binh luan cua chap
        img_comment = binding.imgComment;
        img_comment.setVisibility(View.GONE);
        img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("chapId", chapId);
                bundle.putString("storyId", storyId);
                NavController navController = NavHostFragment.findNavController(FragmentChaperView.this);
                navController.navigate(R.id.action_fragmentChaperView_to_commentFragment, bundle);
            }
        });
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        next_btn = binding.nextBtn;
        next_btn.setVisibility(View.GONE);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chapId<chapCount){
                    // chuyen sang chap tiep theo
                    Bundle bundle = new Bundle();
                    bundle.putInt("chapId", chapId+1);
                    bundle.putInt("currentPage", 1);
                    bundle.putString("storyId", storyId);
                    NavController navController = NavHostFragment.findNavController(FragmentChaperView.this);
                    navController.navigate(R.id.action_fragmentChaperView_self, bundle);
                }
            }
        });
        previous_btn = binding.previousBtn;
        previous_btn.setVisibility(View.GONE);
        previous_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chapId>1){
                    Bundle bundle = new Bundle();
                    bundle.putInt("chapId", chapId-1);
                    bundle.putInt("currentPage", 1);
                    bundle.putString("storyId", storyId);
                    NavController navController = NavHostFragment.findNavController(FragmentChaperView.this);
                    navController.navigate(R.id.action_fragmentChaperView_self, bundle);
                }
            }
        });
        chapId = getArguments().getInt("chapId",0);
        storyId = getArguments().getString("storyId");
        currentPage = getArguments().getInt("currentPage",0);
        //set title for toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chương "+chapId);
        //print to log cat
        Log.d("chapId",chapId+"");
        Log.d("storyId",storyId);
        Log.d("currentPage",currentPage+"");
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
                Log.d("pdfUrl",pdfUrl);
                new DownloadPdf().execute(pdfUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        new DownloadPdf().execute(pdfUrl);
        return binding.getRoot();
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
    public void onPause() {
        super.onPause();
        Log.d("onPause","onPause");
        // them so view
        DocumentReference ref = FirebaseFirestore.getInstance().document("stories/"+storyId);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("history/"+userID+"/"+storyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    FirebaseFirestore.getInstance().runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@NonNull com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(ref);
                            Long views = snapshot.getLong("views");
                            transaction.update(ref, "views", views + 1);
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Succee", Toast.LENGTH_SHORT).show();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseDatabase.getInstance().getReference("history/"+userId+"/"+storyId+"/currentChap").setValue(chapId);
                            FirebaseDatabase.getInstance().getReference("history/"+userId+"/"+storyId+"/currentPage").setValue(pdfView.getCurrentPage());
                        }
                    });
                }
                else {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("history/"+userId+"/"+storyId+"/currentChap").setValue(chapId);
                    FirebaseDatabase.getInstance().getReference("history/"+userId+"/"+storyId+"/currentPage").setValue(pdfView.getCurrentPage());
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