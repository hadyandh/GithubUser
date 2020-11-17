package com.hadyan.githubuser.provider;

import static com.hadyan.githubuser.db.DatabaseContract.AUTHORITY;
import static com.hadyan.githubuser.db.DatabaseContract.TABLE_NAME_USER;
import static com.hadyan.githubuser.db.DatabaseContract.UserColumns.CONTENT_URI;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.hadyan.githubuser.db.UserHelper;

public class GithubUserProvider extends ContentProvider {
    private static final int USER = 1;
    private static final int USER_ID = 2;
    private UserHelper userHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME_USER, USER);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME_USER + "/*", USER_ID);
    }

    public GithubUserProvider() {
    }

    @Override
    public boolean onCreate() {
        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case USER:
                cursor = userHelper.queryAll();
                break;
            case USER_ID:
                cursor = userHelper.queryByUsername(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case USER:
                added = userHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;

        switch (sUriMatcher.match(uri)) {
            case USER_ID:
                Log.d("ID_URI_DELETE", String.valueOf(USER_ID));
                deleted = userHelper.deleteByUsername(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return deleted;
    }
}
