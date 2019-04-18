package com.gnm.katalogfilm.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gnm.katalogfilm.BuildConfig;
import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.adapter.AdapterFilm;
import com.gnm.katalogfilm.api.Api;
import com.gnm.katalogfilm.api.ApiInterface;
import com.gnm.katalogfilm.model.DetailFilm;
import com.gnm.katalogfilm.model.Search;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    public final static String API_KEY = BuildConfig.TMDB_API_KEY;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.cariDulu)
    LinearLayout cariDulu;

    @BindView(R.id.swipeRefresh)
    WaveSwipeRefreshLayout swipeRefresh;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.txtSearch)
    EditText txtSearch;

    @BindView(R.id.btnCari)
    Button btnCari;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AdapterFilm Adapter;
    List<DetailFilm> List;
    ApiInterface Service;
    Call<Search> Call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List = new ArrayList<>();
        initView();
        loading.hide();
        cariDulu.setVisibility(View.VISIBLE);

        swipeRefresh.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefresh.setWaveColor(Color.argb(255, 180, 5, 5));
        swipeRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
                hideKeyboard(SearchActivity.this);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void load() {
        loading.show();
        String q = txtSearch.getText().toString();
        if (q.equals(null) || q.trim().equals("")) {
            Toast.makeText(this, getString(R.string.stNull), Toast.LENGTH_LONG).show();
            loading.hide();
        } else {
            getSearch(q);
        }
        swipeRefresh.setRefreshing(false);
    }

    void initView() {
        Adapter = new AdapterFilm(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getSearch(final String q) {
        Service = Api.getApi().create(ApiInterface.class);
        Call = Service.getSearch(API_KEY, q);

        Call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                List = response.body().getmResults();
                Adapter.setDetail(List);
                recyclerView.setAdapter(Adapter);
                loading.hide();
                cariDulu.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Toast.makeText(SearchActivity.this, getString(R.string.stError), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
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
}
