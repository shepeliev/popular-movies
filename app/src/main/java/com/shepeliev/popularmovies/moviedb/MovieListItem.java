package com.shepeliev.popularmovies.moviedb;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public final class MovieListItem implements Parcelable {

  @SerializedName("id")
  private int mId;

  @SerializedName("poster_path")
  private String mPosterPath;


  protected MovieListItem(Parcel in) {
    mId = in.readInt();
    mPosterPath = in.readString();
  }

  public static final Creator<MovieListItem> CREATOR = new Creator<MovieListItem>() {
    @Override
    public MovieListItem createFromParcel(Parcel in) {
      return new MovieListItem(in);
    }

    @Override
    public MovieListItem[] newArray(int size) {
      return new MovieListItem[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mId);
    dest.writeString(mPosterPath);
  }

  public int getId() {
    return mId;
  }

  public String getPosterPath() {
    return mPosterPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MovieListItem that = (MovieListItem) o;

    if (mId != that.mId) return false;
    return mPosterPath != null ? mPosterPath.equals(that.mPosterPath) : that.mPosterPath == null;

  }

  @Override
  public int hashCode() {
    int result = mId;
    result = 31 * result + (mPosterPath != null ? mPosterPath.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MovieListItem{");
    sb.append("mId=").append(mId);
    sb.append(", mPosterPath='").append(mPosterPath).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
