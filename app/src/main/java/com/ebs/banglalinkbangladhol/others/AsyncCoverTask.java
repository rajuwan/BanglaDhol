package com.ebs.banglalinkbangladhol.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.ebs.banglalinkbangladhol.util.FileUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncCoverTask extends AsyncTask<String, Void, Bitmap> {

	private HttpURLConnection httpConnection;

	InputStream inputStream = null;
	BufferedReader reader = null;

	@Override
	protected Bitmap doInBackground(String... uri) {

		BufferedReader reader = null;
		InputStream stream = null;

		try {

			openHttpUrlConnection(uri[0]);

			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				stream = httpConnection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(stream);

				return bitmap;
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
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

	}

}
