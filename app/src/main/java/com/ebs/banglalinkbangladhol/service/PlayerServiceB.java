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
import com.ebs.banglalinkbangladhol.activity.SplashActivity;
import com.ebs.banglalinkbangladhol.bean.BlurBuilder;
import com.ebs.banglalinkbangladhol.fragment.HomeFragment;
import com.ebs.banglalinkbangladhol.others.RequestTask;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.ebs.banglalinkbangladhol.util.FileUtils;
import com.ebs.banglalinkbangladhol.util.Utilities;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerServiceB extends Service implements OnCompletionListener,
        OnSeekCompleteListener, OnInfoListener, OnBufferingUpdateListener,
        OnErrorListener, OnClickListener, OnSeekBarChangeListener,
        OnPreparedListener {

    public static WeakReference<ImageView> btnPlay, btnNext, btnPrevious,
            btnRepeat, btnShuffle, btnQueue, btnRbt, btnAlbum;
    public static WeakReference<ImageView> mSongThumb, fSongThumb,
            mSongBtnPlay;
    private WeakReference<SeekBar> songProgressBar;
    private WeakReference<ProgressBar> songLoadingBar;
    private WeakReference<SlidingUpPanelLayout> mLayout;
    private WeakReference<TextView> msongTitleLabel, msongArtistLabel,
            msongDuratonLabel, msongTotalDuratonLabel;
    private WeakReference<TextView> fsong_album, fsong_title, fsong_artist,
            fsongCurrentDurationLabel, fsongTotalDurationLabel;
    public static WeakReference<RelativeLayout> miniLayout;
    public static WeakReference<View> fullLayout;
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
    public static final String Image = "imageKey";
    public static final String Url = "urlKey";
    public static final String Code = "codeKey";
    public static final String Length = "lengthKey";

    private SharedPreferences sharedpreference;

    private byte[] art;
    /**
     * Object used for PlaybackService startup waiting.
     */
    private static final Object[] sWait = new Object[0];
    /**
     * The appplication-wide instance of the PlaybackService.
     */
    public static PlayerServiceB sInstance;

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private AudioManager mAudioManager;

    private boolean IsPausedInCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private String imsi = "";
    private String ds;
    private String ds2;
    private String code = null;
    private int lastIndex = -1;
    private ProgressDialog pDialog;
    public static String codeAlbum, nameAlbum, imageAlbum;
    private HttpURLConnection httpConnection;

    //private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;

    @Override
    public void onCreate() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);

        mediaPlayer.reset();

        // pDialog = new ProgressDialog(getApplicationContext(),
        // ProgressDialog.THEME_HOLO_DARK);
        // pDialog.setMessage("Loading Please Wait ...");
        // pDialog.setIndeterminate(false);
        // pDialog.setCancelable(false);

        utils = new Utilities();

        try {

            //songsListingSD = HomeFragment.songsList;
            songsListingSD = SongsManager._songsList;
        } catch (Exception ex) {

            Log.d("Tag", "Error");
        }

        fsongCurrentDurationLabel = new WeakReference<TextView>(MainActivity.songCurrentDurationLabel);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {

            @Override
            public void onAudioFocusChange(int type) {
                // TODO Auto-generated method stub
                switch (type) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // resume playback
                        if (mediaPlayer == null)
                            initMediaPlayer();
                        else if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                        mediaPlayer.setVolume(1.0f, 1.0f);
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Lost focus for an unbounded amount of time: stop playback
                        // and
                        // release media player
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // Lost focus for a short time, but we have to stop
                        // playback. We don't release the media player because
                        // playback
                        // is likely to resume
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.pause();
                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lost focus for a short time, but it's ok to keep playing
                        // at an attenuated level
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.setVolume(0.1f, 0.1f);
                        break;
                }

            }
        };

		/*
		 * int result = mAudioManager.requestAudioFocus( // Use the music
		 * stream. afChangeListener, AudioManager.STREAM_MUSIC, // Request
		 * permanent focus. AudioManager.AUDIOFOCUS_GAIN);
		 *
		 * if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
		 *
		 * }
		 */

        sInstance = this;
        synchronized (sWait) {
            sWait.notifyAll();
        }

        super.onCreate();

    }

    public void initMediaPlayer() {
        // set player properties
        // mediaPlayer.setWakeMode(getApplicationContext(),
        // PowerManager.PARTIAL_WAKE_LOCK);
    }

    // --------------onStartCommand--------------------------------------------//
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            initUI();

            try {

                int songIndex = intent.getIntExtra("songIndex", 0);

                if (songIndex != currentSongIndex) {

                    playSong(songIndex);
                    initNotification(songIndex);
                    currentSongIndex = songIndex;

                    // Editor editor = sharedpreference.edit();
                    // editor.putInt(Index, currentSongIndex);
                    // editor.commit();
					/*
					 * if (songsListingSD.size() > 0) {
					 *
					 * playSong(songIndex); initNotification(songIndex);
					 * currentSongIndex = songIndex;
					 *
					 * } else {
					 *
					 * Toast.makeText(getApplicationContext(),
					 * "No Song Added to Queue", Toast.LENGTH_SHORT) .show();
					 *
					 * }
					 */

                } else if (currentSongIndex != -1) {
                    // msong Title
                    msongTitleLabel.get().setText(
                            songsListingSD.get(currentSongIndex).get(
                                    "songTitle"));
                    // msong Artist
                    msongArtistLabel.get().setText(
                            songsListingSD.get(currentSongIndex).get(
                                    "songArtist"));
                    // fsong Album Name
                    fsong_album.get().setText(
                            songsListingSD.get(currentSongIndex).get(
                                    "songAlbum"));
                    // fsong Title
                    fsong_title.get().setText(
                            songsListingSD.get(currentSongIndex).get(
                                    "songTitle"));
                    // fsong Artist
                    fsong_artist.get().setText(
                            songsListingSD.get(currentSongIndex).get(
                                    "songArtist"));

                    if (mediaPlayer.isPlaying()) {
                        btnPlay.get().setImageResource(R.drawable.player_pause);
                        mSongBtnPlay.get().setImageResource(
                                R.drawable.new_minipause_btn);
                    } else {
                        btnPlay.get().setImageResource(R.drawable.player_play);
                        mSongBtnPlay.get().setImageResource(
                                R.drawable.new_miniplay_btn);

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
            }

            // get the telephony Manager
            telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            phoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {

                    switch (state) {
                        case TelephonyManager.CALL_STATE_OFFHOOK:

                        case TelephonyManager.CALL_STATE_RINGING:

                            try {

                                if (mediaPlayer != null) {

                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                        btnPlay.get().setImageResource(
                                                R.drawable.player_play);
                                        mSongBtnPlay.get().setImageResource(
                                                R.drawable.new_miniplay_btn);
                                    }
                                    IsPausedInCall = true;
                                }

                            } catch (Exception ex) {

                                Log.d("TAG", "Error in Phone Call");
                            }

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:

                            try {

                                if (mediaPlayer != null) {

                                    if (IsPausedInCall) {
                                        IsPausedInCall = false;
                                        if (mediaPlayer.isPlaying()) {
                                            mediaPlayer.start();
                                            // Changing button image to pause button
                                            btnPlay.get().setImageResource(
                                                    R.drawable.player_pause);
                                            mSongBtnPlay.get().setImageResource(
                                                    R.drawable.new_minipause_btn);
                                        }
                                    }
                                }

                            } catch (Exception ex) {

                                Log.d("TAG", "Error in Phone Call");
                            }

                            break;

                    }

                }
            };

            // register the listener with telephony manager
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_CALL_STATE);

        }

        return START_NOT_STICKY;
        // START_NOT_STICKY //START_STICKY
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sInstance = null;
        currentSongIndex = -1;
        // Remove progress bar update Hanlder callBacks
        progressBarHandler.removeCallbacks(mUpdateTimeTask);
        Log.d("Player Service", "Player Service Stopped");
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();

            }
            mediaPlayer.release();
        }
        // cancel the notification
        // cancelNotification();

        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener,
                    PhoneStateListener.LISTEN_NONE);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Init UI
     */
    public void initUI() {

        msongTitleLabel = new WeakReference<TextView>(
                MainActivity.mPlayer_title);
        msongArtistLabel = new WeakReference<TextView>(
                MainActivity.mPlayer_artist);
        msongDuratonLabel = new WeakReference<TextView>(
                MainActivity.mPlayer_duration);
        //msongTotalDuratonLabel = new WeakReference<TextView>(
                //MainActivity.mPlayer_totalduration);

        fsong_album = new WeakReference<TextView>(MainActivity.fPlayer_album);
        fsong_title = new WeakReference<TextView>(MainActivity.fPlayer_title);
        fsong_artist = new WeakReference<TextView>(MainActivity.fPlayer_artist);
        fsongCurrentDurationLabel = new WeakReference<TextView>(
                MainActivity.songCurrentDurationLabel);
        fsongTotalDurationLabel = new WeakReference<TextView>(
                MainActivity.songTotalDurationLabel);
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

        songProgressBar = new WeakReference<SeekBar>(
                MainActivity.songProgressBar);
        songProgressBar.get().setOnSeekBarChangeListener(this);

        songProgressBar.get().setEnabled(false);
        songProgressBar.get().setClickable(false);

        songLoadingBar = new WeakReference<ProgressBar>(
                MainActivity.mProgressBar);

        // songLoadingBar.get().setVisibility(View.VISIBLE);

        sharedpreference = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedpreference.contains(Title)) {
            msongTitleLabel.get()
                    .setText(sharedpreference.getString(Title, ""));
            fsong_title.get().setText(sharedpreference.getString(Title, ""));
        }

        if (sharedpreference.contains(Artist)) {
            msongArtistLabel.get().setText(
                    sharedpreference.getString(Artist, ""));
            fsong_artist.get().setText(sharedpreference.getString(Artist, ""));
        }

        if (sharedpreference.contains(Album)) {
            nameAlbum = sharedpreference.getString(Album, "");
            fsong_album.get().setText(sharedpreference.getString(Album, ""));

        }

        if (sharedpreference.contains(Image)) {
            imageAlbum = sharedpreference.getString(Image, "");
            String saveImage = sharedpreference.getString(Image, "");

        }

        if (sharedpreference.contains(Url)) {
            String savedUrl = sharedpreference.getString(Url, "");

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

        // Play song
        try {

            if (mLayout != null) {
                //mLayout.get().showPanel();
            }

            songProgressBar.get().setEnabled(true);
            songProgressBar.get().setClickable(true);
            String urlFromIndex = songsListingSD.get(songIndex).get("songPath");
            mediaPlayer.reset();
            mediaPlayer.setDataSource(urlFromIndex);
            mediaPlayer.prepare();

            mediaPlayer.start();

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

            // Displaying Song Album
            String songAlbum = songsListingSD.get(songIndex).get("songAlbum");
            if (songAlbum != null) {
                fsong_album.get().setText(songAlbum);
                nameAlbum = songAlbum;
            } else {
                fsong_album.get().setText("Unknown Album");
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
            editor.putString(Image, songImage);
            editor.putString(Code, songCode);
            editor.putString(Length,
                    songsListingSD.get(songIndex).get("songLength"));
            editor.putString(Url, songsListingSD.get(songIndex).get("songPath"));
            editor.commit();
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

                if (SongsManager._songsList.size() == 0) {
                    // Toast.makeText(getApplicationContext(),
                    // "No Song Added to Queue", Toast.LENGTH_SHORT).show();
                    // return;

                }

                if (currentSongIndex != -1) {
                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();
                            // Changing button image to play button
                            mSongBtnPlay.get().setImageResource(
                                    R.drawable.new_miniplay_btn);
                            btnPlay.get().setImageResource(R.drawable.player_play);
                            Log.d("Player Service", "Pause");

                        }
                    } else {
                        // Resume song
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            // Changing button image to pause button
                            mSongBtnPlay.get().setImageResource(
                                    R.drawable.new_minipause_btn);
                            btnPlay.get().setImageResource(R.drawable.player_pause);
                            Log.d("Player Service", "Play");
                        }
                    }
                } else {

                    if (!mediaPlayer.isPlaying() && isPause == false) {

                        HashMap<String, String> song = new HashMap<String, String>();
                        song.put("songTitle", sharedpreference.getString(Title, ""));
                        song.put("songArtist",
                                sharedpreference.getString(Artist, ""));
                        song.put("songAlbum", sharedpreference.getString(Album, ""));
                        song.put("songImage", sharedpreference.getString(Image, ""));
                        song.put("songPath", sharedpreference.getString(Url, ""));
                        song.put("songCode", sharedpreference.getString(Code, ""));
                        song.put("songLength",
                                sharedpreference.getString(Length, ""));

                        if (SongsManager._songsList.contains(song)) {

                            int index = SongsManager._songsList.indexOf(song);

                            playSong(index);
                            currentSongIndex = index;

                        } else {

                            SongsManager._songsList.add(song);

                            int indexvalue = SongsManager._songsList.size() - 1;
                            playSong(indexvalue);
                            initNotification(indexvalue);
                            currentSongIndex = indexvalue;

                        }

                    } else {

                        if (mediaPlayer.isPlaying()) {
                            if (mediaPlayer != null) {
                                mediaPlayer.pause();
                                isPause = true;
                                // Changing button image to play button
                                mSongBtnPlay.get().setImageResource(
                                        R.drawable.new_miniplay_btn);
                                btnPlay.get().setImageResource(
                                        R.drawable.player_play);
                                Log.d("Player Service", "Pause");

                            }
                        } else {
                            // Resume song
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                                // Changing button image to pause button
                                mSongBtnPlay.get().setImageResource(
                                        R.drawable.new_minipause_btn);
                                btnPlay.get().setImageResource(
                                        R.drawable.player_pause);
                                Log.d("Player Service", "Play");
                            }
                        }

                    }

                }

                break;

            case R.id.playerBtnPlay:

                if (SongsManager._songsList.size() == 0) {
                    // Toast.makeText(getApplicationContext(),
                    // "No Song Added to Queue", Toast.LENGTH_SHORT).show();
                    // return;

                }

                if (currentSongIndex != -1) {
                    if (mediaPlayer.isPlaying()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.pause();
                            // Changing button image to play button
                            btnPlay.get().setImageResource(R.drawable.player_play);
                            mSongBtnPlay.get().setImageResource(
                                    R.drawable.new_miniplay_btn);
                            Log.d("Player Service", "Pause");

                        }
                    } else {
                        // Resume song
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            // Changing button image to pause button
                            btnPlay.get().setImageResource(R.drawable.player_pause);
                            mSongBtnPlay.get().setImageResource(
                                    R.drawable.new_minipause_btn);
                            Log.d("Player Service", "Play");
                        }
                    }
                } else {

                    if (!mediaPlayer.isPlaying() && isPause == false) {

                        HashMap<String, String> song = new HashMap<String, String>();
                        song.put("songTitle", sharedpreference.getString(Title, ""));
                        song.put("songArtist",
                                sharedpreference.getString(Artist, ""));
                        song.put("songAlbum", sharedpreference.getString(Album, ""));
                        song.put("songImage", sharedpreference.getString(Image, ""));
                        song.put("songPath", sharedpreference.getString(Url, ""));
                        song.put("songCode", sharedpreference.getString(Code, ""));
                        song.put("songLength",
                                sharedpreference.getString(Length, ""));

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

                        if (mediaPlayer.isPlaying()) {
                            if (mediaPlayer != null) {
                                mediaPlayer.pause();
                                isPause = true;
                                // Changing button image to play button
                                mSongBtnPlay.get().setImageResource(
                                        R.drawable.new_miniplay_btn);
                                btnPlay.get().setImageResource(
                                        R.drawable.player_play);
                                Log.d("Player Service", "Pause");

                            }
                        } else {
                            // Resume song
                            if (mediaPlayer != null) {
                                mediaPlayer.start();
                                // Changing button image to pause button
                                mSongBtnPlay.get().setImageResource(
                                        R.drawable.new_minipause_btn);
                                btnPlay.get().setImageResource(
                                        R.drawable.player_pause);
                                Log.d("Player Service", "Play");
                            }
                        }

                    }

                }
                break;

            case R.id.playerBtnNext:

                if (SongsManager._songsList.size() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "No Song Added to Queue", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if next song is there or not
                Log.d("Player Service", "Next");
                if (currentSongIndex < (songsListingSD.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    initNotification(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    initNotification(0);
                    currentSongIndex = 0;
                }
                break;

            case R.id.playerBtnPrev:

                if (SongsManager._songsList.size() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "No Song Added to Queue", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    initNotification(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songsListingSD.size() - 1);
                    initNotification(songsListingSD.size() - 1);
                    currentSongIndex = songsListingSD.size() - 1;
                }
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

                try {

                    ds = new RequestTask().execute(
                            "http://59.152.103.202/rbtcharge.php?" + imsi).get();
                    // Toast.makeText(getApplicationContext(), ds,
                    // Toast.LENGTH_LONG)
                    // .show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    getApplicationContext(),
                                    AlertDialog.THEME_HOLO_DARK);

                            builder.setTitle(Html
                                    .fromHtml("<font color='#FF6500'>Set RBT</font>"));
                            builder.setMessage(ds)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            Html.fromHtml("<font color='#FF6500'>Yes</font>"),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {

                                                    if (code != null) {

                                                        try {

                                                            ds2 = new RequestTask()
                                                                    .execute(
                                                                            "http://59.152.103.202/setrbt.php?"
                                                                                    + imsi
                                                                                    + "&c="
                                                                                    + code)
                                                                    .get();

                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        } catch (ExecutionException e) {
                                                            e.printStackTrace();
                                                        }

                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(
                                                                getApplicationContext(),
                                                                AlertDialog.THEME_HOLO_DARK);
                                                        builder2.setTitle(Html
                                                                .fromHtml("<font color='#FF6500'>Dear User</font>"));
                                                        builder2.setMessage(ds2)
                                                                .setCancelable(
                                                                        false)
                                                                .setPositiveButton(
                                                                        Html.fromHtml("<font color='#FF6500'>OK</font>"),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(
                                                                                    DialogInterface dialog,
                                                                                    int id) {

                                                                            }
                                                                        });
                                                        AlertDialog alert2 = builder2
                                                                .create();
                                                        alert2.getWindow()
                                                                .setType(
                                                                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                                        alert2.show();

                                                    } else {

                                                        Toast.makeText(
                                                                getApplicationContext(),
                                                                "Please try again later",
                                                                Toast.LENGTH_SHORT)
                                                                .show();

                                                    }

                                                }
                                            })
                                    .setNegativeButton(
                                            Html.fromHtml("<font color='#FF6500'>No</font>"),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {

                                                    dialog.cancel();

                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.getWindow().setType(
                                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                            alert.show();
                            Looper.loop();
                        }
                    }.start();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                break;

            case R.id.lyricsIcon:

			/*if (mLayout != null && mLayout.get().isPanelExpanded()
					|| mLayout.get().isPanelAnchored()) {
				mLayout.get().collapsePanel();

			}

			if (codeOfSelectedAlbum != null && codeOfSelectedAlbum.length() > 7) {

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				AlbumDetailsFragment albumSongsFragment = new AlbumDetailsFragment();
				Bundle args = new Bundle();
				args.putString("albumCode", codeOfSelectedAlbum);
				args.putString("albumName", albumOfSelectedSong);
				// args.putString("albumGenre", selectedGenre);
				// args.putString("albumPublisher", selectedPublisher);
				args.putString("albumImage", imageOfSelectedSong);

				albumSongsFragment.setArguments(args);
				ft.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right);
				ft.replace(R.id.home_container, albumSongsFragment, "fragment");
				ft.addToBackStack(null);
				ft.commit();

			} else {

				Toast.makeText(PlayerService.this, "Album not available.",
						Toast.LENGTH_LONG).show();
			}*/

                break;

        }
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
                Bitmap blurredBitmap = BlurBuilder.blur(PlayerServiceB.this, result);
                //miniLayout.get().setBackground(new BitmapDrawable(blurredBitmap));
                fullLayout.get().setBackground(new BitmapDrawable(blurredBitmap));

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

            msongTotalDuratonLabel.get().setText(
                    "" + utils.milliSecondsToTimer(totalDuration));

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
     * On Song Playing completed if repeat is ON play same song again if shuffle
     * is ON play random song
     * */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(currentSongIndex);
            initNotification(currentSongIndex);

        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand
                    .nextInt((songsListingSD.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
            initNotification(currentSongIndex);

        } else {

            if (SongsManager._songsList.size() > 0) {

                // no repeat or shuffle ON - play next song
                if (currentSongIndex < (songsListingSD.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    initNotification(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    initNotification(0);
                    currentSongIndex = 0;
                }

            } else {

                Toast.makeText(getApplicationContext(),
                        "No Song Added to Queue", Toast.LENGTH_SHORT).show();

            }

        }
    }

    // ---------------------------------------------------------//

    /**
     * Return the PlaybackService instance, creating one if needed.
     */
    public static PlayerServiceB get(Context context) {
        if (sInstance == null) {
            context.startService(new Intent(context, PlayerServiceB.class));

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
    }

    /**
     * Returns true if a PlaybackService instance is active.
     */
    public static boolean hasInstance() {
        return sInstance != null;
    }

    // Set up the notification ID

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this,
                        "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK" + extra,
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Toast.makeText(this, "MEDIA ERROR SERVER DIED" + extra,
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Toast.makeText(this, "MEDIA ERROR UNKNOWN" + extra,
                        Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }

    // Create Notification

    @SuppressLint("NewApi")
    private void initNotification(int songIndex) {

        CharSequence songName = songsListingSD.get(songIndex).get("songTitle");
        CharSequence songArtist = songsListingSD.get(songIndex).get(
                "songArtist");

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.sign_in_icon).setTicker(songName)
                .setOngoing(true).setContentTitle(songName)
                .setContentText(songArtist);
        Notification not = builder.build();

        startForeground(NOTIFICATION_ID, not);

		/*
		 * NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		 * this).setSmallIcon(R.drawable.ic_notification)
		 * .setContentTitle(songName).setContentText(songArtist); Intent
		 * resultIntent = new Intent(this, TwynkActivity.class);
		 * TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		 * stackBuilder.addParentStack(TwynkActivity.class);
		 * stackBuilder.addNextIntent(resultIntent); PendingIntent
		 * resultPendingIntent = stackBuilder.getPendingIntent(0,
		 * PendingIntent.FLAG_UPDATE_CURRENT);
		 * mBuilder.setContentIntent(resultPendingIntent);
		 * mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		 */

		/*
		 * String ns = Context.NOTIFICATION_SERVICE; mNotificationManager =
		 * (NotificationManager) getSystemService(ns); int icon =
		 * R.drawable.ic_notification; Notification notification = new
		 * Notification(icon, null, System.currentTimeMillis());
		 * notification.flags = Notification.FLAG_ONGOING_EVENT; Context context
		 * = getApplicationContext();
		 *
		 * Intent notificationIntent = new Intent(this, TwynkActivity.class);
		 * PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		 * notificationIntent, 0); notification.setLatestEventInfo(this,
		 * songName, songArtist, contentIntent);
		 * mNotificationManager.notify(NOTIFICATION_ID, notification);
		 */

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        songProgressBar.get().setSecondaryProgress(percent);

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub

		/*
		 * switch (what) { case MediaPlayer.MEDIA_INFO_BUFFERING_START:
		 * pDialog.show(); break; case MediaPlayer.MEDIA_INFO_BUFFERING_END:
		 * pDialog.dismiss(); break; }
		 */

        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        // songLoadingBar.get().setVisibility(View.GONE);
        // mp.start();

    }

}
