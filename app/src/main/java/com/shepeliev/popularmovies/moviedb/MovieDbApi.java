package com.shepeliev.popularmovies.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MovieDbApi {

  @GET("movie/top_rated")
  Call<ListResponse<MovieListItem>> getTopRated(@Query("api_key") String apiKey);

  @GET("movie/popular")
  Call<ListResponse<MovieListItem>> getPopular(@Query("api_key") String apiKey);

  @GET("movie/{id}")
  Call<MovieDetails> getDetails(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/{id}/videos")
  Call<ListResponse<Trailer>> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/{id}/reviews")
  Call<ListResponse<Review>> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
