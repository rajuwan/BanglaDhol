package com.ebs.banglalinkbangladhol.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.adapter.DynamicViewAllLoadMoreAdapter;
import com.ebs.banglalinkbangladhol.bean.AlbumSong;
import com.ebs.banglalinkbangladhol.bean.Playlist;
import com.ebs.banglalinkbangladhol.model.ActionClick;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.DBHelperNew;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.ebs.banglalinkbangladhol.service.PlayerService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaylistDetailsFragment extends Fragment {


    public MyPlaylistDetailsFragment() {
        // Required empty public constructor
    }

    private ProgressBar splashbar;
    private ProgressDialog pd;
    private int playlist_Code = 0;
    private String playlist_Title = "";
    private DBHelperNew dbHelper;
    public ListView playListView;
    FragmentActivity activity;
    TextView total_text;
    private Intent playerService;

    ArrayList<HashMap<String, String>> menuItems;

    private static ArrayList<Playlist> allPlaylistSongs = new ArrayList<Playlist>();

    public static final String FRAGMENT_TAG = "MyPlaylistDetailsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.album_details_fragment, container, false);

        splashbar = (ProgressBar) view.findViewById(R.id.ADFProgress);
        playListView = (ListView) view.findViewById(android.R.id.list);
        menuItems = new ArrayList<HashMap<String, String>>();

        dbHelper = new DBHelperNew(getActivity());

        try {

            Bundle args = getArguments();

            if (args != null) {

                playlist_Code = args.getInt("id_playlist"); // use
                playlist_Title = args.getString("title_playlist"); // use

            }

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.playlist_listview_header,
                    playListView, false);

            ImageView album_image = (ImageView) header.findViewById(R.id.backdrop);
            album_image.setImageResource(R.drawable.drawer_top_bg);
            TextView header_text = (TextView) header.findViewById(R.id.section_header);
            header_text.setSelected(true);
            header_text.setText(playlist_Title);
            total_text = (TextView) header.findViewById(R.id.section_songs_count);
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

            playListView.addHeaderView(header, null, false);

            if(playlist_Code > 0){

                Invoke();
            }

        } catch (Exception e) {

            Log.d("TAG", "Error in player service");

        }

        return view;
    }


    public void Invoke(){

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThread reqThread = new RequestThread();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error while retreving device data ");

        }

    }

    public class RequestThread extends Thread {
        @Override
        public void run() {

            synchronized (this) {

                try {

                    allPlaylistSongs = new ArrayList<Playlist>();
                    allPlaylistSongs.clear();
                    allPlaylistSongs = dbHelper.getAllPlaylistSongs(playlist_Code);

                    Log.d("TAG", "Error in request thread");

                } catch (Exception ex) {

                    Log.d("TAG", "Error in request thread");
                }

            }

            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            try {

                if (allPlaylistSongs.size() > 0) {

                    total_text.setText(allPlaylistSongs.size() + " songs");

                    for (int i = 0; i < allPlaylistSongs.size(); i++) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Playlist tmpData = allPlaylistSongs.get(i);
                        map.put("contentid_list", tmpData.getSongCode());
                        map.put("name_list", tmpData.getSongTitle());
                        map.put("artist_list", tmpData.getSongArtist());
                        map.put("albumname_list", tmpData.getSongAlbum());
                        map.put("albumcode_list", tmpData.getSongAlbumCode());
                        map.put("image_list", tmpData.getSongImage());
                        map.put("url_list", tmpData.getSongUrl());
                        map.put("duration_list", tmpData.getSongLength());
                        menuItems.add(map);
                    }

                    playListView.setAdapter(new DynamicViewAllLoadMoreAdapter(getActivity(),  menuItems));

                    playListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            HashMap<String, String> item = new HashMap<String, String>();
                            item = menuItems.get(position - 1);
                            ActionClick.requestForPlay(activity, item, "myplaylist");

                        }
                    });

                }

            } catch (Exception ex) {

                Log.d("TAG", "Error in request thread");
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

            Playlist tmpData = allPlaylistSongs.get(0);

            String title = tmpData.getSongTitle();
            if (title != null) {
                song.put("songTitle", title);
            } else {
                song.put("songTitle", "Unknown Title");
            }

            String artist = tmpData.getSongArtist();
            if (artist != null) {
                song.put("songArtist", artist);
            } else {
                song.put("songArtist", "Unknown Artist");
            }

            String albumName = tmpData.getSongAlbum();

            if (albumName != null) {
                song.put("songAlbum", albumName);
            } else {
                song.put("songAlbum", "Unknown Album");
            }

            song.put("songImage", tmpData.getSongImage());

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                song.put("songPath", tmpData.getSongUrl());
            } else {
                song.put("songPath", tmpData.getSongUrlWowza());
            }

            song.put("songCode", tmpData.getSongCode());
            song.put("songAlbumCode", tmpData.getSongAlbumCode());

            String length = tmpData.getSongLength();
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

                if(item.get("songCode").equals(tmpData.getSongCode())){
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

            for (int i = 1; i < allPlaylistSongs.size(); i++) {

                HashMap<String, String> queue = new HashMap<String, String>();
                Playlist tmpDataAdd = allPlaylistSongs.get(i);

                String titleAdd = tmpDataAdd.getSongTitle();
                if (titleAdd != null) {
                    queue.put("songTitle", titleAdd);
                } else {
                    queue.put("songTitle", "Unknown Title");
                }

                String artistAdd = tmpDataAdd.getSongArtist();
                if (artistAdd != null) {
                    queue.put("songArtist", artistAdd);
                } else {
                    queue.put("songArtist", "Unknown Artist");
                }

                String albumName2 = tmpDataAdd.getSongAlbum();

                if (albumName2 != null) {
                    queue.put("songAlbum", albumName2);
                } else {
                    queue.put("songAlbum", "Unknown Album");
                }

                queue.put("songImage", tmpDataAdd.getSongImage());

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    queue.put("songPath", tmpDataAdd.getSongUrl());
                } else {
                    queue.put("songPath", tmpDataAdd.getSongUrlWowza());
                }

                queue.put("songCode", tmpDataAdd.getSongCode());
                queue.put("songAlbumCode", tmpDataAdd.getSongAlbumCode());

                String lengthAdd = tmpDataAdd.getSongLength();
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

                    if(item.get("songCode").equals(tmpDataAdd.getSongCode())){
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

}
