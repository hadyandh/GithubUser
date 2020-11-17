package com.hadyan.githubuser.helper;

import android.database.Cursor;

import com.hadyan.githubuser.db.DatabaseContract;
import com.hadyan.githubuser.entity.User;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<User> mapCursorToArrayList(Cursor userCursor) {
        ArrayList<User> usersList = new ArrayList<>();
        while (userCursor.moveToNext()) {
            String username = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME));
            String name = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME));
            String company = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY));
            String location = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION));
            String avatar = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR));
            String followers = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS));
            String following = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING));

            usersList.add(new User(username, avatar, name, company, location, followers, following));
        }
        return usersList;
    }

    public static User mapCursorToObject(Cursor userCursor) {
        userCursor.moveToFirst();
        String username = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME));
        String name = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME));
        String company = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY));
        String location = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION));
        String avatar = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR));
        String followers = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS));
        String following = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING));
        return new User(username, avatar, name, company, location, followers, following);
    }
}
