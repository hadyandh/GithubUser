package com.hadyan.githubuser.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

public class User implements Parcelable {
    private String username, avatar, name, company, location, followers, following;

    public User(){
    }

    public User(String username, String avatar, String name, String company, String location, String followers, String following) {
        this.username = username;
        this.avatar = avatar;
        this.name = name;
        this.company = company;
        this.location = location;
        this.followers = followers;
        this.following = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public User(JSONObject object) {
        try {
            this.username = object.getString("login");
            this.avatar = object.getString("avatar_url");
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Error Data", e.getMessage());
        }
    }

    protected User(Parcel in) {
        username = in.readString();
        avatar = in.readString();
        name = in.readString();
        company = in.readString();
        location = in.readString();
        followers = in.readString();
        following = in.readString();
//        following = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(avatar);
        parcel.writeString(name);
        parcel.writeString(company);
        parcel.writeString(location);
        parcel.writeString(followers);
        parcel.writeString(following);
//        parcel.writeInt(following);
    }
}
