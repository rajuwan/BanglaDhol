package com.ebs.banglalinkbangladhol.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.ebs.banglalinkbangladhol.util.FileUtils;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<String, String, String> {

	private HttpURLConnection httpConnection;

	InputStream inputStream = null;
	BufferedReader reader = null;

	@Override
	protected String doInBackground(String... uri) {

		BufferedReader reader = null;
		StringBuffer output = new StringBuffer("");
		InputStream stream = null;

		String result = "";
		try {

			openHttpUrlConnection(uri[0]);

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				stream = httpConnection.getInputStream();

				reader = new BufferedReader(new InputStreamReader(stream,
						"UTF-8"), 8);
				String line = "";
				while ((line = reader.readLine()) != null)
					output.append(line + "\n");
				result = output.toString();
				return result;
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

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		// Do anything with response..
	}

}
