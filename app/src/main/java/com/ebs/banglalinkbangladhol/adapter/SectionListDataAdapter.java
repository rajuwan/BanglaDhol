package com.ebs.banglalinkbangladhol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.model.ActionClick;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private FragmentActivity activity;
    private ArrayList<HashMap<String, String>> data;
    private int rowType;
    private int contentType;

    public SectionListDataAdapter(FragmentActivity activity, ArrayList<HashMap<String, String>> d,
                                  int rowType, int contentType) {

        this.activity = activity;
        this.data = d;
        this.rowType = rowType;
        this.contentType = contentType;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if(rowType == 1){
            if(contentType == 1){
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_item, null);
                SingleItemRowHolder mh = new SingleItemRowHolder(v);
                return mh;
            } else if(contentType == 2 || contentType == 3){
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_card_list_item, null);
                SingleItemRowHolder mh = new SingleItemRowHolder(v);
                return mh;
            }
        } else if(rowType == 2){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_item_vertical_grid, null);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        } else if(rowType == 3){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_item_vertical_list, null);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, final int i) {
        if(contentType == 1){
            holder.title.setText(data.get(i).get("name_list"));

            Picasso.get().load(data.get(i).get("image_list")).placeholder(R.drawable.no_img).
                    error(R.drawable.no_img).into(holder.image);

            //Glide.with(activity).load(data.get(i).get("image_list")).diskCacheStrategy(DiskCacheStrategy.ALL).
                    //centerCrop().error(R.drawable.no_img).into(holder.image);

        } else if(contentType == 2 || contentType == 3){

            holder.title.setText(data.get(i).get("albumname_list"));

            Picasso.get().load(data.get(i).get("image_list")).placeholder(R.drawable.no_img).
                    error(R.drawable.no_img).into(holder.image);

            if(rowType == 3){
                holder.artist.setText("Artist : " + data.get(i).get("artistname_list"));
                holder.cp.setText("By : " + data.get(i).get("cp_list"));
                holder.release.setText("Year : " + data.get(i).get("release_list"));
                holder.songs.setText(data.get(i).get("count_list") + " songs");
            }

           // Glide.with(activity).load(data.get(i).get("image_list")).diskCacheStrategy(DiskCacheStrategy.ALL).
                    //centerCrop().error(R.drawable.no_img).into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contentType == 1){ // single item click
                    HashMap<String, String> item = new HashMap<String, String>();
                    item = data.get(i);
                    ActionClick.requestForPlay(activity, item, "single");
                    return;
                } else if (contentType == 2){ // album item click
                    HashMap<String, String> item = new HashMap<String, String>();
                    item = data.get(i);
                    ActionClick.goToAlbumSingle(activity, item);
                    return;
                } else if (contentType == 3){ // playlist item click
                    HashMap<String, String> item = new HashMap<String, String>();
                    item = data.get(i);
                    ActionClick.goToPlaylistSingle(activity, item);
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        public TextView title, artist, cp, release, songs;
        public ImageView image;

        public SingleItemRowHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.iv_card_image);
            this.title = (TextView) view.findViewById(R.id.tv_card_title);

            if(rowType == 3){
                this.artist = (TextView) view.findViewById(R.id.tv_card_artist);
                this.cp = (TextView) view.findViewById(R.id.tv_card_cp);
                this.release = (TextView) view.findViewById(R.id.tv_card_release);
                this.songs = (TextView) view.findViewById(R.id.tv_card_songs);
            }
        }
    }

}