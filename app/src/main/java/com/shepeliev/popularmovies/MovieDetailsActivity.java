package com.shepeliev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailsActivity extends AppCompatActivity {

  public static final String EXTRA_MOVIE_ID = "movie_id";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_details);

    if (savedInstanceState == null) {
      final Intent intent = getIntent();
      final int movieId = intent != null ? intent.getIntExtra(EXTRA_MOVIE_ID, -1) : -1;

      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.movie_details_fragment, MovieDetailsFragment.getInstance(movieId))
          .commit();
    }
  }
}
