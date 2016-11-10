package com.shepeliev.popularmovies.moviedb;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public final class Review implements Parcelable {

  public static final Creator<Review> CREATOR = new Creator<Review>() {
    @Override
    public Review createFromParcel(Parcel in) {
      return new Review(in);
    }

    @Override
    public Review[] newArray(int size) {
      return new Review[size];
    }
  };

  @SerializedName("author")
  private String mAuthor;

  @SerializedName("content")
  private String mContent;

  private Review(Parcel in) {
    mAuthor = in.readString();
    mContent = in.readString();
  }

  public String getAuthor() {
    return mAuthor;
  }

  public String getContent() {
    return mContent;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mAuthor);
    dest.writeString(mContent);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Review review = (Review) o;

    if (mAuthor != null ? !mAuthor.equals(review.mAuthor) : review.mAuthor != null) return false;
    return mContent != null ? mContent.equals(review.mContent) : review.mContent == null;

  }

  @Override
  public int hashCode() {
    int result = mAuthor != null ? mAuthor.hashCode() : 0;
    result = 31 * result + (mContent != null ? mContent.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Review{");
    sb.append("mAuthor='").append(mAuthor).append('\'');
    sb.append(", mContent='").append(mContent).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
