package com.shepeliev.popularmovies.data;

import android.provider.BaseColumns;

public final class MoviesEntry {

  public static final String TABLE = "movies";

  public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE + "(" +
      Columns._ID + " INTEGER PRIMARY KEY, " +
      Columns.ORIGINAL_TITLE + " TEXT NOT NULL, " +
      Columns.POSTER_PATH + " TEXT, " +
      Columns.OVERVIEW + " TEXT, " +
      Columns.RELEASE_DATE + " TEXT NOT NULL, " +
      Columns.VOTE_AVERAGE + " REAL NOT NULL, " +
      Columns.RUNTIME + " INTEGER NOT NULL);";

  private MoviesEntry() {
  }

  public interface Columns extends BaseColumns {
    String ORIGINAL_TITLE = "title";
    String POSTER_PATH = "poster_path";
    String OVERVIEW = "overview";
    String RELEASE_DATE = "release_date";
    String VOTE_AVERAGE = "vote_average";
    String RUNTIME = "runtime";
  }
}
