package com.shepeliev.popularmovies.moviedb;

import com.shepeliev.popularmovies.BuildConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class MovieDb {

  public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
  private static final String BASE_URL = "https://api.themoviedb.org/3/";
  private final MovieDbApi mMovieDbApi;

  public MovieDb() {
    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build();

    mMovieDbApi = retrofit.create(MovieDbApi.class);
  }

  public void getMovies(Sort sort, final AsyncCallback asyncCallback) {
    Call<MovieResponse> call;
    switch (sort) {
      case TOP_RATED:
        call = mMovieDbApi.getTopRated(BuildConfig.MOVIE_DB_API_KEY);
        break;
      case POPULAR:
        call = mMovieDbApi.getPopular(BuildConfig.MOVIE_DB_API_KEY);
        break;
      default:
        throw new IllegalStateException();
    }


    call.enqueue(new Callback<MovieResponse>() {
      @Override
      public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        if (response.isSuccessful()) {
          asyncCallback.onData(response.body().getResults());
        } else {
          asyncCallback.onError(response.message());
        }
      }

      @Override
      public void onFailure(Call<MovieResponse> call, Throwable t) {
        asyncCallback.onError(t.getMessage());
      }
    });
  }

  public enum Sort {TOP_RATED, POPULAR}

  public interface AsyncCallback {

    void onData(List<Movie> data);

    void onError(String error);
  }
}
