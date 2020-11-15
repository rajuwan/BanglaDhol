package com.ebs.banglalinkbangladhol.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.json.JSONObject;
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

public class SearchFragment extends Fragment {

	public SearchFragment() {

	}

	private ProgressBar splashbar;

	public JSONObject subc_search, subc_search_more;
	private String search_string;

	private ListView searchListView;
	private Button btnLoadMore;
	private List<Song> jsonSearch, jsonSearchMore;
	int current_page = 1;

	ArrayList<HashMap<String, String>> menuItems;
	private Intent playerService;
	FragmentActivity activity;

	public static final String FRAGMENT_TAG = "SearchFragment";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity  = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_content_all_songs, container, false);

		menuItems = new ArrayList<HashMap<String, String>>();
		splashbar = (ProgressBar) view.findViewById(R.id.CASProgress);
		searchListView = (ListView) view.findViewById(android.R.id.list);
		btnLoadMore = new Button(getActivity());

		Bundle args = getArguments();

		if (args != null) {

			search_string = args.getString("Search").trim();

			MyApplication.getInstance().trackEvent("Search", "Search Song", "Tag : " + search_string);

		}

		//getActivity().setTitle("Search Result");

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

	/**
	 * To get Dynamic array List using -- Thread*
	 */
	public class RequestThread extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					if (search_string.length() >= 2) {

						subc_search = ServerUtilities.checkAppSearch(search_string,
								CheckUserInfo.getUserMsisdn(), current_page);

					} else {

						Toast.makeText(getActivity(), "Type at least 2 words",
								Toast.LENGTH_SHORT).show();

					}

				} catch (Exception ex) {

					Log.d("TAG", "Error in data thread");
				}

			}

			search_handler.sendEmptyMessage(0);

		}
	}

	Handler search_handler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(android.os.Message msg) {

			splashbar.setVisibility(View.GONE);

			if (subc_search != null) {

				try {

					jsonSearch = HTTPGateway.getDynamicCatJSONPOST(subc_search);

					if(jsonSearch.size() == 0){
						Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();
						return;
					}

					for (int i = 0; i < jsonSearch.size(); i++) {

						HashMap<String, String> map = new HashMap<String, String>();
						Song tmpData = jsonSearch.get(i);
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

					if(jsonSearch.size() > 20){

						btnLoadMore.setText("Load More");
						btnLoadMore.setTextColor(Color.parseColor("#F2F2F2"));
						btnLoadMore.setTypeface(null, Typeface.BOLD);
						btnLoadMore.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.button_fragment_mylist));
						// Adding button to listview at footer
						searchListView.addFooterView(btnLoadMore);
					}

					searchListView.setAdapter(new DynamicViewAllLoadMoreAdapter(getActivity(),  menuItems));

					btnLoadMore.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// Starting a new async task
							// new loadMoreListView().execute();
							InvokeMore();
						}
					});

					searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
												int position, long id) {

							HashMap<String, String> item = new HashMap<String, String>();
							item = menuItems.get(position);
							ActionClick.requestForPlay(activity, item, "single");

						}
					});


				} catch (Exception ex) {

					Log.d("TAG", "Error while Searching ");
					Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();

				}

			} else {

				Log.d("TAG", "Movie Action data not found");
				Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();

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

					subc_search_more = ServerUtilities.checkAppSearch(search_string,
							CheckUserInfo.getUserMsisdn(), current_page);

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

			if (subc_search_more != null) {

				try {

					jsonSearchMore = HTTPGateway.getDynamicCatJSONPOST(subc_search_more);

					if(jsonSearchMore.size() == 0){
						btnLoadMore.setText("No more item to load");
						return;
					}

					for (int i = 0; i < jsonSearchMore.size(); i++) {

						HashMap<String, String> map = new HashMap<String, String>();
						Song tmpData = jsonSearchMore.get(i);
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

					// get listview current position - used to maintain
					// scroll
					// position
					int currentPosition = searchListView.getFirstVisiblePosition();

					searchListView.setAdapter(new DynamicViewAllLoadMoreAdapter(getActivity(), menuItems));
					// Setting new scroll position
					searchListView.setSelectionFromTop(currentPosition + 1, 0);

				} catch (Exception ex) {

					Log.d("TAG", "Error while Searching ");
					Toast.makeText(getActivity(), "No Result Found", Toast.LENGTH_SHORT).show();

				}

			} else {

				btnLoadMore.setText("No more item to load");

			}

		}
	};

}