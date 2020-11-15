package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.bean.AlbumSingle;
import com.ebs.banglalinkbangladhol.bean.AlbumSong;
import com.ebs.banglalinkbangladhol.model.ActionClick;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.ebs.banglalinkbangladhol.service.PlayerService;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.SongsManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class PlaylistDetailsFragment extends Fragment {

	public PlaylistDetailsFragment() {
	}

	public ProgressBar splashBar;
	private ProgressDialog pd;

	public ListView albumSongList;
	public String albumCode, albumName, albumArtist, albumGenre, albumCp, albumYear, albumImage = "";
	public int albumCount = 0;

	public String albumSongs, contents;

	public List<AlbumSingle> audioList;
	private List<AlbumSong> albumSong;
	private ArrayList<HashMap<String, String>> menuItems;

	public DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();

	private SongsManager songManager;
	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	FragmentActivity activity;
	private Intent playerService;

	TextView total_text;

	public static final String FRAGMENT_TAG = "PlaylistDetailsFragment";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity  = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.album_details_fragment, container, false);

		splashBar = (ProgressBar) rootView.findViewById(R.id.ADFProgress);
		menuItems = new ArrayList<HashMap<String, String>>();

		albumSongList = (ListView) rootView.findViewById(android.R.id.list);

		try {

			Bundle args = getArguments();

			if (args != null) {

				albumCode = args.getString("albumCode"); // use
				albumName = args.getString("albumName"); // use
				albumArtist = args.getString("albumArtist"); // use
				albumGenre = args.getString("albumGenre");
				albumCp = args.getString("albumPublisher");
				albumYear = args.getString("albumYear");
				albumImage = args.getString("albumImage"); // use
				albumCount = args.getInt("albumCount"); // use
			}

		} catch (Exception e) {

			Log.d("TAG", "Error in player service");

		}

		try {

			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.playlist_listview_header,
					albumSongList, false);

			ImageView album_image = (ImageView) header.findViewById(R.id.backdrop);
			TextView header_text = (TextView) header.findViewById(R.id.section_header);
			header_text.setSelected(true);
			TextView cp_text = (TextView) header.findViewById(R.id.section_songs_cp);
			total_text = (TextView) header.findViewById(R.id.section_songs_count);
			TextView year_text = (TextView) header.findViewById(R.id.section_songs_year);
			TextView tv_add_all = (TextView) header.findViewById(R.id.tv_item_action_2);
			tv_add_all.setText("Play All");
			tv_add_all.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					try {

						ContentPlayCheck();

					} catch (Exception ex){
						Log.d("TAG", "Error in AddAllToPlaylist");
					}

				}
			});

			Glide.with(getActivity()).load(albumImage).into(album_image);

			if(albumName != null && albumName.length()>=2){
				header_text.setText(albumName);
			}
			if(albumCp != null && albumCp.length() > 3){
				cp_text.setText("by " + albumCp);
			}
			if(albumCount != 0){
				total_text.setText(albumCount + " songs");
			}
			if(albumYear != null && albumYear.length()> 3){
				year_text.setText("Release year : " + albumYear);
			}

			albumSongList.addHeaderView(header, null, false);

		} catch (Exception ex) {

			Log.d("TAG", "Error in Requested Thread");
		}

		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_img)
				.showImageForEmptyUri(R.drawable.no_img)
				.showImageOnFail(R.drawable.no_img).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		songManager = new SongsManager();
		songsList = songManager.getPlayList();

		if(savedInstanceState == null){
			Invoke();
		}

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void Invoke() {

		try {

			splashBar.setVisibility(View.VISIBLE);

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error in main thread");
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

					albumSongs = ServerUtilities.requestForAlbumSongs(CheckUserInfo.getUserMsisdn(),
							albumCode);

				} catch (Exception ex) {

					Log.d("TAG", "Error in requestForAlbumSongs");
				}

			}

			audio_handler.sendEmptyMessage(0);

		}
	}

	Handler audio_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			splashBar.setVisibility(View.GONE);

			try {

				if (albumSongs != null && albumSongs.length() > 20) {

					audioList = HTTPGateway.getDynamicAlbumSongList(albumSongs);

					if(audioList.size() == 0)
						return;

					for (int i = 0; i < audioList.size(); i++) {

						AlbumSingle tmpData = audioList.get(i);
						contents = tmpData.getAlbumContents();
					}

					albumSong = HTTPGateway.getDynamicAlbumContentList(contents);

					if(albumSong.size() == 0)
						return;

					if(albumSong.size() != 0){
						total_text.setText(albumSong.size() + " songs");
					}

					for (int i = 0; i < albumSong.size(); i++) {

						HashMap<String, String> map = new HashMap<String, String>();
						AlbumSong tmpData = albumSong.get(i);
						map.put("contentid_list", tmpData.getContentid());
						map.put("name_list", tmpData.getName());
						map.put("artist_list", tmpData.getArtist());
						map.put("albumname_list", albumName);
						map.put("albumcode_list", tmpData.getAlbumCode());
						map.put("url_list", tmpData.getUrl());
						map.put("urlwowza_list", tmpData.getUrlWowza());
						map.put("image_list", tmpData.getImageUrl());
						map.put("duration_list", tmpData.getDuration());
						map.put("cp_list", albumCp);
						map.put("genre_list", tmpData.getGenre());
						map.put("catcode_list", tmpData.getCatcode());
						menuItems.add(map);
					}

					albumSongList.setAdapter(new AlbumListAdapter());

					albumSongList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

							HashMap<String, String> item = new HashMap<String, String>();
							item = menuItems.get(position - 1);
							ActionClick.requestForPlay(activity, item, "playlist");

						}
					});


				} else {
					Toast.makeText(getActivity(),
							"Couldn't Connect, Please Try Again", Toast.LENGTH_LONG).show();
				}

			} catch (Exception ex) {
				Toast.makeText(getActivity(),
						"Album Not Available !", Toast.LENGTH_LONG).show();
				Log.d("TAG", "Error in audio_handler");
			}

		}

	};

	public void ContentPlayCheck(){

		try {

			pd = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Loading Please Wait ...");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();

			RequestThreadCPC reqThread = new RequestThreadCPC();
			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured PlayMsisdnCheck");
		}

	}

	public class RequestThreadCPC extends Thread {

		@Override
		public void run() {

			Looper.prepare();

			synchronized (this) {

				try {

					CheckUserInfo.getUserMsisdnInfo(getActivity());

					CheckUserInfo.getUserLoginInfo(getActivity(), CheckUserInfo.getUserMsisdn(),
							CheckUserInfo.getUserPinCode());

				} catch (Exception e) {

					Log.d("TAG", "Error Occured RequestThreadPMC");

				}

			}

			cpc_handler.sendEmptyMessage(0);

		}
	}

	Handler cpc_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			try {

				if (CheckUserInfo.getConCurrentStatus() == 1) {
					ConcurrentDialog();
					return;
				}

				if (CheckUserInfo.getUserNetworkAccess() == 1) {

					AccessTypeOne();


				} else if (CheckUserInfo.getUserNetworkAccess() == 7) {

					AccessTypeSeven();

				} else {

					Snackbar.make(getView(), "Can't process your request now", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
				}

			} catch (Exception ex) {

				Log.d("Login Error", "Error while parsing login data");
			}

		}

	};

	public void goToSubBox() {

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
		ft.replace(R.id.home_container, subscriptionFragment, SubscriptionFragment.FRAGMENT_TAG);
		ft.addToBackStack(SubscriptionFragment.FRAGMENT_TAG);
		ft.commit();
	}

	public void ConcurrentDialog(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("Dear User");
		alertDialogBuilder.setMessage(CheckUserInfo.getConCurrentText());

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	public void AccessTypeOne(){

		if (CheckUserInfo.getMsisdnFromServer().contains("no")) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			alertDialogBuilder.setTitle("Dear User");
			alertDialogBuilder.setMessage(CheckUserInfo.getUserInfoText());
			// set dialog message
			alertDialogBuilder.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int id) {

									dialog.dismiss();

								}
							});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();

			return;
		}

		if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

			if (CheckUserInfo.getUserPlayStatus() == 1) {

				try {

					PlayAllSong();

				} catch (Exception ex) {

					Log.d("Login Error", "Error while open VideoViewNew");

				}

				return;

			} else {

				goToSubBox();

			}

		} else {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());
			alertDialogBuilder.setTitle("Dear User");
			alertDialogBuilder.setMessage(CheckUserInfo.getUserInfoText());

			// set dialog message
			alertDialogBuilder.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int id) {

									dialog.dismiss();

								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();

		}

		return;

	}

	public void AccessTypeSeven(){


		if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

			if (CheckUserInfo.getUserPlayStatus() == 1) {

				try {

					PlayAllSong();

				} catch (Exception ex) {

					Log.d("Login Error",
							"Error while open VideoViewNew");

				}

				return;

			} else {

				goToSubBox();

			}

		} else {

			Intent mainIntent = new Intent(getActivity(), BanglaDholSignUpLogInActivity.class);
			startActivity(mainIntent);

		}

		return;

	}

	public void PlayAllSong(){

		try {
			// Playing 1st song of the playlist

			HashMap<String, String> song = new HashMap<String, String>();

			AlbumSong tmpData = albumSong.get(0);

			String title = tmpData.getName();
			if (title != null) {
				song.put("songTitle", title);
			} else {
				song.put("songTitle", "Unknown Title");
			}

			String artist = tmpData.getArtist();
			if (artist != null) {
				song.put("songArtist", artist);
			} else {
				song.put("songArtist", "Unknown Artist");
			}

			if (albumName != null) {
				song.put("songAlbum", albumName);
			} else {
				song.put("songAlbum", "Unknown Album");
			}

			song.put("songImage", tmpData.getImageUrl());

			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
				song.put("songPath", tmpData.getUrl());
			} else {
				song.put("songPath", tmpData.getUrlWowza());
			}

			song.put("songCode", tmpData.getContentid());
			song.put("songAlbumCode", albumCode);

			String length = tmpData.getDuration();
			if (length != null) {
				song.put("songLength", length);
			} else {
				song.put("songLength", "0.00");
			}

			 /*Check for duplicate song in playlist*/

			int found = 0;
			int index = 0;

			ArrayList<HashMap<String, String>> playList = SongsManager._songsList;

			for (int i = 0; i < playList.size(); i++) {

				HashMap<String, String> item = new HashMap<String, String>();
				item = playList.get(i);

				if(item.get("songCode").equals(tmpData.getContentid())){
					found = 1;
					index = i;
				}
			}

			if(found == 0){

				SongsManager._songsList.add(song);
				int indexvalue = SongsManager._songsList.size() - 1;

				try {

					playerService = new Intent(getActivity(), PlayerService.class);
					playerService.putExtra("songIndex", indexvalue);
					getActivity().startService(playerService);

				} catch (Exception e) {
					Log.d("TAG", "Error in player service");
				}

			} else{

				try {

					playerService = new Intent(getActivity(), PlayerService.class);
					playerService.putExtra("songIndex", index);
					getActivity().startService(playerService);

				} catch (Exception e) {
					Log.d("TAG", "Error in player service");
				}

			}

			// Adding all song to playlist

			for (int i = 1; i < albumSong.size(); i++) {

				HashMap<String, String> queue = new HashMap<String, String>();
				AlbumSong tmpDataAdd = albumSong.get(i);

				String titleAdd = tmpDataAdd.getName();
				if (titleAdd != null) {
					queue.put("songTitle", titleAdd);
				} else {
					queue.put("songTitle", "Unknown Title");
				}

				String artistAdd = tmpDataAdd.getArtist();
				if (artistAdd != null) {
					queue.put("songArtist", artistAdd);
				} else {
					queue.put("songArtist", "Unknown Artist");
				}

				if (albumName != null) {
					queue.put("songAlbum", albumName);
				} else {
					queue.put("songAlbum", "Unknown Album");
				}

				queue.put("songImage", tmpDataAdd.getImageUrl());

				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
					queue.put("songPath", tmpDataAdd.getUrl());
				} else {
					queue.put("songPath", tmpDataAdd.getUrlWowza());
				}

				queue.put("songCode", tmpDataAdd.getContentid());
				queue.put("songAlbumCode", albumCode);

				String lengthAdd = tmpDataAdd.getDuration();
				if (lengthAdd != null) {
					queue.put("songLength", lengthAdd);
				} else {
					queue.put("songLength", "0.00");
				}

				/*Check for content duplicate */

				int found2 = 0;

				ArrayList<HashMap<String, String>> playList2 = SongsManager._songsList;

				for (int p = 0; p < playList2.size(); p++) {

					HashMap<String, String> item = new HashMap<String, String>();
					item = playList2.get(p);

					if(item.get("songCode").equals(tmpDataAdd.getContentid())){
						found2 = 1;
					}
				}

				if(found2 == 0){

					SongsManager._songsList.add(queue);

				}
			}

		} catch (Exception ex){
			Log.d("Error AddToPlaylist", ex.toString());
		}

	}

	class AlbumListAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text, text2, text3, text4;
			public ImageView image;
		}

		@Override
		public int getCount() {

			return menuItems.size();
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

			LayoutInflater mInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {

				view = mInflater.inflate(R.layout.playlist_item_song, parent,
						false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.tv_first);
				holder.text2 = (TextView) view.findViewById(R.id.tv_second);
				holder.text3 = (TextView) view.findViewById(R.id.tv_third);
				holder.text4 = (TextView) view.findViewById(R.id.album_song_duration);
				holder.image = (ImageView) view.findViewById(R.id.iv_image);
				view.setTag(holder);

			} else {
				holder = (ViewHolder) view.getTag();
			}

			HashMap<String, String> item = new HashMap<String, String>();
			item = menuItems.get(position);

			holder.text.setText(item.get("name_list"));
			holder.text2.setText(item.get("artist_list"));
			//holder.text3.setText(item.get("album_list"));
			holder.text4.setText(item.get("duration_list"));
			imageLoader.displayImage(item.get("image_list"), holder.image,
					options, animateFirstListener);

			return view;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

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
