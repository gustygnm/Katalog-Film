package com.gnm.katalogfilm.api;

import com.gnm.katalogfilm.model.DetailFilm;
import com.gnm.katalogfilm.model.Film;
import com.gnm.katalogfilm.model.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/{id}")
    Call<DetailFilm> getDetailFilm(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("search/movie/")
    Call<Search> getSearch(@Query("api_key") String apiKey, @Query("query") String q);

    @GET("movie/now_playing")
    Call<Film> getNow(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<Film> getUpComing(@Query("api_key") String apiKey);

}