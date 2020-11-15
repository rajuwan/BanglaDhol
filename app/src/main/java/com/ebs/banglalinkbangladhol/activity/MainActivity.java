package com.ebs.banglalinkbangladhol.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.adapter.CustomDrawerAdapter;
import com.ebs.banglalinkbangladhol.adapter.RecyclerViewDataAdapter;
import com.ebs.banglalinkbangladhol.fragment.AccountFragment;
import com.ebs.banglalinkbangladhol.fragment.AlbumDetailsFragment;
import com.ebs.banglalinkbangladhol.fragment.FeedBackFragment;
import com.ebs.banglalinkbangladhol.fragment.InfoFragment;
import com.ebs.banglalinkbangladhol.fragment.MyPlaylistFragment;
import com.ebs.banglalinkbangladhol.fragment.SubscriptionFragment;
import com.ebs.banglalinkbangladhol.model.DrawerItem;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.Config;
import com.ebs.banglalinkbangladhol.others.DBHelperNew;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.ebs.banglalinkbangladhol.revamp.activity.FeedbackActivity;
import com.ebs.banglalinkbangladhol.service.PlayerService;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.fragment.HelpFragment;
import com.ebs.banglalinkbangladhol.fragment.HomeFragment;
import com.ebs.banglalinkbangladhol.fragment.PlayerQueueFragment;
import com.ebs.banglalinkbangladhol.fragment.SearchFragment;
import com.ebs.banglalinkbangladhol.others.DBHelper;
import com.ebs.banglalinkbangladhol.bean.Playlist;
import com.ebs.banglalinkbangladhol.others.SongsManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private RelativeLayout mUserDrawer;
    private RelativeLayout mRelativeDrawer;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;

    List<DrawerItem> dataList;

    private String tag, val, res, message, res_fcm;
    private TextView tv_userName, userNumber, tv_SignInUp;
    private ProgressDialog pd;
    private ProgressBar splashbar;
    private String userFeed = null;
    private Dialog dialog;

    public static boolean signback = false;

    private static boolean upgrade = false;
    SearchView searchView;

    public static RelativeLayout miniPlayerLayout, fullPlayerUpperLayout, songProgressLayout;
    public static View fullPlayer;

    // public Intent playerService; mPlayer_totalduration

    public static ImageView imageThumbnail, mPlayerThumb, fPlayerThumb,
            btnRepeat, btnShuffle, btnNext, btnPrevious, btnPlay, btnQueue,
            mPlayer_btnPlay, btnRBT, btnAlbum;

    public static TextView mPlayer_title, mPlayer_artist, mPlayer_duration, fPlayer_album,
            fPlayer_title, fPlayer_artist, songCurrentDurationLabel, songTotalDurationLabel;

    // public static View playershade;
    public static SeekBar songProgressBar;
    public static ProgressBar mProgressBar;
    public Intent playerService;

    public static String DBKEY = "DbKey";

    private AlertDialog levelDialog;
    //private DBHelper dbHelper;
    private DBHelperNew dbHelperNew;
    public static ArrayList<HashMap<String, String>> saveList = new ArrayList<HashMap<String, String>>();
    public static ArrayList<Playlist> allPlaylistSong;

    Activity activity;
    private String imageOfSelectedSong, artistOfSelectedSong, codeOfSelectedAlbum, albumOfSelectedSong;

    private String noti_type, noti_song_title, noti_song_artist,
            noti_song_album, noti_song_image, noti_song_url,
            noti_song_codecode, noti_song_length;
    private String noti_album_code, noti_album_title, noti_album_image;

    private String[] not;

    public static SlidingUpPanelLayout mLayout;
    public static final String SAVED_STATE_ACTION_BAR_HIDDEN = "saved_state_action_bar_hidden";

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public AsyncTask<Void, Void, String> mRegisterTask;

    private PlayerService player;
    boolean serviceBound = false;

    private Fragment contentFragment;
    private HomeFragment homeFragment;
    public static FrameLayout frameLayout;

    private NativeExpressAdView adView;
    private AdRequest request;

    public static boolean hasPlayList = false;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            // ----start service.
            playerService = new Intent(this, PlayerService.class);
            bindService(playerService, serviceConnection, Context.BIND_AUTO_CREATE);

            adView = (NativeExpressAdView) findViewById(R.id.adView);
            request = new AdRequest.Builder().build();
            request.isTestDevice(getApplicationContext());

            if(CheckUserInfo.getShowAdStatus() == 1){

                adView.loadAd(request);
            }

        } catch (Exception e) {

            Log.d("TAG", "Error in player service");

        }

        splashbar = (ProgressBar) findViewById(R.id.spProgress);
        searchView = (SearchView) findViewById(R.id.menu_search);

        // Initializing
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tv_userName = (TextView) findViewById(R.id.txt_user_name_drawer);
        userNumber = (TextView) findViewById(R.id.txt_user_number_drawer);
        tv_SignInUp = (TextView) findViewById(R.id.tv_signInUp);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //Toast.makeText(getApplicationContext(), "onAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                //Toast.makeText(getApplicationContext(), "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                //Toast.makeText(getApplicationContext(), "onAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                //Toast.makeText(getApplicationContext(), "onAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "onAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                //Toast.makeText(getApplicationContext(), "onAdClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                //Toast.makeText(getApplicationContext(), "onAdImpression", Toast.LENGTH_SHORT).show();
            }
        });

        try {

            if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

                userNumber.setText(CheckUserInfo.getUserMsisdn());
                tv_SignInUp.setText("Sign Out");

            } else {

                userNumber.setText("User not signed in !");
                tv_SignInUp.setText("Sign In");

            }

        } catch (Exception ex) {

            userNumber.setText("User not signed in !");
            tv_SignInUp.setText("Sign In");
            Log.d("Tag", "server_msisdn is empty");

        }

        tv_SignInUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

                        mDrawerLayout.closeDrawer(mRelativeDrawer);
                        LayoutInflater li = getLayoutInflater();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MainActivity.this, AlertDialog.THEME_HOLO_DARK);
                        alertDialogBuilder.setTitle("Sign Out");
                        alertDialogBuilder.setMessage("Are you sure want to Sign Out?");
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Confirm",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {

                                                RequestForLogout();

                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {
                                                dialog.cancel();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();

                    } else {

                        mDrawerLayout.closeDrawer(mRelativeDrawer);

                        Intent mainIntent = new Intent(MainActivity.this, BanglaDholSignUpLogInActivity.class);
                        startActivity(mainIntent);

                    }

                } catch (Exception ex) {

                    mDrawerLayout.closeDrawer(mRelativeDrawer);

                    Intent mainIntent = new Intent(MainActivity.this, BanglaDholSignUpLogInActivity.class);
                    startActivity(mainIntent);
                    Log.d("Tag", "Sign Up Button click error");

                }

            }
        });

        // FireBase Integration

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    //String message = intent.getStringExtra("message");
                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        displayFirebaseRegId();

        // SlidingUpMenu Code
        miniPlayerLayout = (RelativeLayout) findViewById(R.id.mini_player_panel);

        fullPlayerUpperLayout = (RelativeLayout) findViewById(R.id.rlPlayerUpperView);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        mLayout.addPanelSlideListener(new PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                miniPlayerLayout.setVisibility(View.GONE);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (newState != null) {
                    if (newState == PanelState.EXPANDED) {
                        miniPlayerLayout.setVisibility(View.GONE);
                    } else if(newState == PanelState.COLLAPSED){
                        miniPlayerLayout.setVisibility(View.VISIBLE);
                    } else if(newState == PanelState.ANCHORED){
                        miniPlayerLayout.setVisibility(View.GONE);
                    } else if(newState == PanelState.HIDDEN){
                        miniPlayerLayout.setVisibility(View.VISIBLE);
                    } else if(newState == PanelState.DRAGGING){
                        miniPlayerLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        mLayout.setFadeOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mRelativeDrawer = (RelativeLayout) findViewById(R.id.relativeDrawer);
        mUserDrawer = (RelativeLayout) findViewById(R.id.userDrawer);
        mUserDrawer.setOnClickListener(userOnClick);

        handleIntent(getIntent());

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Add Drawer Item to dataList
        // dataList.add(new DrawerItem("Categories"));
        dataList.add(new DrawerItem("Home", R.drawable.ic_home)); //0
        dataList.add(new DrawerItem("My Playlist", R.drawable.ic_mylist)); //1
        dataList.add(new DrawerItem("My Account", R.drawable.ic_myaccount));//2
        dataList.add(new DrawerItem("Subscription", R.drawable.ic_sub));//3
        dataList.add(new DrawerItem("Feedback", R.drawable.ic_feedback));//4
        dataList.add(new DrawerItem("Settings")); //5
        dataList.add(new DrawerItem("Help", R.drawable.ic_help));//6
        dataList.add(new DrawerItem("Info", R.drawable.ic_appinfo));//7
        dataList.add(new DrawerItem("Exit", R.drawable.ic_exit));//8

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {

            @Override
            public void onDrawerClosed(View drawerView) {

                supportInvalidateOptionsMenu();

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                supportInvalidateOptionsMenu();

                try{

                    if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED
                            || mLayout.getPanelState() == PanelState.ANCHORED)) {
                        mLayout.setPanelState(PanelState.COLLAPSED);

                    }

                    if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

                        userNumber.setText(CheckUserInfo.getUserMsisdn());
                        tv_SignInUp.setText("Sign Out");

                    } else {

                        userNumber.setText("User not signed in !");
                        tv_SignInUp.setText("Sign In");

                    }

                } catch (Exception ex){

                    userNumber.setText("User not signed in !");
                    tv_SignInUp.setText("Sign In");
                    Log.d("TAG", "Error Occured");

                }

                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(true);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        // new code end here
        activity = this;
        initViews();
        //dbHelper = new DBHelper(getApplicationContext());
        dbHelperNew = new DBHelperNew(getApplicationContext());

        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        boolean actionBarHidden = savedInstanceState != null
                && savedInstanceState.getBoolean(SAVED_STATE_ACTION_BAR_HIDDEN, false);
        if (actionBarHidden) {
            int actionBarHeight = getActionBarHeight();
            setActionBarTranslation(-actionBarHeight);
        }

        if (mLayout != null) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        // ends here
        // Restore playlist data from DB

        try {

            allPlaylistSong = new ArrayList<Playlist>();
            allPlaylistSong.clear();
            allPlaylistSong = dbHelperNew.getAllPlaylistData();

            if (mLayout.getPanelState() != PanelState.HIDDEN) {
                mLayout.setPanelState(PanelState.HIDDEN);
            } else {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }

            if (allPlaylistSong.size() > 0) {

                for (int i = 0; i < allPlaylistSong.size(); i++) {

                    Playlist tmpData = allPlaylistSong.get(i);

                    HashMap<String, String> song = new HashMap<String, String>();
                    song.put("songTitle", tmpData.getSongTitle());
                    song.put("songArtist", tmpData.getSongArtist());
                    song.put("songAlbum", tmpData.getSongAlbum());
                    song.put("songImage", tmpData.getSongImage());
                    song.put("songPath", tmpData.getSongUrl());
                    song.put("songCode", tmpData.getSongCode());
                    song.put("songAlbumCode", tmpData.getSongAlbumCode());
                    song.put("songLength", tmpData.getSongLength());

                    if (!saveList.contains(song)) {
                        saveList.add(song);
                    }
                }

                hasPlayList = true;

            } else{

                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            }

            SongsManager._songsList = saveList;

        } catch (Exception e) {

            Log.d("TAG", "Error while getdata from db");

        }
    }

    @Override
    protected void onResume() {

        try {

            if (getIntent().getStringExtra("FromNotification") != null) {

                String notificationData = getIntent().getStringExtra(
                        "FromNotification");

                if (notificationData.length() > 0) {

                    NotificationTask(notificationData);

                }

            }

            InvokeUserInfo();

            if (signback == true) {

                signback = false;

                if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

                    userNumber.setText(CheckUserInfo.getUserMsisdn());
                    tv_SignInUp.setText("Sign Out");

                } else {

                    userNumber.setText("User not signed in !");
                    tv_SignInUp.setText("Sign In");

                }

            }

        } catch (NullPointerException e) {

            Log.d("Tag", "server_msisdn is empty");

        }

        super.onResume();
    }


    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

       try {
            // Saving playlist in sp
            ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
            String titleOfSelectedSong = "";
            String artistOfSelectedSong = "";
            String albumOfSelectedSong = "";
            String imageOfSelectedSong = "";
            String urlOfSelectedSong = "";
            String codeOfSelectedSong = "";
            String albumCodeOfSelectedSong = "";
            String lengthOfSelectedSong = "";

            SongsManager songManager = new SongsManager();
            songsList = songManager.getPlayList();

            if (songsList.size() > 0) {

                dbHelperNew.deleteRows();

                for (int i = 0; i < songsList.size(); i++) {

                    titleOfSelectedSong = songsList.get(i).get("songTitle");
                    artistOfSelectedSong = songsList.get(i).get("songArtist");
                    albumOfSelectedSong = songsList.get(i).get("songAlbum");
                    imageOfSelectedSong = songsList.get(i).get("songImage");
                    urlOfSelectedSong = songsList.get(i).get("songPath");
                    codeOfSelectedSong = songsList.get(i).get("songCode");
                    albumCodeOfSelectedSong = songsList.get(i).get("songAlbumCode");
                    lengthOfSelectedSong = songsList.get(i).get("songLength");

                    Playlist playlist = new Playlist(titleOfSelectedSong, artistOfSelectedSong,
                            albumOfSelectedSong, imageOfSelectedSong, urlOfSelectedSong,
                            codeOfSelectedSong, albumCodeOfSelectedSong, lengthOfSelectedSong);

                    dbHelperNew.insertSong(playlist);

                }

            } else {

                dbHelperNew.deleteRows();

            }

        } catch (Exception e) {

            Log.d("TAG", "Error in saving data in db");

        }
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        final String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)){

            try {

                if(CheckUserInfo.getUserFcmStatus().contains("yes")){
                    return;
                }

            } catch (Exception ex) {
                Log.d("TAG", "Error in main thread");
            }

            // Try to register again, but not in the UI thread.
            // It's also necessary to cancel the thread
            // onDestroy(),
            // hence the use of AsyncTask instead of a raw
            // thread.
            final Context context = this;
            mRegisterTask = new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    // Register on our server
                    // On server creates a new user

                    try {

                        res_fcm = ServerUtilities.register(context, CheckUserInfo.getUserMsisdn(), regId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return res_fcm;
                }

                @Override
                protected void onPostExecute(String result) {

                    String res = "";

                    if (result != null && result.length() > 5) {

                        try {

                            JSONArray m_arr = new JSONArray(result);

                            for (int i = 0; i < m_arr.length(); i++) {

                                JSONObject m_jObj = m_arr.getJSONObject(i);

                                res = m_jObj.getString("result");

                            }

                            if(res.contains("success")){

                                CheckUserInfo.changeFCMToSp(getApplicationContext(), "yes");
                                //Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    mRegisterTask = null;
                }

            };

            mRegisterTask.execute(null, null, null);


        } else {

        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    public void InvokeUserInfo() {

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThreadIL reqThread = new RequestThreadIL();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error in Requested Thread");
        }
    }

    public class RequestThreadIL extends Thread {

        @Override
        public void run() {

            Looper.prepare();

            synchronized (this) {

                try {

                    CheckUserInfo.getUserMsisdnInfo(getApplicationContext());

                    CheckUserInfo.getUserLoginInfo(getApplicationContext(),
                            CheckUserInfo.getUserMsisdn(), CheckUserInfo.getUserPinCode());

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured in InvokeUserInfo");

                }

            }

            handleril.sendEmptyMessage(0);

        }
    }

    Handler handleril = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            //pd.dismiss();
            splashbar.setVisibility(View.GONE);

            try {

               if ((CheckUserInfo.getAppVersion() > CheckUserInfo.versionCode)) {

                    if (CheckUserInfo.getEnforceStatus() == 1) {

                        ForceUpGrade();

                    } else {

                        if (!upgrade) {

                            upgrade = true;

                            UpGrade();
                        }
                    }
                }

            } catch (Exception ex) {

                Log.d("TAG", "Error Occured");

            }

        }

    };

    private void UpGrade() {

        try {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this, AlertDialog.THEME_HOLO_DARK);
            alertDialogBuilder.setTitle("Update Available");
            alertDialogBuilder
                    .setMessage("Dear User, New Update of Bangla Dhol is available on playstore.");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    try {

                                        String appPackageName = getPackageName();
                                        Intent viewIntent = new Intent(
                                                "android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id="
                                                        + appPackageName));
                                        startActivity(viewIntent);

                                    } catch (Exception e) {

                                        Log.d("TAG", "Error in player service");

                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

        } catch (Exception ex) {

            Log.d("TAG", "Error Occured");

        }

    }

    private void ForceUpGrade() {

        try {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this, AlertDialog.THEME_HOLO_DARK);
            alertDialogBuilder.setTitle("Update Available");
            alertDialogBuilder.setMessage(CheckUserInfo.getEnforceText());

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    try {

                                        String appPackageName = getPackageName();
                                        Intent viewIntent = new Intent(
                                                "android.intent.action.VIEW",
                                                Uri.parse("https://play.google.com/store/apps/details?id="
                                                        + appPackageName));
                                        startActivity(viewIntent);

                                    } catch (Exception e) {

                                        Log.d("TAG", "Error in player service");

                                    }

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    MainActivity.this.finish();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();

        } catch (Exception ex) {

            Log.d("TAG", "Error Occured");

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {

        try {

            //SearchView searchView = (SearchView) findViewById(R.id.menu_search);

            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

                String query = intent.getStringExtra(SearchManager.QUERY);

                Bundle args = new Bundle();
                args.putString("Search", query);

                Fragment fragment = new SearchFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.home_container, fragment,
                        SearchFragment.FRAGMENT_TAG).addToBackStack(SearchFragment.FRAGMENT_TAG).commit();

            }

        } catch (Exception e) {

            Log.d("TAG", "Error in search");

        }

    }

    public void NotificationTask(String notiMsg) {

        try {

            not = notiMsg.split(";");
            noti_type = not[2];

        } catch (Exception ex) {

            Log.d("TAG", "Error in NotificationTask");

        }

        if (noti_type.contains("Song")) {

            try {

                noti_song_title = not[3];
                noti_song_artist = not[4];
                noti_song_album = not[5];
                noti_song_image = not[6];
                noti_song_url = not[7];
                noti_song_length = not[8];

                HashMap<String, String> song = new HashMap<String, String>();
                song.put("songTitle", noti_song_title);

                if (noti_song_artist != null) {
                    song.put("songArtist", noti_song_artist);
                } else {
                    song.put("songArtist", "Unknown Artist");
                }

                if (noti_song_album != null) {
                    song.put("songAlbum", noti_song_album);
                } else {
                    song.put("songAlbum", "Unknown Album");
                }

                song.put("songImage", noti_song_image);
                song.put("songPath", noti_song_url);

                int urlIndex = noti_song_url.indexOf("BG");
                noti_song_codecode = noti_song_url.substring(urlIndex);

                song.put("songCode", noti_song_codecode);

                if (noti_song_length != null) {
                    song.put("songLength", noti_song_length);
                } else {
                    song.put("songLength", "0.00");
                }

                if (!SongsManager._songsList.contains(song)) {

                    SongsManager._songsList.add(song);

                    int indexvalue = SongsManager._songsList.size() - 1;
                    playerService.putExtra("songIndex", indexvalue);
                    try {
                        startService(playerService);
                    } catch (Exception e) {
                        Log.d("TAG", "Error in player service");

                    }

                } else {

                    int index = SongsManager._songsList.indexOf(song);
                    playerService.putExtra("songIndex", index);
                    try {
                        startService(playerService);
                    } catch (Exception e) {
                        Log.d("TAG", "Error in player service");

                    }

                }

            } catch (Exception e) {

                Log.d("TAG", "Error in notification song item");

            }

        } else if (noti_type.contains("Album")) {

            try {

                noti_album_code = not[3];
                noti_album_title = not[4];
                noti_album_image = not[5];

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
                Bundle args = new Bundle();
                args.putString("albumCode", noti_album_code);
                args.putString("albumName", noti_album_title);
                args.putString("albumImage", noti_album_image);

                albumDetailsFragment.setArguments(args);
                //ft.setCustomAnimations(R.anim.slide_in_left,
                //R.anim.slide_out_right);
                ft.replace(R.id.home_container, albumDetailsFragment,
                        AlbumDetailsFragment.FRAGMENT_TAG);
                ft.addToBackStack(AlbumDetailsFragment.FRAGMENT_TAG);
                ft.commit();

            } catch (Exception ex) {

                Log.d("TAG", "Error in notification album item");

            }

        } else if (noti_type.contains("Message")) {

            // Toast.makeText(getApplicationContext(), "Simple Message",
            // Toast.LENGTH_SHORT).show();

        }

    }
     /**
     * Initiaze Views
     */
    private void initViews() {

        // mini player items

        frameLayout = (FrameLayout) findViewById(R.id.home_container);

        mPlayerThumb = (ImageView) findViewById(R.id.mplayer_song_image);
        mPlayer_btnPlay = (ImageView) findViewById(R.id.mplayer_btn_play);
        mPlayer_title = (TextView) findViewById(R.id.mplayer_song_title);
        mPlayer_artist = (TextView) findViewById(R.id.mplayer_artist);
        mPlayer_duration = (TextView) findViewById(R.id.mplayer_duration);
        //mPlayer_totalduration = (TextView) findViewById(R.id.mplayer_totalduration);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_mini_player_loading);
        songProgressLayout = (RelativeLayout) findViewById(R.id.rl_player_progress_bar_container);
        //mProgressBar.setVisibility(View.VISIBLE);
        // full palyer items
        // playershade = (View) findViewById(R.id.playershade);
        songProgressBar = (SeekBar) findViewById(R.id.seekBar);
        fPlayer_album = (TextView) findViewById(R.id.player_bottom_secondary_text);
        fPlayer_title = (TextView) findViewById(R.id.now_playing_track);
        fPlayer_artist = (TextView) findViewById(R.id.now_playing_track_artist);
        songCurrentDurationLabel = (TextView) findViewById(R.id.tvPlayerStartTimer);
        songTotalDurationLabel = (TextView) findViewById(R.id.tvPlayerEndTimer);
        fPlayerThumb = (ImageView) findViewById(R.id.now_playing_thumb);
        btnRepeat = (ImageView) findViewById(R.id.playerBtnRepeat);
        btnShuffle = (ImageView) findViewById(R.id.playerBtnShuffle);
        btnNext = (ImageView) findViewById(R.id.playerBtnNext);
        btnPrevious = (ImageView) findViewById(R.id.playerBtnPrev);
        btnPlay = (ImageView) findViewById(R.id.playerBtnPlay);
        btnQueue = (ImageView) findViewById(R.id.playerQueue);
        btnRBT = (ImageView) findViewById(R.id.videoIcon);
        btnAlbum = (ImageView) findViewById(R.id.lyricsIcon);
        fullPlayer = (View) findViewById(R.id.playershade);

        btnRepeat.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnRBT.setOnClickListener(this);
        btnAlbum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mLayout != null &&
                        (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                                mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

                try {

                    albumOfSelectedSong = PlayerService.nameAlbum;
                    artistOfSelectedSong = PlayerService.artistAlbum;
                    imageOfSelectedSong = PlayerService.imageAlbum;
                    codeOfSelectedAlbum = PlayerService.codeAlbum;

                } catch (Exception e) {

                    Log.d("TAG", "Error in player service");

                }

                if (codeOfSelectedAlbum != null && codeOfSelectedAlbum.length() > 7) {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("albumCode", codeOfSelectedAlbum);
                    args.putString("albumName", albumOfSelectedSong);
                    args.putString("albumArtist", artistOfSelectedSong);
                    args.putString("albumGenre", "");
                    args.putString("albumPublisher", "");
                    args.putString("albumYear", "");
                    args.putInt("albumCount", 0);
                    args.putString("albumImage", imageOfSelectedSong);
                    albumDetailsFragment.setArguments(args);
                    //ft.setCustomAnimations(R.anim.slide_in_left,
                    //R.anim.slide_out_right);
                    ft.replace(R.id.home_container, albumDetailsFragment, AlbumDetailsFragment.FRAGMENT_TAG);
                    ft.addToBackStack(AlbumDetailsFragment.FRAGMENT_TAG);
                    ft.commit();

                } else {

                    Toast.makeText(getApplicationContext(),
                            "Album not available.", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnQueue.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {

                    if (mLayout != null &&
                            (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                                    mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    PlayerQueueFragment playerQueueFragment = new PlayerQueueFragment();
                    ft.replace(R.id.home_container, playerQueueFragment, PlayerQueueFragment.FRAGMENT_TAG);
                    ft.addToBackStack(PlayerQueueFragment.FRAGMENT_TAG);
                    ft.commit();

                }catch (Exception ex){

                }

            }
        });

        mPlayer_btnPlay.setOnClickListener(this);

    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void setActionBarTranslation(float y) {
        // Figure out the actionbar height
        int actionBarHeight = getActionBarHeight();
        // A hack to add the translation to the action bar
        ViewGroup content = ((ViewGroup) findViewById(android.R.id.content)
                .getParent());
        int children = content.getChildCount();
        for (int i = 0; i < children; i++) {
            View child = content.getChildAt(i);
            if (child.getId() != android.R.id.content) {
                if (y <= -actionBarHeight) {
                    child.setVisibility(View.GONE);
                } else {
                    child.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        child.setTranslationY(y);
                    } else {
                        AnimatorProxy.wrap(child).setTranslationY(y);
                    }
                }
            }
        }
    }

    /**
     * Slide menu item click listener
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // getMenuInflater().inflate(R.menu.twynk_menu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bangla_dhol_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
		/*
         * MenuItem item = menu.findItem(R.id.action_toggle); if (mLayout !=
		 * null) { if (mLayout.isPanelHidden()) {
		 * item.setTitle(R.string.action_show); } else {
		 * item.setTitle(R.string.action_hide); } }
		 */

        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.menu_search: {

                // onSearchRequested();
                if (mLayout != null &&
                        (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                                mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

                return true;
            }

		/*
         * case R.id.action_toggle: { if (mLayout != null) { if
		 * (!mLayout.isPanelHidden()) { mLayout.hidePanel();
		 * item.setTitle(R.string.action_show); } else { mLayout.showPanel();
		 * item.setTitle(R.string.action_hide); } } return true; }/* /*case
		 * R.id.action_anchor: { if (mLayout != null) { if
		 * (mLayout.getAnchorPoint() == 1.0f) { mLayout.setAnchorPoint(0.7f);
		 * mLayout.expandPanel(0.7f);
		 * item.setTitle(R.string.action_anchor_disable); } else {
		 * mLayout.setAnchorPoint(1.0f); mLayout.collapsePanel();
		 * item.setTitle(R.string.action_anchor_enable); } } return true; }
		 */
        }
        return super.onOptionsItemSelected(item);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.about).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments

        Fragment fragment = null;

        switch (position) {

            case 0:

                fragment = new HomeFragment();
                tag = HomeFragment.FRAGMENT_TAG;
                break;

            case 1:

                fragment = new MyPlaylistFragment();
                tag = MyPlaylistFragment.FRAGMENT_TAG;
                //fragment = new PlayerQueueFragment();
                //tag = PlayerQueueFragment.FRAGMENT_TAG;
                break;

            case 2:

                fragment = new AccountFragment();
                tag = AccountFragment.FRAGMENT_TAG;
                break;

            case 3:

                fragment = new SubscriptionFragment();
                tag = SubscriptionFragment.FRAGMENT_TAG;
                break;

            case 4:
                startActivity(new Intent(this, FeedbackActivity.class));
                //fragment = new FeedBackFragment();
                //tag = FeedBackFragment.FRAGMENT_TAG;
                break;
            case 6:

                fragment = new HelpFragment();
                tag = HelpFragment.FRAGMENT_TAG;
                break;

            case 7:

                fragment = new InfoFragment();
                tag = InfoFragment.FRAGMENT_TAG;
                break;

            case 8:

                mDrawerLayout.closeDrawer(mRelativeDrawer);
                ExitApp();
                break;

            default:
                break;
        }

        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            while (fragmentManager.popBackStackImmediate())
                ;

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.home_container, fragment, tag);

            if (!(fragment instanceof HomeFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(dataList.get(position).getItemName());
            mDrawerLayout.closeDrawer(mRelativeDrawer);

            /*FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.home_container, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commit(); */

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (dataList.get(position).getTitle() == null) {

                displayView(position);
            }

        }
    }

    private OnClickListener userOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mDrawerLayout.closeDrawer(mRelativeDrawer);
        }
    };

    public void RequestForLogout() {

        try {

            pd = new ProgressDialog(MainActivity.this, ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Signing Out ...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();

            RequestThreadLogout reqThread = new RequestThreadLogout();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error in Requested Thread");
        }
    }

    public class RequestThreadLogout extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    if (CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())
                            && CheckUserInfo.getUserPinCode().length() <= 0) {

                        val = ServerUtilities.requestForLogout(
                                getApplicationContext(),
                                CheckUserInfo.getUserMsisdn(),
                                CheckUserInfo.getUserPinCode(),
                                CheckUserInfo.deviceId, CheckUserInfo.imei,
                                CheckUserInfo.imsi,
                                CheckUserInfo.softwareVersion,
                                CheckUserInfo.simSerialNumber,
                                CheckUserInfo.operator,
                                CheckUserInfo.operatorName,
                                CheckUserInfo.brand, CheckUserInfo.model,
                                CheckUserInfo.release,
                                CheckUserInfo.sdkVersion,
                                CheckUserInfo.versionCode, "yes", "logout");

                    } else {

                        val = ServerUtilities.requestForLogout(
                                getApplicationContext(),
                                CheckUserInfo.getUserMsisdn(),
                                CheckUserInfo.getUserPinCode(),
                                CheckUserInfo.deviceId, CheckUserInfo.imei,
                                CheckUserInfo.imsi,
                                CheckUserInfo.softwareVersion,
                                CheckUserInfo.simSerialNumber,
                                CheckUserInfo.operator,
                                CheckUserInfo.operatorName,
                                CheckUserInfo.brand, CheckUserInfo.model,
                                CheckUserInfo.release,
                                CheckUserInfo.sdkVersion,
                                CheckUserInfo.versionCode, "", "logout");

                    }

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured RequestThreadLogout");

                }

            }

            handlerlogout.sendEmptyMessage(0);

        }
    }

    Handler handlerlogout = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            pd.dismiss();

            try {

                if (val != null) {

                    JSONArray m_arr = new JSONArray(val);

                    for (int i = 0; i < m_arr.length(); i++) {

                        JSONObject m_jObj = m_arr.getJSONObject(i);

                        res = m_jObj.getString("result");
                        message = m_jObj.getString("changetext");

                    }

                    if (res.contains("success")) {

                        CheckUserInfo.removeUserInfo(getApplicationContext());
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();

                    }

                } else {

                    Toast.makeText(MainActivity.this, "Can not process your request now. Try Later.",
                            Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };

    private void ExitApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                if (PlayerService.mediaPlayer.isPlaying()) {

                                    @SuppressWarnings("deprecation")
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            MainActivity.this);
                                    builder.setMessage("App will be run on Background")
                                            .setCancelable(false)
                                            .setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {

                                                            moveTaskToBack(true);
                                                            //MainActivity.this.finish();
                                                        }
                                                    })
                                            .setNegativeButton(
                                                    "Exit",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {

                                                            PlayerService.mediaPlayer.stop();
                                                            MainActivity.this.finish();
                                                        }
                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                } else {

                                    MainActivity.this.finish();

                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    public void onBackPressed() {

        if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED ||
                mLayout.getPanelState() == PanelState.ANCHORED)) {

            mLayout.setPanelState(PanelState.COLLAPSED);

        } else {

            mDrawerLayout.closeDrawer(mRelativeDrawer);

            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else if (contentFragment instanceof HomeFragment
                    || fm.getBackStackEntryCount() == 0) {
                ExitApp();
            }

        }

    }

	/*
     * @Override public void onBackPressed() { FragmentManager fm =
	 * getFragmentManager(); if (fm.getBackStackEntryCount() > 0) {
	 * super.onBackPressed(); } else if (contentFragment instanceof HomeFragment
	 * || fm.getBackStackEntryCount() == 0) { finish(); } }
	 */


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
