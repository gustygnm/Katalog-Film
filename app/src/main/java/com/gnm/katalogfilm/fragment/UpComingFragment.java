package com.gnm.katalogfilm.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gnm.katalogfilm.BuildConfig;
import com.gnm.katalogfilm.R;
import com.gnm.katalogfilm.activity.SearchActivity;
import com.gnm.katalogfilm.adapter.AdapterFilm;
import com.gnm.katalogfilm.api.Api;
import com.gnm.katalogfilm.api.ApiInterface;
import com.gnm.katalogfilm.model.DetailFilm;
import com.gnm.katalogfilm.model.Film;
import com.gnm.katalogfilm.model.Search;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpComingFragment extends Fragment {
    public final static String API_KEY =BuildConfig.TMDB_API_KEY;

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    @BindView(R.id.swipeRefresh)
    WaveSwipeRefreshLayout swipeRefresh;

    AdapterFilm Adapter;
    java.util.List<DetailFilm> List;
    ApiInterface Service;
    Call<Film> Call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_film, container, false);
        ButterKnife.bind(this, view);

        initView();
        if(savedInstanceState!=null){
            ArrayList<DetailFilm> list;
            list = savedInstanceState.getParcelableArrayList("now_movie");
            Adapter.setDetail(list);
            recyclerView.setAdapter(Adapter);
            loading.hide();
        }else{
            getFilm();
        }
        swipeRefresh.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefresh.setWaveColor(Color.argb(255, 180, 5, 5));
        swipeRefresh.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFilm();
            }

        });
        return view;
    }

    void initView() {
        Adapter = new AdapterFilm(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getFilm() {
        loading.show();
        Service = Api.getApi().create(ApiInterface.class);
        Call = Service.getUpComing(API_KEY);

        Call.enqueue(new Callback<Film>() {
            @Override
            public void onResponse(Call<Film> call, Response<Film> response) {
                if (response.body() != null) {
                    List = response.body().getmFilmResults();
                }
                Adapter.setDetail(List);
                recyclerView.setAdapter(Adapter);
                swipeRefresh.setRefreshing(false);
                loading.hide();
            }

            @Override
            public void onFailure(Call<Film> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.stError), Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
                loading.hide();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("now_movie", new ArrayList<>(Adapter.getList()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<DetailFilm> list;
            list = savedInstanceState.getParcelableArrayList("now_movie");
            Adapter.setDetail(list);
            recyclerView.setAdapter(Adapter);
        }
    }
}
