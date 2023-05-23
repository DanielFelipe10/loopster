package com.example.looptser.posts;

import java.util.ArrayList;

public class Posts {

    private ArrayList<Post> postsList;

    public Posts(ArrayList<Post> postsList) {
        this.postsList = postsList;
    }

    public ArrayList<Post> getPostsList() {
        return postsList;
    }

    public void setPostsList(ArrayList<Post> postsList) {
        this.postsList = postsList;
    }
}
