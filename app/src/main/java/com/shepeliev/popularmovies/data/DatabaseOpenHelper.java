package com.shepeliev.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseOpenHelper extends SQLiteOpenHelper {

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "movies.db";

  public DatabaseOpenHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(MoviesEntry.CREATE_TABLE_SQL);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // migrations
  }
}
