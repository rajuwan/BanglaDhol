package com.ebs.banglalinkbangladhol.fragment;

import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Set;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.activity.BanglaDholSignUpLogInActivity;
import com.ebs.banglalinkbangladhol.bean.SubJson;
import com.ebs.banglalinkbangladhol.bean.SubProduct;
import com.ebs.banglalinkbangladhol.json.SubJsonReader;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.HTTPGateway;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

public class SubscriptionFragment extends Fragment {

	public SubscriptionFragment() {

	}

	private ListView list;

	private TextView userStatus;
	private ProgressDialog pd;
	private ProgressBar splashbar;

	private String selected_pack, res_sdp;
	private List<SubProduct> subproducts;
	private String subBack = "";
	private String unsubBack = "";

	private List<SubJson> jsonSub;

	private String val_sdp = null;
	private String sdp_url = "";
	private List<SubJson> jsonSdp;

	private AlertDialog alertDialog = null;
	public AsyncTask<Void, Void, String> mRegisterTask;
	private String isChargeSuccess, serviceid, referenceId = "";

	public static final String FRAGMENT_TAG = "SubscriptionFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_subscription, container, false);

		userStatus = (TextView) view.findViewById(R.id.tv_sub_head);
		list = (ListView) view.findViewById(android.R.id.list);

		getActivity().setTitle("Subscription");
		splashbar = (ProgressBar) view.findViewById(R.id.splashSUBProgress);

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Invoke();

