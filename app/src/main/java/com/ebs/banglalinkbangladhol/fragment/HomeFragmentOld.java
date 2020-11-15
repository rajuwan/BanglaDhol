package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.activity.MainActivity;
import com.ebs.banglalinkbangladhol.adapter.DynamicAlbumAdapter;
import com.ebs.banglalinkbangladhol.adapter.DynamicTwoWayBaseAdapter;
import com.ebs.banglalinkbangladhol.bean.HOMETAG;
import com.ebs.banglalinkbangladhol.bean.Song;
import com.ebs.banglalinkbangladhol.others.AsyncCoverTask;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.MyApplication;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.ebs.banglalinkbangladhol.service.PlayerService;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.Album;
import com.ebs.banglalinkbangladhol.others.SongsManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragmentOld extends Fragment {

    public HomeFragmentOld() {}

    private ProgressDialog pd;
    private ProgressBar splashbar;

    public String idOfSelectedSong, titleOfSelectedSong, artistOfSelectedSong,
            albumOfSelectedSong, albumCodeOfSelectedSong, imageOfSelectedSong,
            urlOfSelectedSong, urlWowzaOfSelectedSong, lengthOfSelectedSong, cpOfSelectedSong,
            genreOfSelectedSong, catcodeOfSelectedSong;

    public String selectedCode, selectedCatname, selectedArtistName, selectedAlbumName,
            selectedPublisher, selectedRelease, selectedImage;
    private Integer selectedCount;

    public JSONObject subc_home_recently_played, subc_home_new_release, subc_home_albums_top,
            subc_home_playlist_top, subc_home_pop_modern, subc_home_movie_song,
            subc_home_folk, subc_home_classical, subc_home_all_time_hit, subc_home_band;

    private String title_recently_played, title_new_release, title_album_top, title_playlist_top,
            title_pop_modern, title_movie_song, title_folk, title_classical, title_all_time_hit,
            title_band;

    private List<Song> jsonRecentlyPlayed, jsonNewRelease;

    public List<Album> albumList, albumList2, albumList3, albumList4, albumList5, albumList6,
            albumList7, albumList8;

    public static String[] itemsIdArray, itemsTitleArray, itemsArtistArray, itemAlbumArray,
            itemAlbumCodeArray, itemsImageArray, itemsUrlArray, itemsUrlWowzaArray, itemsLengthArray,
            itemsCpArray, itemsGenreArray, itemsCatCodeArray;

    public static String[] itemsIdArray2, itemsTitleArray2, itemsArtistArray2, itemAlbumArray2,
            itemAlbumCodeArray2, itemsImageArray2, itemsUrlArray2, itemsUrlWowzaArray2, itemsLengthArray2,
            itemsCpArray2, itemsGenreArray2, itemsCatCodeArray2;

    public static String[] AitemsCodeArray, AitemsCatNameArray, AitemsArtistNameArray,
            AitemsAlbumNameArray, AitemsCpArray, AitemsReleaseArray, AitemsImageArray;

    public static String[] AitemsCodeArray2, AitemsCatNameArray2, AitemsArtistNameArray2,
            AitemsAlbumNameArray2, AitemsCpArray2, AitemsReleaseArray2, AitemsImageArray2;

    public static String[] AitemsCodeArray3, AitemsCatNameArray3, AitemsArtistNameArray3,
            AitemsAlbumNameArray3, AitemsCpArray3, AitemsReleaseArray3, AitemsImageArray3;

    public static String[] AitemsCodeArray4, AitemsCatNameArray4, AitemsArtistNameArray4,
            AitemsAlbumNameArray4, AitemsCpArray4, AitemsReleaseArray4, AitemsImageArray4;

    public static String[] AitemsCodeArray5, AitemsCatNameArray5, AitemsArtistNameArray5,
            AitemsAlbumNameArray5, AitemsCpArray5, AitemsReleaseArray5, AitemsImageArray5;

    public static String[] AitemsCodeArray6, AitemsCatNameArray6, AitemsArtistNameArray6,
            AitemsAlbumNameArray6, AitemsCpArray6, AitemsReleaseArray6, AitemsImageArray6;

    public static String[] AitemsCodeArray7, AitemsCatNameArray7, AitemsArtistNameArray7,
            AitemsAlbumNameArray7, AitemsCpArray7, AitemsReleaseArray7, AitemsImageArray7;

    public static String[] AitemsCodeArray8, AitemsCatNameArray8, AitemsArtistNameArray8,
            AitemsAlbumNameArray8, AitemsCpArray8, AitemsReleaseArray8, AitemsImageArray8;

    public static Integer[] AiitemsCountArray, AiitemsCountArray2, AiitemsCountArray3,
            AiitemsCountArray4, AiitemsCountArray5, AiitemsCountArray6, AiitemsCountArray7, AiitemsCountArray8;

    public byte[] art;
    Bitmap cover_bg =  null;

    public TextView  home_recently_played_title, home_new_release_title, home_albums_title,
            home_playlist_title, home_pop_modern_title, home_movie_song_title, home_folk_title,
            home_classical_title, home_all_time_hit_title, home_band_title;

    public TextView  home_recently_played_see_all, home_new_release_see_all, home_albums_see_all,
            home_playlist_see_all, home_pop_modern_see_all, home_movie_song_see_all, home_folk_see_all,
            home_classical_see_all, home_all_time_hit_see_all, home_band_see_all;

    public TwoWayView  listview_recently_played, listview_new_release, listview_albums,
            listview_playlist, listview_pop_modern, listview_movie_song, listview_folk,
            listview_classical, listview_all_time_hit, listview_band;

    private SongsManager songManager;
    public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    private Intent playerService;
    private TextView tickerTextView;
    private String tickerText = null;

    private AlertDialog levelDialog;
    private RelativeLayout subLayout;
    public static String savedPlaylist = null;

    public static final String FRAGMENT_TAG = "HomeFragment";

    private ImageView promo;
    Bitmap cover = null;
    String PROMO_URL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.magiclistview_home, container, false);

        getActivity().setTitle("Bangla Dhol");

        initViews(view);

        promo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    TopBannerFragment topBannerFragment = new TopBannerFragment();
                    Bundle args = new Bundle();
                    args.putString("url", "HomeDynamicList");
                    topBannerFragment.setArguments(args);
                    ft.replace(R.id.home_container, topBannerFragment, TopBannerFragment.FRAGMENT_TAG);
                    ft.addToBackStack(TopBannerFragment.FRAGMENT_TAG);
                    ft.commit();

                } catch (Exception e) {

                    Log.d("TAG", "Error Occured TopSlide Banner Image Click");

                }
            }
        });

        try {

            tickerText = CheckUserInfo.getTickerText();

            if (tickerText != null) {
                tickerTextView.setText(tickerText);
                subLayout.setVisibility(View.GONE); // need to remove while ok
            } else {
                subLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        songManager = new SongsManager();
        songsList = songManager.getPlayList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Invoke();
        MyApplication.getInstance().trackScreenView("Home Fragment");
    }

    public void LoadTopSlide() {
        // Calling Top Slid Image Show
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Home9Fragment albumsFragment = new Home9Fragment();
        ft.replace(R.id.content_frame, albumsFragment, "fragment");
        // ft.addToBackStack(null);
        ft.commit();
    }

    public void Invoke() {

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThread reqThread = new RequestThread();
            reqThread.start();

        } catch (Exception ex) {
            MyApplication.getInstance().trackException(ex);
            Log.d("TAG", "Error in Requested Thread");
        }
    }

    /**
     * To get Dynamic array List using -- Thread*
     */
    public class RequestThread extends Thread {

        @Override
        public void run() {

            Looper.prepare();

            synchronized (this) {

                try {

                    cover = new AsyncCoverTask().execute(PROMO_URL).get();

                    LoadTopSlide();

                    if(HOMETAG.HOME_RECENTLY_PLAYED == null){

                        subc_home_recently_played = ServerUtilities.
                                requestForRecentPlayed(CheckUserInfo.getUserMsisdn());

                        HOMETAG.HOME_RECENTLY_PLAYED = subc_home_recently_played;

                    } else {

                        subc_home_recently_played = HOMETAG.HOME_RECENTLY_PLAYED;

                    }

                    if(HOMETAG.HOME_NEW_RELEASE == null){

                        subc_home_new_release = ServerUtilities.
                                requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "newrelease", 1, 1);

                        HOMETAG.HOME_NEW_RELEASE = subc_home_new_release;

                    } else {

                        subc_home_new_release = HOMETAG.HOME_NEW_RELEASE;

                    }

                    if(HOMETAG.HOME_ALBUM_TOP == null){

                        subc_home_albums_top = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "newrelease", "album", 1);

                        HOMETAG.HOME_ALBUM_TOP = subc_home_albums_top;

                    } else {

                        subc_home_albums_top = HOMETAG.HOME_ALBUM_TOP;

                    }

                    if(HOMETAG.HOME_PLAYLIST_TOP == null){

                        subc_home_playlist_top = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "alltimehit", "playlist", 1);

                        HOMETAG.HOME_PLAYLIST_TOP = subc_home_playlist_top;

                    } else {

                        subc_home_playlist_top = HOMETAG.HOME_PLAYLIST_TOP;

                    }

                    if(HOMETAG.HOME_POP_MODERN == null){

                        //subc_home_pop_modern = ServerUtilities.
                        //requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "popmodern", 0, 1);
                        subc_home_pop_modern = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "popmodern", "album", 1);

                        HOMETAG.HOME_POP_MODERN = subc_home_pop_modern;

                    } else {

                        subc_home_pop_modern = HOMETAG.HOME_POP_MODERN;

                    }

                    if(HOMETAG.HOME_MOVIE_SONG == null){

                        //subc_home_movie_song = ServerUtilities.
                        //requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "moviesong", 0, 1);
                        subc_home_movie_song = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "moviesong", "album", 1);

                        HOMETAG.HOME_MOVIE_SONG = subc_home_movie_song;

                    } else {

                        subc_home_movie_song = HOMETAG.HOME_MOVIE_SONG;

                    }

                    if(HOMETAG.HOME_FOLK == null){

                        //subc_home_folk = ServerUtilities.
                        //requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "folk", 0, 1);
                        subc_home_folk = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "folk", "album", 1);

                        HOMETAG.HOME_FOLK = subc_home_folk;

                    } else {

                        subc_home_folk = HOMETAG.HOME_FOLK;

                    }

                    if(HOMETAG.HOME_CLASSICAL == null){

                        //subc_home_classical = ServerUtilities.
                        //requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "classical", 0, 1);
                        subc_home_classical = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "classical", "album", 1);

                        HOMETAG.HOME_CLASSICAL = subc_home_classical;

                    } else {

                        subc_home_classical = HOMETAG.HOME_CLASSICAL;

                    }

                    if(HOMETAG.HOME_ALL_TIME_HIT == null){

                        //subc_home_all_time_hit = ServerUtilities.
                        //requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "alltimehit", 0, 1);
                        subc_home_all_time_hit = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "alltimehit", "album", 1);

                        HOMETAG.HOME_ALL_TIME_HIT = subc_home_all_time_hit;

                    } else {

                        subc_home_all_time_hit = HOMETAG.HOME_ALL_TIME_HIT;

                    }

                    if(HOMETAG.HOME_BAND == null){

                        //subc_home_band = ServerUtilities.
                        //requestForCatJsonPost(CheckUserInfo.getUserMsisdn(), "band", 0, 1);
                        subc_home_band = ServerUtilities.
                                requestForAlbumJsonPost(CheckUserInfo.getUserMsisdn(), "band", "album", 1);

                        HOMETAG.HOME_BAND = subc_home_band;

                    } else {

                        subc_home_band = HOMETAG.HOME_BAND;

                    }

                } catch (Exception ex) {
                    MyApplication.getInstance().trackException(ex);
                    Log.d("TAG", "Error in data thread");
                }

            }

            recentlyplayed_handler.sendEmptyMessage(0);
            newrelease_handler.sendEmptyMessage(0);
            albums_handler.sendEmptyMessage(0); //1
            playlist_handler.sendEmptyMessage(0); //2
            popmodern_handler.sendEmptyMessage(0); //3
            moviesong_handler.sendEmptyMessage(0); //4
            folk_handler.sendEmptyMessage(0); //5
            classical_handler.sendEmptyMessage(0); //6
            alltimehit_handler.sendEmptyMessage(0); //7
            band_handler.sendEmptyMessage(0); //8
        }
    }

    // 1. Recently Played

    Handler recentlyplayed_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            if (cover == null) {
                promo.setVisibility(View.GONE);
            } else {
                promo.setVisibility(View.VISIBLE);
                promo.setImageBitmap(cover);
            }

            try {

                if(subc_home_recently_played != null){

                    jsonRecentlyPlayed = HTTPGateway.getDynamicCatJSONPOST(subc_home_recently_played);

                    if(jsonRecentlyPlayed.size() == 0){
                        return;
                    } else {
                        title_recently_played = "Recently Played";
                        home_recently_played_title.setVisibility(View.VISIBLE);
                        home_recently_played_title.setText(title_recently_played);
                        //home_recently_played_see_all.setVisibility(View.VISIBLE);
                        listview_recently_played.setVisibility(View.VISIBLE);
                    }

                    List<String> contentid_list = new ArrayList<String>();
                    List<String> name_list = new ArrayList<String>();
                    List<String> artist_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> image_list = new ArrayList<String>();
                    List<String> url_list = new ArrayList<String>();
                    List<String> urlwowza_list = new ArrayList<String>();
                    List<String> duration_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> genre_list = new ArrayList<String>();
                    List<String> catcode_list = new ArrayList<String>();

                    for (int i = 0; i < jsonRecentlyPlayed.size(); i++) {

                        Song tmpData = jsonRecentlyPlayed.get(i);
                        contentid_list.add(tmpData.getContentid());
                        name_list.add(tmpData.getName());
                        artist_list.add(tmpData.getArtist());
                        albumname_list.add(tmpData.getAlbumname());
                        albumcode_list.add(tmpData.getAlbumcode());
                        image_list.add(tmpData.getImage());
                        url_list.add(tmpData.getUrl());
                        urlwowza_list.add(tmpData.getUrlWowza());
                        duration_list.add(tmpData.getDuration());
                        cp_list.add(tmpData.getCp());
                        genre_list.add(tmpData.getGenre());
                        catcode_list.add(tmpData.getCatcode());
                    }

                    itemsIdArray = contentid_list.toArray(new String[] {});
                    itemsTitleArray = name_list.toArray(new String[] {});
                    itemsArtistArray = artist_list.toArray(new String[] {});
                    itemAlbumArray = albumname_list.toArray(new String[] {});
                    itemAlbumCodeArray = albumcode_list.toArray(new String[] {});
                    itemsImageArray = image_list.toArray(new String[] {});
                    itemsUrlArray = url_list.toArray(new String[] {});
                    itemsUrlWowzaArray = urlwowza_list.toArray(new String[] {});
                    itemsLengthArray = duration_list.toArray(new String[] {});
                    itemsCpArray = cp_list.toArray(new String[] {});
                    itemsGenreArray = genre_list.toArray(new String[] {});
                    itemsCatCodeArray = catcode_list.toArray(new String[] {});

                    listview_recently_played.setAdapter(new DynamicTwoWayBaseAdapter(getActivity(),
                            itemsTitleArray, itemsImageArray));

                    home_recently_played_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToContentSeeAllFragment(title_recently_played, "recent", 0);
                                }
                            });

                    listview_recently_played
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    idOfSelectedSong = itemsIdArray[position];
                                    titleOfSelectedSong = itemsTitleArray[position];
                                    artistOfSelectedSong = itemsArtistArray[position];
                                    albumOfSelectedSong = itemAlbumArray[position];
                                    albumCodeOfSelectedSong = itemAlbumCodeArray[position];
                                    imageOfSelectedSong = itemsImageArray[position];
                                    urlOfSelectedSong = itemsUrlArray[position];
                                    urlWowzaOfSelectedSong = itemsUrlWowzaArray[position];
                                    lengthOfSelectedSong = itemsLengthArray[position];
                                    cpOfSelectedSong = itemsCpArray[position];

                                    ContentPlayCheck();

                                    MyApplication.getInstance().trackEvent("Recently Played", "Song Play",
                                            titleOfSelectedSong + "/" + artistOfSelectedSong + "/" + albumOfSelectedSong);
                                }
                            });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home Recently Played");
            }

        }

    };

    // 2. New Release

    Handler newrelease_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_new_release != null){

                    jsonNewRelease = HTTPGateway.getDynamicCatJSONPOST(subc_home_new_release);

                    if(jsonNewRelease.size() == 0){
                        return;
                    } else{
                        title_new_release = subc_home_new_release.getString("catname");
                        home_new_release_title.setVisibility(View.VISIBLE);
                        home_new_release_title.setText(title_new_release);
                        home_new_release_see_all.setVisibility(View.VISIBLE);
                        listview_new_release.setVisibility(View.VISIBLE);
                    }

                    List<String> contentid_list = new ArrayList<String>();
                    List<String> name_list = new ArrayList<String>();
                    List<String> artist_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> image_list = new ArrayList<String>();
                    List<String> url_list = new ArrayList<String>();
                    List<String> urlwowza_list = new ArrayList<String>();
                    List<String> duration_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> genre_list = new ArrayList<String>();
                    List<String> catcode_list = new ArrayList<String>();

                    for (int i = 0; i < jsonNewRelease.size(); i++) {

                        Song tmpData = jsonNewRelease.get(i);
                        contentid_list.add(tmpData.getContentid());
                        name_list.add(tmpData.getName());
                        artist_list.add(tmpData.getArtist());
                        albumname_list.add(tmpData.getAlbumname());
                        albumcode_list.add(tmpData.getAlbumcode());
                        image_list.add(tmpData.getImage());
                        url_list.add(tmpData.getUrl());
                        urlwowza_list.add(tmpData.getUrlWowza());
                        duration_list.add(tmpData.getDuration());
                        cp_list.add(tmpData.getCp());
                        genre_list.add(tmpData.getGenre());
                        catcode_list.add(tmpData.getCatcode());
                    }

                    itemsIdArray2 = contentid_list.toArray(new String[] {});
                    itemsTitleArray2 = name_list.toArray(new String[] {});
                    itemsArtistArray2 = artist_list.toArray(new String[] {});
                    itemAlbumArray2 = albumname_list.toArray(new String[] {});
                    itemAlbumCodeArray2 = albumcode_list.toArray(new String[] {});
                    itemsImageArray2 = image_list.toArray(new String[] {});
                    itemsUrlArray2 = url_list.toArray(new String[] {});
                    itemsUrlWowzaArray2 = urlwowza_list.toArray(new String[] {});
                    itemsLengthArray2 = duration_list.toArray(new String[] {});
                    itemsCpArray2 = cp_list.toArray(new String[] {});
                    itemsGenreArray2 = genre_list.toArray(new String[] {});
                    itemsCatCodeArray2 = catcode_list.toArray(new String[] {});

                    listview_new_release.setAdapter(new DynamicTwoWayBaseAdapter(getActivity(),
                            itemsTitleArray2, itemsImageArray2));

                    home_new_release_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToContentSeeAllFragment(title_new_release, "newrelease", 1);
                                }
                            });

                    listview_new_release
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    idOfSelectedSong = itemsIdArray2[position];
                                    titleOfSelectedSong = itemsTitleArray2[position];
                                    artistOfSelectedSong = itemsArtistArray2[position];
                                    albumOfSelectedSong = itemAlbumArray2[position];
                                    albumCodeOfSelectedSong = itemAlbumCodeArray2[position];
                                    imageOfSelectedSong = itemsImageArray2[position];
                                    urlOfSelectedSong = itemsUrlArray2[position];
                                    urlWowzaOfSelectedSong = itemsUrlWowzaArray2[position];
                                    lengthOfSelectedSong = itemsLengthArray2[position];
                                    cpOfSelectedSong = itemsCpArray2[position];

                                    ContentPlayCheck();

                                    MyApplication.getInstance().trackEvent("New Release", "Song Play",
                                            titleOfSelectedSong + "/" + artistOfSelectedSong + "/" + albumOfSelectedSong);

                                }
                            });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 3. Albums handlers
    Handler albums_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_albums_top != null){

                    albumList = HTTPGateway.getDynamicAlbumList(subc_home_albums_top);
                    if(albumList.size() == 0){
                        return;
                    } else {
                        title_album_top = subc_home_albums_top.getString("catname");
                        home_albums_title.setVisibility(View.VISIBLE);
                        home_albums_title.setText(title_album_top);
                        home_albums_see_all.setVisibility(View.VISIBLE);
                        listview_albums.setVisibility(View.VISIBLE);
                    }

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
                    AiitemsCountArray = count_list.toArray(new Integer[] {});
                    AitemsImageArray = image_list.toArray(new String[] {});

                    listview_albums.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray, AitemsImageArray));

                    home_albums_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("newrelease", "album", title_album_top);

                                }
                            });

                    listview_albums.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, final int position, long id) {

                            try{

                                selectedCode = AitemsCodeArray[position];
                                selectedCatname = AitemsCatNameArray[position];
                                selectedArtistName = AitemsArtistNameArray[position];
                                selectedAlbumName =AitemsAlbumNameArray[position];
                                selectedPublisher = AitemsCpArray[position];
                                selectedRelease = AitemsReleaseArray[position];
                                selectedCount = AiitemsCountArray[position];
                                selectedImage = AitemsImageArray[position];

                                goToAlbumSingle();

                                MyApplication.getInstance().trackEvent("New Albums", "Album",
                                        selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);

                            } catch (Exception ex){
                                Log.d("TAG", "Error in Album OnItemClick");
                            }

                        }
                    });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 10. Playlist handlers
    Handler playlist_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_playlist_top != null){

                    albumList2 = HTTPGateway.getDynamicAlbumList(subc_home_playlist_top);
                    if(albumList2.size() == 0){
                        return;
                    } else{
                        title_playlist_top = subc_home_playlist_top.getString("catname");
                        home_playlist_title.setVisibility(View.VISIBLE);
                        home_playlist_title.setText(title_playlist_top);
                        home_playlist_see_all.setVisibility(View.VISIBLE);
                        listview_playlist.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList2.size(); i++) {

                        Album tmpData = albumList2.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }

                    AitemsCodeArray2 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray2 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray2 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray2 = albumname_list.toArray(new String[] {});
                    AitemsCpArray2 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray2 = release_list.toArray(new String[] {});
                    AiitemsCountArray2 = count_list.toArray(new Integer[] {});
                    AitemsImageArray2 = image_list.toArray(new String[] {});

                    listview_playlist.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray2, AitemsImageArray2));

                    home_playlist_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("alltimehit", "playlist", title_playlist_top);

                                }
                            });

                    listview_playlist
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray2[position];
                                        selectedCatname = AitemsCatNameArray2[position];
                                        selectedArtistName = AitemsArtistNameArray2[position];
                                        selectedAlbumName =AitemsAlbumNameArray2[position];
                                        selectedPublisher = AitemsCpArray2[position];
                                        selectedRelease = AitemsReleaseArray2[position];
                                        selectedCount = AiitemsCountArray2[position];
                                        selectedImage = AitemsImageArray2[position];

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

                                        MyApplication.getInstance().trackEvent("Top Playlist", "Playlist",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);

                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 4. Pop Modern

    Handler popmodern_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_pop_modern != null){

                    albumList3 = HTTPGateway.getDynamicAlbumList(subc_home_pop_modern);
                    if(albumList3.size() == 0){
                        return;
                    } else {
                        title_pop_modern = subc_home_pop_modern.getString("catname");
                        home_pop_modern_title.setVisibility(View.VISIBLE);
                        home_pop_modern_title.setText(title_pop_modern);
                        home_pop_modern_see_all.setVisibility(View.VISIBLE);
                        listview_pop_modern.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList3.size(); i++) {

                        Album tmpData = albumList3.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }


                    AitemsCodeArray3 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray3 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray3 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray3 = albumname_list.toArray(new String[] {});
                    AitemsCpArray3 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray3 = release_list.toArray(new String[] {});
                    AiitemsCountArray3 = count_list.toArray(new Integer[] {});
                    AitemsImageArray3 = image_list.toArray(new String[] {});

                    listview_pop_modern.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray3, AitemsImageArray3));

                    home_pop_modern_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("popmodern", "album", title_pop_modern);

                                }
                            });

                    listview_pop_modern
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray3[position];
                                        selectedCatname = AitemsCatNameArray3[position];
                                        selectedArtistName = AitemsArtistNameArray3[position];
                                        selectedAlbumName =AitemsAlbumNameArray3[position];
                                        selectedPublisher = AitemsCpArray3[position];
                                        selectedRelease = AitemsReleaseArray3[position];
                                        selectedCount = AiitemsCountArray3[position];
                                        selectedImage = AitemsImageArray3[position];

                                        goToAlbumSingle();

                                        MyApplication.getInstance().trackEvent("Pop Modern", "Album",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);

                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 5. Movie Song

    Handler moviesong_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_movie_song != null){

                    albumList4 = HTTPGateway.getDynamicAlbumList(subc_home_movie_song);
                    if(albumList4.size() == 0){
                        return;
                    } else {

                        title_movie_song = subc_home_movie_song.getString("catname");
                        home_movie_song_title.setVisibility(View.VISIBLE);
                        home_movie_song_title.setText(title_movie_song);
                        home_movie_song_see_all.setVisibility(View.VISIBLE);
                        listview_movie_song.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList4.size(); i++) {

                        Album tmpData = albumList4.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }


                    AitemsCodeArray4 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray4 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray4 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray4 = albumname_list.toArray(new String[] {});
                    AitemsCpArray4 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray4 = release_list.toArray(new String[] {});
                    AiitemsCountArray4 = count_list.toArray(new Integer[] {});
                    AitemsImageArray4 = image_list.toArray(new String[] {});

                    listview_movie_song.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray4, AitemsImageArray4));

                    home_movie_song_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("moviesong", "album", title_movie_song);

                                }
                            });

                    listview_movie_song
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray4[position];
                                        selectedCatname = AitemsCatNameArray4[position];
                                        selectedArtistName = AitemsArtistNameArray4[position];
                                        selectedAlbumName =AitemsAlbumNameArray4[position];
                                        selectedPublisher = AitemsCpArray4[position];
                                        selectedRelease = AitemsReleaseArray4[position];
                                        selectedCount = AiitemsCountArray4[position];
                                        selectedImage = AitemsImageArray4[position];

                                        goToAlbumSingle();

                                        MyApplication.getInstance().trackEvent("Movie Song", "Album",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);

                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 6. Folk

    Handler folk_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_folk != null){

                    albumList5 = HTTPGateway.getDynamicAlbumList(subc_home_folk);
                    if(albumList5.size() == 0){
                        return;
                    } else {

                        title_folk = subc_home_folk.getString("catname");
                        home_folk_title.setVisibility(View.VISIBLE);
                        home_folk_title.setText(title_folk);
                        home_folk_see_all.setVisibility(View.VISIBLE);
                        listview_folk.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList5.size(); i++) {

                        Album tmpData = albumList5.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }


                    AitemsCodeArray5 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray5 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray5 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray5 = albumname_list.toArray(new String[] {});
                    AitemsCpArray5 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray5 = release_list.toArray(new String[] {});
                    AiitemsCountArray5 = count_list.toArray(new Integer[] {});
                    AitemsImageArray5 = image_list.toArray(new String[] {});

                    listview_folk.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray5, AitemsImageArray5));

                    home_folk_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("folk", "album", title_folk);

                                }
                            });

                    listview_folk
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray5[position];
                                        selectedCatname = AitemsCatNameArray5[position];
                                        selectedArtistName = AitemsArtistNameArray5[position];
                                        selectedAlbumName =AitemsAlbumNameArray5[position];
                                        selectedPublisher = AitemsCpArray5[position];
                                        selectedRelease = AitemsReleaseArray5[position];
                                        selectedCount = AiitemsCountArray5[position];
                                        selectedImage = AitemsImageArray5[position];

                                        goToAlbumSingle();

                                        MyApplication.getInstance().trackEvent("Folk", "Album",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);


                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });
                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 7. Classical

    Handler classical_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_classical != null){

                    albumList6 = HTTPGateway.getDynamicAlbumList(subc_home_classical);
                    if(albumList6.size() == 0){
                        return;
                    } else {

                        title_classical = subc_home_classical.getString("catname");
                        home_classical_title.setVisibility(View.VISIBLE);
                        home_classical_title.setText(title_classical);
                        home_classical_see_all.setVisibility(View.VISIBLE);
                        listview_classical.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList6.size(); i++) {

                        Album tmpData = albumList6.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }


                    AitemsCodeArray6 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray6 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray6 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray6 = albumname_list.toArray(new String[] {});
                    AitemsCpArray6 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray6 = release_list.toArray(new String[] {});
                    AiitemsCountArray6 = count_list.toArray(new Integer[] {});
                    AitemsImageArray6 = image_list.toArray(new String[] {});

                    listview_classical.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray6, AitemsImageArray6));

                    home_classical_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("classical", "album", title_classical);

                                }
                            });

                    listview_classical
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray6[position];
                                        selectedCatname = AitemsCatNameArray6[position];
                                        selectedArtistName = AitemsArtistNameArray6[position];
                                        selectedAlbumName =AitemsAlbumNameArray6[position];
                                        selectedPublisher = AitemsCpArray6[position];
                                        selectedRelease = AitemsReleaseArray6[position];
                                        selectedCount = AiitemsCountArray6[position];
                                        selectedImage = AitemsImageArray6[position];

                                        goToAlbumSingle();

                                        MyApplication.getInstance().trackEvent("Classical", "Album",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);


                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });

                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 8. All Time Hit

    Handler alltimehit_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_all_time_hit != null){

                    albumList7 = HTTPGateway.getDynamicAlbumList(subc_home_all_time_hit);
                    if(albumList7.size() == 0){
                        return;
                    } else {

                        title_all_time_hit = subc_home_all_time_hit.getString("catname");
                        home_all_time_hit_title.setVisibility(View.VISIBLE);
                        home_all_time_hit_title.setText(title_all_time_hit);
                        home_all_time_hit_see_all.setVisibility(View.VISIBLE);
                        listview_all_time_hit.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList7.size(); i++) {

                        Album tmpData = albumList7.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }


                    AitemsCodeArray7 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray7 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray7 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray7 = albumname_list.toArray(new String[] {});
                    AitemsCpArray7 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray7 = release_list.toArray(new String[] {});
                    AiitemsCountArray7 = count_list.toArray(new Integer[] {});
                    AitemsImageArray7 = image_list.toArray(new String[] {});

                    listview_all_time_hit.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray7, AitemsImageArray7));

                    home_all_time_hit_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("alltimehit", "album", title_all_time_hit);

                                }
                            });

                    listview_all_time_hit
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray7[position];
                                        selectedCatname = AitemsCatNameArray7[position];
                                        selectedArtistName = AitemsArtistNameArray7[position];
                                        selectedAlbumName =AitemsAlbumNameArray7[position];
                                        selectedPublisher = AitemsCpArray7[position];
                                        selectedRelease = AitemsReleaseArray7[position];
                                        selectedCount = AiitemsCountArray7[position];
                                        selectedImage = AitemsImageArray7[position];

                                        goToAlbumSingle();

                                        MyApplication.getInstance().trackEvent("All Time Hit", "Album",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);

                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });
                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
            }

        }

    };

    // 9. Band

    Handler band_handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if(subc_home_band != null){

                    albumList8 = HTTPGateway.getDynamicAlbumList(subc_home_band);
                    if(albumList8.size() == 0){
                        return;
                    } else {

                        title_band = subc_home_band.getString("catname");
                        home_band_title.setVisibility(View.VISIBLE);
                        home_band_title.setText(title_band);
                        home_band_see_all.setVisibility(View.VISIBLE);
                        listview_band.setVisibility(View.VISIBLE);
                    }

                    List<String> albumcode_list = new ArrayList<String>();
                    List<String> catname_list = new ArrayList<String>();
                    List<String> artistname_list = new ArrayList<String>();
                    List<String> albumname_list = new ArrayList<String>();
                    List<String> cp_list = new ArrayList<String>();
                    List<String> release_list = new ArrayList<String>();
                    List<Integer> count_list = new ArrayList<Integer>();
                    List<String> image_list = new ArrayList<String>();

                    for (int i = 0; i < albumList8.size(); i++) {

                        Album tmpData = albumList8.get(i);
                        albumcode_list.add(tmpData.getAlbumCode());
                        catname_list.add(tmpData.getCatname());
                        artistname_list.add(tmpData.getArtistname());
                        albumname_list.add(tmpData.getAlbumName());
                        cp_list.add(tmpData.getCp());
                        release_list.add(tmpData.getRelease());
                        count_list.add(tmpData.getCount());
                        image_list.add(tmpData.getAlbumImgUrl());
                    }


                    AitemsCodeArray8 = albumcode_list.toArray(new String[] {});
                    AitemsCatNameArray8 = catname_list.toArray(new String[] {});
                    AitemsArtistNameArray8 = artistname_list.toArray(new String[] {});
                    AitemsAlbumNameArray8 = albumname_list.toArray(new String[] {});
                    AitemsCpArray8 = cp_list.toArray(new String[] {});
                    AitemsReleaseArray8 = release_list.toArray(new String[] {});
                    AiitemsCountArray8 = count_list.toArray(new Integer[] {});
                    AitemsImageArray8 = image_list.toArray(new String[] {});

                    listview_band.setAdapter(new DynamicAlbumAdapter(getActivity(),
                            AitemsAlbumNameArray8, AitemsImageArray8));

                    home_band_see_all
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    goToAlbumSeeAllFragment("band", "album", title_band);

                                }
                            });

                    listview_band
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, final int position, long id) {

                                    try{

                                        selectedCode = AitemsCodeArray8[position];
                                        selectedCatname = AitemsCatNameArray8[position];
                                        selectedArtistName = AitemsArtistNameArray8[position];
                                        selectedAlbumName =AitemsAlbumNameArray8[position];
                                        selectedPublisher = AitemsCpArray8[position];
                                        selectedRelease = AitemsReleaseArray8[position];
                                        selectedCount = AiitemsCountArray8[position];
                                        selectedImage = AitemsImageArray8[position];

                                        goToAlbumSingle();

                                        MyApplication.getInstance().trackEvent("Band", "Album",
                                                selectedAlbumName + "/" + selectedArtistName + "/" + selectedCode);


                                    } catch (Exception ex){
                                        Log.d("TAG", "Error in Album OnItemClick");
                                    }

                                }
                            });


                }

            } catch (Exception e) {
                MyApplication.getInstance().trackException(e);
                Log.d("TAG", "Error in Home New Arrival");
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
            MyApplication.getInstance().trackException(ex);
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
                    MyApplication.getInstance().trackException(e);
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

                    Log.d("Login Error",
                            "Error while open VideoViewNew");

                }

                return;

            } else {

                goToSubBox();

            }

        } else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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

        final String[] option = new String[] {"Play now", "Go to Album", "Add to Playlist" };

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
                                GoToSongAlbum();
                                break;

                            case 2:
                                AddToPlaylist();
                                break;
                        }
                    }
                });

        // Then I do what I need with the builder
        // (except for setTitle();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pop_up_dialog, null);
        // set values for custom dialog components -
        // title, image and button
        View view_bg = (View) dialogView.findViewById(R.id.view_bg);
        TextView track = (TextView) dialogView.findViewById(R.id.trackname);
        track.setText(titleOfSelectedSong);
        TextView artist = (TextView) dialogView.findViewById(R.id.artist);
        artist.setText(artistOfSelectedSong);
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

        Glide.with(getActivity()).load(imageOfSelectedSong).into(image);

        builder.setCustomTitle(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void PlaySingleSong(){ // 1

        try {

            MainActivity.songProgressLayout.setVisibility(View.VISIBLE);

            HashMap<String, String> song = new HashMap<String, String>();
            song.put("songTitle", titleOfSelectedSong);

            if (artistOfSelectedSong != null) {
                song.put("songArtist", artistOfSelectedSong);
            } else {
                song.put("songArtist", "Unknown Artist");
            }

            if (albumOfSelectedSong != null) {
                song.put("songAlbum", albumOfSelectedSong);
            } else {
                song.put("songAlbum", "Unknown Album");
            }

            song.put("songImage", imageOfSelectedSong);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                song.put("songPath", urlOfSelectedSong);
            } else {
                song.put("songPath", urlWowzaOfSelectedSong);
            }

            song.put("songCode", idOfSelectedSong);
            song.put("songAlbumCode", albumCodeOfSelectedSong);

            if (lengthOfSelectedSong != null) {
                song.put("songLength", lengthOfSelectedSong);
            } else {
                song.put("songLength", "0.00");
            }

            if (!SongsManager._songsList.contains(song)) {

                SongsManager._songsList.add(song);

                int indexvalue = SongsManager._songsList.size() - 1;

                try {

                    playerService = new Intent(getActivity(), PlayerService.class);
                    playerService.putExtra("songIndex", indexvalue);
                    getActivity().startService(playerService);

                } catch (Exception e) {
                    Log.d("TAG", "Error in player service");
                }

            } else {

                int index = SongsManager._songsList.indexOf(song);

                try {

                    playerService = new Intent(getActivity(), PlayerService.class);
                    playerService.putExtra("songIndex", index);
                    getActivity().startService(playerService);

                } catch (Exception e) {
                    Log.d("TAG", "Error in player service");
                }
            }

        } catch (Exception ex){
            Log.d("Erroe while goto play", ex.toString());
        }
    }

    public void AddToPlaylist(){ //2

        try {

            HashMap<String, String> queue = new HashMap<String, String>();
            queue.put("songTitle", titleOfSelectedSong);

            if (artistOfSelectedSong != null) {
                queue.put("songArtist", artistOfSelectedSong);
            } else {
                queue.put("songArtist", "Unknown Artist");
            }

            if (albumOfSelectedSong != null) {
                queue.put("songAlbum", albumOfSelectedSong);
            } else {
                queue.put("songAlbum", "Unknown Album");
            }

            queue.put("songImage", imageOfSelectedSong);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                queue.put("songPath", urlOfSelectedSong);
            } else {
                queue.put("songPath", urlWowzaOfSelectedSong);
            }

            queue.put("songCode", idOfSelectedSong);
            queue.put("songAlbumCode", albumCodeOfSelectedSong);

            if (lengthOfSelectedSong != null) {
                queue.put("songLength", lengthOfSelectedSong);
            } else {
                queue.put("songLength", "0.00");
            }

            if (!SongsManager._songsList.contains(queue)) {

                SongsManager._songsList.add(queue);
            }

        } catch (Exception ex){
            Log.d("Error AddToPlaylist", ex.toString());
        }

    }

    public void GoToSongAlbum(){ //3

        try {

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
            Bundle args = new Bundle();
            args.putString("albumCode", albumCodeOfSelectedSong);
            args.putString("albumName", albumOfSelectedSong);
            args.putString("albumArtist", artistOfSelectedSong);
            args.putString("albumGenre", "");
            args.putString("albumPublisher", cpOfSelectedSong);
            args.putString("albumYear", "");
            args.putInt("albumCount", 0);
            args.putString("albumImage", imageOfSelectedSong);
            albumDetailsFragment.setArguments(args);
            ft.replace(R.id.home_container, albumDetailsFragment, AlbumDetailsFragment.FRAGMENT_TAG);
            ft.addToBackStack(null);
            ft.commit();

        } catch (Exception ex){
            MyApplication.getInstance().trackException(ex);
            Log.d("Error GoToSongAlbum", ex.toString());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    public void goToContentSeeAllFragment(String catTitle, String catCode, int tc){

        try {

            MyApplication.getInstance().trackEvent(catTitle, "View All", catCode);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ContentAllSongsFragment contentAllSongsFragment = new ContentAllSongsFragment();
            Bundle args = new Bundle();
            args.putString("catTitle", catTitle);
            args.putString("catCode", catCode);
            args.putInt("tc", tc);
            contentAllSongsFragment.setArguments(args);
            //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            ft.replace(R.id.home_container, contentAllSongsFragment);
            ft.addToBackStack(null);
            ft.commit();

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured TopSlide Banner Image Click");

        }

    }


    public void goToAlbumSeeAllFragment(String albumCt, String albumType, String albumCategory){

        try {

            MyApplication.getInstance().trackEvent(albumCt, "View All", albumCategory);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            AlbumFragment albumFragment = new AlbumFragment();
            Bundle args = new Bundle();
            args.putString("albumCt", albumCt);
            args.putString("albumType", albumType);
            args.putString("albumCategory", albumCategory);
            albumFragment.setArguments(args);
            ft.replace(R.id.home_container, albumFragment, AlbumFragmentOld.FRAGMENT_TAG);
            ft.addToBackStack(null);
            ft.commit();

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured TopSlide Banner Image Click");

        }

    }

    public void goToAlbumSingle(){

        try {

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

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured TopSlide Banner Image Click");

        }

    }

    public void initViews(View view){

        splashbar = (ProgressBar) view.findViewById(R.id.splashHomeProgress);
        subLayout = (RelativeLayout) view.findViewById(R.id.Subscription_Layout);
        tickerTextView = (TextView) view.findViewById(R.id.tv_subscriptionText);
        tickerTextView.setSelected(true);
        promo = (ImageView) view.findViewById(R.id.promoBanner);

        listview_recently_played = (TwoWayView) view.findViewById(R.id.recently_played_listview);
        listview_new_release = (TwoWayView) view.findViewById(R.id.newrelease_listview);
        listview_albums = (TwoWayView) view.findViewById(R.id.albums_listview);
        listview_playlist = (TwoWayView) view.findViewById(R.id.playlist_listview);
        listview_pop_modern = (TwoWayView) view.findViewById(R.id.popmodern_listview);
        listview_movie_song = (TwoWayView) view.findViewById(R.id.moviesong_listview);
        listview_folk = (TwoWayView) view.findViewById(R.id.folk_listview);
        listview_classical = (TwoWayView) view.findViewById(R.id.classical_listview);
        listview_all_time_hit = (TwoWayView) view.findViewById(R.id.alltimehit_listview);
        listview_band = (TwoWayView) view.findViewById(R.id.band_listview);

        home_recently_played_see_all = (TextView) view.findViewById(R.id.home_recently_played_see_all);
        home_new_release_see_all = (TextView) view.findViewById(R.id.home_new_release_see_all);
        home_albums_see_all = (TextView) view.findViewById(R.id.home_albums_see_all);
        home_playlist_see_all = (TextView) view.findViewById(R.id.home_playlist_see_all);
        home_pop_modern_see_all = (TextView) view.findViewById(R.id.home_popmodern_see_all);
        home_movie_song_see_all = (TextView) view.findViewById(R.id.home_moviesong_see_all);
        home_folk_see_all = (TextView) view.findViewById(R.id.home_folk_see_all);
        home_classical_see_all = (TextView) view.findViewById(R.id.home_classical_see_all);
        home_all_time_hit_see_all = (TextView) view.findViewById(R.id.home_alltimehit_see_all);
        home_band_see_all = (TextView) view.findViewById(R.id.home_band_see_all);

        // Category Titles
        home_recently_played_title = (TextView) view.findViewById(R.id.title_recently_played);
        home_new_release_title = (TextView) view.findViewById(R.id.title_newrelease);
        home_albums_title = (TextView) view.findViewById(R.id.title_albums);
        home_playlist_title = (TextView) view.findViewById(R.id.title_playlist);
        home_pop_modern_title = (TextView) view.findViewById(R.id.title_popmodern);
        home_movie_song_title = (TextView) view.findViewById(R.id.title_moviesong);
        home_folk_title = (TextView) view.findViewById(R.id.title_folk);
        home_classical_title = (TextView) view.findViewById(R.id.title_classical);
        home_all_time_hit_title = (TextView) view.findViewById(R.id.title_alltimehit);
        home_band_title = (TextView) view.findViewById(R.id.title_band);

    }

}