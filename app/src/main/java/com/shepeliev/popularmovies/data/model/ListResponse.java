package com.shepeliev.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class ListResponse<T> {

  @SerializedName("results")
  private List<T> mResults;

  public List<T> getResults() {
    return mResults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ListResponse that = (ListResponse) o;

    return mResults != null ? mResults.equals(that.mResults) : that.mResults == null;

  }

  @Override
  public int hashCode() {
    return mResults != null ? mResults.hashCode() : 0;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("MovieDbResponse{");
    sb.append("mResults=").append(mResults);
    sb.append('}');
    return sb.toString();
  }
}
