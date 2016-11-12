package com.shepeliev.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shepeliev.popularmovies.data.model.Movie;
import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends Fragment {

  private static final String MOVIES_RECYCLER_VIEW_STATE = "moviesRecyclerViewState";
  private static final String MOVIE_LIST_STATE = "moviesListState";
  private final MovieListAdapter mMovieListAdapter = new MovieListAdapter();

  @BindView(R.id.movies_grid_view)
  RecyclerView mMoviesRecyclerView;

  private MovieListFragmentListener mMovieListFragmentListener;
  private List<Movie> mMovies;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_list_movies, container, false);
    ButterKnife.bind(this, rootView);

    mMoviesRecyclerView.setAdapter(mMovieListAdapter);
    final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    mMoviesRecyclerView.setLayoutManager(layoutManager);
    mMoviesRecyclerView.setHasFixedSize(true);

    if (savedInstanceState != null) {
      mMovies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE);
      layoutManager
          .onRestoreInstanceState(savedInstanceState.getParcelable(MOVIES_RECYCLER_VIEW_STATE));
    }

    return rootView;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    Parcelable moviesRecyclerViewState =
        mMoviesRecyclerView.getLayoutManager().onSaveInstanceState();
    outState.putParcelable(MOVIES_RECYCLER_VIEW_STATE, moviesRecyclerViewState);
    if (mMovies != null) {
      outState.putParcelableArrayList(MOVIE_LIST_STATE, new ArrayList<>(mMovies));
    }
  }


  private void loadMovies() {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String sortStr = preferences.getString(getString(R.string.pref_sort_key),
        getString(R.string.pref_top_rated_value));
    MovieDb.Sort sort = MovieDb.Sort.valueOf(sortStr);

    MovieDb.getInstance(getContext())
        .getMovies(sort)
        .subscribe(

            movieList -> {
              mMovies = movieList;
              mMovieListAdapter.notifyDataSetChanged();
              mMovieListFragmentListener.onMoviesLoaded(mMovies);
            },

            throwable -> ErrorActions.networkError(getContext(), throwable)
        );
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mMovies == null) {
      loadMovies();
    }
  }

  @Override
  public void onStop() {
    mMovies = null;
    super.onStop();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof MovieListFragmentListener) {
      mMovieListFragmentListener = (MovieListFragmentListener) context;
    } else {
      throw new IllegalArgumentException("Context should implement " +
          MovieListFragmentListener.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    mMovieListFragmentListener = null;
    super.onDetach();
  }

  public interface MovieListFragmentListener {

    void onMoviesLoaded(List<Movie> movies);

    void onMovieClick(Movie movie);

  }

  class MovieListAdapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public MovieListFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      ImageView view = (ImageView) LayoutInflater
          .from(parent.getContext())
          .inflate(R.layout.movie_list_item, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final Movie movie = mMovies.get(position);

      holder.itemView.setOnClickListener(v -> {
        if (mMovieListFragmentListener != null) {
          mMovieListFragmentListener.onMovieClick(movie);
        }
      });

      Picasso
          .with(holder.itemView.getContext())
          .load(movie.getPosterPath())
          .placeholder(R.drawable.poster_placeholder)
          .into((ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
      return mMovies != null ? mMovies.size() : 0;
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
      super(itemView);
    }
  }

}
