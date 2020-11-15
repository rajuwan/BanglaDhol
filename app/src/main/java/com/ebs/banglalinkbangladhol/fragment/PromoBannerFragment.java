package com.ebs.banglalinkbangladhol.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.AsyncCoverTask;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;

public class PromoBannerFragment extends Fragment {

	public PromoBannerFragment() {
	}

	private TextView tv_promo;
	private String response;
	ProgressBar splashbar;

	private ImageView promo;
	Bitmap cover = null;
	String PROMO_URL, PROMO_TEXT;

	public static final String FRAGMENT_TAG = "TopBannerFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_promo_new, container, false);

		try {

			Bundle args = getArguments();

			if (args != null) {

				PROMO_URL = args.getString("imgUrl"); // use
				PROMO_TEXT = args.getString("textUrl"); // use
			}

		} catch (Exception ex) {

			Log.d("TAG", "Error in Requested Thread");
		}

		splashbar = (ProgressBar) rootView.findViewById(R.id.splashTBProgress);
		promo = (ImageView) rootView.findViewById(R.id.promoBanner);
		tv_promo = (TextView) rootView.findViewById(R.id.promo_text);

		if (savedInstanceState != null) {

		} else {

			Invoke();

		}

		return rootView;
	}

	public void Invoke() {

		try {

			splashbar.setVisibility(View.VISIBLE);

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThread extends Thread {
		@Override
		public void run() {

			Looper.prepare();

			synchronized (this) {

				try {

					cover = new AsyncCoverTask().execute(PROMO_URL).get();

					response = HTTPGateway.getHttpResponseString(PROMO_TEXT);

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

			if (cover == null) {

				promo.setVisibility(View.GONE);

			} else {

				promo.setVisibility(View.VISIBLE);
				promo.setImageBitmap(cover);
			}

			if (response != null) {

				tv_promo.setText(response);

			} else {
				
				Toast.makeText(getActivity(), "No Internet Connection, Please Try Again Later.",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

}
