package com.gnm.katalogfilm.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gnm.katalogfilm.database.DatabaseContract;
import com.gnm.katalogfilm.database.FilmHelper;

import static com.gnm.katalogfilm.database.DatabaseContract.AUTHORITY;
import static com.gnm.katalogfilm.database.DatabaseContract.CONTENT_URI;

public class Provider extends ContentProvider {

    private static final int FILM = 100;
    private static final int FILM_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,
                DatabaseContract.FilmColumns.TABLE_NAME, FILM);

        sUriMatcher.addURI(AUTHORITY,
                DatabaseContract.FilmColumns.TABLE_NAME+ "/#",
                FILM_ID);
    }

    private FilmHelper filmHelper;

    @Override
    public boolean onCreate() {
        filmHelper = new FilmHelper(getContext());
        filmHelper.open();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        Log.v("FilmDetail", ""+match);
        Log.v("FilmDetail", ""+uri);
        Log.v("FilmDetail", ""+uri.getLastPathSegment());
        switch(match){
            case FILM:
                cursor = filmHelper.queryProvider();
                break;
            case FILM_ID:
                cursor = filmHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added ;

        switch (sUriMatcher.match(uri)){
            case FILM:
                added = filmHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int filmDeleted;

        Log.v("FilmDetail1", ""+uri);
        int match = sUriMatcher.match(uri);
        Log.v("FilmDetail1", ""+match);
        switch (match) {
            case FILM_ID:
                filmDeleted =  filmHelper.deleteProvider(uri.getLastPathSegment());
                Log.v("FilmDetail1", ""+filmDeleted);
                break;
            default:
                filmDeleted = 0;
                break;
        }

        if (filmDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return filmDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int filmUpdated ;
        switch (sUriMatcher.match(uri)) {
            case FILM_ID:
                filmUpdated =  filmHelper.updateProvider(uri.getLastPathSegment(),values);
                break;
            default:
                filmUpdated = 0;
                break;
        }

        if (filmUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return filmUpdated;
    }
}
