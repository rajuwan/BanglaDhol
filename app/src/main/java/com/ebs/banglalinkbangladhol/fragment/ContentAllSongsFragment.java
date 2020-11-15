package com.ebs.banglalinkbangladhol.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.adapter.DynamicViewAllLoadMoreAdapter;
import com.ebs.banglalinkbangladhol.bean.Song;
import com.ebs.banglalinkbangladhol.model.ActionClick;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.MyApplication;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.ebs.banglalinkbangladhol.service.PlayerService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentAllSongsFragment extends Fragment {

    public ContentAllSongsFragment() {
        // Required empty public constructor
    }

    private ProgressBar splashbar;
    private ProgressDialog pd;
    public JSONObject subc_see_all, load_more_seeall;
    private List<Song> jsonSongs, jsonSongsMore;

    public ListView seeAllListView;

    public String catCode, actionbarTitle;
    public int tc = 0;
    private Button btnLoadMore;
    int current_page = 1;

    ArrayList<HashMap<String, String>> menuItems;
    FragmentActivity activity;

    private NativeExpressAdView adView;
    private AdRequest request;

    public static final String FRAGMENT_TAG = "ContentAllSongsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_content_all_songs, container, false);

        seeAllListView = (ListView) view.findViewById(android.R.id.list);
        splashbar = (ProgressBar) view.findViewById(R.id.CASProgress);
        menuItems = new ArrayList<HashMap<String, String>>();
        btnLoadMore = new Button(getActivity());

        Bundle args = getArguments();

        if (args != null) {

            actionbarTitle = args.getString("catTitle");
            catCode = args.getString("catCode");
            tc = args.getInt("tc");
        }

        try {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            ViewGroup header = (ViewGroup) layoutInflater.inflate(R.layout.viewall_listview_header,
                    seeAllListView, false);

            TextView header_text = (TextView) header.findViewById(R.id.section_header);
            header_text.setText(actionbarTitle);

            adView = (NativeExpressAdView) header.findViewById(R.id.adView);
            request = new AdRequest.Builder().build();
            request.isTestDevice(getActivity());

            try {

                if(CheckUserInfo.getShowAdHomeStatus() == 1){
                    adView.loadAd(request);
                }

            } catch (Exception ex) {

                Log.d("AD ERROR SeeAll", "getShowAdHomeStatus");
            }

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

            seeAllListView.addHeaderView(header, null, false);

        } catch (Exception ex) {

            Log.d("TAG", "Error in Requested Thread");
        }

        if(savedInstanceState == null){
            Invoke();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void Invoke() {

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThread reqThread = new RequestThread();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error in Requested Thread");
        }
    }

    public class RequestThread extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {


                    subc_see_all = ServerUtilities.requestForCatJsonPost(CheckUserInfo.getUserMsisdn(),
                            catCode, tc, current_page);

                } catch (Exception ex) {

                    Log.d("TAG", "Error in data thread");
                }

            }

            content_see_all_handler.sendEmptyMessage(0);

        }
    }

    Handler content_see_all_handler = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            if (subc_see_all != null) {

                jsonSongs = HTTPGateway.getDynamicCatJSONPOST(subc_see_all);

                for (int i = 0; i < jsonSongs.size(); i++) {

                    HashMap<String, String> map = new HashMap<String, String>();
                    Song tmpData = jsonSongs.get(i);
                    map.put("contentid_list", tmpData.getContentid());
                    map.put("name_list", tmpData.getName());
                    map.put("artist_list", tmpData.getArtist());
                    map.put("albumname_list", tmpData.getAlbumname());
                    map.put("albumcode_list", tmpData.getAlbumcode());
                    map.put("image_list", tmpData.getImage());
                    map.put("url_list", tmpData.getUrl());
                    map.put("urlwowza_list", tmpData.getUrlWowza());
                    map.put("duration_list", tmpData.getDuration());
                    map.put("cp_list", tmpData.getCp());
                    map.put("genre_list", tmpData.getGenre());
                    map.put("catcode_list", tmpData.getCatcode());
                    menuItems.add(map);
                }

                // Creating a button - Load More

                if(jsonSongs.size() > 20){

                    btnLoadMore.setText("Load More");
                    btnLoadMore.setTextColor(Color.parseColor("#F2F2F2"));
                    btnLoadMore.setTypeface(null, Typeface.BOLD);
                    btnLoadMore.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.button_fragment_mylist));
                    // Adding button to listview at footer
                    seeAllListView.addFooterView(btnLoadMore);
                }

                seeAllListView.setAdapter(new DynamicViewAllLoadMoreAdapter(getActivity(),  menuItems));

                seeAllListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        HashMap<String, String> item = new HashMap<String, String>();
                        item = menuItems.get(position - 1);
                        ActionClick.requestForPlay(activity, item, "single");
                    }
                });

                btnLoadMore.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // Starting a new async task
                        // new loadMoreListView().execute();
                        InvokeMore();
                    }
                });

            } else {

                Log.d("TAG", "See All data not found");

            }
        }

    };

    public void InvokeMore() {

        try {

            splashbar.setVisibility(View.VISIBLE);

            RequestThreadMore reqThread = new RequestThreadMore();

            reqThread.start();

        } catch (Exception ex) {

            Log.d("TAG", "Error in Requested Thread");
        }
    }

    public class RequestThreadMore extends Thread {

        @Override
        public void run() {

            synchronized (this) {

                try {

                    current_page += 1;

                    load_more_seeall = ServerUtilities.requestForCatJsonPost(CheckUserInfo.getUserMsisdn(),
                            catCode, tc, current_page);

                } catch (Exception ex) {

                    Log.d("TAG", "Error in data thread");
                }

            }

            search_handler_more.sendEmptyMessage(0);

        }
    }

    Handler search_handler_more = new Handler() {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(android.os.Message msg) {

            splashbar.setVisibility(View.GONE);

            if (load_more_seeall != null) {

                try {

                    jsonSongsMore = HTTPGateway.getDynamicCatJSONPOST(load_more_seeall);

                    if(jsonSongsMore.size() == 0){
                        btnLoadMore.setText("No more item to load");
                        return;
                    }

                    for (int i = 0; i < jsonSongsMore.size(); i++) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Song tmpData = jsonSongsMore.get(i);
                        map.put("contentid_list", tmpData.getContentid());
                        map.put("name_list", tmpData.getName());
                        map.put("artist_list", tmpData.getArtist());
                        map.put("albumname_list", tmpData.getAlbumname());
                        map.put("albumcode_list", tmpData.getAlbumcode());
                        map.put("image_list", tmpData.getImage());
                        map.put("url_list", tmpData.getUrl());
                        map.put("urlwowza_list", tmpData.getUrlWowza());
                        map.put("duration_list", tmpData.getDuration());
                        map.put("cp_list", tmpData.getCp());
                        map.put("genre_list", tmpData.getGenre());
                        map.put("catcode_list", tmpData.getCatcode());
                        menuItems.add(map);
                    }

                    int currentPosition = seeAllListView.getFirstVisiblePosition();

                    seeAllListView.setAdapter(new DynamicViewAllLoadMoreAdapter(getActivity(), menuItems));
                    // Setting new scroll position
                    seeAllListView.setSelectionFromTop(currentPosition + 1, 0);

                } catch (Exception ex) {

                    btnLoadMore.setText("No more item to load");
                    Log.d("TAG", "Error while Searching ");
                    Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();

                }

            } else {

                btnLoadMore.setText("No more item to load");

            }

        }
    };

}
