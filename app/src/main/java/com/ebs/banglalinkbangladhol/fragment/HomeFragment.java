package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.adapter.RecyclerViewDataAdapter;
import com.ebs.banglalinkbangladhol.bean.Product;
import com.ebs.banglalinkbangladhol.bean.Promo;
import com.ebs.banglalinkbangladhol.bean.Song;
import com.ebs.banglalinkbangladhol.model.RecentData;
import com.ebs.banglalinkbangladhol.model.SectionDataModel;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.MyApplication;
import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.bean.Album;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;
import com.ebs.banglalinkbangladhol.others.SongsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

	public HomeFragment() {

	}

	private ProgressBar splashbar;
	private SongsManager songManager;
	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	/* New Layout implementation*/
	RecyclerView recycler_view;
	RecyclerViewDataAdapter adapter;
	public static ArrayList<SectionDataModel> allDataSet = new ArrayList<SectionDataModel>();
	ArrayList<HashMap<String, String>> menuItems;

	FragmentActivity activity;
	private JSONArray response;
	private List<Promo> jsonPromoArray;
	private List<Song> jsonSongArray;
	private List<Album> jsonAlbumArray;
	private List<Product> jsonSliderArray;
	public static boolean cdback = false;

	private NativeExpressAdView adView;
	private AdRequest request;

	public static final String FRAGMENT_TAG = "HomeFragment";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity  = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home_new, container, false);

		getActivity().setTitle("Bangla Dhol");

		initViews(view);

		if (allDataSet.size() == 0) {

			Invoke();

		} else {

			adapter = new RecyclerViewDataAdapter(activity, allDataSet);
			recycler_view.setAdapter(adapter);
		}

		songManager = new SongsManager();
		songsList = songManager.getPlayList();

		adView = (NativeExpressAdView) view.findViewById(R.id.adView);
		request = new AdRequest.Builder().build();
		request.isTestDevice(getActivity());

		try {

			if(CheckUserInfo.getShowAdHomeStatus() == 1){
				adView.loadAd(request);
			}

		} catch (Exception ex) {

			Log.d("AD ERROR HOME", "getShowAdHomeStatus");
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

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (cdback == true) {

			cdback = false;
			allDataSet = new ArrayList<SectionDataModel>();

			Invoke();
		}

		MyApplication.getInstance().trackScreenView("Home Fragment");
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

					response = ServerUtilities.requestForHomeContent(CheckUserInfo.getUserMsisdn());

				} catch (Exception ex) {

					Log.d("TAG", "Error in requestForAlbumSongs");
				}

			}

			home_handler.sendEmptyMessage(0);

		}
	}

	Handler home_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			splashbar.setVisibility(View.GONE);

			try {

				if(response != null){

					for (int i = 0; i < response.length(); i++) {

						JSONObject productObj = response.getJSONObject(i);

						SectionDataModel dm = new SectionDataModel();
						dm.setCatCode(productObj.getString("catcode"));
						dm.setHeaderTitle(productObj.getString("catname"));
						dm.setContentType(productObj.getInt("contenttype"));
						dm.setContentViewType(productObj.getInt("contentviewtype"));

						if(productObj.getInt("contenttype") == 1){

							JSONArray m_arr = productObj.getJSONArray("contents");

							jsonSongArray = HTTPGateway.getDynamicSongJSONArray(m_arr);

							menuItems = new ArrayList<HashMap<String, String>>();

							for (int j = 0; j < jsonSongArray.size(); j++) {

								HashMap<String, String> map = new HashMap<String, String>();
								Song tmpData = jsonSongArray.get(j);
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

							if(i == 2){

								RecentData.recentList = menuItems;

							}

						} else if(productObj.getInt("contenttype") == 2 || productObj.getInt("contenttype") == 3){

							JSONArray m_arr = productObj.getJSONArray("contents");

							jsonAlbumArray = HTTPGateway.getDynamicAlbumJSONArray(m_arr);

							menuItems = new ArrayList<HashMap<String, String>>();

							for (int k = 0; k < jsonAlbumArray.size(); k++) {

								HashMap<String, String> map = new HashMap<String, String>();
								Album tmpData = jsonAlbumArray.get(k);
								map.put("albumcode_list", tmpData.getAlbumCode());
								map.put("catname_list", tmpData.getCatname());
								map.put("artistname_list", tmpData.getArtistname());
								map.put("albumname_list", tmpData.getAlbumName());
								map.put("cp_list", tmpData.getCp());
								map.put("release_list", tmpData.getRelease());
								map.put("count_list", String.valueOf(tmpData.getCount()));
								map.put("image_list", tmpData.getAlbumImgUrl());
								menuItems.add(map);
							}

						} else if(productObj.getInt("contenttype") == 4){

							JSONArray m_arr = productObj.getJSONArray("contents");

							jsonSliderArray = HTTPGateway.getDynamicSliderJSONArray(m_arr);

							menuItems = new ArrayList<HashMap<String, String>>();

							for (int k = 0; k < jsonSliderArray.size(); k++) {

								HashMap<String, String> map = new HashMap<String, String>();
								Product tmpData = jsonSliderArray.get(k);
								map.put("albumcode_list", tmpData.getAlbumcode());
								map.put("catname_list", tmpData.getCatcode());
								map.put("artistname_list", tmpData.getArtist());
								map.put("name_list", tmpData.getName());
								map.put("count_list", String.valueOf(tmpData.getSongs()));
								map.put("image_list", tmpData.getImg_url());
								menuItems.add(map);
							}

						} else if(productObj.getInt("contenttype") == 5){

							JSONArray m_arr = productObj.getJSONArray("contents");

							jsonPromoArray = HTTPGateway.getDynamicPromoJSONArray(m_arr);

							menuItems = new ArrayList<HashMap<String, String>>();

							for (int k = 0; k < jsonPromoArray.size(); k++) {

								HashMap<String, String> map = new HashMap<String, String>();
								Promo tmpData = jsonPromoArray.get(k);
								map.put("show_list", String.valueOf(tmpData.getShow()));
								map.put("url_list", tmpData.getPromourl());
								map.put("text_list", tmpData.getPromotexturl());
								menuItems.add(map);
							}
						}

						dm.setSectionData(menuItems);
						allDataSet.add(dm);

					}

					/**
					 * Footer view will be added here
					 * */

					ArrayList<HashMap<String, String>> blank = new ArrayList<HashMap<String, String>>();

					SectionDataModel dm = new SectionDataModel();
					dm.setCatCode("");
					dm.setHeaderTitle("");
					dm.setContentType(0);
					dm.setContentViewType(6);
					dm.setSectionData(blank);

					allDataSet.add(dm);

					adapter = new RecyclerViewDataAdapter(activity, allDataSet);
					recycler_view.setAdapter(adapter);
				}

			} catch (Exception ex) {
				Log.d("TAG", "Error in home_handler");
			}

		}

	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	public void initViews(View view){

		splashbar = (ProgressBar) view.findViewById(R.id.splashHomeProgress);
		recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view_main);
		recycler_view.setNestedScrollingEnabled(false);
		recycler_view.setHasFixedSize(true);
		recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

	}

}