package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.service.PlayerService;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.RequestTask;
import com.ebs.banglalinkbangladhol.bean.SingleAudio;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class TopBannerFragment extends Fragment {

	public TopBannerFragment() {
	}

	public ListView seeAllListView;
	public TextView catagoryTitle, catagoryTotal;
	private ProgressDialog pd;
	public String selectedMenuItem;

	public String titleOfSelectedSong, artistOfSelectedSong,
			albumOfSelectedSong, urlOfSelectedSong, imageOfSelectedSong,
			codeOfSelectedSong, lengthOfSelectedSong, codeOfSelectedAlbum;

	public String subc_audio;

	public ArrayList<HashMap<String, String>> urlArrayList;
	public ArrayList<SingleAudio> audioList;

	public HashMap<String, String> urlWithTitleHashMap = new HashMap<String, String>();
	public static String[] itemsTitleArray, itemsArtistArray, itemAlbumArray,
			itemsImageArray, itemsUrlArray, itemsLenghtArray;

	public DisplayImageOptions options;
	public ImageLoader imageLoader = ImageLoader.getInstance();

	private SongsManager songManager;
	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	private Intent playerService;
	private String imsi = "";

	private String status = "";
	private String pack = "&d=start30";
	private AlertDialog levelDialog;
	private String wifiCheck = "";
	private String subCheck = null;

	private String cat_title = null;
	private String cat_data = null;

	public static final String FRAGMENT_TAG = "TopBannerFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.audio_see_all_fragment,
				container, false);

		// ----start service.
		playerService = new Intent(getActivity(), PlayerService.class);
		playerService.putExtra("songIndex", PlayerService.currentSongIndex);

		try {

			getActivity().startService(playerService);

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
					.show();

		}

		seeAllListView = (ListView) rootView.findViewById(android.R.id.list);
		catagoryTitle = (TextView) rootView.findViewById(R.id.tv_item_title);
		catagoryTotal = (TextView) rootView.findViewById(R.id.tv_item_subtitle);

		Bundle args = getArguments();

		if (args != null) {
			// catagoryTitle.setText(args.getString("catagory"));
			selectedMenuItem = args.getString("url");
		}

		if (savedInstanceState != null) {

		} else {

			Invoke();

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

		return rootView;
	}

	public void Invoke() {
		try {

			pd = new ProgressDialog(getActivity(),
					ProgressDialog.THEME_HOLO_DARK);
			pd.setMessage("Loading Please Wait ...");
			pd.setIndeterminate(false);
			pd.setCancelable(true);
			pd.show();

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
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

					//subc_audio = HTTPGateway.getHttpResponseString(selectedMenuItem, imsi);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			audio_handler.sendEmptyMessage(0);

		}
	}

	Handler audio_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			if (subc_audio != null && subc_audio.length() > 20) {

				cat_title = HTTPGateway.getDynamicTitle(subc_audio);

				if (cat_title != null) {

					catagoryTitle.setText(cat_title);

				}

				subc_audio = HTTPGateway.getDynamicData(subc_audio);

				audioList = HTTPGateway.getDynamicAudioList(subc_audio);

				List<String> title_list = new ArrayList<String>();
				List<String> artist_list = new ArrayList<String>();
				List<String> album_list = new ArrayList<String>();
				List<String> image_list = new ArrayList<String>();
				List<String> url_list = new ArrayList<String>();
				List<String> length_list = new ArrayList<String>();

				for (int i = 0; i < audioList.size(); i++) {
					SingleAudio tmpData = audioList.get(i);
					title_list.add(tmpData.audioTitle);
					artist_list.add(tmpData.audioArtist);
					album_list.add(tmpData.audioAlbum);
					image_list.add(tmpData.audioImgUrl);
					url_list.add(tmpData.audioUrl);
					length_list.add(tmpData.audioLength);

				}

				itemsTitleArray = title_list.toArray(new String[] {});
				itemsArtistArray = artist_list.toArray(new String[] {});
				itemAlbumArray = album_list.toArray(new String[] {});
				itemsImageArray = image_list.toArray(new String[] {});
				itemsUrlArray = url_list.toArray(new String[] {});
				itemsLenghtArray = length_list.toArray(new String[] {});

				AnimationSet set = new AnimationSet(true);

				Animation animation = new AlphaAnimation(0.0f, 1.0f);
				animation.setDuration(25);
				set.addAnimation(animation);

				animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
						0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				animation.setDuration(300);
				set.addAnimation(animation);

				LayoutAnimationController controller = new LayoutAnimationController(
						set, 0.5f);

				seeAllListView.setAdapter(new AudioAdapter());
				seeAllListView.setLayoutAnimation(controller);
				int count = seeAllListView.getAdapter().getCount();
				catagoryTotal.setText(String.valueOf(count) + " songs");

				seeAllListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, final int position, long id) {
								ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

								titleOfSelectedSong = itemsTitleArray[position];
								artistOfSelectedSong = itemsArtistArray[position];
								albumOfSelectedSong = itemAlbumArray[position];
								imageOfSelectedSong = itemsImageArray[position];
								urlOfSelectedSong = itemsUrlArray[position];
								lengthOfSelectedSong = itemsLenghtArray[position];

								int urlIndex = urlOfSelectedSong.indexOf("BG");
								codeOfSelectedSong = urlOfSelectedSong
										.substring(urlIndex);

								if (imageOfSelectedSong != null
										&& imageOfSelectedSong.length() > 10) {

									int imgIndex = imageOfSelectedSong
											.lastIndexOf("/");
									if (imgIndex != -1) {
										codeOfSelectedAlbum = imageOfSelectedSong
												.substring(imgIndex + 1,
														imageOfSelectedSong
																.length() - 8);
									}

								}

								final String[] option = new String[] {
										"Play now", "Add to Playlist",
										"Go to Album" };

								ArrayAdapter<String> adapter = new ArrayAdapter<String>(
										getActivity(),
										android.R.layout.select_dialog_item,
										option);

								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setAdapter(adapter,
										new OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												switch (which) {
												case 0:

													if (status.contains("sub")) {

														try {

															wifiCheck = new RequestTask()
																	.execute(
																			"http://banglalinkdhol.com/bmusicapp_subcheck.php?"
																					+ imsi)
																	.get();

														} catch (InterruptedException e) {
															e.printStackTrace();
														} catch (ExecutionException e) {
															e.printStackTrace();
														}

														if (wifiCheck != null) {

															if (wifiCheck
																	.contains("nomsisdn")) {

																AlertDialog.Builder builder2 = new AlertDialog.Builder(
																		getActivity(),
																		AlertDialog.THEME_HOLO_DARK);
																builder2.setTitle("Important!");
																builder2.setMessage("Please use Banglalink Mobile data to use this App.");
																builder2.setNegativeButton(
																		"OK",
																		new DialogInterface.OnClickListener() {
																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int id) {
																				// do
																				// nothing
																			}
																		});
																builder2.show();

															} else if (wifiCheck
																	.contains("sub")) {

																HashMap<String, String> song = new HashMap<String, String>();
																song.put(
																		"songTitle",
																		titleOfSelectedSong);

																if (artistOfSelectedSong != null) {
																	song.put(
																			"songArtist",
																			artistOfSelectedSong);
																} else {
																	song.put(
																			"songArtist",
																			"Unknown Artist");
																}

																if (albumOfSelectedSong != null) {
																	song.put(
																			"songAlbum",
																			albumOfSelectedSong);
																} else {
																	song.put(
																			"songAlbum",
																			"Unknown Album");
																}

																song.put(
																		"songImage",
																		imageOfSelectedSong);
																song.put(
																		"songPath",
																		urlOfSelectedSong);
																song.put(
																		"songCode",
																		codeOfSelectedSong);
																song.put(
																		"songLength",
																		lengthOfSelectedSong);

																SongsManager._songsList
																		.add(song);
																int indexvalue = SongsManager._songsList
																		.size() - 1;
																playerService
																		.putExtra(
																				"songIndex",
																				indexvalue);
																try {
																	getActivity()
																			.startService(
																					playerService);
																} catch (Exception e) {
																	Log.d("Tag",
																			"Player Service not working");

																}

															}

														} else {

															Toast.makeText(
																	getActivity(),
																	"Check your Internet Connection",
																	Toast.LENGTH_LONG)
																	.show();

														}

													} else {

														try {

															subCheck = new RequestTask()
																	.execute(
																			"http://banglalinkdhol.com/bmusicapp_subcheck.php?"
																					+ imsi)
																	.get();

														} catch (InterruptedException e) {
															e.printStackTrace();
														} catch (ExecutionException e) {
															e.printStackTrace();
														}

														if (subCheck != null) {

															String[] p = subCheck
																	.split(";");
															String statusNow = p[0];
															String statusText = p[1];

															if (statusNow
																	.contains("sub")) {

																HashMap<String, String> song = new HashMap<String, String>();
																song.put(
																		"songTitle",
																		titleOfSelectedSong);

																if (artistOfSelectedSong != null) {
																	song.put(
																			"songArtist",
																			artistOfSelectedSong);
																} else {
																	song.put(
																			"songArtist",
																			"Unknown Artist");
																}

																if (albumOfSelectedSong != null) {
																	song.put(
																			"songAlbum",
																			albumOfSelectedSong);
																} else {
																	song.put(
																			"songAlbum",
																			"Unknown Album");
																}

																song.put(
																		"songImage",
																		imageOfSelectedSong);
																song.put(
																		"songPath",
																		urlOfSelectedSong);
																song.put(
																		"songCode",
																		codeOfSelectedSong);
																song.put(
																		"songLength",
																		lengthOfSelectedSong);

																SongsManager._songsList
																		.add(song);
																int indexvalue = SongsManager._songsList
																		.size() - 1;
																playerService
																		.putExtra(
																				"songIndex",
																				indexvalue);
																try {
																	getActivity()
																			.startService(
																					playerService);
																} catch (Exception e) {
																	Log.d("Tag",
																			"Player Service not working");

																}

															} else if (statusNow
																	.contains("onprocess")) {

																AlertDialog.Builder builder2 = new AlertDialog.Builder(
																		getActivity(),
																		AlertDialog.THEME_HOLO_DARK);
																builder2.setTitle(Html
																		.fromHtml("<font color='#FF6500'>Subscription</font>"));
																builder2.setMessage(statusText);
																builder2.setNegativeButton(
																		"OK",
																		new DialogInterface.OnClickListener() {
																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int id) {
																				// do
																				// nothing
																			}
																		});
																builder2.show();

															} else if (statusNow
																	.contains("expired")) {

																AlertDialog.Builder builder2 = new AlertDialog.Builder(
																		getActivity(),
																		AlertDialog.THEME_HOLO_DARK);
																builder2.setTitle(Html
																		.fromHtml("<font color='#FF6500'>Subscription</font>"));
																builder2.setMessage(statusText);
																builder2.setNegativeButton(
																		"OK",
																		new DialogInterface.OnClickListener() {
																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int id) {
																				// do
																				// nothing
																			}
																		});
																builder2.show();

															} else {

																FragmentTransaction ft = getFragmentManager()
																		.beginTransaction();
																SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
																ft.replace(
																		R.id.home_container,
																		subscriptionFragment,
																		SubscriptionFragment.FRAGMENT_TAG);
																ft.addToBackStack(SubscriptionFragment.FRAGMENT_TAG);
																ft.commit();

																// MainActivity
																// activity =
																// new
																// MainActivity();
																// activity.SubAlertBox(getActivity());

															}

														} else {

															Toast.makeText(
																	getActivity(),
																	"Check your Internet Connection",
																	Toast.LENGTH_LONG)
																	.show();

														}

													}

													break;

												case 1:

													if (status.contains("sub")) {

														try {

															wifiCheck = new RequestTask()
																	.execute(
																			"http://banglalinkdhol.com/bmusicapp_subcheck.php?"
																					+ imsi)
																	.get();

														} catch (InterruptedException e) {
															e.printStackTrace();
														} catch (ExecutionException e) {
															e.printStackTrace();
														}

														if (wifiCheck != null) {

															if (wifiCheck
																	.contains("nomsisdn")) {

																AlertDialog.Builder builder2 = new AlertDialog.Builder(
																		getActivity(),
																		AlertDialog.THEME_HOLO_DARK);
																builder2.setTitle("Important!");
																builder2.setMessage("Please use Banglalink Mobile data to use this App.");
																builder2.setNegativeButton(
																		"OK",
																		new DialogInterface.OnClickListener() {
																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int id) {
																				// do
																				// nothing
																			}
																		});
																builder2.show();

															} else if (wifiCheck
																	.contains("sub")) {

																HashMap<String, String> queue = new HashMap<String, String>();
																queue.put(
																		"songTitle",
																		titleOfSelectedSong);

																if (artistOfSelectedSong != null) {
																	queue.put(
																			"songArtist",
																			artistOfSelectedSong);
																} else {
																	queue.put(
																			"songArtist",
																			"Unknown Artist");
																}

																if (albumOfSelectedSong != null) {
																	queue.put(
																			"songAlbum",
																			albumOfSelectedSong);
																} else {
																	queue.put(
																			"songAlbum",
																			"Unknown Album");
																}

																queue.put(
																		"songImage",
																		imageOfSelectedSong);
																queue.put(
																		"songPath",
																		urlOfSelectedSong);
																queue.put(
																		"songCode",
																		codeOfSelectedSong);
																queue.put(
																		"songLength",
																		lengthOfSelectedSong);

																if (SongsManager._songsList
																		.size() < 40) {

																	if (!SongsManager._songsList
																			.contains(queue)) {

																		SongsManager._songsList
																				.add(queue);

																	}

																} else {

																	AlertDialog.Builder builder2 = new AlertDialog.Builder(
																			getActivity(),
																			AlertDialog.THEME_HOLO_DARK);
																	builder2.setTitle(Html
																			.fromHtml("<font color='#FF6500'>Limit Exceeded!</font>"));
																	builder2.setMessage("Dear user you have already added 40 songs in your playlist.");
																	builder2.setNegativeButton(
																			"OK",
																			new DialogInterface.OnClickListener() {
																				@Override
																				public void onClick(
																						DialogInterface dialog,
																						int id) {
																					// do
																					// nothing
																				}
																			});
																	builder2.show();

																}

															}

														} else {

															Toast.makeText(
																	getActivity(),
																	"Check your Internet Connection",
																	Toast.LENGTH_LONG)
																	.show();

														}

													} else {

														try {

															subCheck = new RequestTask()
																	.execute(
																			"http://banglalinkdhol.com/bmusicapp_subcheck.php?"
																					+ imsi)
																	.get();

														} catch (InterruptedException e) {
															e.printStackTrace();
														} catch (ExecutionException e) {
															e.printStackTrace();
														}

														if (subCheck != null) {

															String[] p = subCheck
																	.split(";");
															String statusNow = p[0];
															String statusText = p[1];

															if (statusNow
																	.contains("sub")) {

																HashMap<String, String> queue = new HashMap<String, String>();
																queue.put(
																		"songTitle",
																		titleOfSelectedSong);

																if (artistOfSelectedSong != null) {
																	queue.put(
																			"songArtist",
																			artistOfSelectedSong);
																} else {
																	queue.put(
																			"songArtist",
																			"Unknown Artist");
																}

																if (albumOfSelectedSong != null) {
																	queue.put(
																			"songAlbum",
																			albumOfSelectedSong);
																} else {
																	queue.put(
																			"songAlbum",
																			"Unknown Album");
																}

																queue.put(
																		"songImage",
																		imageOfSelectedSong);
																queue.put(
																		"songPath",
																		urlOfSelectedSong);
																queue.put(
																		"songCode",
																		codeOfSelectedSong);
																queue.put(
																		"songLength",
																		lengthOfSelectedSong);

																if (SongsManager._songsList
																		.size() < 40) {

																	if (!SongsManager._songsList
																			.contains(queue)) {

																		SongsManager._songsList
																				.add(queue);

																	}

																} else {

																	AlertDialog.Builder builder2 = new AlertDialog.Builder(
																			getActivity(),
																			AlertDialog.THEME_HOLO_DARK);
																	builder2.setTitle(Html
																			.fromHtml("<font color='#FF6500'>Limit Exceeded!</font>"));
																	builder2.setMessage("Dear user you have already added 40 songs in your playlist.");
																	builder2.setNegativeButton(
																			"OK",
																			new DialogInterface.OnClickListener() {
																				@Override
																				public void onClick(
																						DialogInterface dialog,
																						int id) {
																					// do
																					// nothing
																				}
																			});
																	builder2.show();

																}

															} else if (statusNow
																	.contains("onprocess")) {

																AlertDialog.Builder builder2 = new AlertDialog.Builder(
																		getActivity(),
																		AlertDialog.THEME_HOLO_DARK);
																builder2.setTitle(Html
																		.fromHtml("<font color='#FF6500'>Subscription</font>"));
																builder2.setMessage(statusText);
																builder2.setNegativeButton(
																		"OK",
																		new DialogInterface.OnClickListener() {
																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int id) {
																				// do
																				// nothing
																			}
																		});
																builder2.show();

															} else if (statusNow
																	.contains("expired")) {

																AlertDialog.Builder builder2 = new AlertDialog.Builder(
																		getActivity(),
																		AlertDialog.THEME_HOLO_DARK);
																builder2.setTitle(Html
																		.fromHtml("<font color='#FF6500'>Subscription</font>"));
																builder2.setMessage(statusText);
																builder2.setNegativeButton(
																		"OK",
																		new DialogInterface.OnClickListener() {
																			@Override
																			public void onClick(
																					DialogInterface dialog,
																					int id) {
																				// do
																				// nothing
																			}
																		});
																builder2.show();

															} else {

																FragmentTransaction ft = getFragmentManager()
																		.beginTransaction();
																SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
																ft.replace(
																		R.id.home_container,
																		subscriptionFragment,
																		SubscriptionFragment.FRAGMENT_TAG);
																ft.addToBackStack(SubscriptionFragment.FRAGMENT_TAG);
																ft.commit();

																// MainActivity
																// activity =
																// new
																// MainActivity();
																// activity.SubAlertBox(getActivity());

															}

														} else {

															Toast.makeText(
																	getActivity(),
																	"Check your Internet Connection",
																	Toast.LENGTH_LONG)
																	.show();

														}

													}

													break;

												case 2:

													if (codeOfSelectedAlbum != null
															&& codeOfSelectedAlbum
																	.length() > 7) {

														FragmentTransaction ft = getFragmentManager()
																.beginTransaction();
														AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
														Bundle args = new Bundle();
														args.putString(
																"albumCode",
																codeOfSelectedAlbum);
														args.putString(
																"albumName",
																albumOfSelectedSong);
														// args.putString("albumGenre",
														// selectedGenre);
														// args.putString("albumPublisher",
														// selectedPublisher);
														args.putString(
																"albumImage",
																imageOfSelectedSong);

														albumDetailsFragment
																.setArguments(args);
														//ft.setCustomAnimations(
																//R.anim.slide_in_left,
																//R.anim.slide_out_right);
														ft.replace(
																R.id.home_container,
																albumDetailsFragment,
																AlbumDetailsFragment.FRAGMENT_TAG);
														ft.addToBackStack(AlbumDetailsFragment.FRAGMENT_TAG);
														ft.commit();

													} else {

														Toast.makeText(
																getActivity(),
																"Album not available.",
																Toast.LENGTH_LONG)
																.show();
													}

													break;

												}

											}
										});

								// Then I do what I need with the builder
								// (except for setTitle();
								LayoutInflater inflater = getActivity()
										.getLayoutInflater();
								View dialogView = inflater.inflate(
										R.layout.pop_up_dialog, null);
								// set values for custom dialog components -
								// title, image and button
								TextView track = (TextView) dialogView
										.findViewById(R.id.trackname);
								track.setText(titleOfSelectedSong);
								TextView artist = (TextView) dialogView
										.findViewById(R.id.artist);
								artist.setText(artistOfSelectedSong);
								ImageView image = (ImageView) dialogView
										.findViewById(R.id.thumb);
								imageLoader.displayImage(imageOfSelectedSong,
										image, options, animateFirstListener);

								builder.setCustomTitle(dialogView);

								AlertDialog alert = builder.create();
								alert.show();

							}
						});

			} else {
				Toast.makeText(getActivity(),
						"Couldn't Connect, Please Try Again", Toast.LENGTH_LONG)
						.show();
			}
		}

	};

	class AudioAdapter extends BaseAdapter {

		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text, text2, text3, text4;
			public ImageView image;
		}

		@Override
		public int getCount() {

			return itemsUrlArray.length;
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

			holder.text.setText(itemsTitleArray[position]);
			holder.text2.setText(itemsArtistArray[position] + "-");
			holder.text3.setText(itemAlbumArray[position]);
			holder.text4.setText(itemsLenghtArray[position]);

			imageLoader.displayImage(itemsImageArray[position], holder.image,
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
		/*if (!PlayerService.mediaPlayer.isPlaying()) {
			getActivity().stopService(playerService);
			cancelNotification();
		}*/

		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	// -- Cancel Notification
	/*public void cancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getActivity()
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(PlayerService.NOTIFICATION_ID);
	}*/

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
