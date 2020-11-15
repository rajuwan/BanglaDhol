package com.ebs.banglalinkbangladhol.others;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.bean.Account_Subscription_DataModel;
import com.ebs.banglalinkbangladhol.bean.AlbumPlaylist;
import com.ebs.banglalinkbangladhol.bean.AlbumSingle;
import com.ebs.banglalinkbangladhol.bean.AlbumSong;
import com.ebs.banglalinkbangladhol.bean.LoginJson;
import com.ebs.banglalinkbangladhol.bean.MsisdnJson;
import com.ebs.banglalinkbangladhol.bean.Album;
import com.ebs.banglalinkbangladhol.bean.Product;
import com.ebs.banglalinkbangladhol.bean.Promo;
import com.ebs.banglalinkbangladhol.bean.SingleAudio;
import com.ebs.banglalinkbangladhol.bean.SinglePlaylist;
import com.ebs.banglalinkbangladhol.bean.Song;
import com.ebs.banglalinkbangladhol.bean.SubJson;
import com.ebs.banglalinkbangladhol.util.TagName;

public class HTTPGateway {

	public static final String BASE_URL_MSISDN = "http://banglalinkdhol.com/v2/";
	public static final String BASE_URL = "https://banglalinkdhol.com/v2/";

	public static final String HOME_URL = BASE_URL + "bangladhol_json_app_home.php?";

	public static final String BANNER_URL = BASE_URL + "bangladhol_banners_app.php?";
	public static final String CHECK_MSISDN = BASE_URL_MSISDN + "bangladhol_request_msisdn.php?src=android";
	public static final String SIGNUP = BASE_URL + "bangladhol_signup.php?";
	public static final String LOGIN_DETAILS = BASE_URL + "bangladhol_makemylogingettoken.php?";
	public static final String LOGOUT_REQ = BASE_URL + "bangladhol_makemylogout.php?";
	public static final String CAT_JSON_POST = BASE_URL + "bangladhol_json_app.php?";
	public static final String CAT_JSON_ALBUM = BASE_URL + "bangladhol_json_album.php?";
	public static final String ALBUM_DETAILS = BASE_URL + "bangladhol_json_album_single.php?";
	public static final String RECENTLY_PLAYED = BASE_URL + "bangladhol_recentplayed2.php?";

	public static final String SUBSCHEMES = BASE_URL + "bangladhol_subschemes.php?";
	public static final String INSTANT_SUB = BASE_URL + "bangladhol_sub_instant.php?";
	public static final String INSTANT_UNSUB = BASE_URL + "bangladhol_unsub_instant.php?";

	public static final String MY_ACCOUNT = BASE_URL + "bangladhol_myaccount.php?";
	public static final String APPINFO = BASE_URL + "bangladhol_appinfo.php?";
	public static final String SEARCH = BASE_URL + "bangladhol_search_app.php?";
	public static final String FCM_SERVER_URL = BASE_URL + "bangladhol_entergcmid_ios.php?";
	public static final String POST_FEEDBACK = BASE_URL + "bangladhol_postfeedback.php?";

	public static final String CHECK_SDP = BASE_URL + "bangladhol_sub_instant_sdp.php?";
	public static final String SUB_STATUS_CHECK = BASE_URL + "bangladhol_subscription_status_check.php?";

	//================================================================================//

	public static final String VIDEO_DETAILS = BASE_URL + "flixlist_json_app_single.php?";
	public static final String POST_RATING = BASE_URL + "flix_rating.php?";
	public static final String ADD_MY_LIST = BASE_URL + "flix_setmylist.php?";
	public static final String REMOVE_MY_LIST = BASE_URL + "flix_unsetmylist.php?";
	public static final String MY_LIST = BASE_URL + "flixlist_json_mylsit_app.php?";

