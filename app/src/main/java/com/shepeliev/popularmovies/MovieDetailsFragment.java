package com.shepeliev.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shepeliev.popularmovies.data.model.Movie;
import com.shepeliev.popularmovies.data.model.Review;
import com.shepeliev.popularmovies.data.model.Trailer;
import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class MovieDetailsFragment extends Fragment {

  private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
  private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
  private static final String MOVIE_ID = "movieId";
  private static final String MOVIE_STATE = "movie";
  private static final String TRAILERS_STATE = "trailers";
  private static final String REVIEWS_STATE = "reviews";

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

  @BindView(R.id.favorite_button)
  Button mFavoriteButton;

  private MovieDb mMovieDb;
  private Movie mMovie;
  private List<Trailer> mTrailers;
  private List<Review> mReviews;

  private Subscription mMovieSubscription;
  private Subscription mTrailersSubscription;
  private Subscription mReviewSubscription;

  private MenuItem mShareMenuItem;
  private ShareActionProvider mShareActionProvider;

  public static MovieDetailsFragment getInstance(int movieId) {
    final MovieDetailsFragment fragment = new MovieDetailsFragment();
    final Bundle args = new Bundle();
    args.putInt(MOVIE_ID, movieId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    mMovieDb = MovieDb.getInstance(getContext());
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
    ButterKnife.bind(this, rootView);

    if (savedInstanceState != null) {
      restoreInstanceState(savedInstanceState);
    } else {
      bindViews(getArguments().getInt(MOVIE_ID));
    }

    return rootView;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (mMovie != null) {
      outState.putParcelable(MOVIE_STATE, mMovie);
    }
    if (mTrailers != null) {
      outState.putParcelableArrayList(TRAILERS_STATE, new ArrayList<>(mTrailers));
    }
    if (mReviews != null) {
      outState.putParcelableArrayList(REVIEWS_STATE, new ArrayList<>(mReviews));
    }
  }

  @Override
  public void onDestroyView() {
    if (mMovieSubscription != null) {
      mMovieSubscription.unsubscribe();
    }
    if (mTrailersSubscription != null) {
      mTrailersSubscription.unsubscribe();
    }
    if (mReviewSubscription != null) {
      mReviewSubscription.unsubscribe();
    }
    super.onDestroyView();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.details_fragment_menu, menu);
    mShareMenuItem = menu.findItem(R.id.action_share);
    mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareMenuItem);
    bindShareAction();
  }

  private void restoreInstanceState(Bundle savedInstanceState) {
    if (!isDetached()) {
      bindMovie(savedInstanceState.getParcelable(MOVIE_STATE));
      bindTrailers(savedInstanceState.getParcelableArrayList(TRAILERS_STATE));
      bindReviews(savedInstanceState.getParcelableArrayList(REVIEWS_STATE));
    }
  }

  private void bindViews(int movieId) {
    if (movieId < 0) {
      return;
    }

    mMovieSubscription = mMovieDb.getMovieDetails(movieId).subscribe(
        this::bindMovie,
        throwable -> ErrorActions.networkError(getContext(), throwable)
    );
    mTrailersSubscription = mMovieDb.getTrailers(movieId).subscribe(
        this::bindTrailers,
        throwable -> Log.e(LOG_TAG, "Network error", throwable)
    );
    mReviewSubscription = mMovieDb.getReviews(movieId).subscribe(
        this::bindReviews,
        throwable -> Log.e(LOG_TAG, "Network error", throwable)
    );
  }

  private void bindReviews(List<Review> reviewList) {
    if (reviewList == null) {
      return;
    }

    mReviews = reviewList;
    if (mReviews.size() == 0) {
      return;
    }

    mReviewsContainer.setVisibility(View.VISIBLE);

    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    mReviewList.setAdapter(new ReviewListAdapter(mReviews));
    mReviewList.setLayoutManager(layoutManager);
  }

  private void bindTrailers(List<Trailer> trailerList) {
    if (mShareMenuItem != null) {
      mShareMenuItem.setVisible(false);
    }

    if (trailerList == null) {
      return;
    }

    mTrailers = trailerList;
    if (mTrailers.size() == 0) {
      return;
    }

    mTrailersContainer.setVisibility(View.VISIBLE);

    final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setAutoMeasureEnabled(true);
    mTrailerList.setAdapter(new TrailerListAdapter(mTrailers));
    mTrailerList.setLayoutManager(layoutManager);

    bindShareAction();
  }

  private void bindShareAction() {
    if (mMovie == null ||
        mTrailers == null ||
        mShareMenuItem == null ||
        mShareActionProvider == null ||
        mTrailers.size() == 0) {
      return;
    }

    final String shareText = getContext().getString(R.string.share_text, mMovie.getOriginalTitle(),
        YOUTUBE_URL + mTrailers.get(0).getKey());
    final Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, shareText);
    intent.setType("text/plain");
    mShareActionProvider.setShareIntent(intent);
    mShareMenuItem.setVisible(true);
  }

  private void bindMovie(Movie movie) {
    if (movie == null) {
      return;
    }

    mMovie = movie;
    mTitleTextView.setText(mMovie.getOriginalTitle());
    mReleaseDateTextView.setText(mMovie.getReleaseDate());
    mRuntimeTextView.setText(getString(R.string.runtime, mMovie.getRuntime()));
    mVoteTextView.setText(getString(R.string.vote_average, mMovie.getVoteAverage()));
    mOverviewTextView.setText(mMovie.getOverview());

    mProgressBar.setVisibility(View.GONE);
    mDetailsContainer.setVisibility(View.VISIBLE);

    Picasso
        .with(getActivity())
        .load(mMovie.getPosterPath())
        .placeholder(R.drawable.poster_placeholder)
        .into(mPosterImageView);

    mMovieDb
        .isFavorite(mMovie.getId())
        .subscribe(isFavorite -> bindFavoriteButton(mMovie, isFavorite));

    bindShareAction();
  }

  private void bindFavoriteButton(Movie movie, boolean isFavorite) {
    final View.OnClickListener addToFavorites = view ->
        mMovieDb.saveMovie(movie).subscribe(v -> bindFavoriteButton(movie, true));

    final View.OnClickListener removeFromFavorites = view ->
        mMovieDb.deleteMovie(movie).subscribe(v -> bindFavoriteButton(movie, false));

    final String buttonText = isFavorite ?
        getContext().getString(R.string.remove_from_favorites) :
        getContext().getString(R.string.add_to_favorites);
    mFavoriteButton.setText(buttonText);
    mFavoriteButton.setOnClickListener(isFavorite ? removeFromFavorites : addToFavorites);
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
