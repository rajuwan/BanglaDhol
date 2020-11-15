package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.Album;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

public class AlbumFragmentOld extends Fragment {

	public AlbumFragmentOld() {
	}

	private ProgressBar splashbar;
	public String  albumCT, albumType, albumCategory, item;

	public String selectedCode, selectedCatname, selectedArtistName, selectedAlbumName,
			selectedPublisher, selectedRelease, selectedImage;

	private Integer selectedCount;
	int current_page = 1;

	public JSONObject albums;

	public ArrayList<HashMap<String, String>> urlArrayList;

	public List<Album> albumList;

	public HashMap<String, String> urlWithTitleHashMap = new HashMap<String, String>();

	public static String[] AitemsCodeArray, AitemsCatNameArray, AitemsArtistNameArray,
			AitemsAlbumNameArray, AitemsCpArray, AitemsReleaseArray, AitemsImageArray;
	public static Integer[] AiitemsCountArray;

	public byte[] art;

	public GridView gridview_album;

	public DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();

	public static final String FRAGMENT_TAG = "AlbumFragmentOld";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_album, container, false);

		gridview_album = (GridView) view.findViewById(R.id.gridview_album);
		splashbar = (ProgressBar) view.findViewById(R.id.splashAlbumProgress);

		try {

			Bundle args = getArguments();

			if (args != null) {

				albumCT = args.getString("albumCt"); // use
				albumType = args.getString("albumType"); // use
				albumCategory = args.getString("albumCategory"); //use
			}

			if(albumCategory != null){
				getActivity().setTitle(albumCategory);
			}

		} catch (Exception e) {

			Log.d("TAG", "Error in player service");

		}

		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_img)
				.showImageForEmptyUri(R.drawable.no_img)
				.showImageOnFail(R.drawable.no_img).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					Fragment fragment = new HomeFragment();
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.home_container, fragment);
					ft.addToBackStack(null);
					ft.commit();

					return true;
				} else {
					return false;
				}
			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		Invoke();
	}

	public void Invoke() {

		try {

			splashbar.setVisibility(View.VISIBLE);

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.i("TAG", "Requested Thread");
		}
	}

	/**
	 * To get Dynamic array List using -- Thread*
	 */
	public class RequestThread extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					albums = ServerUtilities.requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(),
							albumCT, albumType, current_page);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			album_handler.sendEmptyMessage(0);

		}
	}

	Handler album_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			splashbar.setVisibility(View.GONE);

			if (albums != null) {

				albumList = HTTPGateway.getDynamicAlbumList(albums);

				List<String> albumcode_list = new ArrayList<String>();
				List<String> catname_list = new ArrayList<String>();
				List<String> artistname_list = new ArrayList<String>();
				List<String> albumname_list = new ArrayList<String>();
				List<String> cp_list = new ArrayList<String>();
				List<String> release_list = new ArrayList<String>();
				List<Integer> count_list = new ArrayList<Integer>();
				List<String> image_list = new ArrayList<String>();

				for (int i = 0; i < albumList.size(); i++) {

					Album tmpData = albumList.get(i);
					albumcode_list.add(tmpData.getAlbumCode());
					catname_list.add(tmpData.getCatname());
					artistname_list.add(tmpData.getArtistname());
					albumname_list.add(tmpData.getAlbumName());
					cp_list.add(tmpData.getCp());
					release_list.add(tmpData.getRelease());
					count_list.add(tmpData.getCount());
					image_list.add(tmpData.getAlbumImgUrl());
				}

				AitemsCodeArray = albumcode_list.toArray(new String[] {});
				AitemsCatNameArray = catname_list.toArray(new String[] {});
				AitemsArtistNameArray = artistname_list.toArray(new String[] {});
				AitemsAlbumNameArray = albumname_list.toArray(new String[] {});
				AitemsCpArray = cp_list.toArray(new String[] {});
				AitemsReleaseArray = release_list.toArray(new String[] {});
				AiitemsCountArray= count_list.toArray(new Integer[] {});
				AitemsImageArray= image_list.toArray(new String[] {});

				gridview_album.setAdapter(new AlbumAdapter());

			} else {

				Toast.makeText(getActivity(),
						"Couldn't Connect, Please Try Again", Toast.LENGTH_LONG)
						.show();
			}
		}

	};

	class AlbumAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return AitemsCodeArray.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {

				view = mInflater.inflate(R.layout.albumgrid_row_item, parent,
						false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.gridText);
				holder.image = (ImageView) view.findViewById(R.id.gridImage);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text.setText(AitemsAlbumNameArray[position]);

			imageLoader.displayImage(AitemsImageArray[position], holder.image,
					options, animateFirstListener);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					try{

						selectedCode = AitemsCodeArray[position];
						selectedCatname = AitemsCatNameArray[position];
						selectedArtistName = AitemsArtistNameArray[position];
						selectedAlbumName =AitemsAlbumNameArray[position];
						selectedPublisher = AitemsCpArray[position];
						selectedRelease = AitemsReleaseArray[position];
						selectedCount = AiitemsCountArray[position];
						selectedImage = AitemsImageArray[position];

						if(albumType.contains("album")){

							FragmentTransaction ft = getFragmentManager().beginTransaction();
							AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
							Bundle args = new Bundle();
							args.putString("albumCode", selectedCode);
							args.putString("albumName", selectedAlbumName);
							args.putString("albumArtist", selectedArtistName);
							args.putString("albumGenre", selectedCatname);
							args.putString("albumPublisher", selectedPublisher);
							args.putString("albumYear", selectedRelease);
							args.putInt("albumCount", selectedCount);
							args.putString("albumImage", selectedImage);
							albumDetailsFragment.setArguments(args);
							ft.replace(R.id.home_container, albumDetailsFragment, AlbumDetailsFragment.FRAGMENT_TAG);
							ft.addToBackStack(null);
							ft.commit();

						} else{

							FragmentTransaction ft = getFragmentManager().beginTransaction();
							PlaylistDetailsFragment playlistDetailsFragment = new PlaylistDetailsFragment();
							Bundle args = new Bundle();
							args.putString("albumCode", selectedCode);
							args.putString("albumName", selectedAlbumName);
							args.putString("albumArtist", selectedArtistName);
							args.putString("albumGenre", selectedCatname);
							args.putString("albumPublisher", selectedPublisher);
							args.putString("albumYear", selectedRelease);
							args.putInt("albumCount", selectedCount);
							args.putString("albumImage", selectedImage);
							playlistDetailsFragment.setArguments(args);
							ft.replace(R.id.home_container, playlistDetailsFragment, PlaylistDetailsFragment.FRAGMENT_TAG);
							ft.addToBackStack(null);
							ft.commit();

						}

					} catch (Exception ex){

						Log.d("Error", "Album onitem click");

					}

				}
			});

			return view;
		}
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
