package com.example.looptser.posts;

public class Post {
    private String postImage, postDescription, userUid, userName, userImage;
    private String date;

    public Post(String postImage, String postDescription, String userUid,String userName, String userImage, String date) {
        this.postImage = postImage;
        this.postDescription = postDescription;
        this.userUid = userUid;
        this.userName = userName;
        this.userImage = userImage;
        this.date = date;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
