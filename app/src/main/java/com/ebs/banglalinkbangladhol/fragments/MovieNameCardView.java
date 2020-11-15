package com.ebs.banglalinkbangladhol.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.ebs.banglalinkbangladhol.R;

public class MovieNameCardView extends RecyclerView.Adapter<MovieNameCardView.DataObjectHolder>{

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    ArrayList<Movie> data;
    int pos;
    String heading_nws,detail_nws;
    int []image;
    Fragment fragment;
    private static MyClickListener myClickListener;
    Context context;

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row,parent,false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

       // SorbosheshModel object_sorboshesh= data.get(position);
      /*  holder.heading.setText(object_sorboshesh.getHeading());
        holder.mImage.setImageResource(image[position]);

        pos=position;
        heading_nws=object_sorboshesh.getHeading();
        detail_nws =object_sorboshesh.getDescricption();*/
       // Toast.makeText(fragment.getContext(),String.valueOf(pos),Toast.LENGTH_LONG).show();

        Movie movie = data.get(position);
        holder.heading.setText(movie.getTitle());
    }

    public void deleteItem(int index){
        data.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public  class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      TextView heading;//adress,distance,latitude,longitude;
      ImageView mImage;
        public DataObjectHolder(final View itemView) {
            super(itemView);
            heading = (TextView) itemView.findViewById(R.id.title);
         //   mImage = (ImageView) itemView.findViewById(R.id.imageView_sorboshesh);
            Log.i(LOG_TAG, "Adding Listener");
        //    itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(), view);

        }
    }
    public void setOnItemClickListener(MyClickListener myClickListener){
        this.myClickListener = myClickListener;
    }
    public MovieNameCardView(ArrayList<Movie> data){
        this.data = data;
        this.image=image;
        this.fragment=fragment;
    }


    public interface MyClickListener{
            public void onItemClick(int position, View v);
        }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
