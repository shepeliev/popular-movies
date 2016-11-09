package com.shepeliev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.shepeliev.popularmovies.moviedb.MovieDetails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment {

  public static final String EXTRA_MOVIE_ID = "movie_id";

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

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
    ButterKnife.bind(this, rootView);

    Intent intent = getActivity().getIntent();
    int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1);
    if (movieId > -1) {
      MovieDb.getInstance().getMovieDetails(movieId,
          new ErrorHandledAsyncCallback<MovieDetails>(getActivity()) {
            @Override
            public void onData(MovieDetails data) {
              mTitleTextView.setText(data.getOriginalTitle());
              mReleaseDateTextView.setText(data.getReleaseDate());
              mRuntimeTextView.setText(getString(R.string.runtime, data.getRuntime()));
              mVoteTextView.setText(getString(R.string.vote_average, data.getVoteAverage()));
              mOverviewTextView.setText(data.getOverview());

              mProgressBar.setVisibility(View.GONE);
              mDetailsContainer.setVisibility(View.VISIBLE);

              Picasso
                  .with(getActivity())
                  .load(MovieDb.IMAGE_BASE_URL + data.getPosterPath())
                  .placeholder(R.drawable.poster_placeholder)
                  .into(mPosterImageView);
            }
          });
    }

    return rootView;
  }
}
