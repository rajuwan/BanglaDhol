package com.ebs.banglalinkbangladhol.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.ebs.banglalinkbangladhol.fragment.AlbumDetailsFragment;
import com.ebs.banglalinkbangladhol.fragment.PlaylistDetailsFragment;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.Product;
import com.ebs.banglalinkbangladhol.fragment.Home9Fragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageSlideAdapter extends PagerAdapter {
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener imageListener;
	FragmentActivity activity;
	//Activity activity;
	List<Product> products;
	Home9Fragment homeFragment;

	public ImageSlideAdapter(FragmentActivity activity, List<Product> products,
			Home9Fragment homeFragment) {
		
		this.activity = activity;
		this.homeFragment = homeFragment;
		this.products = products;
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.no_img_album)
				.showStubImage(R.drawable.no_img_album)
				.showImageForEmptyUri(R.drawable.no_img_album).cacheInMemory()
				.cacheOnDisc().build();

		imageListener = new ImageDisplayListener();
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.vp_image, container, false);

		ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

				AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
				Bundle args = new Bundle();
				args.putString("albumCode", ((Product) products.get(position)).getAlbumcode());
				args.putString("albumName", ((Product) products.get(position)).getName());
				args.putString("albumArtist", ((Product) products.get(position)).getArtist());
				args.putString("albumGenre", ((Product) products.get(position)).getCatcode());
				args.putString("albumPublisher", ((Product) products.get(position)).getLabelname());
				args.putString("albumYear", "");
				args.putInt("albumCount", ((Product) products.get(position)).getSongs());
				args.putString("albumImage", ((Product) products.get(position)).getImg_url());
				albumDetailsFragment.setArguments(args);
				//ft.setCustomAnimations(R.anim.slide_in_left,
						//R.anim.slide_out_right);
				ft.replace(R.id.home_container, albumDetailsFragment, PlaylistDetailsFragment.FRAGMENT_TAG);
				ft.addToBackStack(null);
				ft.commit();

			}
		});

		imageLoader.displayImage(((Product) products.get(position)).getImg_url(), mImageView,
				options, imageListener);
		container.addView(view);
		return view;
	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	private static class ImageDisplayListener extends
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