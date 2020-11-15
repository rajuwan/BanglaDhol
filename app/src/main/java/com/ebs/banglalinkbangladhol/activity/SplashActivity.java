package com.ebs.banglalinkbangladhol.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.ConnectionDetector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends Activity {

	private ProgressBar splashbar;
	public ConnectionDetector cd;

	private Dialog dialog;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);

		//printHashKey();

		splashbar = (ProgressBar) findViewById(R.id.splashProgress);
		cd = new ConnectionDetector(getApplicationContext());

		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present

			dialog = new Dialog(SplashActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.custom_dialog);

			TextView textHeader = (TextView) dialog
					.findViewById(R.id.tv_ni_text);
			textHeader.setText("No Connection");

			TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
			text.setText("Dear User, Please check your mobile data connection and try again.");

			Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
			dialogButtonCancel.setText("Cancel");
			dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					finish();
				}
			});

			Button dialogButtonRetry = (Button) dialog.findViewById(R.id.btn_dialog_retry);
			dialogButtonRetry.setText("Retry");
			dialogButtonRetry.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
					finish();
					startActivity(intent);
				}
			});

			if (!isFinishing()) {

				dialog.show();

			}

			return;

		}

		try {

			splashbar.setVisibility(View.VISIBLE);

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error when check subcheck with imsi");

		}

	}

	public class RequestThread extends Thread {
		@Override
		public void run() {

			// Looper.prepare();
			synchronized (this) {

				try {

					CheckUserInfo.getUserMsisdnInfo(getApplicationContext());

				} catch (Exception ex) {

					Log.d("TAG", "Error in getUserMsisdnInfo");
				}

			}
			handler.sendEmptyMessage(0);

		}
	}

	@SuppressLint("NewApi")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			splashbar.setVisibility(View.GONE);

			try {

				Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);

				Bundle anim = ActivityOptions.makeCustomAnimation(getApplicationContext(),
						R.anim.animation, R.anim.animation2).toBundle();

				startActivity(mainIntent, anim);

			} catch (Exception ex) {

				Log.d("TAG", "Error in SplashActivity handleMessage");

			}

		}

	};

	public void printHashKey() {

		try {

			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String hashKey = new String(Base64.encode(md.digest(), 0));
				Log.i("ss", "printHashKey() Hash Key: " + hashKey);
			}

		} catch (NoSuchAlgorithmException e) {
			Log.e("ss", "printHashKey()", e);
		} catch (Exception e) {
			Log.e("ss", "printHashKey()", e);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();
	}

	@Override
	protected void onDestroy() {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

}
