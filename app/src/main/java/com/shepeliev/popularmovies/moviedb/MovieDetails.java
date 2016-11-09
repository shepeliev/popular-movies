package com.shepeliev.popularmovies.moviedb;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetails implements Parcelable {

  public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
    @Override
    public MovieDetails createFromParcel(Parcel in) {
      return new MovieDetails(in);
    }

    @Override
    public MovieDetails[] newArray(int size) {
      return new MovieDetails[size];
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

  @SerializedName("runtime")
  private int mRuntime;

  private MovieDetails(Parcel in) {
    mOriginalTitle = in.readString();
    mPosterPath = in.readString();
    mOverview = in.readString();
    mReleaseDate = in.readString();
    mVoteAverage = in.readDouble();
    mRuntime = in.readInt();
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
    dest.writeInt(mRuntime);
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

  public int getRuntime() {
    return mRuntime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MovieDetails that = (MovieDetails) o;

    if (Double.compare(that.mVoteAverage, mVoteAverage) != 0) return false;
    if (mRuntime != that.mRuntime) return false;
    if (mOriginalTitle != null ?
        !mOriginalTitle.equals(that.mOriginalTitle) : that.mOriginalTitle != null)
      return false;
    if (mPosterPath != null ? !mPosterPath.equals(that.mPosterPath) : that.mPosterPath != null)
      return false;
    if (mOverview != null ? !mOverview.equals(that.mOverview) : that.mOverview != null)
      return false;
    return mReleaseDate != null ?
        mReleaseDate.equals(that.mReleaseDate) : that.mReleaseDate == null;

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
    result = 31 * result + mRuntime;
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MovieDetails{");
    sb.append("mOriginalTitle='").append(mOriginalTitle).append('\'');
    sb.append(", mPosterPath='").append(mPosterPath).append('\'');
    sb.append(", mOverview='").append(mOverview).append('\'');
    sb.append(", mReleaseDate='").append(mReleaseDate).append('\'');
    sb.append(", mVoteAverage=").append(mVoteAverage);
    sb.append(", mRuntime=").append(mRuntime);
    sb.append('}');
    return sb.toString();
  }
}
