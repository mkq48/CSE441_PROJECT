package com.example.rcs;

import com.google.firebase.auth.FirebaseAuth;

public class User {
    private String imageUrl;
    private String userName;

    public String getCurrentUserId() {
//        return FirebaseAuth.getInstance().getCurrentUser().getUid();
        return "user1";
    }
    public User getUserById(String id){

        return new User();
    }
    public User getCurrentUser(){
        return new User();
    }
    public void addNewUser(String userId, String imageUrl, String userName){
        // khi đăng nhập :
        // khi đăng nhập, check user đã tồn tại trong db hay chưa,
        // nếu chưa tồn tại thêm bản ghi mới gồm tên, imgUrl vào db, nếu đã có bỏ qua
    }

    public User() {
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getUserName() {
        return userName;
    }


    public void updateImageOfCurrentUser(){
        // up anh len storage
        // lay url update len auth
        // cap nhat url len bang user trong cloud firestore
    }
    public void updateCurrentUser(String name, String email){

    }
    public void updatePassWordOfCurrentUser(String passWord){

    }
}
