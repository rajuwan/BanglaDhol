package com.ebs.banglalinkbangladhol.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ebs.banglalinkbangladhol.R;

/**
 * Created by Rajuwan on 02-Mar-17.
 */

public class DynamicViewAllLoadMoreAdapter extends BaseAdapter {

    public DisplayImageOptions options;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private final Context context;
    private ArrayList<HashMap<String, String>> data;

    public DynamicViewAllLoadMoreAdapter(Context context, ArrayList<HashMap<String, String>> d) {

        this.context = context;
        this.data = d;

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_img)
                .showImageForEmptyUri(R.drawable.no_img)
                .showImageOnFail(R.drawable.no_img).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {

            view = mInflater.inflate(R.layout.content_see_all_item, parent,
                    false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.tv_first);
            holder.text2 = (TextView) view.findViewById(R.id.tv_second);
            holder.text3 = (TextView) view.findViewById(R.id.tv_third);
            holder.text4 = (TextView) view.findViewById(R.id.song_duration);
            holder.image = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        HashMap<String, String> item = new HashMap<String, String>();
        item = data.get(position);

        holder.text.setText(item.get("name_list"));
        holder.text2.setText(item.get("artist_list") + "-");
        holder.text3.setText(item.get("albumname_list"));
        holder.text4.setText(item.get("duration_list"));

        imageLoader.displayImage(item.get("image_list"), holder.image,
                options, animateFirstListener);

        return view;
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
