package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PlayerQueueFragment extends Fragment {

	public PlayerQueueFragment() {
	}

	public ProgressBar splashBar;
	private ProgressDialog pd;

	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	public ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options;
	private QueueAdapter adapter;
	private HashMap<String, String> song;
	private ListView queueList;
	private int songIndex = -1;

	private Intent playerService;

	public static final String FRAGMENT_TAG = "PlayerQueueFragment";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		//setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*if(savedInstanceState != null){
			songIndex = savedInstanceState.getInt("songIndex");
			Toast.makeText(getActivity(), String.valueOf(songIndex), Toast.LENGTH_SHORT).show();
		}*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.player_queue_listview, container, false);

		getActivity().setTitle("Player Queue");

		queueList = (ListView) rootView.findViewById(android.R.id.list);
		splashBar = (ProgressBar) rootView.findViewById(R.id.PlaylistProgress);

		songsListData = new ArrayList<HashMap<String, String>>();

		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_img)
				.showImageForEmptyUri(R.drawable.no_img)
				.showImageOnFail(R.drawable.no_img).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		if(savedInstanceState == null){
			Invoke();
		}

		/*rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					if (getFragmentManager().getBackStackEntryCount() == 0) {
						getActivity().finish();
					} else {
						getFragmentManager().popBackStack();
					}
					return true;
				} else {
					return false;
				}
			}
		});*/

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//outState.putInt("songIndex", songIndex);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		/*if(savedInstanceState != null){
			songIndex = savedInstanceState.getInt("songIndex");
			Toast.makeText(getActivity(), String.valueOf(songIndex), Toast.LENGTH_SHORT).show();
		}*/
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

	public class RequestThread extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					SongsManager plm = new SongsManager();
					// get all songs from sdcard
					songsListData = plm.getPlayList();

					/*songsListData.clear();
					// looping through playlist
					for (int i = 0; i < songsList.size(); i++) {
						// creating new HashMap
						song = songsList.get(i);
						// adding HashList to ArrayList
						if (!songsListData.contains(song)) {
							songsListData.add(song);

						}
					}*/


				} catch (Exception ex) {

					Log.d("TAG", "Error in search");
				}

			}

			playlist_handler.sendEmptyMessage(0);

		}
	}

	Handler playlist_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			splashBar.setVisibility(View.GONE);

			if (songsListData.size() > 0) {

				adapter = new QueueAdapter();
				queueList.setAdapter(adapter);

				queueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						try {

							songIndex = position;
							adapter.notifyDataSetChanged();
							ContentPlayCheck();

						} catch (Exception e) {
							Log.d("Tag", "Player Service not working");

						}

					}
				});

			} else {

				Toast.makeText(getActivity(), "No Item added in Playlist", Toast.LENGTH_LONG).show();

			}

		}

	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_clear:

				try {

					SongsManager._songsList.clear();
					songsListData.clear();
					adapter.notifyDataSetChanged();

				} catch (Exception e) {
					Log.d("Tag", "Cannot do delete operation");

				}

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

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

					PlaySong();

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
			alertDialogBuilder.setMessage(CheckUserInfo
					.getUserInfoText());

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

					PlaySong();

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

	public void PlaySong(){

		final String[] option = new String[] {"Play now", "Remove" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.select_dialog_item, option);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setAdapter(adapter,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(
							DialogInterface dialog, int which) {

						switch (which) {

							case 0:
								PlaySingleSong();
								break;
							case 1:
								RemoveFromPlaylist();
								break;
						}
					}
				});

		// Then I do what I need with the builder
		// (except for setTitle();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.pop_up_dialog, null);
		// set values for custom dialog components -
		// text, image and button
		View view_bg = (View) dialogView.findViewById(R.id.view_bg);
		TextView track = (TextView) dialogView.findViewById(R.id.trackname);
		track.setText(songsListData.get(songIndex).get("songTitle"));
		TextView artist = (TextView) dialogView.findViewById(R.id.artist);
		artist.setText(songsListData.get(songIndex).get("songArtist"));
		ImageView image = (ImageView) dialogView.findViewById(R.id.thumb);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {

			try{

				//cover_bg = new AsyncCoverTask().execute(imageOfSelectedSong).get();
				//Bitmap blurredBitmap = BlurBuilder.blur(getActivity(), cover_bg);
				//view_bg.setBackground(new BitmapDrawable(blurredBitmap));
				//iv_BookCoverBG.setImageBitmap(blurredBitmap);

			} catch (Exception ex){
				Log.d("Error in Blurr image", ex.toString());
			}
		}

		Glide.with(getActivity()).load(songsListData.get(songIndex).get("songImage")).into(image);

		builder.setCustomTitle(dialogView);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void PlaySingleSong(){ // 1

		try {

			playerService = new Intent(getActivity(), PlayerService.class);
			playerService.putExtra("songIndex", songIndex);
			getActivity().startService(playerService);

		} catch (Exception ex){
			Log.d("Erroe while goto play", ex.toString());
		}
	}

	public void RemoveFromPlaylist(){ // 2

		try {

			HashMap<String, String> song;
			song = songsListData.get(songIndex);
			SongsManager._songsList.remove(song);
			// MainActivity.saveList.remove(song);
			songsListData.remove(songIndex);
			adapter.notifyDataSetChanged();


		} catch (Exception ex){
			Log.d("Error while remove", ex.toString());
		}
	}

	// Queue Playlist Adapter
	class QueueAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView queueTitle;
			public TextView queueArtist;
			public TextView queueAlbum;
			public TextView queueLength;
			public ImageView queueImage;
		}

		@Override
		public int getCount() {
			return songsListData.size();
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

				view = mInflater.inflate(R.layout.player_queue_item2, parent, false);
				holder = new ViewHolder();

				holder.queueImage = (ImageView) view.findViewById(R.id.iv_playlist_image);
				holder.queueTitle = (TextView) view.findViewById(R.id.tvSongName);
				holder.queueTitle.setSelected(true);
				holder.queueArtist = (TextView) view.findViewById(R.id.tvArtistName);
				holder.queueAlbum = (TextView) view.findViewById(R.id.tvAlbumName);
				holder.queueLength = (TextView) view.findViewById(R.id.playlist_item_duration);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			imageLoader.displayImage(songsListData.get(position).get("songImage"),
					holder.queueImage, options, animateFirstListener);

			holder.queueTitle.setText(songsListData.get(position).get("songTitle"));
			holder.queueArtist.setText(songsListData.get(position).get("songArtist")
					+ " - ");
			holder.queueAlbum.setText(songsListData.get(position).get("songAlbum"));
			holder.queueLength.setText(songsListData.get(position).get("songLength"));

			if(getItemId(position) == songIndex){
				view.setBackgroundColor(Color.parseColor("#EA0E12"));
			}else{
				view.setBackgroundColor(Color.TRANSPARENT);
			}

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
