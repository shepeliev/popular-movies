package com.shepeliev.popularmovies.moviedb;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.shepeliev.popularmovies.BuildConfig;
import com.shepeliev.popularmovies.data.DatabaseOpenHelper;
import com.shepeliev.popularmovies.data.MoviesEntry;
import com.shepeliev.popularmovies.data.model.ListResponse;
import com.shepeliev.popularmovies.data.model.Movie;
import com.shepeliev.popularmovies.data.model.MovieStorIOSQLiteDeleteResolver;
import com.shepeliev.popularmovies.data.model.MovieStorIOSQLiteGetResolver;
import com.shepeliev.popularmovies.data.model.MovieStorIOSQLitePutResolver;
import com.shepeliev.popularmovies.data.model.Review;
import com.shepeliev.popularmovies.data.model.Trailer;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class MovieDb {

  private static final String LOG_TAG = MovieDb.class.getSimpleName();
  private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";
  private static final String BASE_URL = "https://api.themoviedb.org/3/";
  private static MovieDb sInstance;
  private final MovieDbApi mMovieDbApi;
  private final StorIOSQLite mStorIOSQLite;

  private MovieDb(Context context) {
    Retrofit retrofit = new Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build();

    mMovieDbApi = retrofit.create(MovieDbApi.class);

    mStorIOSQLite = DefaultStorIOSQLite.builder()
        .sqliteOpenHelper(new DatabaseOpenHelper(context))
        .defaultScheduler(Schedulers.io())
        .addTypeMapping(Movie.class, SQLiteTypeMapping.<Movie>builder()
            .putResolver(new MovieStorIOSQLitePutResolver())
            .getResolver(new MovieStorIOSQLiteGetResolver())
            .deleteResolver(new MovieStorIOSQLiteDeleteResolver())
            .build())
        .build();
  }

  public static MovieDb getInstance(Context context) {
    if (sInstance == null) {
      sInstance = new MovieDb(context);
    }
    return sInstance;
  }

  public Single<List<Movie>> getMovies(Sort sort) {
    switch (sort) {
      case TOP_RATED:
      case POPULAR: {
        final Single<ListResponse<Movie>> single = sort == Sort.TOP_RATED ?
            mMovieDbApi.getTopRatedObservable(BuildConfig.MOVIE_DB_API_KEY) :
            mMovieDbApi.getPopularObservable(BuildConfig.MOVIE_DB_API_KEY);

        return single
            .map(ListResponse::getResults)
            .map(
                list -> {
                  for (Movie movie : list) {
                    movie.setPosterPath(IMAGE_BASE_URL + movie.getPosterPath());
                  }

                  return list;
                })
            .observeOn(AndroidSchedulers.mainThread());
      }

      case FAV:
        return mStorIOSQLite.get()
            .listOfObjects(Movie.class)
            .withQuery(Query.builder().table(MoviesEntry.TABLE).build())
            .prepare()
            .asRxSingle()
            .observeOn(AndroidSchedulers.mainThread());
      default:
        throw new IllegalStateException();
    }
  }

  public Single<Movie> getMovieDetails(int id) {
    return mStorIOSQLite.get()
        .object(Movie.class)
        .withQuery(
            Query.builder()
                .table(MoviesEntry.TABLE)
                .where(MoviesEntry.Columns._ID + "=?")
                .whereArgs(id)
                .build())
        .prepare()
        .asRxSingle()
        .flatMap(movieFromDb -> {
          if (movieFromDb != null) {
            Log.i(LOG_TAG, "Got movie from DB.");
            return Single.just(movieFromDb);
          }

          Log.i(LOG_TAG, "No movie in DB. Fallback to TheMovieDb.org");

          return mMovieDbApi
              .getDetailsObservable(id, BuildConfig.MOVIE_DB_API_KEY)
              .map(movieFromWeb ->
                  movieFromWeb.setPosterPath(IMAGE_BASE_URL + movieFromWeb.getPosterPath()));
        })
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

  public Single<Boolean> isFavorite(int id) {
    return mStorIOSQLite.get()
        .object(Movie.class)
        .withQuery(
            Query.builder()
                .table(MoviesEntry.TABLE)
                .where(MoviesEntry.Columns._ID + "=?")
                .whereArgs(id)
                .build())
        .prepare()
        .asRxSingle()
        .map(movie -> movie != null)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Single<Void> saveMovie(Movie movie) {
    return mStorIOSQLite.put()
        .object(movie)
        .prepare()
        .asRxSingle()
        .doOnError(throwable -> Log.e(LOG_TAG, "Saving " + movie + " failed", throwable))
        .map(putResult -> (Void) null)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Single<Void> deleteMovie(Movie movie) {
    return mStorIOSQLite.delete()
        .object(movie)
        .prepare()
        .asRxSingle()
        .doOnError(throwable -> Log.e(LOG_TAG, "Deleting " + movie + " failed", throwable))
        .map(deleteResult -> (Void) null)
        .observeOn(AndroidSchedulers.mainThread());
  }

  public enum Sort {TOP_RATED, POPULAR, FAV}
}
