package com.ebs.banglalinkbangladhol.model;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.activity.MainActivity;
import com.ebs.banglalinkbangladhol.adapter.DialogAdapter;
import com.ebs.banglalinkbangladhol.adapter.RecyclerViewDataAdapter;
import com.ebs.banglalinkbangladhol.bean.AlbumSong;
import com.ebs.banglalinkbangladhol.bean.Playlist;
import com.ebs.banglalinkbangladhol.bean.PlaylistItem;
import com.ebs.banglalinkbangladhol.fragment.AlbumDetailsFragment;
import com.ebs.banglalinkbangladhol.fragment.AlbumFragment;
import com.ebs.banglalinkbangladhol.fragment.ContentAllSongsFragment;
import com.ebs.banglalinkbangladhol.fragment.HomeFragment;
import com.ebs.banglalinkbangladhol.fragment.MyPlaylistDetailsFragment;
import com.ebs.banglalinkbangladhol.fragment.PlaylistDetailsFragment;
import com.ebs.banglalinkbangladhol.fragment.PromoBannerFragment;
import com.ebs.banglalinkbangladhol.fragment.SubscriptionFragment;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.DBHelperNew;
import com.ebs.banglalinkbangladhol.others.MyApplication;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.ebs.banglalinkbangladhol.service.PlayerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajuwan on 10-Apr-17.
 */

public final class ActionClick {

    private static ProgressDialog pd;
    private static FragmentActivity mActivity;
    private static Intent playerService;
    private static HashMap<String, String> mItem;
    private static String fromSection;
    private static ArrayList<PlaylistItem> allPlaylist;

    private static int fk_ID = -1;

    private static String idOfSelectedSong, titleOfSelectedSong, artistOfSelectedSong,
            albumOfSelectedSong, albumCodeOfSelectedSong, imageOfSelectedSong,
            urlOfSelectedSong, urlWowzaOfSelectedSong, lengthOfSelectedSong, cpOfSelectedSong,
            genreOfSelectedSong, catcodeOfSelectedSong;

    private static String selectedCode, selectedCatname, selectedArtistName, selectedAlbumName,
            selectedPublisher, selectedRelease, selectedImage;

    public static void goToPromoFragment(FragmentActivity activity, String imgUrl, String textUrl){

        try {

            mActivity = activity;

            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            PromoBannerFragment promoBannerFragment = new PromoBannerFragment();
            Bundle args = new Bundle();
            args.putString("imgUrl", imgUrl);
            args.putString("textUrl", textUrl);
            promoBannerFragment.setArguments(args);
            ft.replace(R.id.home_container, promoBannerFragment, PromoBannerFragment.FRAGMENT_TAG);
            ft.addToBackStack(PromoBannerFragment.FRAGMENT_TAG);
            ft.commit();

            MyApplication.getInstance().trackEvent("", "Promo Click", "");

        } catch (Exception ex){
            MyApplication.getInstance().trackException(ex);
            Log.d("Error GoToSongAlbum", ex.toString());
        }

    }

    public static void goToContentSeeAllFragment(FragmentActivity activity, String catTitle,
                                                 String catCode, int tc){

        try {

            mActivity = activity;

            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ContentAllSongsFragment contentAllSongsFragment = new ContentAllSongsFragment();
            Bundle args = new Bundle();
            args.putString("catTitle", catTitle);
            args.putString("catCode", catCode);
            args.putInt("tc", tc);
            contentAllSongsFragment.setArguments(args);
            ft.replace(R.id.home_container, contentAllSongsFragment, ContentAllSongsFragment.FRAGMENT_TAG);
            ft.addToBackStack(ContentAllSongsFragment.FRAGMENT_TAG);
            ft.commit();

            MyApplication.getInstance().trackEvent(catTitle, "View All", catCode);

        } catch (Exception ex){
            MyApplication.getInstance().trackException(ex);
            Log.d("Error GoToSongAlbum", ex.toString());
        }

    }

