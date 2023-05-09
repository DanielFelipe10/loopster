package com.example.looptser.users;

public class Users {
    String uid;
    String name;
    String email;
    String imgUriProfile;
    String imgUriBackground;

    public Users() {}

    public Users(String uid, String name, String email, String imgUriProfile, String imgUriBackground) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imgUriProfile = imgUriProfile;
        this.imgUriBackground = imgUriBackground;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUriProfile() {
        return imgUriProfile;
    }

    public void setImgUriProfile(String imgUriProfile) {
        this.imgUriProfile = imgUriProfile;
    }

    public String getImgUriBackground() {
        return imgUriBackground;
    }

    public void setImgUriBackground(String imgUriBackground) {
        this.imgUriBackground = imgUriBackground;
    }
}
