package com.ebs.banglalinkbangladhol.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.MainActivity;
import com.ebs.banglalinkbangladhol.adapter.RecyclerViewDataAdapter;
import com.ebs.banglalinkbangladhol.bean.BlurBuilder;
import com.ebs.banglalinkbangladhol.others.AsyncCoverTask;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.ebs.banglalinkbangladhol.receiver.NotificationBroadcast;
import com.ebs.banglalinkbangladhol.util.FileUtils;
import com.ebs.banglalinkbangladhol.util.UtilFunctions;
import com.ebs.banglalinkbangladhol.util.Utilities;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
		MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnErrorListener, OnClickListener, OnSeekBarChangeListener,
		MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {

	//Handle incoming phone calls
	private boolean ongoingCall = false;
	private PhoneStateListener phoneStateListener;
	private TelephonyManager telephonyManager;

	public static final String ACTION_PLAY = "com.ebs.banglalinkbangladhol.ACTION_PLAY";
	public static final String ACTION_PAUSE = "com.ebs.banglalinkbangladhol.ACTION_PAUSE";
	public static final String ACTION_PREVIOUS = "com.ebs.banglalinkbangladhol.ACTION_PREVIOUS";
	public static final String ACTION_NEXT = "com.ebs.banglalinkbangladhol.ACTION_NEXT";
	public static final String ACTION_STOP = "com.ebs.banglalinkbangladhol.ACTION_STOP";

	private static boolean currentVersionSupportBigNotification = false;
	private static boolean currentVersionSupportLockScreenControls = false;
	BroadcastReceiver mReceiver;

	private ComponentName remoteComponentName;
	private RemoteControlClient remoteControlClient;

	RemoteViews simpleContentView, expandedView;
	Notification notification;
	private static final int NOTIFICATION_ID = 101;
	private NotificationManager mNotificationManager;

	//MediaSession
	//private MediaSessionManager mediaSessionManager;
	//private MediaSessionCompat mediaSession;
	//private MediaControllerCompat.TransportControls transportControls;
    //msongTotalDuratonLabel

	public static WeakReference<ImageView> btnPlay, btnNext, btnPrevious,
			btnRepeat, btnShuffle, btnQueue, btnRbt, btnAlbum;
	public static WeakReference<ImageView> mSongThumb, fSongThumb,
			mSongBtnPlay;
	private WeakReference<SeekBar> songProgressBar;
	private WeakReference<ProgressBar> songLoadingBar;
	private WeakReference<SlidingUpPanelLayout> mLayout;
	private WeakReference<TextView> msongTitleLabel, msongArtistLabel,
			msongDuratonLabel;
	private WeakReference<TextView> fsong_album, fsong_title, fsong_artist,
			fsongCurrentDurationLabel, fsongTotalDurationLabel;
	public static WeakReference<RelativeLayout> miniLayout, fullUpperLayout, songProgressLayout;
	public static WeakReference<View> fullLayout;
	private WeakReference<FrameLayout> frameLayout;
	public static MediaPlayer mediaPlayer;
	private Handler progressBarHandler = new Handler();
	private Utilities utils;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private boolean isPause = false;
	private ArrayList<HashMap<String, String>> songsListingSD = new ArrayList<HashMap<String, String>>();
	public static int currentSongIndex = -1;

	public static final String MyPREFERENCES = "MyPrefs";
	public static final String Index = "indexmKey";
	public static final String Title = "titleKey";
	public static final String Artist = "artistKey";
	public static final String Album = "albumKey";
	public static final String AlbumCode = "albumCodeKey";
	public static final String Image = "imageKey";
	public static final String SongUrl = "urlKey";
	public static final String Code = "codeKey";
	public static final String Length = "lengthKey";

	private SharedPreferences sharedpreference;

	private byte[] art;
	private String code = "";
	/**
	 * Object used for PlaybackService startup waiting.
	 */
	private static final Object[] sWait = new Object[0];
	/**
	 * The appplication-wide instance of the PlaybackService.
	 */
	//public static PlayerService sInstance;


	private AudioManager mAudioManager;

	private int resumePosition;
	private ProgressDialog pDialog;
	public static String codeAlbum, nameAlbum, artistAlbum, imageAlbum;
	private HttpURLConnection httpConnection;

	private final IBinder iBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return iBinder;
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
				KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
				if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
					return;

				switch (keyEvent.getKeyCode()) {
					case KeyEvent.KEYCODE_HEADSETHOOK:
					case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

						break;
					case KeyEvent.KEYCODE_MEDIA_PLAY:
						break;
					case KeyEvent.KEYCODE_MEDIA_PAUSE:
						break;
					case KeyEvent.KEYCODE_MEDIA_STOP:
						break;
					case KeyEvent.KEYCODE_MEDIA_NEXT:

						break;
					case KeyEvent.KEYCODE_MEDIA_PREVIOUS:

						break;
				}

			} else {

				if (intent.getAction().equals(PlayerService.ACTION_PLAY)) {

					MediaPlayer_play_pause();

				} else if (intent.getAction().equals(PlayerService.ACTION_PAUSE)) {

					MediaPlayer_play_pause();

				} else if (intent.getAction().equals(PlayerService.ACTION_NEXT)) {

					MediaPlayer_Next();

				} else if (intent.getAction().equals(PlayerService.ACTION_STOP)) {

					Close_App(context);

				} else if (intent.getAction().equals(PlayerService.ACTION_PREVIOUS)) {

					MediaPlayer_Previous();

				}
			}
		}

		// constructor
		public MyReceiver() {

		}
	}

	@Override
	public void onCreate() {

		initUI();

		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_PLAY);
		filter.addAction(ACTION_PAUSE);
		filter.addAction(ACTION_NEXT);
		filter.addAction(ACTION_PREVIOUS);
		filter.addAction(ACTION_STOP);

		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);

		currentVersionSupportBigNotification = UtilFunctions
				.currentVersionSupportBigNotification();
		currentVersionSupportLockScreenControls = UtilFunctions
				.currentVersionSupportLockScreenControls();
		// Manage incoming phone calls during playback.
		// Pause MediaPlayer on incoming call,
		// Resume on hangup.
		callStateListener();
		//ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
		registerBecomingNoisyReceiver();

		initMediaPlayer();

		utils = new Utilities();

		try {
			//songsListingSD = HomeFragment.songsList;
			songsListingSD = SongsManager._songsList;
		} catch (Exception ex) {

			Log.d("Tag", "Error");
		}

		fsongCurrentDurationLabel = new WeakReference<TextView>(MainActivity.songCurrentDurationLabel);

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		//mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		/*sInstance = this;
		synchronized (sWait) {
			sWait.notifyAll();
		}*/

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null) {

			initUI();

			try {

				int songIndex = intent.getIntExtra("songIndex", 0);

				if (songIndex != currentSongIndex) {

					playSong(songIndex);
					initNotification(songIndex);
					//buildNotification(PlaybackStatus.PLAYING, songIndex);
					currentSongIndex = songIndex;

					/*if (mediaSessionManager == null) {
						try {
							initMediaSession();
							//initMediaPlayer();
						} catch (RemoteException e) {
							e.printStackTrace();
							stopSelf();
						}
						buildNotification(PlaybackStatus.PLAYING, songIndex);
					}*/

				} else if (currentSongIndex != -1) {
					// msong Title
					msongTitleLabel.get().setText(
							songsListingSD.get(currentSongIndex).get("songTitle"));
					// msong Artist
					msongArtistLabel.get().setText(
							songsListingSD.get(currentSongIndex).get("songArtist"));
					// fsong Album Name
					fsong_album.get().setText(
							songsListingSD.get(currentSongIndex).get("songAlbum"));
					// fsong Title
					fsong_title.get().setText(
							songsListingSD.get(currentSongIndex).get("songTitle"));
					// fsong Artist
					fsong_artist.get().setText(
							songsListingSD.get(currentSongIndex).get("songArtist"));

					if (mediaPlayer.isPlaying()) {
						btnPlay.get().setImageResource(R.drawable.player_pause);
						mSongBtnPlay.get().setImageResource(R.drawable.new_minipause_btn);
					} else {
						btnPlay.get().setImageResource(R.drawable.player_play);
						mSongBtnPlay.get().setImageResource(R.drawable.new_miniplay_btn);

						// sometimes required to clear notification
						// mNotificationManager.cancel(NOTIFICATION_ID);
					}

				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				stopSelf();
			}

			//Request audio focus
			if (requestAudioFocus() == false) {
				//Could not gain focus
				stopSelf();
			}

			//Handle Intent action from MediaSession.TransportControls
			//handleIncomingActions(intent);

		}
		return START_NOT_STICKY;
		// START_NOT_STICKY //START_STICKY
		//return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		//mediaSession.release();
		removeNotification();
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//sInstance = null;

		try {

			currentSongIndex = -1;
			if (mediaPlayer != null) {
				stopMedia();
				mediaPlayer.release();
			}

			//Disable the PhoneStateListener
			if (phoneStateListener != null) {
				telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
			}

			removeNotification();
			//unregister BroadcastReceivers
			unregisterReceiver(becomingNoisyReceiver);
            unregisterReceiver(mReceiver);
			// Remove progress bar update Hanlder callBacks
			progressBarHandler.removeCallbacks(mUpdateTimeTask);
			Log.d("Player Service", "Player Service Stopped");
			removeAudioFocus();

		} catch (Exception ex){
			Log.d("Error", "onDestroy " + ex.toString());
		}
	}

	public class LocalBinder extends Binder{

		public  PlayerService getService(){
			return PlayerService.this;
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {

		songProgressBar.get().setSecondaryProgress(percent);

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {

		// check for repeat is ON or OFF
		if (isRepeat) {
			// repeat is on play same song again
			playSong(currentSongIndex);
			initNotification(currentSongIndex);
			//buildNotification(PlaybackStatus.PLAYING, currentSongIndex);

		} else if (isShuffle) {
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsListingSD.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
			initNotification(currentSongIndex);
			//buildNotification(PlaybackStatus.PLAYING, currentSongIndex);

		} else {

			if (SongsManager._songsList.size() > 0) {

				// no repeat or shuffle ON - play next song
				if (currentSongIndex < (songsListingSD.size() - 1)) {
					playSong(currentSongIndex + 1);
					initNotification(currentSongIndex + 1);
					//buildNotification(PlaybackStatus.PLAYING, currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				} else {
					// play first song
					playSong(0);
					initNotification(0);
					//buildNotification(PlaybackStatus.PLAYING, 0);
					currentSongIndex = 0;
				}

			} else {

				Toast.makeText(getApplicationContext(),
						"No Song Added to Queue", Toast.LENGTH_SHORT).show();

			}

		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {
			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK" + extra);
				break;
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
				Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED" + extra);
				break;
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN" + extra);
				break;
		}

		return false;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
        songProgressLayout.get().setVisibility(View.GONE);
       // mSongBtnPlay.get().setVisibility(View.VISIBLE);
		playMedia();
		//
		// mp.start();

	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAudioFocusChange(int focusChange) {

		switch (focusChange){

			case AudioManager.AUDIOFOCUS_GAIN:
				//resume playback
				/*if(mediaPlayer == null) initMediaPlayer();
				else if(!mediaPlayer.isPlaying()) mediaPlayer.start();
				mediaPlayer.setVolume(1.0f, 1.0f);*/
				//Toast.makeText(getApplicationContext(), "AUDIOFOCUS_GAIN", Toast.LENGTH_LONG).show();

				if(mediaPlayer == null) initMediaPlayer();
				if (mediaPlayer != null) {
					if(!isPause){
						resumeMedia();
						mediaPlayer.setVolume(1.0f, 1.0f);
					}
				}

				break;

			case AudioManager.AUDIOFOCUS_LOSS:
				// Lost focus for an unbounded amount of time: stop playback and release media player
				/*if (mediaPlayer.isPlaying()) mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;*/

				if (mediaPlayer != null) {
					pauseMedia();
					resumePosition = mediaPlayer.getCurrentPosition();
					//mediaPlayer.release();
					//mediaPlayer = null;
				}

				//Toast.makeText(getApplicationContext(), "AUDIOFOCUS_LOSS", Toast.LENGTH_LONG).show();

				break;
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
				// Lost focus for a short time, but we have to stop
				// playback. We don't release the media player because playback
				// is likely to resume
				//if (mediaPlayer.isPlaying()) mediaPlayer.pause();
				if (mediaPlayer != null) {
					pauseMedia();
				}
				//Toast.makeText(getApplicationContext(), "AUDIOFOCUS_LOSS_TRANSIENT", Toast.LENGTH_LONG).show();
				break;
			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
				// Lost focus for a short time, but it's ok to keep playing
				// at an attenuated level
				//if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);

				if (mediaPlayer != null) {
					if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
				}
				//Toast.makeText(getApplicationContext(), "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK", Toast.LENGTH_LONG).show();
				break;
		}

	}

	private boolean requestAudioFocus() {
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			//Focus gained
			return true;
		}
		//Could not gain focus
		return false;
	}

	private boolean removeAudioFocus() {
		return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this);
	}

	/**
	 * ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs
	 */
	private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//pause audio on ACTION_AUDIO_BECOMING_NOISY
			pauseMedia();
			//buildNotification(PlaybackStatus.PAUSED);
			//buildNotification(PlaybackStatus.PAUSED, currentSongIndex);
		}
	};

	private void registerBecomingNoisyReceiver() {
		//register after getting audio focus
		IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
		registerReceiver(becomingNoisyReceiver, intentFilter);
	}

	/**
	 * Handle PhoneState changes
	 */
	private void callStateListener() {
		// Get the telephony manager
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//Starting listening for PhoneState changes
		phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
					//if at least one call exists or the phone is ringing
					//pause the MediaPlayer
					case TelephonyManager.CALL_STATE_OFFHOOK:
					case TelephonyManager.CALL_STATE_RINGING:
						if (mediaPlayer != null) {
							pauseMedia();
							ongoingCall = true;
						}
						//Toast.makeText(getApplicationContext(), "CALL_STATE_RINGING", Toast.LENGTH_LONG).show();
						break;
					case TelephonyManager.CALL_STATE_IDLE:
						// Phone idle. Start playing.
						if (mediaPlayer != null) {
							if (ongoingCall && !isPause) {
								ongoingCall = false;
								resumeMedia();
							}
						}
						//Toast.makeText(getApplicationContext(), "CALL_STATE_IDLE", Toast.LENGTH_LONG).show();
						break;
				}
			}
		};
		// Register the listener with the telephony manager
		// Listen for changes to the device call state.
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public void initMediaPlayer() {

		mediaPlayer = new MediaPlayer();
		//Set up MediaPlayer event listeners
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnInfoListener(this);
		mediaPlayer.reset();

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	@SuppressLint("NewApi")
	private void playMedia(){

		try {

			if(mediaPlayer == null) return;

			if(!mediaPlayer.isPlaying()){
				mediaPlayer.start();
				btnPlay.get().setImageResource(R.drawable.player_pause);
				mSongBtnPlay.get().setImageResource(R.drawable.new_minipause_btn);

				simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
				simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);
				if (currentVersionSupportBigNotification) {
					expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
					expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
				}

				notification.contentView = simpleContentView;
				if (currentVersionSupportBigNotification) {
					notification.bigContentView = expandedView;
				}
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				startForeground(NOTIFICATION_ID, notification);
			}

		} catch(Exception ex){
			Log.d("Error in ", "playMedia - " + ex.toString());
		}

	}

	private void stopMedia(){

		try {

			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				btnPlay.get().setImageResource(R.drawable.player_play);
				mSongBtnPlay.get().setImageResource(R.drawable.new_miniplay_btn);

				simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
				simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
				if (currentVersionSupportBigNotification) {
					expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
					expandedView.setViewVisibility(R.id.btnPause, View.GONE);
				}

				/*notification.contentView = simpleContentView;
				if (currentVersionSupportBigNotification) {
					notification.bigContentView = expandedView;
				}
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				startForeground(NOTIFICATION_ID, notification);*/
			}

		} catch(Exception ex){
			Log.d("Error in ", "stopMedia - " + ex.toString());
		}

	}

	@SuppressLint("NewApi")
	private void pauseMedia(){

		try {

			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
				isPause = true;
				resumePosition = mediaPlayer.getCurrentPosition();
				btnPlay.get().setImageResource(R.drawable.player_play);
				mSongBtnPlay.get().setImageResource(R.drawable.new_miniplay_btn);

				simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
				simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
				if (currentVersionSupportBigNotification) {
					expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
					expandedView.setViewVisibility(R.id.btnPause, View.GONE);
				}

				notification.contentView = simpleContentView;
				if (currentVersionSupportBigNotification) {
					notification.bigContentView = expandedView;
				}
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				startForeground(NOTIFICATION_ID, notification);
			}

		} catch(Exception ex){
			Log.d("Error in ", "pauseMedia - " + ex.toString());
		}

	}

	@SuppressLint("NewApi")
	private void resumeMedia(){

		try {

			if(mediaPlayer == null) return;

			if(!mediaPlayer.isPlaying()){

				mediaPlayer.seekTo(resumePosition);
				mediaPlayer.start();
				btnPlay.get().setImageResource(R.drawable.player_pause);
				mSongBtnPlay.get().setImageResource(R.drawable.new_minipause_btn);

				simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
				simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);
				if (currentVersionSupportBigNotification) {
					expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
					expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
				}

				notification.contentView = simpleContentView;
				if (currentVersionSupportBigNotification) {
					notification.bigContentView = expandedView;
				}
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				startForeground(NOTIFICATION_ID, notification);
			}

		} catch(Exception ex){
			Log.d("Error in ", "resumeMedia - " + ex.toString());
		}

	}

	/**
	 * MediaSession and Notification actions
	 */
	/*private void initMediaSession() throws RemoteException {
		if (mediaSessionManager != null) return; //mediaSessionManager exists

		mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
		// Create a new MediaSession
		mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
		//Get MediaSessions transport controls
		transportControls = mediaSession.getController().getTransportControls();
		//set MediaSession -> ready to receive media commands
		mediaSession.setActive(true);
		//indicate that the MediaSession handles transport control commands
		// through its MediaSessionCompat.Callback.
		mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

		//Set mediaSession's MetaData
		updateMetaData();

		// Attach Callback to receive MediaSession updates
		mediaSession.setCallback(new MediaSessionCompat.Callback() {
			// Implement callbacks
			@Override
			public void onPlay() {
				super.onPlay();

				resumeMedia();
				//buildNotification(PlaybackStatus.PLAYING);
				buildNotification(PlaybackStatus.PLAYING, currentSongIndex);
			}

			@Override
			public void onPause() {
				super.onPause();

				pauseMedia();
				//buildNotification(PlaybackStatus.PAUSED);
				buildNotification(PlaybackStatus.PAUSED, currentSongIndex);
			}

			@Override
			public void onSkipToNext() {
				super.onSkipToNext();

				MediaPlayer_Next();
				updateMetaData();
				//buildNotification(PlaybackStatus.PLAYING);
				buildNotification(PlaybackStatus.PLAYING, currentSongIndex);
			}

			@Override
			public void onSkipToPrevious() {
				super.onSkipToPrevious();

				MediaPlayer_Previous();
				updateMetaData();
				//buildNotification(PlaybackStatus.PLAYING);
				buildNotification(PlaybackStatus.PLAYING, currentSongIndex);
			}

			@Override
			public void onStop() {
				super.onStop();
				removeNotification();
				//Stop the service
				stopSelf();
			}

			@Override
			public void onSeekTo(long position) {
				super.onSeekTo(position);
			}
		});
	}*/

	/*private void updateMetaData() {
		Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
				R.drawable.no_img); //replace with medias albumArt
		// Update the current metadata
		mediaSession.setMetadata(new MediaMetadataCompat.Builder()
				.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
				.putString(MediaMetadataCompat.METADATA_KEY_ARTIST,
						songsListingSD.get(currentSongIndex).get("songArtist"))
				.putString(MediaMetadataCompat.METADATA_KEY_ALBUM,
						songsListingSD.get(currentSongIndex).get("songAlbum"))
				.putString(MediaMetadataCompat.METADATA_KEY_TITLE,
						songsListingSD.get(currentSongIndex).get("songTitle"))
				.build());
	}*/

	/*private void buildNotification(PlaybackStatus playbackStatus, int songIndex) {

		int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
		PendingIntent play_pauseAction = null;

		//Build a new notification according to the current state of the MediaPlayer
		if (playbackStatus == PlaybackStatus.PLAYING) {
			notificationAction = android.R.drawable.ic_media_pause;
			//create the pause action
			play_pauseAction = playbackAction(1);
		} else if (playbackStatus == PlaybackStatus.PAUSED) {
			notificationAction = android.R.drawable.ic_media_play;
			//create the play action
			play_pauseAction = playbackAction(0);
		}

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.no_img); //replace with your own image

		// Create a new Notification
		NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
				// Hide the timestamp
				.setShowWhen(false)
				// Set the Notification style
				.setStyle(new NotificationCompat.MediaStyle()
						// Attach our MediaSession token
						.setMediaSession(mediaSession.getSessionToken())
						// Show our playback controls in the compat view
						.setShowActionsInCompactView(0, 1, 2))
				// Set the Notification color
				.setColor(getResources().getColor(R.color.colorPrimary))
				// Set the large and small icons
				.setLargeIcon(largeIcon)
				.setSmallIcon(android.R.drawable.stat_sys_headset)
				// Set Notification content information
				.setContentText(songsListingSD.get(songIndex).get("songArtist"))
				.setContentTitle(songsListingSD.get(songIndex).get("songAlbum"))
				.setContentInfo(songsListingSD.get(songIndex).get("songTitle"))
				// Add playback actions
				.addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
				.addAction(notificationAction, "pause", play_pauseAction)
				.addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
	}*/

	/*private PendingIntent playbackAction(int actionNumber) {

		Intent playbackAction = new Intent(this, PlayerService.class);
		switch (actionNumber) {
			case 0:
				// Play
				playbackAction.setAction(ACTION_PLAY);
				return PendingIntent.getService(this, actionNumber, playbackAction, 0);
			case 1:
				// Pause
				playbackAction.setAction(ACTION_PAUSE);
				return PendingIntent.getService(this, actionNumber, playbackAction, 0);
			case 2:
				// Next track
				playbackAction.setAction(ACTION_NEXT);
				return PendingIntent.getService(this, actionNumber, playbackAction, 0);
			case 3:
				// Previous track
				playbackAction.setAction(ACTION_PREVIOUS);
				return PendingIntent.getService(this, actionNumber, playbackAction, 0);
			default:
				break;
		}
		return null;
	}*/

	private void removeNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	/*private void handleIncomingActions(Intent playbackAction) {
		if (playbackAction == null || playbackAction.getAction() == null) return;

		String actionString = playbackAction.getAction();
		if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
			transportControls.play();
		} else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
			transportControls.pause();
		} else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
			transportControls.skipToNext();
		} else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
			transportControls.skipToPrevious();
		} else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
			transportControls.stop();
		}
	}*/

	/**
	 * Init UI
	 */
	public void initUI() {

		frameLayout = new WeakReference<FrameLayout>(MainActivity.frameLayout);

		msongTitleLabel = new WeakReference<TextView>(MainActivity.mPlayer_title);
		msongArtistLabel = new WeakReference<TextView>(MainActivity.mPlayer_artist);
		msongDuratonLabel = new WeakReference<TextView>(MainActivity.mPlayer_duration);
		//msongTotalDuratonLabel = new WeakReference<TextView>(MainActivity.mPlayer_totalduration);

		fsong_album = new WeakReference<TextView>(MainActivity.fPlayer_album);
		fsong_title = new WeakReference<TextView>(MainActivity.fPlayer_title);
		fsong_artist = new WeakReference<TextView>(MainActivity.fPlayer_artist);
		fsongCurrentDurationLabel = new WeakReference<TextView>(MainActivity.songCurrentDurationLabel);
		fsongTotalDurationLabel = new WeakReference<TextView>(MainActivity.songTotalDurationLabel);
		mLayout = new WeakReference<SlidingUpPanelLayout>(MainActivity.mLayout);
		mSongThumb = new WeakReference<ImageView>(MainActivity.mPlayerThumb);
		fSongThumb = new WeakReference<ImageView>(MainActivity.fPlayerThumb);
		btnPlay = new WeakReference<ImageView>(MainActivity.btnPlay);
		btnNext = new WeakReference<ImageView>(MainActivity.btnNext);
		btnPrevious = new WeakReference<ImageView>(MainActivity.btnPrevious);
		btnRepeat = new WeakReference<ImageView>(MainActivity.btnRepeat);
		btnShuffle = new WeakReference<ImageView>(MainActivity.btnShuffle);
		btnRbt = new WeakReference<ImageView>(MainActivity.btnRBT);
		//btnAlbum = new WeakReference<ImageView>(MainActivity.btnAlbum);
		mSongBtnPlay = new WeakReference<ImageView>(MainActivity.mPlayer_btnPlay);
		// mSongBtnPlay.get().setVisibility(View.VISIBLE);

		miniLayout = new WeakReference<RelativeLayout>(MainActivity.miniPlayerLayout);
		fullUpperLayout = new WeakReference<RelativeLayout>(MainActivity.fullPlayerUpperLayout);
		songProgressLayout = new WeakReference<RelativeLayout>(MainActivity.songProgressLayout);
		fullLayout = new WeakReference<View>(MainActivity.fullPlayer);
		btnPlay.get().setOnClickListener(this);
		btnNext.get().setOnClickListener(this);
		btnPrevious.get().setOnClickListener(this);
		btnRepeat.get().setOnClickListener(this);
		btnShuffle.get().setOnClickListener(this);
		btnRbt.get().setOnClickListener(this);
		//btnAlbum.get().setOnClickListener(this);
		mSongBtnPlay.get().setOnClickListener(this);
		// TODO Auto-generated method stub

		songProgressBar = new WeakReference<SeekBar>(MainActivity.songProgressBar);
		songProgressBar.get().setOnSeekBarChangeListener(this);

		songProgressBar.get().setEnabled(false);
		songProgressBar.get().setClickable(false);

		songLoadingBar = new WeakReference<ProgressBar>(MainActivity.mProgressBar);

		// songLoadingBar.get().setVisibility(View.VISIBLE);

		sharedpreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		if (sharedpreference.contains(Title)) {
			msongTitleLabel.get().setText(sharedpreference.getString(Title, ""));
			fsong_title.get().setText(sharedpreference.getString(Title, ""));
		}

		if (sharedpreference.contains(Artist)) {
			msongArtistLabel.get().setText(sharedpreference.getString(Artist, ""));
			fsong_artist.get().setText(sharedpreference.getString(Artist, ""));
		}

		if (sharedpreference.contains(Album)) {
			nameAlbum = sharedpreference.getString(Album, "");
			fsong_album.get().setText(sharedpreference.getString(Album, ""));

		}

		if (sharedpreference.contains(AlbumCode)) {
			codeAlbum = sharedpreference.getString(AlbumCode, "");
		}

		if (sharedpreference.contains(Image)) {
			imageAlbum = sharedpreference.getString(Image, "");
			String saveImage = sharedpreference.getString(Image, "");
		}

		if (sharedpreference.contains(SongUrl)) {
			String savedUrl = sharedpreference.getString(SongUrl, "");

		}

		if (sharedpreference.contains(Code)) {
			code = sharedpreference.getString(Code, "");

		}

		if (sharedpreference.contains(Length)) {
			String savedLength = sharedpreference.getString(Length, "");
		}

		if (sharedpreference.contains(Index)) {
			// currentSongIndex = sharedpreference.getInt(Index, 0);
		}

		String previouslyEncodedImage = sharedpreference.getString(
				"image_data", "");
		if (!previouslyEncodedImage.equalsIgnoreCase("")) {
			byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			mSongThumb.get().setImageBitmap(bitmap);
			fSongThumb.get().setImageBitmap(bitmap);
		}

	}

	// -------------------------------------------------------------//

	/**
	 * @author www.9android.net
	 * @param songIndex
	 *            : index of song
	 */
	public void playSong(int songIndex) {

		try {

			if(CheckUserInfo.getUserPlayStatus() == 0){
				return;
			}

			if (mLayout.get().getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
				mLayout.get().setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
				RecyclerViewDataAdapter.footer.setVisibility(View.INVISIBLE);
			}
			// Displaying Song title
			String songTitle = songsListingSD.get(songIndex).get("songTitle");
			if (songTitle != null) {
				msongTitleLabel.get().setText(songTitle);
			} else {
				msongTitleLabel.get().setText("No Title Found");
			}
			// Displaying Song Artist
			String songArtist = songsListingSD.get(songIndex).get("songArtist");
			msongArtistLabel.get().setText(songArtist);
			fsong_artist.get().setText(songArtist);
			artistAlbum = songArtist;

			// Displaying Song Album
			String songAlbum = songsListingSD.get(songIndex).get("songAlbum");
			if (songAlbum != null) {
				fsong_album.get().setText(songAlbum);
				nameAlbum = songAlbum;
			} else {
				fsong_album.get().setText("Unknown Album");
			}

			String songAlbumCode = songsListingSD.get(songIndex).get("songAlbumCode");
			if (songAlbumCode != null) {
				codeAlbum = songAlbumCode;
			}

			// Displaying Song title
			if (songTitle != null) {
				fsong_title.get().setText(songTitle);
			} else {
				fsong_title.get().setText("Unknown Title");
			}

			// Displaying mSong Thumb
			String songImage = songsListingSD.get(songIndex).get("songImage");
			
			if(songImage!= null){
				imageAlbum = songImage;
			}
			
			String songCode = songsListingSD.get(songIndex).get("songCode");

			new DownloadImageTask(mSongThumb).execute(songImage);
			new DownloadImageTask(fSongThumb).execute(songImage);
			// save playlist data in json formate
			JSONArray result = new JSONArray(songsListingSD);

			// SharedPrefData
			Editor editor = sharedpreference.edit();
			editor.putString(Title, songTitle);
			editor.putString(Artist, songArtist);
			editor.putString(Album, songAlbum);
			editor.putString(AlbumCode, codeAlbum);
			editor.putString(Image, songImage);
			editor.putString(Code, songCode);
			editor.putString(Length, songsListingSD.get(songIndex).get("songLength"));
			editor.putString(SongUrl, songsListingSD.get(songIndex).get("songPath"));
			editor.commit();

			songProgressBar.get().setEnabled(true);
			songProgressBar.get().setClickable(true);

			String urlFromIndex = songsListingSD.get(songIndex).get("songPath");
			String finalUrl = "";
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

				finalUrl = urlFromIndex + "&username=" + CheckUserInfo.getUserMsisdn() +
						"&token=" + CheckUserInfo.getUserToken(); // higher device

			} else {

				finalUrl = urlFromIndex + "?token=" + CheckUserInfo.getUserToken(); // lower device

			}

			mediaPlayer.reset();
			mediaPlayer.setDataSource(finalUrl);
			mediaPlayer.prepare();

			//mediaPlayer.prepareAsync();
			//mediaPlayer.start();
			// Changing Button Image to pause image
			btnPlay.get().setImageResource(R.drawable.player_pause);
			mSongBtnPlay.get().setImageResource(R.drawable.new_minipause_btn);
			// set Progress bar values

			songProgressBar.get().setProgress(0);
			songProgressBar.get().setMax(100);
			// Updating progress bar
			updateProgressBar();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// -------------------------------------------------------------------------//
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.mplayer_btn_play:

			MediaPlayer_play_pause();

			break;

		case R.id.playerBtnPlay:

			MediaPlayer_play_pause();

			break;

		case R.id.playerBtnNext:

			MediaPlayer_Next();

			break;

		case R.id.playerBtnPrev:

			MediaPlayer_Previous();

			break;

		case R.id.playerBtnRepeat:

			if (isRepeat) {
				isRepeat = false;
				Toast.makeText(getApplicationContext(), "Repeat is OFF",
						Toast.LENGTH_SHORT).show();
				btnRepeat.get().setImageResource(R.drawable.player_repeat);
			} else {
				// make repeat to true
				isRepeat = true;
				Toast.makeText(getApplicationContext(), "Repeat is ON",
						Toast.LENGTH_SHORT).show();
				// make shuffle to false
				isShuffle = false;
				btnRepeat.get().setImageResource(R.drawable.player_repeat_all);
				btnShuffle.get().setImageResource(R.drawable.player_shuffle);
			}
			break;

		case R.id.playerBtnShuffle:

			if (isShuffle) {
				isShuffle = false;
				Toast.makeText(getApplicationContext(), "Shuffle is OFF",
						Toast.LENGTH_SHORT).show();
				btnShuffle.get().setImageResource(R.drawable.player_shuffle);
			} else {
				// make repeat to true
				isShuffle = true;
				Toast.makeText(getApplicationContext(), "Shuffle is ON",
						Toast.LENGTH_SHORT).show();
				// make shuffle to false
				isRepeat = false;
				btnShuffle.get().setImageResource(
						R.drawable.player_shuffle_active);
				btnRepeat.get().setImageResource(R.drawable.player_repeat);
			}
			break;

		case R.id.playerQueue:
			break;

		case R.id.videoIcon:
			break;

		case R.id.lyricsIcon:
			break;

		}
	}

	public void MediaPlayer_play_pause(){

		if (songsListingSD.size() == 0) {
			Toast.makeText(getApplicationContext(), "No Song Added to Queue", Toast.LENGTH_SHORT).show();
			return;
		}

		if (currentSongIndex != -1) {

			if(mediaPlayer == null) initMediaPlayer();

			if(mediaPlayer != null){

				if(mediaPlayer.isPlaying()){

					pauseMedia();

				} else {

					resumeMedia();

				}

			}

		} else {

			if(mediaPlayer == null){
				initMediaPlayer();
			}

			if (!mediaPlayer.isPlaying()) {

				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", sharedpreference.getString(Title, ""));
				song.put("songArtist", sharedpreference.getString(Artist, ""));
				song.put("songAlbum", sharedpreference.getString(Album, ""));
				song.put("songImage", sharedpreference.getString(Image, ""));
				song.put("songPath", sharedpreference.getString(SongUrl, ""));
				song.put("songAlbumCode", sharedpreference.getString(AlbumCode, ""));
				song.put("songCode", sharedpreference.getString(Code, ""));
				song.put("songLength", sharedpreference.getString(Length, ""));

				if (SongsManager._songsList.contains(song)) {

					int index = SongsManager._songsList.indexOf(song);
					playSong(index);
					initNotification(index);
					currentSongIndex = index;

				} else {

					SongsManager._songsList.add(song);
					int indexvalue = SongsManager._songsList.size() - 1;
					playSong(indexvalue);
					initNotification(indexvalue);
					currentSongIndex = indexvalue;
				}

			}

		}

		/* New code ends here */

		if (currentSongIndex != -1) {

			/*if(mediaPlayer == null) return;

			if (mediaPlayer.isPlaying()) {
				if (mediaPlayer != null) {
					mediaPlayer.pause();
					// Changing button image to play button
					btnPlay.get().setImageResource(R.drawable.player_play);
					mSongBtnPlay.get().setImageResource(R.drawable.new_miniplay_btn);
					Log.d("Player Service", "Pause");

					simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
					simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
					if (currentVersionSupportBigNotification) {
						expandedView.setViewVisibility(R.id.btnPause, View.GONE);
						expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
					}

					notification.contentView = simpleContentView;
					if (currentVersionSupportBigNotification) {
						notification.bigContentView = expandedView;
					}
					notification.flags |= Notification.FLAG_ONGOING_EVENT;
					startForeground(NOTIFICATION_ID, notification);

				}
			} else {
				// Resume song
				if (mediaPlayer != null) {
					mediaPlayer.start();
					// Changing button image to pause button
					btnPlay.get().setImageResource(R.drawable.player_pause);
					mSongBtnPlay.get().setImageResource(R.drawable.new_minipause_btn);
					Log.d("Player Service", "Play");

					simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
					simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);

					if (currentVersionSupportBigNotification) {
						expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
						expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
					}
					notification.contentView = simpleContentView;
					if (currentVersionSupportBigNotification) {
						notification.bigContentView = expandedView;
					}
					notification.flags |= Notification.FLAG_ONGOING_EVENT;
					startForeground(NOTIFICATION_ID, notification);
				}
			}*/

		} else {

			/*if(mediaPlayer == null) return;

			if (!mediaPlayer.isPlaying() && isPause == false) {

				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", sharedpreference.getString(Title, ""));
				song.put("songArtist", sharedpreference.getString(Artist, ""));
				song.put("songAlbum", sharedpreference.getString(Album, ""));
				song.put("songImage", sharedpreference.getString(Image, ""));
				song.put("songPath", sharedpreference.getString(SongUrl, ""));
				song.put("songAlbumCode", sharedpreference.getString(AlbumCode, ""));
				song.put("songCode", sharedpreference.getString(Code, ""));
				song.put("songLength", sharedpreference.getString(Length, ""));

				if (SongsManager._songsList.contains(song)) {

					int index = SongsManager._songsList.indexOf(song);
					playSong(index);
					initNotification(index);
					currentSongIndex = index;

				} else {

					SongsManager._songsList.add(song);
					int indexvalue = SongsManager._songsList.size() - 1;
					playSong(indexvalue);
					initNotification(indexvalue);
					currentSongIndex = indexvalue;
				}

			} else {

				if(mediaPlayer == null) return;
				if (mediaPlayer.isPlaying()) {
					if (mediaPlayer != null) {
						mediaPlayer.pause();
						isPause = true;
						// Changing button image to play button
						mSongBtnPlay.get().setImageResource(R.drawable.new_miniplay_btn);
						btnPlay.get().setImageResource(R.drawable.player_play);
						Log.d("Player Service", "Pause");

						simpleContentView.setViewVisibility(R.id.btnPause, View.GONE);
						simpleContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
						if (currentVersionSupportBigNotification) {
							expandedView.setViewVisibility(R.id.btnPause, View.GONE);
							expandedView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
						}
						notification.contentView = simpleContentView;
						if (currentVersionSupportBigNotification) {
							notification.bigContentView = expandedView;
						}
						notification.flags |= Notification.FLAG_ONGOING_EVENT;
						startForeground(NOTIFICATION_ID, notification);

					}
				} else {
					// Resume song
					if (mediaPlayer != null) {
						mediaPlayer.start();
						// Changing button image to pause button
						mSongBtnPlay.get().setImageResource(R.drawable.new_minipause_btn);
						btnPlay.get().setImageResource(R.drawable.player_pause);
						Log.d("Player Service", "Play");

						simpleContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
						simpleContentView.setViewVisibility(R.id.btnPlay, View.GONE);
						if (currentVersionSupportBigNotification) {
							expandedView.setViewVisibility(R.id.btnPause, View.VISIBLE);
							expandedView.setViewVisibility(R.id.btnPlay, View.GONE);
						}

						notification.contentView = simpleContentView;
						if (currentVersionSupportBigNotification) {
							notification.bigContentView = expandedView;
						}
						notification.flags |= Notification.FLAG_ONGOING_EVENT;
						startForeground(NOTIFICATION_ID, notification);
					}
				}

			}*/

		}
	}

	public void MediaPlayer_Next() {

		stopMedia();

		if (songsListingSD.size() == 0) {
			Toast.makeText(getApplicationContext(),
					"No Song Added to Queue", Toast.LENGTH_SHORT).show();
			return;
		}
		// check if next song is there or not
		Log.d("Player Service", "Next");
		if (currentSongIndex < (songsListingSD.size() - 1)) {
			playSong(currentSongIndex + 1);
			initNotification(currentSongIndex + 1);
			//buildNotification(PlaybackStatus.PLAYING, currentSongIndex + 1);
			currentSongIndex = currentSongIndex + 1;
		} else {
			// play first song
			playSong(0);
			initNotification(0);
			//buildNotification(PlaybackStatus.PLAYING, 0);
			currentSongIndex = 0;
		}
	}

	public void MediaPlayer_Previous() {

		stopMedia();

		if (songsListingSD.size() == 0) {
			Toast.makeText(getApplicationContext(),
					"No Song Added to Queue", Toast.LENGTH_SHORT).show();
			return;
		}

		if (currentSongIndex > 0) {
			playSong(currentSongIndex - 1);
			initNotification(currentSongIndex - 1);
			//buildNotification(PlaybackStatus.PLAYING, currentSongIndex - 1);
			currentSongIndex = currentSongIndex - 1;
		} else {
			// play last song
			playSong(songsListingSD.size() - 1);
			initNotification(songsListingSD.size() - 1);
			//buildNotification(PlaybackStatus.PLAYING, songsListingSD.size() - 1);
			currentSongIndex = songsListingSD.size() - 1;
		}

	}

	public void Close_App(Context context) {

		try {

			removeNotification();

			currentSongIndex = -1;
			if (mediaPlayer != null) {
				pauseMedia();
			}

			stopForeground(true);

			/*Intent i = new Intent(context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.stopService(i);*/

		} catch (Exception ex){Log.d("Error in : " , "Close_App - " + ex.toString());
		}

		/*Intent i = new Intent(context, PlayerService.class);
		context.stopService(i);
		stopMedia();
		stopSelf();
		removeNotification();
		Intent in = new Intent(context, MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/

	}

	public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		private final WeakReference<ImageView> bmImage;

		public DownloadImageTask(WeakReference<ImageView> bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Bitmap doInBackground(String... urls) {

			String urldisplay = urls[0];

			BufferedReader reader = null;
			InputStream stream = null;

			try {

				openHttpUrlConnection(urldisplay);

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

					stream = httpConnection.getInputStream();

					Bitmap bitmap = BitmapFactory.decodeStream(stream);
					// to save bitmap in shared preference
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
					byte[] b = baos.toByteArray();
					String imageEncoded = Base64.encodeToString(b,
							Base64.DEFAULT);

					Editor edit = sharedpreference.edit();
					edit.putString("image_data", imageEncoded);
					edit.commit();

					return bitmap;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				FileUtils.close(stream);
				FileUtils.close(reader);
			}
			return null;
		}

		private void openHttpUrlConnection(String urlString) throws IOException {
			Log.d("urlstring in parser", urlString + "");
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			httpConnection = (HttpURLConnection) connection;
			httpConnection.setConnectTimeout(30000);
			httpConnection.setRequestMethod("GET");

			httpConnection.connect();
		}
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			ImageView imageView = bmImage.get();

			if (result != null && imageView != null) {
				imageView.setImageBitmap(result);
				Bitmap blurredBitmap = BlurBuilder.blur(PlayerService.this, result);
				//miniLayout.get().setBackground(new BitmapDrawable(blurredBitmap));
				fullLayout.get().setBackground(new BitmapDrawable(blurredBitmap));
				//fullUpperLayout.get().setBackground(new BitmapDrawable(blurredBitmap));

			} else {
				imageView.setImageResource(R.drawable.no_img);

			}

		}
	}

	// ----------------onSeekBar Change Listener------------------------------//
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		progressBarHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		@Override
		public void run() {
			long totalDuration = 0;
			try {
				totalDuration = mediaPlayer.getDuration();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			long currentDuration = 0;
			try {
				currentDuration = mediaPlayer.getCurrentPosition();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			// Displaying Total Duration time
			fsongTotalDurationLabel.get().setText(
					"" + utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			fsongCurrentDurationLabel.get().setText(
					"" + utils.milliSecondsToTimer(currentDuration));
			// Displaying time completed playing
			msongDuratonLabel.get().setText(
					"" + utils.milliSecondsToTimer(currentDuration));

			//msongTotalDuratonLabel.get().setText(
					//"" + utils.milliSecondsToTimer(totalDuration));

			// Updating progress bar
			int progress = (utils.getProgressPercentage(currentDuration,
					totalDuration));
			// Log.d("Progress", ""+progress);
			songProgressBar.get().setProgress(progress);
			// Running this thread after 100 milliseconds
			progressBarHandler.postDelayed(this, 100);
			// Log.d("AndroidBuildingMusicPlayerActivity","Runable  progressbar");
		}
	};

	// ----------------on Seekbar Change
	// Listener---------------------------------------//
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {
	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);
		// update timer progress again
		updateProgressBar();

	}

	/**
	 * Return the PlaybackService instance, creating one if needed.
	 */
	/*public static PlayerService get(Context context) {
		if (sInstance == null) {
			context.startService(new Intent(context, PlayerService.class));

			while (sInstance == null) {
				try {
					synchronized (sWait) {
						sWait.wait();
					}
				} catch (InterruptedException ignored) {
				}
			}
		}

		return sInstance;
	}*/

	/**
	 * Returns true if a PlaybackService instance is active.
	 */
	/*public static boolean hasInstance() {
		return sInstance != null;
	}*/

	@SuppressLint("NewApi")
	private void initNotification(int songIndex) {

		String songName = songsListingSD.get(songIndex).get("songTitle");
		String songArtist = songsListingSD.get(songIndex).get("songArtist");
		String songAlbum = songsListingSD.get(songIndex).get("songAlbum");
		String songImage = songsListingSD.get(songIndex).get("songImage");

		simpleContentView = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.custom_notification);
		expandedView = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.big_notification);

		Intent notIntent = new Intent(this, MainActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification = new NotificationCompat.Builder(
				getApplicationContext()).setSmallIcon(android.R.drawable.stat_sys_headset)
				.setContentIntent(pendInt).setContentTitle(songName).build();

		setListeners(simpleContentView);
		setListeners(expandedView);

		notification.contentView = simpleContentView;
		if (currentVersionSupportBigNotification) {
			notification.bigContentView = expandedView;
		}

		try {

			Bitmap cover = null;

			try {
				cover = new AsyncCoverTask().execute(songImage).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (cover != null) {
				notification.contentView.setImageViewBitmap(R.id.imageViewAlbumArt, cover);
				if (currentVersionSupportBigNotification) {
					notification.bigContentView.setImageViewBitmap(R.id.imageViewAlbumArt, cover);
				}
			} else {
				notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.no_img);
				if (currentVersionSupportBigNotification) {
					notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.no_img);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
		notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);

		if (currentVersionSupportBigNotification) {
			notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
			notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
		}

		notification.contentView.setTextViewText(R.id.textSongName, songName);
		notification.contentView.setTextViewText(R.id.textAlbumName, songArtist);
		if (currentVersionSupportBigNotification) {
			notification.bigContentView.setTextViewText(R.id.textSongName, songName);
			notification.bigContentView.setTextViewText(R.id.textAlbumName, songArtist);
			notification.bigContentView.setTextViewText(R.id.status_bar_album_name, songAlbum);
		}
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, notification);
		//startForeground(NOTIFICATION_ID, notification);
	}

	/**
	 * Notification click listeners
	 *
	 * @param view
	 */
	public void setListeners(RemoteViews view) {

		Intent previous = new Intent(ACTION_PREVIOUS);
		Intent delete = new Intent(ACTION_STOP);
		Intent pause = new Intent(ACTION_PAUSE);
		Intent next = new Intent(ACTION_NEXT);
		Intent play = new Intent(ACTION_PLAY);

		PendingIntent pPrevious = PendingIntent.getBroadcast(
				getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

		PendingIntent pDelete = PendingIntent.getBroadcast(
				getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

		PendingIntent pPause = PendingIntent.getBroadcast(
				getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPause, pPause);

		PendingIntent pNext = PendingIntent.getBroadcast(
				getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnNext, pNext);

		PendingIntent pPlay = PendingIntent.getBroadcast(
				getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
		view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

	}

	@SuppressWarnings("deprecation")
	private void RegisterRemoteClient() {
		remoteComponentName = new ComponentName(getApplicationContext(),
				new NotificationBroadcast().ComponentName());
		try {
			if (remoteControlClient == null) {
				mAudioManager.registerMediaButtonEventReceiver(remoteComponentName);
				Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
				mediaButtonIntent.setComponent(remoteComponentName);
				PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(
						this, 0, mediaButtonIntent, 0);
				remoteControlClient = new RemoteControlClient(
						mediaPendingIntent);
				mAudioManager.registerRemoteControlClient(remoteControlClient);
			}
			remoteControlClient
					.setTransportControlFlags(RemoteControlClient.FLAG_KEY_MEDIA_PLAY
							| RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
							| RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
							| RemoteControlClient.FLAG_KEY_MEDIA_STOP
							| RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
							| RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
		} catch (Exception ex) {
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void UpdateMetadata(int index) {
		if (remoteControlClient == null)
			return;
		RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
		metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM,
				songsListingSD.get(index).get("songAlbum"));
		metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST,
				songsListingSD.get(index).get("songArtist"));
		metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE,
				songsListingSD.get(index).get("songTitle"));

		String songImage = songsListingSD.get(index).get("songImage");

		Bitmap cover = null;

		try {
			cover = new AsyncCoverTask().execute(songImage).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (cover == null) {
			cover = BitmapFactory.decodeResource(getResources(), R.drawable.no_img);
		}
		metadataEditor.putBitmap(
				RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, cover);
		metadataEditor.apply();
		mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
	}

}
