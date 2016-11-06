package com.shepeliev.popularmovies.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MovieDbApi {

  @GET("movie/top_rated")
  Call<MovieResponse> getTopRated(@Query("api_key") String apiKey);

  @GET("movie/popular")
  Call<MovieResponse> getPopular(@Query("api_key") String apiKey);
}
