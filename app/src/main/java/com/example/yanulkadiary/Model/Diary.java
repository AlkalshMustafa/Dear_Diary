package com.example.yanulkadiary.Model;

public class Diary {
    public String userName;
    public String fullUserName;
    public String profileImg;
    public String image;
    public String title;
    public String desc;
    public String timestamp;
    public String userId;

    public Diary(String userName, String fullUserName, String profileImg, String image, String title, String desc, String timestamp, String userId) {
        this.userName = userName;
        this.fullUserName = fullUserName;
        this.profileImg = profileImg;
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public Diary(String title, String desc, String image, String timestamp, String userId) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public Diary(String title, String desc, String image, String timestamp, String userId, String profileImg, String userName) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.timestamp = timestamp;
        this.userId = userId;
        this.profileImg = profileImg;
        this.userName = userName;
    }

    public Diary() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFullUserName() {
        return fullUserName;
    }

    public void setFullUserName(String fullUserName) {
        this.fullUserName = fullUserName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
