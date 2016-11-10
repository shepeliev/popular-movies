package com.shepeliev.popularmovies.moviedb;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerList {

  @SerializedName("results")
  private List<Trailer> mResults;

  public List<Trailer> getResults() {
    return mResults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TrailerList that = (TrailerList) o;

    return mResults != null ? mResults.equals(that.mResults) : that.mResults == null;

  }

  @Override
  public int hashCode() {
    return mResults != null ? mResults.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("TrailerList{");
    sb.append("mResults=").append(mResults);
    sb.append('}');
    return sb.toString();
  }
}