    public static void goToAlbumSeeAllFragment(FragmentActivity activity, String albumCt,
                                               String albumType, String albumCategory){

        try {

            mActivity = activity;

            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            AlbumFragment albumFragment = new AlbumFragment();
            Bundle args = new Bundle();
            args.putString("albumCt", albumCt);
            args.putString("albumType", albumType);
            args.putString("albumCategory", albumCategory);
            albumFragment.setArguments(args);
            ft.replace(R.id.home_container, albumFragment, AlbumFragment.FRAGMENT_TAG);
            ft.addToBackStack(AlbumFragment.FRAGMENT_TAG);
            ft.commit();

            MyApplication.getInstance().trackEvent(albumCt, "View All", albumCategory);

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured TopSlide Banner Image Click");

        }

    }

    public static void goToAlbumSingle(FragmentActivity activity, HashMap<String, String> item){

        try {

            mActivity = activity;

            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
            Bundle args = new Bundle();
            args.putString("albumCode", item.get("albumcode_list"));
            args.putString("albumName", item.get("albumname_list"));
            args.putString("albumArtist", item.get("artistname_list"));
            args.putString("albumGenre", item.get("catname_list"));
            args.putString("albumPublisher", item.get("cp_list"));
            args.putString("albumYear", item.get("release_list"));
            args.putString("albumCount", item.get("count_list"));
            args.putString("albumImage", item.get("image_list"));
            albumDetailsFragment.setArguments(args);
            ft.replace(R.id.home_container, albumDetailsFragment, AlbumDetailsFragment.FRAGMENT_TAG);
            ft.addToBackStack(AlbumDetailsFragment.FRAGMENT_TAG);
            ft.commit();

            MyApplication.getInstance().trackEvent(item.get("albumname_list"), "Album Single",
                    item.get("artistname_list"));

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured TopSlide Banner Image Click");

        }

    }

    public static void goToPlaylistSingle(FragmentActivity activity, HashMap<String, String> item){

        try {

            mActivity = activity;

            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            PlaylistDetailsFragment playlistDetailsFragment = new PlaylistDetailsFragment();
            Bundle args = new Bundle();
            args.putString("albumCode", item.get("albumcode_list"));
            args.putString("albumName", item.get("albumname_list"));
            args.putString("albumArtist", item.get("artistname_list"));
            args.putString("albumGenre", item.get("catname_list"));
            args.putString("albumPublisher", item.get("cp_list"));
            args.putString("albumYear", item.get("release_list"));
            args.putString("albumCount", item.get("count_list"));
            args.putString("albumImage", item.get("image_list"));
            playlistDetailsFragment.setArguments(args);
            ft.replace(R.id.home_container, playlistDetailsFragment, PlaylistDetailsFragment.FRAGMENT_TAG);
            ft.addToBackStack(PlaylistDetailsFragment.FRAGMENT_TAG);
            ft.commit();

            MyApplication.getInstance().trackEvent(item.get("albumname_list"), "Playlist Single",
                    item.get("artistname_list"));

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured TopSlide Banner Image Click");

        }

    }

    public static void requestForPlay(FragmentActivity activity, HashMap<String, String> item, String from) {

        try {

            mItem = item;
            mActivity = activity;
            fromSection = from;

            idOfSelectedSong = item.get("contentid_list"); // 1
            titleOfSelectedSong = item.get("name_list"); // 2
            artistOfSelectedSong = item.get("artist_list"); // 3
            albumOfSelectedSong = item.get("albumname_list"); // 4
            albumCodeOfSelectedSong = item.get("albumcode_list"); // 5
            imageOfSelectedSong = item.get("image_list"); // 6
            urlOfSelectedSong = item.get("url_list"); // 7
            urlWowzaOfSelectedSong = item.get("urlwowza_list"); // 8
            lengthOfSelectedSong = item.get("duration_list"); // 9
            cpOfSelectedSong = item.get("cp_list"); // 10
            genreOfSelectedSong = item.get("genre_list"); // 11
            catcodeOfSelectedSong = item.get("catcode_list"); // 12

            ContentPlayCheck();

            MyApplication.getInstance().trackEvent(idOfSelectedSong, "Song Play", titleOfSelectedSong);

        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured requestForPlay");

        }

    }

