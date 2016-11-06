package com.shepeliev.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.shepeliev.popularmovies.moviedb.Movie;
import com.shepeliev.popularmovies.moviedb.MovieDb;

import java.util.List;

public class MovieListFragment extends Fragment {

  private final MovieDb mMovieDb = new MovieDb();
  private MovieListFragmentListener mListener;
  private MovieListAdapter mMovieListAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_list_movies, container, false);

    mMovieListAdapter = new MovieListAdapter(getActivity());
    GridView moviesGrid = (GridView) rootView.findViewById(R.id.movies_grid_view);
    moviesGrid.setAdapter(mMovieListAdapter);
    moviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
          mListener.onMovieClick(mMovieListAdapter.getItem(position));
        }
      }
    });

    loadMovies();

    return rootView;
  }

  private void loadMovies() {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String sortStr = preferences.getString(getString(R.string.pref_sort_key),
        getString(R.string.pref_top_rated_value));
    MovieDb.Sort sort = MovieDb.Sort.valueOf(sortStr);

    mMovieDb.getMovies(sort, new MovieDb.AsyncCallback() {
      @Override
      public void onData(List<Movie> data) {
        mMovieListAdapter.clear();
        for (Movie movie : data) {
          mMovieListAdapter.add(movie);
        }
      }

      @Override
      public void onError(String error) {
        Toast.makeText(getActivity(), getString(R.string.network_error, error),
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    loadMovies();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof MovieListFragmentListener) {
      mListener = (MovieListFragmentListener) context;
    } else {
      throw new IllegalArgumentException("Context should implement " +
          MovieListFragmentListener.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    mListener = null;
    super.onDetach();
  }

  public interface MovieListFragmentListener {

    void onMovieClick(Movie movie);
  }

}
