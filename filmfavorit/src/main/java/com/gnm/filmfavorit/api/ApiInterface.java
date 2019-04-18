package com.gnm.filmfavorit.api;

import com.gnm.filmfavorit.model.DetailFilm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/{id}")
    Call<DetailFilm> getDetailFilm(@Path("id") String id, @Query("api_key") String apiKey);

}