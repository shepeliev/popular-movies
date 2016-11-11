package com.shepeliev.popularmovies;

import android.content.Context;
import android.widget.Toast;

public final class ErrorActions {

  private ErrorActions() {
  }

  public static void networkError(Context context, Throwable throwable) {
    Toast.makeText(context, context.getString(R.string.network_error, throwable.getMessage()),
        Toast.LENGTH_SHORT).show();
  }
}
