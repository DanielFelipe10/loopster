package com.example.looptser.posts;

import java.util.Date;

public class Post {
    private String postImage, postDescription;
    private Date date;

    public Post(String postImage, String postDescription, Date date) {
        this.postImage = postImage;
        this.postDescription = postDescription;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
