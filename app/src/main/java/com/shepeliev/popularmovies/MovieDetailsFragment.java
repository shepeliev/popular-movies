package com.shepeliev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shepeliev.popularmovies.moviedb.ListResponse;
import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.shepeliev.popularmovies.moviedb.MovieDetails;
import com.shepeliev.popularmovies.moviedb.Review;
import com.shepeliev.popularmovies.moviedb.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsFragment extends Fragment {

  public static final String EXTRA_MOVIE_ID = "movie_id";
  private static final MovieDb sMovieDb = MovieDb.getInstance();
  private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

  @BindView(R.id.progress_bar)
  ProgressBar mProgressBar;

  @BindView(R.id.details_container)
  NestedScrollView mDetailsContainer;

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

  @BindView(R.id.trailers_container)
  LinearLayout mTrailersContainer;

  @BindView(R.id.trailer_list)
  RecyclerView mTrailerList;

  @BindView(R.id.reviews_container)
  LinearLayout mReviewsContainer;

  @BindView(R.id.review_list)
  RecyclerView mReviewList;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
    ButterKnife.bind(this, rootView);

    final Intent intent = getActivity().getIntent();
    final int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1);

    if (movieId <= -1) {
      return rootView;
    }

    sMovieDb.getMovieDetails(movieId).subscribe(
        this::bindDetails,
        throwable -> ErrorActions.networkError(getContext(), throwable)
    );
    sMovieDb.getTrailers(movieId).subscribe(
        this::bindTrailers,
        throwable -> ErrorActions.networkError(getContext(), throwable)
    );
    sMovieDb.getReviews(movieId).subscribe(
        this::bindReviews,
        throwable -> ErrorActions.networkError(getContext(), throwable)
    );

    return rootView;
  }

  private void bindReviews(ListResponse<Review> reviewList) {
    final List<Review> reviews = reviewList.getResults();
    if (reviews.size() == 0) {
      return;
    }

    mReviewsContainer.setVisibility(View.VISIBLE);

    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    mReviewList.setAdapter(new ReviewListAdapter(reviews));
    mReviewList.setLayoutManager(layoutManager);
  }

  private void bindTrailers(ListResponse<Trailer> trailerList) {
    final List<Trailer> trailers = trailerList.getResults();
    if (trailers.size() == 0) {
      return;
    }

    mTrailersContainer.setVisibility(View.VISIBLE);

    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    mTrailerList.setAdapter(new TrailerListAdapter(trailers));
    mTrailerList.setLayoutManager(layoutManager);
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

    TrailerViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class ReviewViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.review_author_text_view)
    TextView mAuthorTextView;

    @BindView(R.id.review_content_text_view)
    TextView mContentTextView;

    ReviewViewHolder(View itemView) {
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
      final View rootView =
          LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
      return new TrailerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
      holder.itemView.setOnClickListener(v -> {
        final Trailer trailer = mTrailers.get(position);
        final Intent intent =
            new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + trailer.getKey()));
        getContext().startActivity(intent);
      });
      holder.mTrailerName.setText(mTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
      return mTrailers.size();
    }
  }

  private class ReviewListAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private final List<Review> mReviews;

    private ReviewListAdapter(List<Review> reviews) {
      mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View rootView =
          LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
      return new ReviewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
      holder.mAuthorTextView.setText(mReviews.get(position).getAuthor());
      holder.mContentTextView.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
      return mReviews.size();
    }
  }
}
