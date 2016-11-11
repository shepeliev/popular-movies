package com.shepeliev.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public final class Trailer implements Parcelable {

  public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
    @Override
    public Trailer createFromParcel(Parcel in) {
      return new Trailer(in);
    }

    @Override
    public Trailer[] newArray(int size) {
      return new Trailer[size];
    }
  };

  @SerializedName("key")
  private String mKey;

  @SerializedName("name")
  private String mName;

  private Trailer(Parcel in) {
    mKey = in.readString();
    mName = in.readString();
  }

  public String getKey() {
    return mKey;
  }

  public String getName() {
    return mName;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mKey);
    dest.writeString(mName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Trailer trailer = (Trailer) o;

    if (mKey != null ? !mKey.equals(trailer.mKey) : trailer.mKey != null) return false;
    return mName != null ? mName.equals(trailer.mName) : trailer.mName == null;
  }

  @Override
  public int hashCode() {
    int result = mKey != null ? mKey.hashCode() : 0;
    result = 31 * result + (mName != null ? mName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return mName;
  }
}
