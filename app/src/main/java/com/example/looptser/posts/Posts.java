package com.example.looptser.posts;

import java.util.ArrayList;

public class Posts {
    private String userName, userImage;
    private ArrayList<Post> postsList;

    public Posts(String userName, String userImage, ArrayList<Post> postsList) {
        this.userName = userName;
        this.userImage = userImage;
        this.postsList = postsList;
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

    public ArrayList<Post> getPostsList() {
        return postsList;
    }

    public void setPostsList(ArrayList<Post> postsList) {
        this.postsList = postsList;
    }
}
