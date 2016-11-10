package com.shepeliev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.shepeliev.popularmovies.moviedb.MovieDetails;
import com.shepeliev.popularmovies.moviedb.Trailer;
import com.shepeliev.popularmovies.moviedb.TrailerList;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment {

  public static final String EXTRA_MOVIE_ID = "movie_id";
  private static final MovieDb sMovieDb = MovieDb.getInstance();

  @BindView(R.id.progress_bar)
  ProgressBar mProgressBar;

  @BindView(R.id.details_container)
  ScrollView mDetailsContainer;

  @BindView(R.id.title_text_view)
  TextView mTitleTextView;

  @BindView(R.id.release_date_text_view)
  TextView mReleaseDateTextView;

  @BindView(R.id.runtime_text_view)
  TextView mRuntimeTextView;

  @BindView(R.id.vote_text_view)
  TextView mVoteTextView;

  @BindView(R.id.overview_text_view)
  TextView mOverviewTextView;

  @BindView(R.id.poster_image_view)
  ImageView mPosterImageView;

  @BindView(R.id.trailer_list)
  RecyclerView mTrailerList;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
    ButterKnife.bind(this, rootView);

    Intent intent = getActivity().getIntent();
    int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1);
    if (movieId > -1) {
      sMovieDb.getMovieDetails(movieId, new ErrorHandledAsyncCallback<MovieDetails>(getContext()) {
        @Override
        public void onData(MovieDetails data) {
          bindDetails(data);
        }
      });

      sMovieDb.getTrailers(movieId, new ErrorHandledAsyncCallback<TrailerList>(getContext()) {
        @Override
        public void onData(TrailerList data) {
          bindTrailers(data.getResults());
        }
      });
    }

    return rootView;
  }

  private void bindTrailers(List<Trailer> results) {
    mTrailerList.setAdapter(new TrailerListAdapter(results));
  }

  private void bindDetails(MovieDetails movieDetails) {
    mTitleTextView.setText(movieDetails.getOriginalTitle());
    mReleaseDateTextView.setText(movieDetails.getReleaseDate());
    mRuntimeTextView.setText(getString(R.string.runtime, movieDetails.getRuntime()));
    mVoteTextView.setText(getString(R.string.vote_average, movieDetails.getVoteAverage()));
    mOverviewTextView.setText(movieDetails.getOverview());

    mProgressBar.setVisibility(View.GONE);
    mDetailsContainer.setVisibility(View.VISIBLE);

    Picasso
        .with(getActivity())
        .load(MovieDb.IMAGE_BASE_URL + movieDetails.getPosterPath())
        .placeholder(R.drawable.poster_placeholder)
        .into(mPosterImageView);
  }

  class TrailerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.trailer_name_text_view)
    TextView mTrailerName;

    public TrailerViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private class TrailerListAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    private final List<Trailer> mTrailers;

    private TrailerListAdapter(List<Trailer> trailers) {
      mTrailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View rootView =
          LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
      return new TrailerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
      holder.itemView.setOnClickListener(v -> {
        Trailer trailer = mTrailers.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW,
            Uri.parse("https://www.youtube.com/watch?v=" + trailer.getKey()));
        getContext().startActivity(intent);
      });
      holder.mTrailerName.setText(mTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
      return mTrailers.size();
    }
  }
}
