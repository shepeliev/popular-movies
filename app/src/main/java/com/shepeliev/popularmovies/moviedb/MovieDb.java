package com.shepeliev.popularmovies.moviedb;

import com.shepeliev.popularmovies.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class MovieDb {

  public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
  private static final String BASE_URL = "https://api.themoviedb.org/3/";
  private static final MovieDb sInstance = new MovieDb();
  private final MovieDbApi mMovieDbApi;

  private MovieDb() {
    Retrofit retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build();

    mMovieDbApi = retrofit.create(MovieDbApi.class);
  }

  public static MovieDb getInstance() {
    return sInstance;
  }

  public Observable<ListResponse<MovieListItem>> getMovies(Sort sort) {
    switch (sort) {
      case TOP_RATED:
        return mMovieDbApi
            .getTopRatedObservable(BuildConfig.MOVIE_DB_API_KEY)
            .observeOn(AndroidSchedulers.mainThread());
      case POPULAR:
        return mMovieDbApi
            .getPopularObservable(BuildConfig.MOVIE_DB_API_KEY)
            .observeOn(AndroidSchedulers.mainThread());
      default:
        throw new IllegalStateException();
    }
  }

  public Observable<MovieDetails> getMovieDetails(int id) {
    return mMovieDbApi
        .getDetailsObservable(id, BuildConfig.MOVIE_DB_API_KEY)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<ListResponse<Trailer>> getTrailers(int id) {
    return mMovieDbApi
        .getTrailersObservable(id, BuildConfig.MOVIE_DB_API_KEY)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<ListResponse<Review>> getReviews(int id) {
    return mMovieDbApi
        .getReviewsObservable(id, BuildConfig.MOVIE_DB_API_KEY)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public enum Sort {TOP_RATED, POPULAR}
}
