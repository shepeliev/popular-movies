package com.shepeliev.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.shepeliev.popularmovies.moviedb.Movie;
import com.shepeliev.popularmovies.moviedb.MovieDb;
import com.squareup.picasso.Picasso;

public class MovieListAdapter extends ArrayAdapter<Movie> {

  public MovieListAdapter(Context context) {
    super(context, R.layout.movie_list_item);
  }

  @NonNull
  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    ImageView itemView = (ImageView) convertView;

    if (itemView == null) {
      itemView = (ImageView) LayoutInflater
          .from(getContext())
          .inflate(R.layout.movie_list_item, parent, false);
    }

    Movie item = getItem(position);
    if (item != null && item.getPosterPath() != null) {
      Picasso.with(getContext()).load(MovieDb.IMAGE_BASE_URL + item.getPosterPath()).into(itemView);
    }

    return itemView;
  }

}
