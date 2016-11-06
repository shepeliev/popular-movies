package com.shepeliev.popularmovies.moviedb;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public final class Movie implements Parcelable {

  public static final Creator<Movie> CREATOR = new Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel in) {
      return new Movie(in);
    }

    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };

  @SerializedName("original_title")
  private String mOriginalTitle;

  @SerializedName("poster_path")
  private String mPosterPath;

  @SerializedName("overview")
  private String mOverview;

  @SerializedName("release_date")
  private String mReleaseDate;

  @SerializedName("vote_average")
  private double mVoteAverage;

  private Movie(Parcel parcel) {
    mOriginalTitle = parcel.readString();
    mPosterPath = parcel.readString();
    mOverview = parcel.readString();
    mReleaseDate = parcel.readString();
    mVoteAverage = parcel.readDouble();
  }

  public String getOriginalTitle() {
    return mOriginalTitle;
  }

  public String getPosterPath() {
    return mPosterPath;
  }

  public String getOverview() {
    return mOverview;
  }

  public String getReleaseDate() {
    return mReleaseDate;
  }

  public double getVoteAverage() {
    return mVoteAverage;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mOriginalTitle);
    dest.writeString(mPosterPath);
    dest.writeString(mOverview);
    dest.writeString(mReleaseDate);
    dest.writeDouble(mVoteAverage);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Movie movie = (Movie) o;

    if (Double.compare(movie.mVoteAverage, mVoteAverage) != 0) return false;
    if (mOriginalTitle != null ?
        !mOriginalTitle.equals(movie.mOriginalTitle) : movie.mOriginalTitle != null)
      return false;
    if (mPosterPath != null ? !mPosterPath.equals(movie.mPosterPath) : movie.mPosterPath != null)
      return false;
    if (mOverview != null ? !mOverview.equals(movie.mOverview) : movie.mOverview != null)
      return false;
    return mReleaseDate != null ?
        mReleaseDate.equals(movie.mReleaseDate) : movie.mReleaseDate == null;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = mOriginalTitle != null ? mOriginalTitle.hashCode() : 0;
    result = 31 * result + (mPosterPath != null ? mPosterPath.hashCode() : 0);
    result = 31 * result + (mOverview != null ? mOverview.hashCode() : 0);
    result = 31 * result + (mReleaseDate != null ? mReleaseDate.hashCode() : 0);
    temp = Double.doubleToLongBits(mVoteAverage);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Movie{");
    sb.append("mOriginalTitle='").append(mOriginalTitle).append('\'');
    sb.append(", mPosterPath='").append(mPosterPath).append('\'');
    sb.append(", mOverview='").append(mOverview).append('\'');
    sb.append(", mReleaseDate='").append(mReleaseDate).append('\'');
    sb.append(", mVoteAverage=").append(mVoteAverage);
    sb.append('}');
    return sb.toString();
  }
}
