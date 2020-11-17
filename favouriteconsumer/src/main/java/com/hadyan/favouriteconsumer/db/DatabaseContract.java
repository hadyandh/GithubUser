package com.hadyan.favouriteconsumer.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.hadyan.githubuser";
    private static final String SCHEME = "content";

    public static final String TABLE_NAME_USER = "user";

    public static final class UserColumns implements BaseColumns {
        public static String USERNAME = "username";
        public static String NAME = "name";
        public static String COMPANY = "company";
        public static String LOCATION = "location";
        public static String FOLLOWERS = "followers";
        public static String FOLLOWING = "following";
        public static String AVATAR = "avatar";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME_USER)
                .build();
    }
}
