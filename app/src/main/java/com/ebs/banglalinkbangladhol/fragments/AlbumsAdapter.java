package com.ebs.banglalinkbangladhol.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.ebs.banglalinkbangladhol.R;

/**
 * Created by Rajuwan on 01-Mar-17.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    //private Context mContext;
    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //count = (TextView) view.findViewById(R.id.count);
            //thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public AlbumsAdapter(List<Movie> moviesList) {
        //this.mContext = mContext;
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //Album album = albumList.get(position);
        Movie movie = moviesList.get(position);
        //holder.title.setText(contentNameArray[position]);
        holder.title.setText(movie.getTitle());
        //holder.count.setText( + " songs");

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
