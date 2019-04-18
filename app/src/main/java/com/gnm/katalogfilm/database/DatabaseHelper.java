package com.gnm.katalogfilm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db_katalogfilm";

    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FILM = String.format("CREATE TABLE " + DatabaseContract.FilmColumns.TABLE_NAME
            + " (" + DatabaseContract.FilmColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.FilmColumns.FILM_ID + " TEXT NOT NULL, " + DatabaseContract.FilmColumns.FILM_JUDUL + " TEXT NOT NULL, " +
            DatabaseContract.FilmColumns.FILM_IMG + "  TEXT NOT NULL)"
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FILM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FilmColumns.TABLE_NAME);
        onCreate(db);
    }
}
