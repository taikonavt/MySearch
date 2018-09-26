package com.example.maxim.mysearch;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.maxim.mysearch";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SEARCH = "search";
    public static final String TAG = "myTag";

    public static final class SearchEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SEARCH)
                .build();
        public static final String TABLE_NAME = "city_list";
        public static final String COLUMN_CITY_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_LON = "lon";
        public static final String COLUMN_LAT = "lat";
    }
}
