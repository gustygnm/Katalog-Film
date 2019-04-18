package com.gnm.filmfavorit.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gnm.filmfavorit.BuildConfig;
import com.gnm.filmfavorit.R;
import com.gnm.filmfavorit.adapter.AdapterFilm;
import com.gnm.filmfavorit.api.Api;
import com.gnm.filmfavorit.api.ApiInterface;
import com.gnm.filmfavorit.database.DatabaseContract;
import com.gnm.filmfavorit.model.DetailFilm;
import com.gnm.filmfavorit.model.Favorite;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<Cursor> {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    Call<DetailFilm> callFilm;
    AdapterFilm adapter;
    ArrayList<DetailFilm> arrayFilm;
    ArrayList<Favorite> arrayFavorit;
    ApiInterface apiInterface;

    private final int FILM_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        getSupportLoaderManager().initLoader(FILM_ID, null, this);

        loading.hide();
    }

    void initView() {
        adapter = new AdapterFilm(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        arrayFavorit = new ArrayList<>();
        arrayFilm = new ArrayList<>();
        return new CursorLoader(this, DatabaseContract.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        arrayFavorit = getItem(data);
        for (Favorite m : arrayFavorit) {
            getFavoriteMovies(m.getId());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        arrayFavorit = getItem(null);
    }

    private ArrayList<Favorite> getItem(Cursor cursor) {
        ArrayList<Favorite> movieFavoriteArrayList = new ArrayList<>();
        cursor.moveToFirst();
        Favorite favorite;
        if (cursor.getCount() > 0) {
            do {
                favorite = new Favorite(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseContract.FilmColumns.FILM_ID)));
                movieFavoriteArrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        return movieFavoriteArrayList;
    }

    private void getFavoriteMovies(String id) {
        apiInterface = Api.getApi().create(ApiInterface.class);
        callFilm = apiInterface.getDetailFilm(id, BuildConfig.TMDB_API_KEY);
        callFilm.enqueue(new Callback<DetailFilm>() {
            @Override
            public void onResponse(Call<DetailFilm> call, Response<DetailFilm> response) {
                arrayFilm.add(response.body());
                adapter.setDetail(arrayFilm);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<DetailFilm> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Terjadi masalah!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if (arrayFilm != null) {
            arrayFilm.clear();
            adapter.setDetail(arrayFilm);
            recyclerView.setAdapter(adapter);
        }
//        getSupportLoaderManager().restartLoader(FILM_ID, null, this);
        super.onResume();
    }

    boolean doubleBackToExit = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity();
        } else {
            Toast.makeText(this, getString(R.string.message_exit), Toast.LENGTH_SHORT).show();
        }
        int timee = 2000;
        this.doubleBackToExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, timee);
    }
}
