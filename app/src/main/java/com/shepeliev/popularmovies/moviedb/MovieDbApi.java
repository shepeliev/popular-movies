package com.shepeliev.popularmovies.moviedb;

import com.shepeliev.popularmovies.data.model.ListResponse;
import com.shepeliev.popularmovies.data.model.Movie;
import com.shepeliev.popularmovies.data.model.Review;
import com.shepeliev.popularmovies.data.model.Trailer;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Single;

interface MovieDbApi {

  @GET("movie/top_rated")
  Single<ListResponse<Movie>> getTopRatedObservable(@Query("api_key") String apiKey);

  @GET("movie/popular")
  Single<ListResponse<Movie>> getPopularObservable(@Query("api_key") String apiKey);

  @GET("movie/{id}")
  Single<Movie> getDetailsObservable(@Path("id") int id,
                                     @Query("api_key") String apiKey);

  @GET("movie/{id}/videos")
  Single<ListResponse<Trailer>> getTrailersObservable(@Path("id") int id,
                                                      @Query("api_key") String apiKey);

  @GET("movie/{id}/reviews")
  Single<ListResponse<Review>> getReviewsObservable(@Path("id") int id,
                                                    @Query("api_key") String apiKey);
}
