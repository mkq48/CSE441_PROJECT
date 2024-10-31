package com.example.rcs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;


public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private TextView tv_genre, tv_author, tv_name;
    private RecyclerView rv_ressult;
    private Button btn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

    }

    public void initViews() {
        // Liên kết các view với ID trong XML
        searchView = findViewById(R.id.search_view);
        tv_genre = findViewById(R.id.tv_genre);
        tv_author = findViewById(R.id.tv_author);
        tv_name = findViewById(R.id.tv_story);
        rv_ressult = findViewById(R.id.rv_ressult);
        btn_search = findViewById(R.id.btn_search);

        // Xử lý sự kiện khi nhấn nút tìm kiếm
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức searchByStoryName() khi nhấn nút tìm kiếm
                searchByStoryName();
            }
        });

        // Thiết lập sự kiện cho SearchView khi thực hiện tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Gọi phương thức searchByStoryName() khi người dùng nhấn tìm kiếm
                searchByStoryName();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Có thể cập nhật danh sách kết quả khi người dùng nhập text
                return false;
            }
        });

        // Thiết lập sự kiện khi nhấn vào TextView tv_name (Tên truyện)
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_name.setTextColor(Color.RED); // Màu đỏ cho Tên truyện
                tv_author.setTextColor(Color.BLACK); // Đặt lại màu gốc cho các TextView khác
                tv_genre.setTextColor(Color.BLACK);
            }
        });

        // Thiết lập sự kiện khi nhấn vào TextView tv_author (Tên tác giả)
        tv_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_author.setTextColor(Color.BLUE); // Màu xanh cho Tên tác giả
                tv_name.setTextColor(Color.BLACK); // Đặt lại màu gốc cho các TextView khác
                tv_genre.setTextColor(Color.BLACK);
            }
        });

        // Thiết lập sự kiện khi nhấn vào TextView tv_genre (Thể loại)
        tv_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_genre.setTextColor(Color.GREEN); // Màu xanh lá cho Thể loại
                tv_name.setTextColor(Color.BLACK); // Đặt lại màu gốc cho các TextView khác
                tv_author.setTextColor(Color.BLACK);
            }
        });
    }

    public void loadData(){

    }
    public void searchByStoryName(){
        // lấy chuỗi tìm kiếm của searchview
        String keyWord;

    }
}