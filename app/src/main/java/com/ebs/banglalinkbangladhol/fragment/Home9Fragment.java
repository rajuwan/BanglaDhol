package com.ebs.banglalinkbangladhol.fragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.adapter.ImageSlideAdapter;
import com.ebs.banglalinkbangladhol.bean.Product;
import com.ebs.banglalinkbangladhol.json.GetJSONObject;
import com.ebs.banglalinkbangladhol.json.JsonReader;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.util.CheckNetworkConnection;
import com.ebs.banglalinkbangladhol.util.CirclePageIndicator;
import com.ebs.banglalinkbangladhol.util.PageIndicator;
import com.ebs.banglalinkbangladhol.util.TagName;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class Home9Fragment extends Fragment {

	public static final String ARG_ITEM_ID = "home_fragment";

	private static final long ANIM_VIEWPAGER_DELAY = 4000;
	private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

	// UI References
	private ViewPager mViewPager;
	//TextView imgNameTxt;
	PageIndicator mIndicator;

	AlertDialog alertDialog;

	List<Product> products;
	RequestImgTask task;
	boolean stopSliding = false;
	String message;

	private Runnable animateViewPager;
	private Handler handler;

	String url = HTTPGateway.BANNER_URL;
	
	FragmentActivity activity;
	//Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		findViewById(view);

		mIndicator.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnPageChangeListener(new PageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction()) {

				case MotionEvent.ACTION_CANCEL:
					break;

				case MotionEvent.ACTION_UP:
					// calls when touch release on ViewPager
					if (products != null && products.size() != 0) {
						stopSliding = false;
						runnable(products.size());
						handler.postDelayed(animateViewPager,
								ANIM_VIEWPAGER_DELAY_USER_VIEW);
					}
					break;

				case MotionEvent.ACTION_MOVE:
					// calls when ViewPager touch
					if (handler != null && stopSliding == false) {
						stopSliding = true;
						handler.removeCallbacks(animateViewPager);
					}
					break;
				}
				return false;
			}
		});

		return view;
	}

	private void findViewById(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		//imgNameTxt = (TextView) view.findViewById(R.id.img_name);
	}

	public void runnable(final int size) {
		handler = new Handler();
		animateViewPager = new Runnable() {
			public void run() {
				if (!stopSliding) {
					if (mViewPager.getCurrentItem() == size - 1) {
						mViewPager.setCurrentItem(0);
					} else {
						mViewPager.setCurrentItem(
								mViewPager.getCurrentItem() + 1, true);
					}
					handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}

	@Override
	public void onResume() {
		if (products == null) {
			sendRequest();
		} else {

			mViewPager.setAdapter(new ImageSlideAdapter(activity, products,
					Home9Fragment.this));

			mIndicator.setViewPager(mViewPager);
			//imgNameTxt.setText(""
					//+ ((Product) products.get(mViewPager.getCurrentItem()))
							//.getName());
			runnable(products.size());
			// Re-run callback
			handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (task != null)
			task.cancel(true);
		if (handler != null) {
			// Remove callback
			handler.removeCallbacks(animateViewPager);
		}
		super.onPause();
	}

	private void sendRequest() {
		if (CheckNetworkConnection.isConnectionAvailable(activity)) {
			task = new RequestImgTask(activity);
			task.execute(url);
		} else {
			message = getResources().getString(R.string.no_internet_connection);
			//showAlertDialog(message, true);
		}
	}

	public void showAlertDialog(String message, final boolean finish) {
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (finish)
							activity.finish();
					}
				});
		alertDialog.show();
	}

	private class PageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				if (products != null) {
					//imgNameTxt.setText(""
							//+ ((Product) products.get(mViewPager
									//.getCurrentItem())).getName());
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private class RequestImgTask extends AsyncTask<String, Void, List<Product>> {
		private final WeakReference<Activity> activityWeakRef;
		Throwable error;

		public RequestImgTask(Activity context) {
			this.activityWeakRef = new WeakReference<Activity>(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected List<Product> doInBackground(String... urls) {
			try {

				//JSONObject jsonObject = getJsonObject(urls[0]);

				String jsonString = getJsonObject(urls[0]);

				if (jsonString != null) {

					products = JsonReader.getHome(jsonString);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return products;
		}

		/**
		 * It returns jsonObject for the specified url.
		 * 
		 * @param url
		 * @return JSONObject
		 */
		public String getJsonObject(String url) {
			String jsonObject = null;
			try {
				jsonObject = GetJSONObject.getJSONObject(url);
			} catch (Exception e) {
			}
			return jsonObject;
		}

		@Override
		protected void onPostExecute(List<Product> result) {
			super.onPostExecute(result);

			if (activityWeakRef != null && !activityWeakRef.get().isFinishing()) {
				if (error != null && error instanceof IOException) {
					message = getResources().getString(R.string.time_out);
					showAlertDialog(message, true);
				} else if (error != null) {
					message = getResources().getString(R.string.error_occured);
					showAlertDialog(message, true);
				} else {
					products = result;
					if (result != null) {
						if (products != null && products.size() != 0) {

							mViewPager.setAdapter(new ImageSlideAdapter(
									activity, products, Home9Fragment.this));

							mIndicator.setViewPager(mViewPager);
							//imgNameTxt.setText(""
									//+ ((Product) products.get(mViewPager
										//	.getCurrentItem())).getName());
							runnable(products.size());
							handler.postDelayed(animateViewPager,
									ANIM_VIEWPAGER_DELAY);
						} else {
							//imgNameTxt.setText("No Products");
						}
					} else {
					}
				}
			}
		}
	}
}