	public static final String POST_PLAY_TIME = BASE_URL + "flix_postplaytime.php?";
	public static final String POST_COMMENT = BASE_URL + "flix_postcomment.php?";

	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public static ArrayList<SingleAudio> audioList = new ArrayList<SingleAudio>();
	private static ArrayList<SingleAudio> searchList = new ArrayList<SingleAudio>();
	private static ArrayList<SinglePlaylist> playlistList = new ArrayList<SinglePlaylist>();
	private static ArrayList<AlbumSingle> albumSingleList = new ArrayList<AlbumSingle>();
	private static ArrayList<AlbumPlaylist> playlistSongList = new ArrayList<AlbumPlaylist>();
	private static String pipe, sPipe, d_title, d_data;

	//New Type Data from here
	private static List<MsisdnJson> msisdnData = new ArrayList<MsisdnJson>();
	private static List<LoginJson> loginData = new ArrayList<LoginJson>();
	private static List<SubJson> subData = new ArrayList<SubJson>();

	private static final String TAG_CONTENTS_SUBSCRIPTION = "subscriptionlog";
	private static JSONArray contents_subscription = null;
	private static List<Account_Subscription_DataModel> acc_subscriptionData = new ArrayList<Account_Subscription_DataModel>();

	private static List<Song> jsonData = new ArrayList<Song>();
	private static List<Album> albumList = new ArrayList<Album>();
	private static List<AlbumSingle> albumSongsList = new ArrayList<AlbumSingle>();
	private static List<AlbumSong> albumContents = new ArrayList<AlbumSong>();
	private static List<Product> products = new ArrayList<Product>();
	private static List<Promo> promoList = new ArrayList<Promo>();


	public static String getHttpResponseString(String staticUrl) {

		String url = staticUrl;
		String response = "";
		try {

			response = new RequestTask().execute(url).get();

		} catch (Exception ex) {
			return null;
		}

		return response;
	}



	@SuppressLint("NewApi")
	public static List<Promo> getDynamicPromoJSONArray(JSONArray jsonArray) {

		promoList.clear();

		try {

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject m_jObj = jsonArray.getJSONObject(i);
				promoList.add(new Promo(m_jObj.getInt("showinapp"),m_jObj.getString("promourl"),
						m_jObj.getString("promotexturl")));
			}

		} catch (Exception e) {
			return null;
		}

