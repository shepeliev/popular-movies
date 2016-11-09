package com.shepeliev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.shepeliev.popularmovies.moviedb.MovieListItem;

public class MainActivity extends AppCompatActivity
    implements MovieListFragment.MovieListFragmentListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onMovieClick(MovieListItem movieListItem) {
    Intent intent = new Intent(this, MovieDetailsActivity.class);
    intent.putExtra(MovieDetailsFragment.EXTRA_MOVIE_ID, movieListItem.getId());
    startActivity(intent);
  }
}
