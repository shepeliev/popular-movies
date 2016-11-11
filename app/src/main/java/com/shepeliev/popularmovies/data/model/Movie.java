package com.shepeliev.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.shepeliev.popularmovies.data.MoviesEntry;

@StorIOSQLiteType(table = MoviesEntry.TABLE)
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

  @SerializedName("id")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns._ID, key = true)
  int mId;

  @SerializedName("original_title")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns.ORIGINAL_TITLE)
  String mOriginalTitle;

  @SerializedName("poster_path")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns.POSTER_PATH)
  String mPosterPath;

  @SerializedName("overview")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns.OVERVIEW)
  String mOverview;

  @SerializedName("release_date")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns.RELEASE_DATE)
  String mReleaseDate;

  @SerializedName("vote_average")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns.VOTE_AVERAGE)
  double mVoteAverage;

  @SerializedName("runtime")
  @StorIOSQLiteColumn(name = MoviesEntry.Columns.RUNTIME)
  int mRuntime;

  Movie() {
    // default constructor for stor IO type mapper
  }

  private Movie(Parcel in) {
    mId = in.readInt();
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
    dest.writeInt(mId);
    dest.writeString(mOriginalTitle);
    dest.writeString(mPosterPath);
    dest.writeString(mOverview);
    dest.writeString(mReleaseDate);
    dest.writeDouble(mVoteAverage);
    dest.writeInt(mRuntime);
  }

  public int getId() {
    return mId;
  }

  public String getOriginalTitle() {
    return mOriginalTitle;
  }

  public String getPosterPath() {
    return mPosterPath;
  }

  public Movie setPosterPath(String posterPath) {
    mPosterPath = posterPath;
    return this;
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

    Movie that = (Movie) o;

    if (mId != that.mId) return false;
    if (Double.compare(that.mVoteAverage, mVoteAverage) != 0) return false;
    if (mRuntime != that.mRuntime) return false;
    if (mOriginalTitle != null ? !mOriginalTitle.equals(that.mOriginalTitle) : that.mOriginalTitle != null)
      return false;
    if (mPosterPath != null ? !mPosterPath.equals(that.mPosterPath) : that.mPosterPath != null)
      return false;
    if (mOverview != null ? !mOverview.equals(that.mOverview) : that.mOverview != null)
      return false;
    return mReleaseDate != null ? mReleaseDate.equals(that.mReleaseDate) : that.mReleaseDate == null;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = mId;
    result = 31 * result + (mOriginalTitle != null ? mOriginalTitle.hashCode() : 0);
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
    sb.append("mId=").append(mId);
    sb.append(", mOriginalTitle='").append(mOriginalTitle).append('\'');
    sb.append(", mPosterPath='").append(mPosterPath).append('\'');
    sb.append(", mOverview='").append(mOverview).append('\'');
    sb.append(", mReleaseDate='").append(mReleaseDate).append('\'');
    sb.append(", mVoteAverage=").append(mVoteAverage);
    sb.append(", mRuntime=").append(mRuntime);
    sb.append('}');
    return sb.toString();
  }
}
