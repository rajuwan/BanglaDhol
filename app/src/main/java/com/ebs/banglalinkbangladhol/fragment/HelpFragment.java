package com.ebs.banglalinkbangladhol.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

public class HelpFragment extends Fragment {

	public HelpFragment() {}

	private TextView tv_help;
	private String response;
	ProgressBar splashbar;

	public static final String FRAGMENT_TAG = "HelpFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_appinfo, container, false);

		//getActivity().setTitle("Help");

		tv_help = (TextView) rootView.findViewById(R.id.tv_help);
		splashbar = (ProgressBar) rootView.findViewById(R.id.helpProgress);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Invoke();
	}

	public void Invoke(){

		try {

			splashbar.setVisibility(View.VISIBLE);

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error in request thread");
		}

	}


	public class RequestThread extends Thread {
		@Override
		public void run() {

			synchronized (this) {

				try {

					response = ServerUtilities.checkAppInfo(getActivity(), "help", CheckUserInfo.getUserMsisdn());

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

			if (response != null) {

				try {

					JSONObject respObject = new JSONObject(response);
					String ds = respObject.getString("details");
					tv_help.setText(Html.fromHtml(ds));
					// just adding a tag

				} catch (Exception e) {

				}

			} else {
				Toast.makeText(getActivity(), "No Internet Connection, Please Try Again Later.",
						Toast.LENGTH_SHORT).show();
			}
		}

	};

}
