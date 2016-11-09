package com.shepeliev.popularmovies;

import android.content.Context;
import android.widget.Toast;

import com.shepeliev.popularmovies.moviedb.MovieDb;

public abstract class ErrorHandledAsyncCallback<T> implements MovieDb.AsyncCallback<T> {

  private final Context mContext;

  protected ErrorHandledAsyncCallback(Context context) {
    mContext = context;
  }

  @Override
  public void onError(String error) {
    Toast.makeText(mContext, mContext.getString(R.string.network_error, error),
        Toast.LENGTH_SHORT).show();
  }
}
