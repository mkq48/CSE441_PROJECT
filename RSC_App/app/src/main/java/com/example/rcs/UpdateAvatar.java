package com.example.rcs;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.hdodenhof.circleimageview.CircleImageView;


import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;

import android.widget.Toast;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class UpdateAvatar extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private Uri imageUri;
    private ImageView btnBack;
    private CircleImageView avartar;
    private Button btnCamera, btnLibrary;
    private TextView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_avatar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();
        initListener();
    }

    private void initUI(){
        btnBack = findViewById(R.id.btnBack);
        avartar = findViewById(R.id.avatar);
        btnCamera = findViewById(R.id.btnCamera);
        btnLibrary = findViewById(R.id.btnLibrary);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToProfile();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UpdateAvatar.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(UpdateAvatar.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateAvatar.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA_PERMISSION_CODE);
                } else {
                    openCamera();
                }
            }
        });


    }

    private void updateAvatar(){

    }

    private void navigateToProfile(){
        Intent intent = new Intent(UpdateAvatar.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                // Lấy ảnh từ URI
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // Lưu ảnh vào bộ nhớ điện thoại
                String savedImagePath = saveImageToInternalStorage(bitmap);

                // Cập nhật ImageView để hiển thị ảnh
                avartar.setImageBitmap(bitmap);

                btnSave.setVisibility(View.VISIBLE);

                if (savedImagePath != null) {
                    Toast.makeText(this, "Image saved to: " + savedImagePath, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = new File(getExternalFilesDir(null), "MyImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "avatar_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(directory, fileName);
        String filePath = imageFile.getAbsolutePath();

        try (OutputStream fos = new FileOutputStream(imageFile)) {
            // Nén và ghi ảnh vào file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera or storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}