		return promoList;
	}

	@SuppressLint("NewApi")
	public static List<Song> getDynamicSongJSONArray(JSONArray jsonArray) {

		jsonData.clear();

		try {

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject m_jObj = jsonArray.getJSONObject(i);
				jsonData.add(new Song(m_jObj.getString("contentid"),m_jObj.getString("name"),
						m_jObj.getString("artistname"), m_jObj.getString("albumname"),
						m_jObj.getString("albumcode"), m_jObj.getString("image_location"),
						m_jObj.getString("url"), m_jObj.getString("urlwowza"),
						m_jObj.getString("duration"),m_jObj.getString("cp"),
						m_jObj.getString("genre"),m_jObj.getString("catcode")));
			}

		} catch (Exception e) {
			return null;
		}

		return jsonData;
	}

	@SuppressLint("NewApi")
	public static List<Album> getDynamicAlbumJSONArray(JSONArray jsonArray) {

		albumList.clear();

		try {

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject m_jObj = jsonArray.getJSONObject(i);
				albumList.add(new Album(m_jObj.getString("albumcode"),m_jObj.getString("catname"),
						m_jObj.getString("artistname"), m_jObj.getString("albumname"),
						m_jObj.getString("cp"), m_jObj.getString("release"),
						m_jObj.getInt("songs"), m_jObj.getString("image_location")));
			}

		} catch (Exception e) {
			return null;
		}

		return albumList;
	}

	@SuppressLint("NewApi")
	public static List<Product> getDynamicSliderJSONArray(JSONArray jsonArray) {

		products.clear();

		try {

			Product product;
			for (int i = 0; i < jsonArray.length(); i++) {

				product = new Product();

				JSONObject productObj = jsonArray.getJSONObject(i);
				product.setAlbumcode(productObj.getString(TagName.KEY_CODE));
				product.setName(productObj.getString(TagName.KEY_NAME));
				product.setArtist(productObj.getString(TagName.KEY_ARTIST));
				//product.setLabelname(productObj.getString(TagName.KEY_PUBLISHER));
				product.setSongs(productObj.getInt(TagName.KEY_COUNT));
				product.setImg_url(productObj.getString(TagName.KEY_COVER_URL));
				product.setCatcode(productObj.getString(TagName.KEY_CAT_CODE));

				products.add(product);
			}

		} catch (Exception e) {
			return null;
		}

		return products;
	}

	@SuppressLint("NewApi")
	public static List<Song> getDynamicCatJSONPOST(JSONObject jsonObject) {

		jsonData.clear();

		try {

			JSONArray m_arr = jsonObject.getJSONArray("contents");

			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				jsonData.add(new Song(m_jObj.getString("contentid"),m_jObj.getString("name"),
						m_jObj.getString("artistname"), m_jObj.getString("albumname"),
						m_jObj.getString("albumcode"), m_jObj.getString("image_location"),
						m_jObj.getString("url"), m_jObj.getString("urlwowza"),
						m_jObj.getString("duration"),m_jObj.getString("cp"),
						m_jObj.getString("genre"),m_jObj.getString("catcode")));
			}

		} catch (Exception e) {
			return null;
		}

		return jsonData;
	}

	@SuppressLint("NewApi")
	public static List<AlbumSingle> getDynamicAlbumSongList(String json) {

		albumSongsList.clear();

		try {

			JSONArray m_arr = new JSONArray(json);
			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				albumSongsList.add(new AlbumSingle(m_jObj.getString("albumcat"),
						m_jObj.getString("albumname"), m_jObj.getString("artistname"),
						m_jObj.getString("labelname"), m_jObj.getString("contents")));
			}

		} catch (Exception e) {
			return null;
		}

		return albumSongsList;
	}

	public static List<AlbumSong> getDynamicAlbumContentList(String json) {

		albumContents.clear();

		try {

			JSONArray m_arr = new JSONArray(json);
			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				albumContents.add(new AlbumSong(m_jObj.getString("contentid"), m_jObj.getString("name"),
						m_jObj.getString("artistname"), m_jObj.getString("url"), m_jObj.getString("urlwowza"),
						m_jObj.getString("image_location"), m_jObj.getString("duration"),
						m_jObj.getString("genre"),m_jObj.getString("catname"),m_jObj.getString("albumcode")));
			}

		} catch (Exception e) {
			return null;
		}
		return albumContents;
	}

	public static List<MsisdnJson> getMsisdnCredential(String json) {

		msisdnData.clear();

		try {

			JSONArray m_arr = new JSONArray(json);
			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				msisdnData.add(new MsisdnJson(m_jObj.getString("msisdn"),
						m_jObj.getInt("network"), m_jObj.getString("infotext"),
						m_jObj.getString("ticker"), m_jObj.getInt("showad")));
			}

		} catch (Exception e) {
			return null;
		}

		return msisdnData;

	}

	public static List<SubJson> getSubCredential(String json) {

		subData.clear();

		try {

			JSONArray m_arr = new JSONArray(json);
			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				subData.add(new SubJson(m_jObj.getInt("userinfo"), m_jObj
						.getString("response"), m_jObj.getString("userdetail"),
						m_jObj.getInt("play")));
			}

		} catch (Exception e) {
			return null;
		}
		return subData;
	}

	public static List<SubJson> getSdpCredential(String json) {

		subData.clear();

		try {

			JSONArray m_arr = new JSONArray(json);
			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				subData.add(new SubJson(m_jObj.getInt("userinfo"), m_jObj.getString("response"),
						m_jObj.getString("userdetail"), m_jObj.getInt("play"),
						m_jObj.getString("sdpurl")));
			}

		} catch (Exception e) {
			return null;
		}

		return subData;

	}

	// My Account Subscription data json parsing

	public static List<Account_Subscription_DataModel> getDynamicAccount_Subscription_DataModel(
			String json) {

		acc_subscriptionData.clear();

		try {

			JSONObject jsonObj = new JSONObject(json);
			// Getting JSON Array node
			contents_subscription = jsonObj.getJSONArray(TAG_CONTENTS_SUBSCRIPTION);

			// looping through All Contents
			for (int i = 0; i < contents_subscription.length(); i++) {
				JSONObject m_jObj = contents_subscription.getJSONObject(i);

				acc_subscriptionData.add(new Account_Subscription_DataModel(
						m_jObj.getString("datetime"), m_jObj.getString("purpose"),
						m_jObj.getString("pack"), m_jObj.getString("amount")));

			}

		} catch (Exception e) {

			return null;

		}

		return acc_subscriptionData;
	}

	public static List<LoginJson> getLoginCredential(String json) {

		loginData.clear();

		try {

			JSONArray m_arr = new JSONArray(json);
			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				loginData.add(new LoginJson(m_jObj.getString("result"), m_jObj
						.getString("token"), m_jObj.getInt("play"), m_jObj
						.getString("packcode"), m_jObj.getString("packname"),
						m_jObj.getString("packtext"), m_jObj.getString("msisdn"),
						m_jObj.getInt("enforce"), m_jObj.getString("enforcetext"),
						m_jObj.getInt("currentversion"), m_jObj.getInt("concurrent"),
						m_jObj.getString("concurrenttext"), m_jObj.getInt("showad")));
			}

		} catch (Exception e) {
			return null;
		}

		return loginData;

	}

	public static ArrayList<SingleAudio> getDynamicAudioList(String audio) {

		try {

			audioList.clear();
			String[] audioParam = audio.split("\n");

			for (int i = 0; i < audioParam.length; i++) {

				String temp = audioParam[i].replace("\r", "");
				String[] p2 = temp.split(";");

				SingleAudio sa = new SingleAudio();
				sa.audioTitle = p2[0];
				sa.audioArtist = p2[1];
				sa.audioAlbum = p2[2];
				sa.audioImgUrl = p2[3];
				sa.audioUrl = p2[4];
				sa.audioLength = p2[5];
				audioList.add(sa);
			}

		} catch (Exception e) {

			Log.d("TAG", "Error Occured");

		}

		return audioList;
	}

	public static String getDynamicData(String data) {

		try {
			
			int urlIndex = data.lastIndexOf("|");
			if (urlIndex != -1) {
				d_data = data.substring(urlIndex + 1);
			}

		} catch (Exception e) {
			
			Log.d("TAG", "Error Occured");

		}

		return d_data;

	}
	
	public static String getDynamicTitle(String data) {

		try {
			
			int urlIndex = data.lastIndexOf("|");
			if (urlIndex != -1) {
				d_title = data.substring(0 , urlIndex);
			}

		} catch (Exception e) {
			
			Log.d("TAG", "Error Occured");

		}

		return d_title;

	}

	public static ArrayList<SingleAudio> getDynamicSearchList(String search) {

		try {

			searchList.clear();
			int urlIndex = search.indexOf("|");
			if (urlIndex != -1) {
				sPipe = search.substring(urlIndex + 1, search.length() - 1);
			}
			String[] audioParam = sPipe.split("\n");

			for (int i = 0; i < audioParam.length; i++) {

				String temp = audioParam[i].replace("\r", "");
				String[] p2 = temp.split(";");

				SingleAudio sa = new SingleAudio();
				sa.audioTitle = p2[0];
				sa.audioArtist = p2[1];
				sa.audioAlbum = p2[2];
				sa.audioImgUrl = p2[3];
				sa.audioUrl = p2[4];
				sa.audioLength = p2[5];
				searchList.add(sa);
			}

		} catch (Exception e) {

			Log.d("TAG", "Error Occured");

		}

		return searchList;
	}

	// get all playlist data
	public static ArrayList<AlbumPlaylist> getDynamicPlaylistSongList(
			String playlistSongs) {

		try {

			playlistSongList.clear();
			String[] audioParam = playlistSongs.split("\n");

			for (int i = 0; i < audioParam.length; i++) {

				String temp = audioParam[i].replace("\r", "");
				String[] p2 = temp.split(";");

				AlbumPlaylist ap = new AlbumPlaylist();
				ap.playlistSongTitle = p2[0];
				ap.playlistSongArtist = p2[1];
				ap.playlistSongUrl = p2[2];
				ap.playlistSongImage = p2[3];
				ap.playlistSongLength = p2[4];

				playlistSongList.add(ap);
			}

		} catch (Exception e) {

			Log.d("TAG", "Error Occured");

		}

		return playlistSongList;
	}


	@SuppressLint("NewApi")
	public static List<Album> getDynamicAlbumList(JSONObject jsonObject) {

		albumList.clear();

		try {

			JSONArray m_arr = jsonObject.getJSONArray("contents");

			for (int i = 0; i < m_arr.length(); i++) {

				JSONObject m_jObj = m_arr.getJSONObject(i);
				albumList.add(new Album(m_jObj.getString("albumcode"),m_jObj.getString("catname"),
						m_jObj.getString("artistname"), m_jObj.getString("albumname"),
						m_jObj.getString("cp"), m_jObj.getString("release"),
						m_jObj.getInt("songs"), m_jObj.getString("image_location")));
			}

		} catch (Exception e) {
			return null;
		}

		return albumList;
	}

	public static ArrayList<SinglePlaylist> getDynamicPlaylistsList(
			String playlist) {

		try {

			playlistList.clear();
			int urlIndex = playlist.indexOf("|");
			if (urlIndex != -1) {
				pipe = playlist.substring(urlIndex + 1, playlist.length() - 1);
			}
			String[] albumParam = pipe.split("\n");

			for (int i = 0; i < albumParam.length; i++) {

				String temp = albumParam[i].replace("\r", "");
				String[] p2 = temp.split(";");

				SinglePlaylist sp = new SinglePlaylist();
				sp.playlistCode = p2[0];
				sp.playlistName = p2[1];
				sp.playlistGenre = p2[2];
				sp.playlistPublisher = p2[3];
				sp.playlistSongCount = Integer.parseInt(p2[4].toString());
				sp.playlistImgUrl = p2[5];

				playlistList.add(sp);
			}

		} catch (Exception e) {

			Log.d("TAG", "Error Occured");

		}

		return playlistList;
	}

	public static ArrayList<HashMap<String, String>> getDynamicItemLists(
			String s) {

		try {

			String[] param = s.split("\n");

			for (int i = 0; i < param.length; i++) {
				String[] p = param[i].split(",");
				HashMap<String, String> song = new HashMap<String, String>();

				song.put(p[0], p[1]);

				songsList.add(song);
			}

		} catch (Exception e) {

			Log.d("TAG", "Error Occured");

		}

		return songsList;
	}

	public static String getHttpSearchString(String query) {

		String url = SEARCH + query;

		String response = "";

		try {

			// response = getHttpRetrievedString(url);
			response = new RequestTask().execute(url).get();

		} catch (Exception ex) {

			return null;
		}

		return response;

	}

	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return (name.endsWith(".3gp|mp4") || name.endsWith(".3GP|MP4"));
		}
	}

	/**
	 * Function to convert milliseconds time to Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	/**
	 * Function to get Progress percentage
	 * 
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public int getProgressPercentage(long currentDuration, long totalDuration) {
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * 
	 * @param progress
	 *            -
	 * @param totalDuration
	 *            returns current duration in milliseconds
	 * */
	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = totalDuration / 1000;
		currentDuration = (int) ((((double) progress) / 100) * totalDuration);

		// return current duration in milliseconds
		return currentDuration * 1000;
	}

	// Methods For connectivity checking
	public static boolean isConnected(Context context) {
		ConnectivityManager conmgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conmgr.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isConnectionCheck(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			// Log.v(TAG, "Internet Connection Not Present");
			return false;
		}
	}

}
