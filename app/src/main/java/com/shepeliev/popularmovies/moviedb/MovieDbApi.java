package com.shepeliev.popularmovies.moviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface MovieDbApi {

  @Deprecated
  @GET("movie/top_rated")
  Call<ListResponse<MovieListItem>> getTopRated(@Query("api_key") String apiKey);

  @Deprecated
  @GET("movie/popular")
  Call<ListResponse<MovieListItem>> getPopular(@Query("api_key") String apiKey);

  @Deprecated
  @GET("movie/{id}")
  Call<MovieDetails> getDetails(@Path("id") int id, @Query("api_key") String apiKey);

  @Deprecated
  @GET("movie/{id}/videos")
  Call<ListResponse<Trailer>> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

  @Deprecated
  @GET("movie/{id}/reviews")
  Call<ListResponse<Review>> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

  @GET("movie/top_rated")
  Observable<ListResponse<MovieListItem>> getTopRatedObservable(@Query("api_key") String apiKey);

  @GET("movie/popular")
  Observable<ListResponse<MovieListItem>> getPopularObservable(@Query("api_key") String apiKey);

  @GET("movie/{id}")
  Observable<MovieDetails> getDetailsObservable(@Path("id") int id,
                                                @Query("api_key") String apiKey);

  @GET("movie/{id}/videos")
  Observable<ListResponse<Trailer>> getTrailersObservable(@Path("id") int id,
                                                          @Query("api_key") String apiKey);

  @GET("movie/{id}/reviews")
  Observable<ListResponse<Review>> getReviewsObservable(@Path("id") int id,
                                                        @Query("api_key") String apiKey);
}
