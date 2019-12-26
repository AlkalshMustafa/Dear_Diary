package com.example.yanulkadiary.Model;

public class UserInfor {

    public String fullName;
    public String profilPiceUrl;

    public UserInfor(String fullName, String profilPiceUrl) {
        this.fullName = fullName;
        this.profilPiceUrl = profilPiceUrl;
    }

    public UserInfor() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilPiceUrl() {
        return profilPiceUrl;
    }

    public void setProfilPiceUrl(String profilPiceUrl) {
        this.profilPiceUrl = profilPiceUrl;
    }
}
