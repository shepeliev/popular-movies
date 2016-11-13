package com.shepeliev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.shepeliev.popularmovies.data.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MovieListFragment.MovieListFragmentListener {

  private boolean mTwoPane = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (findViewById(R.id.movie_details_fragment) != null) {
      mTwoPane = true;
    } else {
      mTwoPane = false;
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      final Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onMoviesLoaded(List<Movie> movies) {
    if (mTwoPane && movies.size() > 0) {
      final FragmentManager fragmentManager = getSupportFragmentManager();
      fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

      fragmentManager
          .beginTransaction()
          .replace(R.id.movie_details_fragment, MovieDetailsFragment.getInstance(movies.get(0).getId()))
          .commit();
    }
  }

  @Override
  public void onMovieClick(Movie movie) {
    if (mTwoPane) {

      getSupportFragmentManager().beginTransaction()
          .replace(R.id.movie_details_fragment, MovieDetailsFragment.getInstance(movie.getId()))
          .addToBackStack(null)
          .commit();
    } else {
      final Intent intent = new Intent(this, MovieDetailsActivity.class);
      intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movie.getId());
      startActivity(intent);
    }
  }
}