    public static void requestForAddAllToPlaylist(FragmentActivity activity, String albumName,
                                                  String albumCode, List<AlbumSong> albumSong) {

        try {

            mActivity = activity;

            for (int i = 0; i < albumSong.size(); i++) {

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

                int found = 0;

                ArrayList<HashMap<String, String>> playList = SongsManager._songsList;

                for (int x = 0; x < playList.size(); x++) {

                    HashMap<String, String> item = new HashMap<String, String>();
                    item = playList.get(x);

                    if(item.get("songCode").equals(tmpDataAdd.getContentid())){
                        found = 1;
                    }
                }

                if(found == 0){

                    SongsManager._songsList.add(queue);

                }

            }

            Toast.makeText(mActivity, String.valueOf(albumSong.size() + " songs added to playlist"),
                    Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            MyApplication.getInstance().trackException(e);
            Log.d("TAG", "Error Occured Error AddAllToPlaylist");

        }

    }

    static void ContentPlayCheck(){

        try {

            pd = new ProgressDialog(mActivity, ProgressDialog.STYLE_SPINNER);
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

    static class RequestThreadCPC extends Thread {

        @Override
        public void run() {

            Looper.prepare();

            synchronized (this) {

                try {

                    CheckUserInfo.getUserMsisdnInfo(mActivity);

                    CheckUserInfo.getUserLoginInfo(mActivity, CheckUserInfo.getUserMsisdn(),
                            CheckUserInfo.getUserPinCode());

                } catch (Exception e) {
                    MyApplication.getInstance().trackException(e);
                    Log.d("TAG", "Error Occured RequestThreadPMC");

                }

            }

            cpc_handler.sendEmptyMessage(0);

        }
    }

    static Handler cpc_handler = new Handler() {
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

                    Toast.makeText(mActivity, "Can't process your request now", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {

                Log.d("Login Error", "Error while parsing login data");
            }

        }

    };

    static void goToSubBox() {

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        SubscriptionFragment subscriptionFragment = new SubscriptionFragment();
        ft.replace(R.id.home_container, subscriptionFragment, SubscriptionFragment.FRAGMENT_TAG);
        ft.addToBackStack(SubscriptionFragment.FRAGMENT_TAG);
        ft.commit();
    }

    static void ConcurrentDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
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

    static void AccessTypeOne(){

        if (CheckUserInfo.getMsisdnFromServer().contains("no")) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
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

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
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

    static void AccessTypeSeven(){


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

            Intent mainIntent = new Intent(mActivity, BanglaDholSignUpLogInActivity.class);
            mActivity.startActivity(mainIntent);
        }

        return;

    }

    static void PlaySong(){

        final String[] option;
        final Integer[] icons;

        if(fromSection.contains("single")){

            option = new String[] {"Play now", "Add to Queue", "Add to Playlist", "Go to Album", "Share"};
            icons = new Integer[] {R.drawable.ic_pn_playnow, R.drawable.ic_pn_addtoqueue,
                    R.drawable.ic_pn_addtoplaylist, R.drawable.ic_pn_album, R.drawable.ic_pn_share};

        } else if(fromSection.contains("myplaylist")){

            option = new String[] {"Play now", "Add to Queue", "Go to Album", "Share"};
            icons = new Integer[] {R.drawable.ic_pn_playnow, R.drawable.ic_pn_addtoqueue,
                    R.drawable.ic_pn_album, R.drawable.ic_pn_share};
        } else {

            option = new String[] {"Play now", "Add to Queue", "Add to Playlist", "Share"};
            icons = new Integer[] {R.drawable.ic_pn_playnow, R.drawable.ic_pn_addtoqueue,
                    R.drawable.ic_pn_addtoplaylist, R.drawable.ic_pn_share};

        }

        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_listview_dialog);

        TextView track = (TextView) dialog.findViewById(R.id.trackname);
        track.setText(titleOfSelectedSong);
        TextView artist = (TextView) dialog.findViewById(R.id.artist);
        artist.setText(artistOfSelectedSong);
        ImageView image = (ImageView) dialog.findViewById(R.id.thumb);

        Glide.with(mActivity).load(imageOfSelectedSong).into(image);
        ListView lv = (ListView) dialog.findViewById(R.id.custom_list);

        // Change MyActivity.this and myListOfItems to your own values
        DialogAdapter adapter = new DialogAdapter(mActivity ,option, icons);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{

                    if(option[position].contains("Play now")){

                        PlaySingleSong();

                    } else if(option[position].contains("Add to Queue")){

                        AddToQueue();

                    } else if(option[position].contains("Add to Playlist")){

                        AddToPlaylist();

                    } else if(option[position].contains("Go to Album")){

                        GoToSongAlbum();

                    } else if(option[position].contains("Share")){

                        GoToShare();
                    }

                    dialog.dismiss();

                } catch (Exception ex){

                    Log.d("onItemClick", ex.toString());

                }

                /*switch (position) {

                    case 0:
                        PlaySingleSong();
                        break;

                    case 1:
                        AddToQueue();
                        AddToPlaylist();
                        break;

                    case 2:

                        if(fromSection.contains("single")){
                            GoToSongAlbum();
                        } else {
                            GoToShare();
                        }

                        break;

                    case 3:
                        GoToShare();
                        break;

                    case 4:
                        GoToShare();
                        break;
                }*/

            }
        });

