package com.shepeliev.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.shepeliev.popularmovies.moviedb.MovieList;
import com.shepeliev.popularmovies.moviedb.MovieListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends Fragment {

  private final MovieListAdapter mMovieListAdapter = new MovieListAdapter();

  @BindView(R.id.movies_grid_view)
  RecyclerView mMoviesRecyclerView;

  private MovieListFragmentListener mMovieListFragmentListener;
  private List<MovieListItem> mMovies;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_list_movies, container, false);
    ButterKnife.bind(this, rootView);

    mMoviesRecyclerView.setAdapter(mMovieListAdapter);
    mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    mMoviesRecyclerView.setHasFixedSize(true);

    loadMovies();

    return rootView;
  }

  private void loadMovies() {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String sortStr = preferences.getString(getString(R.string.pref_sort_key),
        getString(R.string.pref_top_rated_value));
    MovieDb.Sort sort = MovieDb.Sort.valueOf(sortStr);

    MovieDb.getInstance().getMovies(sort, new ErrorHandledAsyncCallback<MovieList>(getActivity()) {
      @Override
      public void onData(MovieList data) {
        mMovies = data.getResults();
        mMovieListAdapter.notifyDataSetChanged();
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

    void onMovieClick(MovieListItem movieListItem);
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
      final MovieListItem movieListItem = mMovies.get(position);

      holder.itemView.setOnClickListener(v -> {
        if (mMovieListFragmentListener != null) {
          mMovieListFragmentListener.onMovieClick(movieListItem);
        }
      });

      Picasso
          .with(holder.itemView.getContext())
          .load(MovieDb.IMAGE_BASE_URL + movieListItem.getPosterPath())
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
