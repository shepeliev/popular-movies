package com.shepeliev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shepeliev.popularmovies.moviedb.Movie;
import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment {

  public static final String EXTRA_MOVIE = "movie";

  @BindView(R.id.title_text_view)
  TextView titleTextView;

  @BindView(R.id.release_date_text_view)
  TextView releaseDateTextView;

  @BindView(R.id.vote_text_view)
  TextView voteTextView;

  @BindView(R.id.overview_text_view)
  TextView overviewTextView;

  @BindView(R.id.poster_image_view)
  ImageView posterImageView;


  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
    ButterKnife.bind(this, rootView);

    Intent intent = getActivity().getIntent();
    Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);

    titleTextView.setText(movie.getOriginalTitle());
    releaseDateTextView.setText(movie.getReleaseDate());
    voteTextView.setText(getString(R.string.vote_average, movie.getVoteAverage()));
    overviewTextView.setText(movie.getOverview());

    Picasso
        .with(getActivity())
        .load(MovieDb.IMAGE_BASE_URL + movie.getPosterPath())
        .placeholder(R.drawable.poster_placeholder)
        .into(posterImageView);

    return rootView;
  }
}
