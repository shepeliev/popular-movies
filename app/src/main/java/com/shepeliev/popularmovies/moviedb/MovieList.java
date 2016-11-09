package com.shepeliev.popularmovies.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class MovieList {

  @SerializedName("page")
  private int mPage;

  @SerializedName("results")
  private List<MovieListItem> mResults;

  @SerializedName("total_results")
  private int mTotalResults;

  @SerializedName("total_pages")
  private int mTotalPages;

  public int getPage() {
    return mPage;
  }

  public List<MovieListItem> getResults() {
    return mResults;
  }

  public int getTotalResults() {
    return mTotalResults;
  }

  public int getTotalPages() {
    return mTotalPages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MovieList that = (MovieList) o;

    if (mPage != that.mPage) return false;
    if (mTotalResults != that.mTotalResults) return false;
    if (mTotalPages != that.mTotalPages) return false;
    return mResults != null ? mResults.equals(that.mResults) : that.mResults == null;

  }

  @Override
  public int hashCode() {
    int result = mPage;
    result = 31 * result + (mResults != null ? mResults.hashCode() : 0);
    result = 31 * result + mTotalResults;
    result = 31 * result + mTotalPages;
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MovieResponse{");
    sb.append("mPage=").append(mPage);
    sb.append(", mResults=").append(mResults);
    sb.append(", mTotalResults=").append(mTotalResults);
    sb.append(", mTotalPages=").append(mTotalPages);
    sb.append('}');
    return sb.toString();
  }
}
