package com.gnm.katalogfilm.database;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.gnm.katalogfilm.database.DatabaseContract.FilmColumns.TABLE_NAME;

public class DatabaseContract {

    public static final class FilmColumns implements BaseColumns {

        public static final String TABLE_NAME = "tbl_favorit";
        public static String FILM_ID = "id_film";
        public static String FILM_JUDUL = "judul";
        public static String FILM_IMG = "img";
    }

    public static final String AUTHORITY = "com.gnm.katalogfilm";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();
}
