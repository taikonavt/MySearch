package com.example.maxim.mysearch;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    public static final int CODE_SEARCH = 100;
    private DatabaseHelper dbHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private static final String TAG = "myTag";

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        matcher.addURI(authority, Contract.PATH_SEARCH + "/*", CODE_SEARCH);
        return  matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        Log.d(TAG, "quety() " + uri.toString());
        Cursor cursor;
//        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Contract.SearchEntry.TABLE_NAME);

        Log.d(TAG, uri.toString());
        switch (uriMatcher.match(uri)){
            case CODE_SEARCH:{
                String[] args = new String[] {uri.getLastPathSegment() + "*"};
                String sql =
                        "SELECT * FROM " + Contract.SearchEntry.TABLE_NAME +
                        " WHERE " + Contract.SearchEntry.COLUMN_NAME +
                        " MATCH ?" + " LIMIT 3;";
                Log.d(TAG, sql);
//                cursor = db.rawQuery(sql, args);
                String selection = Contract.SearchEntry.COLUMN_NAME + " MATCH ?";
                cursor = builder.query(dbHelper.getReadableDatabase(), null, selection,
                        args, null, null, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
