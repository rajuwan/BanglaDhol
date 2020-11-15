package com.ebs.banglalinkbangladhol.adapter;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.Playlist;
import com.ebs.banglalinkbangladhol.bean.PlaylistItem;

import java.util.ArrayList;

/**
 * Created by hp on 4/21/2017.
 */

public class PlayListAdapter extends BaseAdapter {

    Activity context;
    ArrayList<PlaylistItem> option;

    private static LayoutInflater inflater = null;

    public PlayListAdapter(Activity mainActivity, ArrayList<PlaylistItem> option) {
        // TODO Auto-generated constructor stub
        this.context = mainActivity;
        this.option = option;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return option.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView title;
        ImageView icon;
        RadioButton checked;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.playlist_item, null);
        PlaylistItem obj = option.get(position);
        holder.title = (TextView) rowView.findViewById(R.id.tv_titlePlaylist);
        holder.checked = (RadioButton) rowView.findViewById(R.id.rb_checkedPlaylist);
        holder.title.setText(obj.getPlaylistTitle());

        return rowView;
    }

}
