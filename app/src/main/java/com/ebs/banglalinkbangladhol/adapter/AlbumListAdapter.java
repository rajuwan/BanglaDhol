package com.ebs.banglalinkbangladhol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebs.banglalinkbangladhol.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajuwan on 15-Apr-17.
 */

public class AlbumListAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<HashMap<String, String>> data;

    public AlbumListAdapter(Context context, ArrayList<HashMap<String, String>> d) {

        this.context = context;
        data = d;
    }

    private class ViewHolder {
        public TextView text, text2, text3, text4;
        public ImageView image;
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {

            view = mInflater.inflate(R.layout.album_item_song_list, parent,
                    false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.tv_first);
            holder.text2 = (TextView) view.findViewById(R.id.tv_second);
            holder.text3 = (TextView) view.findViewById(R.id.tv_Row);
            holder.text4 = (TextView) view.findViewById(R.id.album_song_duration);
            // holder.image = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        HashMap<String, String> item = new HashMap<String, String>();
        item = data.get(position);

        holder.text.setText(item.get("name_list"));
        holder.text2.setText(item.get("artist_list"));
        holder.text3.setText(String.valueOf(position + 1));
        holder.text4.setText(item.get("duration_list"));

        return view;
    }
}
