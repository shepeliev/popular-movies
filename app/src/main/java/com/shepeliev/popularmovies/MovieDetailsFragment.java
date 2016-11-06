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

public class MovieDetailsFragment extends Fragment {

  public static final String EXTRA_MOVIE = "movie";

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

    Intent intent = getActivity().getIntent();
    Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);

    TextView titleTextView = (TextView) rootView.findViewById(R.id.title_text_view);
    titleTextView.setText(movie.getOriginalTitle());

    TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.release_date_text_view);
    releaseDateTextView.setText(movie.getReleaseDate());

    TextView voteTextView = (TextView) rootView.findViewById(R.id.vote_text_view);
    voteTextView.setText(getString(R.string.vote_average, movie.getVoteAverage()));

    TextView overviewTextView = (TextView) rootView.findViewById(R.id.overview_text_view);
    overviewTextView.setText(movie.getOverview());

    ImageView posterImageView = (ImageView) rootView.findViewById(R.id.poster_image_view);
    Picasso
        .with(getActivity())
        .load(MovieDb.IMAGE_BASE_URL + movie.getPosterPath())
        .into(posterImageView);

    return rootView;
  }
}