		super.onResume();
	}

	public void Invoke() {

		try {

			splashbar.setVisibility(View.VISIBLE);

			RequestThread reqThread = new RequestThread();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error in main thread");
		}
	}

	/**
	 * To get User status using -- Thread*
	 */
	public class RequestThread extends Thread {

		@Override
		public void run() {

			Looper.prepare();

			synchronized (this) {

				try {

					CheckUserInfo.getUserMsisdnInfo(getActivity());

					CheckUserInfo.getUserLoginInfo(getActivity(),
							CheckUserInfo.getUserMsisdn(), CheckUserInfo.getUserPinCode());

					JSONObject jsonObject = ServerUtilities
							.requestForSubSchemes(getActivity(), CheckUserInfo.getUserMsisdn());

					if (jsonObject != null) {

						subproducts = SubJsonReader.getHome(jsonObject);

					}

				} catch (Exception ex) {

					Log.d("TAG", "Error in search");
				}

			}

			sub_handler.sendEmptyMessage(0);

		}
	}

	@SuppressLint("HandlerLeak")
	Handler sub_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			splashbar.setVisibility(View.GONE);

			if (subproducts != null) {

				list.setAdapter(new SubAdapter());

				userStatus.setText(CheckUserInfo.getUserPackText());

			} else {

				userStatus.setText("Please try again later");

			}

		}

	};

	class SubAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView sub_text, sub_txt_button;
			public RelativeLayout rl;
		}

		@Override
		public int getCount() {

			return subproducts.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater mInflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {

				view = mInflater.inflate(R.layout.item_subscription, parent, false);
				holder = new ViewHolder();

				holder.rl = (RelativeLayout) view.findViewById(R.id.rl_box);

				holder.sub_text = (TextView) view.findViewById(R.id.tv_sub_type);
				holder.sub_txt_button = (TextView) view.findViewById(R.id.tv_item_subscription);

				view.setTag(holder);

			} else {

				holder = (ViewHolder) view.getTag();
			}

			holder.sub_text.setText(subproducts.get(position).getText());

			if (CheckUserInfo.getUserPackCode().equals(subproducts.get(position).getPack())) {

				holder.sub_txt_button.setText("Unsubscribe");
				holder.sub_txt_button.setEnabled(true);
				holder.rl.setBackgroundResource(R.drawable.sub_type_selected);

			} else {

				if (CheckUserInfo.getUserPackCode().contains("nopack")) {

					holder.sub_txt_button.setText("Subscribe");
					holder.sub_txt_button.setEnabled(true);

				} else {

					holder.sub_txt_button.setText("Subscribe");
					holder.sub_txt_button.setEnabled(false);

				}

			}

			holder.sub_txt_button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (holder.sub_txt_button.isEnabled() == false) {

						Toast.makeText(getActivity(),
								"You need to Unsubscribe from current package to get a new one",
								Toast.LENGTH_LONG).show();
					}

					String subtxt = holder.sub_txt_button.getText().toString();
					selected_pack = subproducts.get(position).getPack();

					if (subtxt.contains("Subscribe")) {

						if (selected_pack != null) {

							if (!CheckUserInfo.isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

								Intent mainIntent = new Intent(getActivity(), BanglaDholSignUpLogInActivity.class);
								startActivity(mainIntent);

							} else {

								checkForDataUser();

								/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
										getActivity(), AlertDialog.THEME_HOLO_DARK);
								alertDialogBuilder.setTitle("Subscription");
								alertDialogBuilder.setMessage(subproducts.get(position).getRegMsg());
								// set dialog message
								alertDialogBuilder
										.setCancelable(false)
										.setPositiveButton(
												"Confirm",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {

														if (isValidPhoneNumber(CheckUserInfo.getUserMsisdn())) {

															Subscription();

														} else {

															try {

																AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
																		getActivity(), AlertDialog.THEME_HOLO_DARK);
																alertDialogBuilder.setTitle("Dear User");
																alertDialogBuilder.setMessage(CheckUserInfo.getUserPackText());

																// set dialog message
																alertDialogBuilder.setCancelable(false)
																		.setPositiveButton(
																				"OK",
																				new DialogInterface.OnClickListener() {
																					public void onClick(
																							DialogInterface dialog,
																							int id) {

																						dialog.dismiss();

																					}
																				});

																AlertDialog alertDialog = alertDialogBuilder.create();
																// show it
																alertDialog.show();

															} catch (Exception ex) {

																Log.d("TAG",
																		"Error Occured");

															}

														}

													}
												})
										.setNegativeButton(
												"Cancel",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.cancel();
													}
												});

								// create alert dialog
								AlertDialog alertDialog = alertDialogBuilder
										.create();
								// show it
								alertDialog.show();*/
							}

						}

					} else if (subtxt.contains("Unsubscribe")) {

						if (selected_pack != null) {

							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									getActivity(), AlertDialog.THEME_HOLO_DARK);
							alertDialogBuilder.setTitle("UnSubscription");
							alertDialogBuilder.setMessage("Are you sure want to unsubscribe from "
											+ subproducts.get(position).getName() + " pack?");
							// set dialog message
							alertDialogBuilder
									.setCancelable(false)
									.setPositiveButton(
											"Confirm",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {

													UnSubscription();

												}
											})
									.setNegativeButton(
											"Cancel",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.cancel();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
							// show it
							alertDialog.show();

						}

					} else {

						Toast.makeText(getActivity(), "Can't process your request now",
								Toast.LENGTH_SHORT).show();

					}

				}
			});

			return view;
		}
	}

	/* New Data User Checking Starts */
	public void checkForDataUser() {

		try {

			pd = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_DARK);
			pd.setMessage("Requesting...");
			pd.setIndeterminate(false);
			pd.setCancelable(true);
			pd.show();

			RequestThreadDataUser reqThread = new RequestThreadDataUser();
			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadDataUser extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					CheckUserInfo.getUserMsisdnInfo(getActivity());

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			datausercheck_handler.sendEmptyMessage(0);

		}
	}

	Handler datausercheck_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			try {

				if(CheckUserInfo.getMsisdnFromServer().contains("yes")){

					requestForSdpUrl();

				} else {

					DataRequiredDialog();

				}

			} catch (Exception e) {

				Log.d("Tag", "Error while get from signup");

			}

		}

	};

	public void requestForSdpUrl() {

		try {

			pd = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_DARK);
			pd.setMessage("Requesting...");
			pd.setIndeterminate(false);
			pd.setCancelable(true);
			pd.show();

			RequestThreadSDP reqThread = new RequestThreadSDP();
			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadSDP extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					val_sdp = ServerUtilities.requestForSDP(CheckUserInfo.getUserMsisdn(), selected_pack);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			reqsdp_handler.sendEmptyMessage(0);

		}
	}

	Handler reqsdp_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			try {

				if (val_sdp != null && val_sdp.length() > 10) {

					jsonSdp = HTTPGateway.getSdpCredential(val_sdp);

					for (int i = 0; i < jsonSdp.size(); i++) {

						SubJson tmpData = jsonSdp.get(i);

						sdp_url = tmpData.getSdpUrl();

					}

				}

				if(sdp_url != null && sdp_url.length()>10){

					popUp(sdp_url);
				}

				//Toast.makeText(getActivity(), sdp_url, Toast.LENGTH_LONG).show();

			} catch (Exception e) {

				Log.d("Tag", "Error while get from signup");

			}

		}

	};

	public void popUp(String url) {

		try {


			pd = new ProgressDialog(getActivity(), ProgressDialog.STYLE_HORIZONTAL);
			pd.setTitle("BanglaDhol");
			pd.setMessage("Processing request...");
			pd.setIndeterminate(true);
			pd.setCancelable(true);
			//progressDialog.show();

			final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setCancelable(true);

			WebView webView = new WebView(getActivity());
			webView.getSettings().setLoadsImagesAutomatically(true);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setSupportZoom(false);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
			webView.getSettings().setAllowContentAccess(true);
			webView.loadUrl(url);

			/*Map<String, String> params = new HashMap<String, String>();
			params.put("msisdn", CheckUserInfo.getUserMsisdn());

			StringBuilder bodyBuilder = new StringBuilder();
			Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
			// constructs the POST body using the parameters
			while (iterator.hasNext()) {
				Map.Entry<String, String> param = iterator.next();
				bodyBuilder.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					bodyBuilder.append('&');
				}
			}
			String body = bodyBuilder.toString();
			byte[] bytes = body.getBytes();

			webView.postUrl(url, bytes);*/

			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {

					try{

						view.loadUrl(url);

						if(url.contains("isChargeSuccess")){

							Uri uri = Uri.parse(url);
							Set<String> paramNames = uri.getQueryParameterNames();
							for (String key: paramNames) {

								if(key.contains("serviceid")){

									serviceid = uri.getQueryParameter(key);

								}

								if(key.contains("isChargeSuccess")){

									isChargeSuccess = uri.getQueryParameter(key);

								}

								if(key.contains("referenceId")){

									referenceId = uri.getQueryParameter(key);

								}

							}

							postSubStatus(serviceid, isChargeSuccess, referenceId);
							alertDialog.dismiss();

						} else {
							alertDialog.dismiss();
						}

					} catch (Exception ex){
						Log.d("web redirect error", ex.toString());

					}

					return true;
				}

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					pd.show();
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					pd.dismiss();
				}
			});

			alert.setView(webView);

			alertDialog = alert.show();
			//alert.show().getWindow().setLayout(700,900);


		}   catch (Exception e) {
			Log.e("popup error", " is : ", e);
		}

	}

	private void postSubStatus(final String serviceid, final String chargestatus, final String referenceid) {

		try {

			mRegisterTask = new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... params) {
					// Register on our server
					// On server creates a new user

					try {

						res_sdp = ServerUtilities.updateSubStatus(CheckUserInfo.getUserMsisdn(),
								serviceid, chargestatus, referenceid);

					} catch (Exception e) {
						e.printStackTrace();
					}

					return res_sdp;
				}

				@Override
				protected void onPostExecute(String result) {

					String res = "";

					if (result != null && result.length() > 5) {

						try {

							JSONObject m_obj = new JSONObject(result);

							res  = m_obj.getString("result");

							if(res.contains("success")){

								Invoke();

							}

							userStatus.setText(m_obj.getString("response"));

							Toast.makeText(getActivity(), m_obj.getString("response"), Toast.LENGTH_LONG).show();

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					mRegisterTask = null;
				}

			};

			mRegisterTask.execute(null, null, null);

		} catch (Exception ex) {
			Log.d("TAG", "Error in main thread");
		}

	}

	public void DataRequiredDialog(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("Dear User");
		alertDialogBuilder.setMessage("Please Use Banglalink Mobile Data for Subscription.");
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	private static boolean isValidPhoneNumber(String mobile) {
		String regEx = "^[0-9]{13}$";
		return mobile.matches(regEx);
	}

	public void Subscription() {

		try {

			pd = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Sending Subscription Request ...");
			pd.setIndeterminate(false);
			pd.setCancelable(true);
			pd.show();

			RequestThreadSubscription reqThread = new RequestThreadSubscription();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadSubscription extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					subBack = ServerUtilities.requestForSubscription(CheckUserInfo.getUserMsisdn(),
							   			selected_pack);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			subscription_handler.sendEmptyMessage(0);

		}
	}

	Handler subscription_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			if (subBack != null && subBack.length() > 5) {

				try {

					String response = "";
					int play = 0;

					jsonSub = HTTPGateway.getSubCredential(subBack);

					for (int i = 0; i < jsonSub.size(); i++) {

						SubJson tmpData = jsonSub.get(i);

						response = tmpData.getResponse();
						play = tmpData.getPlay();

					}

					if (play == 1) {

						Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

						Invoke();

					} else {

						userStatus.setText(response);

					}

				} catch (Exception e) {

					Log.d("Tag", "Error while get from signup");

				}

			}

		}

	};

	public void UnSubscription() {

		try {

			pd = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Sending UnSubscription Request ...");
			pd.setIndeterminate(false);
			pd.setCancelable(true);
			pd.show();

			RequestThreadUnSubscription reqThread = new RequestThreadUnSubscription();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadUnSubscription extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					unsubBack = ServerUtilities.requestForUnSubscription(
							getActivity(), CheckUserInfo.getUserMsisdn(),
							CheckUserInfo.deviceId, CheckUserInfo.imsi,
							CheckUserInfo.imei, CheckUserInfo.softwareVersion,
							CheckUserInfo.simSerialNumber,
							CheckUserInfo.operator, CheckUserInfo.operatorName,
							CheckUserInfo.brand, CheckUserInfo.model,
							CheckUserInfo.release, CheckUserInfo.sdkVersion,
							CheckUserInfo.versionCode);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			unsubscription_handler.sendEmptyMessage(0);

		}
	}

	Handler unsubscription_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			if (unsubBack != null && unsubBack.length() > 5) {

				try {

					String response = "";
					int play = 0;

					jsonSub = HTTPGateway.getSubCredential(unsubBack);

					for (int i = 0; i < jsonSub.size(); i++) {

						SubJson tmpData = jsonSub.get(i);

						response = tmpData.getResponse();
						play = tmpData.getPlay();

					}

					if (play == 0) {

						CheckUserInfo.changePlayToSp(getActivity(), play);
						Toast.makeText(getActivity(), response,
								Toast.LENGTH_LONG).show();

						Invoke();

					} else if (response != null) {

						userStatus.setText(response);

					}

				} catch (Exception e) {

					Log.d("Tag", "Error while get from unsub");

				}

			}

		}

	};

}
