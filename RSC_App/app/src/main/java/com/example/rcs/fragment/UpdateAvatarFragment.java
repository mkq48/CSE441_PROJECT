package com.example.rcs.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.graphics.Bitmap;
import android.net.Uri;

import android.provider.MediaStore;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.rcs.databinding.FragmentUpdateAvatarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class UpdateAvatarFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_PERMISSION_CODE = 102;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private FragmentUpdateAvatarBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUpdateAvatarBinding.inflate(getLayoutInflater(),container,false);
        binding.btnSave.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        binding.btnCamera.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });
        binding.btnGallery.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY_PERMISSION_CODE);
            } else {
                openGallery();
            }
        });
        binding.btnSave.setOnClickListener(view -> uploadUserAvatar(currentUser.getUid(), imageUri));
        loadAvatar();
        return binding.getRoot();
    }
    private void loadAvatar(){
        if (currentUser != null) {
            Uri photoUrl = currentUser.getPhotoUrl();
            if (photoUrl != null) {
                Glide.with(UpdateAvatarFragment.this)
                        .load(photoUrl)
                        .override(500, 500)
                        .into(binding.avatar);
            }
        }
    }
    private void updateAvatar(String avatarUrl){

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang cập nhật ảnh...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(avatarUrl))
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();

                        NavController navController = NavHostFragment.findNavController(UpdateAvatarFragment.this);
                        navController.popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Cập nhật ảnh đại diện thất bại", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                binding.btnSave.setVisibility(View.VISIBLE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                saveImageToInternalStorage(bitmap);
                Glide.with(UpdateAvatarFragment.this)
                        .load(imageUri)
                        .override(500, 500)
                        .into(binding.avatar);
                binding.btnSave.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = new File(getActivity().getExternalFilesDir(null), "MyImages");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "avatar_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(directory, fileName);
        String filePath = imageFile.getAbsolutePath();

        try (OutputStream fos = new FileOutputStream(imageFile)) {
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
                Toast.makeText(getContext(), "Camera or storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == GALLERY_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        imageUri = data.getData();
                        Glide.with(UpdateAvatarFragment.this)
                                .load(imageUri)
                                .override(500, 500)
                                .into(binding.avatar);
                        binding.btnSave.setVisibility(View.VISIBLE);
                    }
                }
            });
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    public void uploadUserAvatar(String userId, Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("avatars/" + userId + ".jpg");

        UploadTask uploadTask = storageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {

            storageRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                String avatarUrl = downloadUrl.toString();
                Log.d("UploadAvatar", "Avatar URL: " + avatarUrl);


                saveAvatarUrlToDatabase(userId, avatarUrl);
                updateAvatar(avatarUrl);
            });
        }).addOnFailureListener(exception -> Log.e("UploadAvatar", "Error uploading avatar", exception));
    }

    private void saveAvatarUrlToDatabase(String userId, String avatarUrl) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.document(userId).update("imageUrl", avatarUrl)
                .addOnSuccessListener(aVoid -> {
                    Log.d("onUpdate", "Avatar URL stored in Firestore.");
                }).addOnFailureListener(e -> {
                    Log.w("onUpdate", "Error storing avatar URL", e);
                });
    }
}