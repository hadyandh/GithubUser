package com.hadyan.githubuser.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.hadyan.githubuser.db.DatabaseContract.TABLE_NAME_USER;

public class UserHelper {
    private static final String DATABASE_TABLE = TABLE_NAME_USER;
    private static DatabaseHelper dbHelper;
    private static UserHelper INSTANCE;
    private static SQLiteDatabase database;

    private UserHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static UserHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryByUsername(String username){
        return database.query(DATABASE_TABLE, null
                , "USERNAME" + " = ?"
                , new String[]{username}
                , null
                , null
                , null
                , null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteByUsername(String username) {
        return database.delete(DATABASE_TABLE, "USERNAME" + " = ?", new String[]{username});
    }
}
