package com.ebs.banglalinkbangladhol.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebs.banglalinkbangladhol.R;

import java.util.ArrayList;

/**
 * Created by hp on 4/21/2017.
 */

public class DialogAdapter extends BaseAdapter {

    Activity context;
    String[] option;
    Integer[] icons;

    private static LayoutInflater inflater = null;

    public DialogAdapter(Activity mainActivity, String[] option, Integer[] icons) {
        // TODO Auto-generated constructor stub
        this.context = mainActivity;
        this.option = option;
        this.icons = icons;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return option.length;
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
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();

        View rowView;

        rowView = inflater.inflate(R.layout.custom_dialog_list_item, null);

        holder.tv = (TextView) rowView.findViewById(R.id.drawer_itemName);
        holder.img = (ImageView) rowView.findViewById(R.id.drawer_icon);
        holder.tv.setText(option[position]);
        holder.img.setImageResource(icons[position]);

        return rowView;
    }

}
