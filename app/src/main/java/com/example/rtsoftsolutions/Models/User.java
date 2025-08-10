package com.example.rtsoftsolutions.Models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.UserInfo;

public class User implements UserInfo {

    private String user_display_name ;
    private String user_email ;
    private String user_id ;
    private String user_phoneno ;
    private String user_photo_url ;


public User() {
}

public User(String name , String email , String role , String id , String course , String batch , String phoneno , String photo_url) {
         user_display_name = name ;
         user_id = id ;
         user_email = email ;
         user_phoneno = phoneno ;
         user_photo_url = photo_url ;
    }


    // all setters
    public void setUserName(String username) {
    user_display_name = username ;
    }
    public void setUser_id(String userid) {
    user_id = userid ;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return Uri.parse(user_photo_url) ;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return user_display_name ;
    }

    @Nullable
    @Override
    public String getEmail() {
        return user_email ;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
     return user_phoneno;
    }

    @NonNull
    @Override
    public String getProviderId() {
        return "";
    }

    @NonNull
    @Override
    public String getUid() {
        return user_id;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }
}
