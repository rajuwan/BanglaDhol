package com.ebs.banglalinkbangladhol.json;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;

public class GetJSONObject {

	public static String getJSONObject(String url) throws IOException,
			JSONException {
		JSONParser jsonParser = new JSONParser();
		String jsonObject = null;
		// Use HttpURLConnection
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			jsonObject = jsonParser.getJSONHttpURLConnection(url);
		} else {
			// use HttpClient
			// jsonObject = jsonParser.getJSONHttpClient(url);
		}
		return jsonObject;
	}

	public static JSONObject getJSONObject2(String url) throws IOException,
			JSONException {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		// Use HttpURLConnection
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			jsonObject = jsonParser.getJSONHttpURLConnection2(url);
		} else {
			// use HttpClient
			// jsonObject = jsonParser.getJSONHttpClient(url);
		}
		return jsonObject;
	}
}
