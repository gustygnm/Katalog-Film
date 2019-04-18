package com.gnm.katalogfilm.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.gnm.katalogfilm.BuildConfig;
import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.adapter.AdapterFilm;
import com.gnm.katalogfilm.api.Api;
import com.gnm.katalogfilm.api.ApiInterface;
import com.gnm.katalogfilm.database.DatabaseContract;
import com.gnm.katalogfilm.model.DetailFilm;
import com.gnm.katalogfilm.model.Favorite;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritActivity extends AppCompatActivity {

    public final static String API_KEY = BuildConfig.TMDB_API_KEY;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AdapterFilm Adapter;
    List<DetailFilm> List;
    ApiInterface Service;
    Call<DetailFilm> FavoriteCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List = new ArrayList<>();
        initView();
        if(savedInstanceState!=null){
            ArrayList<DetailFilm> list;
            list = savedInstanceState.getParcelableArrayList("now_movie");
            Adapter.setDetail(list);
            recyclerView.setAdapter(Adapter);
        }else{
            load();
        }
        loading.hide();
    }

    void load() {
        loading.show();
        ArrayList<Favorite> movieFavoriteArrayList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(DatabaseContract.CONTENT_URI, null,
                null, null, null, null);
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
        for (Favorite mF : movieFavoriteArrayList) {
            getFavoriteMovies(mF.getId());
        }
    }

    void initView() {
        Adapter = new AdapterFilm(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getFavoriteMovies(String id) {
        Service = Api.getApi().create(ApiInterface.class);
        FavoriteCall = Service.getDetailFilm(id, API_KEY);

        FavoriteCall.enqueue(new Callback<DetailFilm>() {
            @Override
            public void onResponse(Call<DetailFilm> call, Response<DetailFilm> response) {
                List.add(response.body());
                Adapter.setDetail(List);
                recyclerView.setAdapter(Adapter);
                loading.hide();
            }

            @Override
            public void onFailure(Call<DetailFilm> call, Throwable t) {
                Toast.makeText(FavoritActivity.this, "Terjadi masalah!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("now_movie", new ArrayList<>(Adapter.getList()));
    }
}
