package com.shepeliev.popularmovies.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MovieDbApi {

  @GET("movie/top_rated")
  Call<MovieList> getTopRated(@Query("api_key") String apiKey);

  @GET("movie/popular")
  Call<MovieList> getPopular(@Query("api_key") String apiKey);

  @GET("movie/{id}")
  Call<MovieDetails> getDetails(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/{id}/videos")
  Call<TrailerList> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/{id}/reviews")
  Call<ListResponse<Review>> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
