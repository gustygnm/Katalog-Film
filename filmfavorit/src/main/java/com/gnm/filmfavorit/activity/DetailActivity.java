package com.gnm.filmfavorit.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gnm.filmfavorit.BuildConfig;
import com.gnm.filmfavorit.R;
import com.gnm.filmfavorit.api.Api;
import com.gnm.filmfavorit.api.ApiInterface;
import com.gnm.filmfavorit.database.DatabaseContract;
import com.gnm.filmfavorit.model.DetailFilm;
import com.gnm.filmfavorit.util.DateFormat;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public final static String API_KEY = BuildConfig.TMDB_API_KEY;
    public final static String BASE_POSTER_URL = BuildConfig.URL_IMAGE;
    public final static String BASE_BACKDROP_URL = BuildConfig.URL_IMAGE;

    ApiInterface Service;
    Call<DetailFilm> Call;

    @BindView(R.id.txtOverview)
    TextView txtOverview;

    @BindView(R.id.imgBg)
    ImageView imgBackground;

    @BindView(R.id.txtReleaseDate)
    TextView txtDate;

    @BindView(R.id.txtJudulD)
    TextView txtJudul;

    @BindView(R.id.txtTagline)
    TextView txtTagline;

    @BindView(R.id.imgFoto)
    ImageView imgCover;

    @BindView(R.id.txtRating)
    TextView txtRating;

    @BindView(R.id.txtDuration)
    TextView txtDuration;

    @BindView(R.id.txtLanguage)
    TextView txtLanguage;

    @BindView(R.id.scTampilan)
    ScrollView scTampilan;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.root_parent)
    CoordinatorLayout root_parent;

    String id_film;
    Integer id_favorit;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loading.show();
        id_film = getIntent().getStringExtra("id");
        getDetail(id_film);
    }

    private void getDetail(final String q) {
        Service = Api.getApi().create(ApiInterface.class);
        Call = Service.getDetailFilm(q, API_KEY);

        Call.enqueue(new Callback<DetailFilm>() {
            @Override
            public void onResponse(Call<DetailFilm> call, Response<DetailFilm> response) {
                scTampilan.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(BASE_POSTER_URL + response.body().getmPosterPath())
                        .placeholder(R.drawable.no_image2)
                        .error(R.drawable.no_image2)
                        .into(imgCover);

                Picasso.get()
                        .load(BASE_BACKDROP_URL + response.body().getmBackdropPath())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(imgBackground);

                txtJudul.setText(response.body().getmTitle());
                txtOverview.setText(response.body().getmOverview());
                txtDate.setText(DateFormat.getDateDay(response.body().getmReleaseDate()));
                txtRating.setText(response.body().getmVoteAverage().toString());
                txtDuration.setText(String.valueOf(response.body().getmRuntime()));
                txtLanguage.setText(response.body().getmOriginalLanguage().toUpperCase());
                txtTagline.setText(response.body().getmTagline());

                id_favorit = response.body().getmId();
                loading.hide();
            }

            @Override
            public void onFailure(Call<DetailFilm> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Terjadi masalah!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorit_menu, menu);
        this.menu = menu;
        if (isRecordExists(id_film)) {
            if (this.menu != null) {
                this.menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bookmark));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        } else if (id == R.id.menu_favorit) {
            if (!isRecordExists(id_film)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContract.FilmColumns.FILM_ID, id_film);
                getContentResolver().insert(DatabaseContract.CONTENT_URI, contentValues);
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bookmark));
                Snackbar snackbar = Snackbar.make(root_parent, R.string.add_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.greenThema));
                snackbar.show();
            } else {
                Uri uri = DatabaseContract.CONTENT_URI;
                uri = uri.buildUpon().appendPath(id_film).build();
                getContentResolver().delete(uri, null, null);
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border));
                Snackbar snackbar = Snackbar.make(root_parent, R.string.remove_favorite, Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.redThema));
                snackbar.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isRecordExists(String id) {
        String selection = " id_film = ?";
        String[] selectionArgs = {id};
        String[] projection = {DatabaseContract.FilmColumns.FILM_ID};
        Uri uri = DatabaseContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = getContentResolver().query(uri, projection,
                selection, selectionArgs, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