        dialog.show();
    }

    static void PlaySingleSong(){ // 0

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

            /*Check for duplicate song in playlist*/

            int found = 0;
            int index = 0;

            ArrayList<HashMap<String, String>> playList = SongsManager._songsList;

            for (int i = 0; i < playList.size(); i++) {

                HashMap<String, String> item = new HashMap<String, String>();
                item = playList.get(i);

                if(item.get("songCode").equals(idOfSelectedSong)){
                    found = 1;
                    index = i;
                }
            }

            if(found == 0){

                SongsManager._songsList.add(song);
                int indexvalue = SongsManager._songsList.size() - 1;

                try {

                    playerService = new Intent(mActivity, PlayerService.class);
                    playerService.putExtra("songIndex", indexvalue);
                    mActivity.startService(playerService);

                } catch (Exception e) {
                    Log.d("TAG", "Error in player service");
                }

            } else{

                //int index = SongsManager._songsList.indexOf(song);

                try {

                    playerService = new Intent(mActivity, PlayerService.class);
                    playerService.putExtra("songIndex", index);
                    mActivity.startService(playerService);

                } catch (Exception e) {
                    Log.d("TAG", "Error in player service");
                }

            }

            int flag = 0;
            RecentData recentData = new RecentData();
            ArrayList<HashMap<String, String>> menuItems = recentData.getRecentList();

            for (int i = 0; i < menuItems.size(); i++) {

                HashMap<String, String> item = new HashMap<String, String>();
                item = menuItems.get(i);

                if(item.get("contentid_list").equals(mItem.get("contentid_list"))){
                    flag = 1;
                }
            }

            if(flag == 0){

                menuItems.add(0, mItem);
                RecentData.recentList = menuItems;

            } else {

                menuItems.remove(mItem);
                menuItems.add(0, mItem);
                RecentData.recentList = menuItems;
            }

            SectionDataModel dm = new SectionDataModel();
            dm.setCatCode("recent");
            dm.setHeaderTitle("Recently Played");
            dm.setContentType(1);
            dm.setContentViewType(1);
            dm.setSectionData(menuItems);

            ArrayList<SectionDataModel> allDataSet = HomeFragment.allDataSet;
            allDataSet.set(2, dm);

            RecyclerViewDataAdapter recyclerViewDataAdapter = new RecyclerViewDataAdapter(mActivity, allDataSet);
            recyclerViewDataAdapter.notifyDataSetChanged();

        } catch (Exception ex){
            Log.d("Erroe while goto play", ex.toString());
        }
    }

    static void AddToQueue(){ // 1

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

            /*Check for content duplicate */

            int found = 0;

            ArrayList<HashMap<String, String>> playList = SongsManager._songsList;

            for (int i = 0; i < playList.size(); i++) {

                HashMap<String, String> item = new HashMap<String, String>();
                item = playList.get(i);

                if(item.get("songCode").equals(idOfSelectedSong)){
                    found = 1;
                }
            }

            if(found == 0){

                SongsManager._songsList.add(queue);
                Toast.makeText(mActivity, "Added to Queue", Toast.LENGTH_SHORT).show();

            } else{

                Toast.makeText(mActivity, "Already Added to Queue", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex){
            Log.d("Error AddToQueue", ex.toString());
        }

    }

    static void AddToPlaylist(){

        try {

            final DBHelperNew dbHelper;
            dbHelper = new DBHelperNew(mActivity);

            allPlaylist = new ArrayList<PlaylistItem>();
            allPlaylist.clear();
            allPlaylist = dbHelper.getAllPlaylist();

            if (allPlaylist.size() > 0) {

                LayoutInflater li = mActivity.getLayoutInflater();
                View playlistView = li.inflate(R.layout.custom_playlist_add, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setView(playlistView);

                // set dialog message
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if(fk_ID != -1){

                                            insertSongToContentDB(fk_ID);

                                        } else {

                                            Toast.makeText(mActivity, "Select a playlist or Create New",
                                                    Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                final AlertDialog alertDialog = alertDialogBuilder.create();

                final TextView tv_createPlayList = (TextView) playlistView.findViewById(R.id.tv_createPlaylist);

                tv_createPlayList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(alertDialog != null && alertDialog.isShowing()){
                            alertDialog.dismiss();
                        }

                        createNewPlaylist();
                    }
                });

                final ListView lv = (ListView) playlistView.findViewById(R.id.lv_playlistList);

                ArrayList<String> titleList = new ArrayList<String>();

                for (int i = 0; i < allPlaylist.size(); i++) {

                    PlaylistItem tmpData = allPlaylist.get(i);
                    titleList.add(tmpData.getPlaylistTitle());
                }


                ArrayAdapter<String> arrayAdapter;

                arrayAdapter = new ArrayAdapter<String>(mActivity,
                        android.R.layout.simple_list_item_single_choice, titleList);

                //PlayListAdapter adapter = new PlayListAdapter(mActivity, allPlaylist);
                lv.setAdapter(arrayAdapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        fk_ID = allPlaylist.get(position).getId();

                    }
                });

                alertDialog.show();


            } else {

                createNewPlaylist();
            }

        } catch (Exception e) {
            Log.d("Error AddToPlaylist",  e.toString());
        }

    }

    public static void createNewPlaylist(){

        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);

        final EditText edittext = new EditText(mActivity);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setTitle("Create Playlist");
        alert.setView(edittext);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                try {

                    String playlistName = "";
                    final DBHelperNew dbHelper;
                    dbHelper = new DBHelperNew(mActivity);

                    playlistName  = edittext.getText().toString();

                    if(playlistName.length() > 0){

                        long row_id = dbHelper.insertPlayList(playlistName);

                        if(row_id != -1){

                            int fk_id = (int) row_id ;

                            insertSongToContentDB(fk_id);
                        }

                    } else {

                        Toast.makeText(mActivity, "Can not create with empty name", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex){

                    Log.d("Error Insert", ex.toString());

                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });

        alert.show();
    }

    public static void insertSongToContentDB(int fk_id){


        final DBHelperNew dbHelper;
        dbHelper = new DBHelperNew(mActivity);

        Boolean isSongExists = dbHelper.searchSongExistsInPlaylist(fk_id, idOfSelectedSong);

        if(isSongExists ==true){

            Toast.makeText(mActivity,"Already Added To PlayList",Toast.LENGTH_SHORT).show();

        } else {

            Playlist playList = new Playlist(fk_id, titleOfSelectedSong, artistOfSelectedSong,
                    albumOfSelectedSong, imageOfSelectedSong, urlOfSelectedSong, urlWowzaOfSelectedSong,
                    idOfSelectedSong, albumCodeOfSelectedSong, lengthOfSelectedSong, cpOfSelectedSong,
                    genreOfSelectedSong, catcodeOfSelectedSong);

            long val = dbHelper.insertSongContent(playList);

            if(val != -1) {
                Toast.makeText(mActivity,"Added To Playlist",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity,"Already Added To PlayList",Toast.LENGTH_SHORT).show();
            }

        }

    }

    public static boolean deletePlaylist(int fk_id){


        final DBHelperNew dbHelper;
        dbHelper = new DBHelperNew(mActivity);

        Boolean value = dbHelper.deletePlaylist(fk_id);

        if(value == true){

            Toast.makeText(mActivity, "PlayList Deleted Successfully", Toast.LENGTH_SHORT).show();

            return true;

        } else {

            return false;
        }

    }

    static void GoToSongAlbum(){ //2

        try {

            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
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
            ft.addToBackStack(AlbumDetailsFragment.FRAGMENT_TAG);
            ft.commit();

        } catch (Exception ex){
            MyApplication.getInstance().trackException(ex);
            Log.d("Error GoToSongAlbum", ex.toString());
        }

    }

    static void GoToShare(){ // 3

        try {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.ebs.banglalinkbangladhol");
            mActivity.startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } catch (Exception ex){
            MyApplication.getInstance().trackException(ex);
            Log.d("Error GoToShare", ex.toString());
        }

    }

    public static void goToPlaylistById(FragmentActivity activity, int playlist_id, String playlist_title){

        try {

            mActivity = activity;

            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            MyPlaylistDetailsFragment playlistDetailsFragment = new MyPlaylistDetailsFragment();
            Bundle args = new Bundle();
            args.putInt("id_playlist", playlist_id);
            args.putString("title_playlist", playlist_title);
            playlistDetailsFragment.setArguments(args);
            ft.replace(R.id.home_container, playlistDetailsFragment, MyPlaylistDetailsFragment.FRAGMENT_TAG);
            ft.addToBackStack(MyPlaylistDetailsFragment.FRAGMENT_TAG);
            ft.commit();

            MyApplication.getInstance().trackEvent("", "My Playlist", "");

        } catch (Exception ex){
            MyApplication.getInstance().trackException(ex);
            Log.d("Error goToPlaylistById", ex.toString());
        }

    }

    /*static void PlaySong(){

        final String[] option;


        if(fromSection.contains("single")){

            option = new String[] {"Play now", "Add to Playlist", "Go to Album"};

        } else{

            option = new String[] {"Play now", "Add to Playlist"};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                mActivity, android.R.layout.select_dialog_item, option);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

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
                                AddToQueue();
                                break;

                            case 2:
                                GoToSongAlbum();
                                break;
                        }
                    }
                });

        // Then I do what I need with the builder
        // (except for setTitle();
        LayoutInflater inflater = mActivity.getLayoutInflater();
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

        Glide.with(mActivity).load(imageOfSelectedSong).into(image);

        builder.setCustomTitle(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }*/

}
