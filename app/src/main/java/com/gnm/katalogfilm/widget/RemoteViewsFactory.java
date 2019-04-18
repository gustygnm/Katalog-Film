package com.gnm.katalogfilm.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gnm.katalogfilm.BuildConfig;
import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.database.DatabaseContract;
import com.gnm.katalogfilm.model.Favorite;

import java.util.concurrent.ExecutionException;

public class RemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private final static String BASE_POSTER_URL = BuildConfig.URL_IMAGE;
    int appWidgetId;
    private Cursor cursor;

    public RemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private Favorite getFav(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }
        return new Favorite(cursor.getString(cursor.getColumnIndexOrThrow(
                DatabaseContract.FilmColumns.FILM_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.FILM_IMG)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.FILM_JUDUL)));
    }

    @Override
    public void onCreate() {
        cursor = context.getContentResolver().query(
                DatabaseContract.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(
                DatabaseContract.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Favorite fav = getFav(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.film_widget_item);

        Log.d("Widgetku", "Widgetku : " + fav.getJudul());

        try {
            Bitmap preview = Glide.with(context)
                    .asBitmap()
                    .load(BASE_POSTER_URL + fav.getImg())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.img_widget, preview);
            rv.setTextViewText(R.id.tv_movie_title, fav.getJudul());
            Log.d("Berhasil ==============", fav.getImg());
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Widget Load Error", "error");
        }
        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.img_widget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
