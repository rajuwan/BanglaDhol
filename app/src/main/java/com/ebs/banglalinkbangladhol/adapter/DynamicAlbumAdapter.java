package com.ebs.banglalinkbangladhol.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

import com.ebs.banglalinkbangladhol.R;

public class
DynamicAlbumAdapter extends BaseAdapter {

    public DisplayImageOptions options;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private final Context context;
    private String[] contentTitleArray, contentImageArray;

    public DynamicAlbumAdapter(Context context, String[] contentTitleArray, String[] contentImageArray) {

        this.context = context;
        this.contentTitleArray = contentTitleArray;
        this.contentImageArray = contentImageArray;

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_img)
                .showImageForEmptyUri(R.drawable.no_img)
                .showImageOnFail(R.drawable.no_img).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    private class ViewHolder {
        public TextView text;
        public ImageView image;
    }

    @Override
    public int getCount() {

        return contentTitleArray.length;
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

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {

            view = mInflater.inflate(R.layout.album_card_list_item, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.iv_card_image);
            holder.text = (TextView) view.findViewById(R.id.tv_card_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(contentTitleArray[position]);

        imageLoader.displayImage(contentImageArray[position], holder.image,
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