package com.shepeliev.popularmovies.moviedb;

import com.shepeliev.popularmovies.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class MovieDb {

  public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
  private static final String BASE_URL = "https://api.themoviedb.org/3/";
  private static final MovieDb sInstance = new MovieDb();
  private final MovieDbApi mMovieDbApi;

  private MovieDb() {
    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build();

    mMovieDbApi = retrofit.create(MovieDbApi.class);
  }

  public static MovieDb getInstance() {
    return sInstance;
  }

  public void getMovies(Sort sort, AsyncCallback<MovieList> asyncCallback) {
    Call<MovieList> call;
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

    enqueueCall(call, asyncCallback);
  }

  public void getMovieDetails(int id, AsyncCallback<MovieDetails> asyncCallback) {
    enqueueCall(mMovieDbApi.getDetails(id, BuildConfig.MOVIE_DB_API_KEY), asyncCallback);
  }

  private <T> void enqueueCall(Call<T> call, final AsyncCallback<T> asyncCallback) {
    call.enqueue(new Callback<T>() {
      @Override
      public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
          asyncCallback.onData(response.body());
        } else {
          asyncCallback.onError(response.message());
        }
      }

      @Override
      public void onFailure(Call<T> call, Throwable t) {
        asyncCallback.onError(t.getMessage());
      }
    });
  }

  public enum Sort {TOP_RATED, POPULAR}

  public interface AsyncCallback<T> {

    void onData(T data);

    void onError(String error);
  }
}
