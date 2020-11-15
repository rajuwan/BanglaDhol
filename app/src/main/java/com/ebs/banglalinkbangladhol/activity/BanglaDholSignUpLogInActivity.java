package com.ebs.banglalinkbangladhol.activity;

import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.ebs.banglalinkbangladhol.R;
import com.ebs.banglalinkbangladhol.fragment.HomeFragment;
import com.ebs.banglalinkbangladhol.others.CheckUserInfo;
import com.ebs.banglalinkbangladhol.others.ServerUtilities;

public class BanglaDholSignUpLogInActivity extends Activity {

	private TelephonyManager mTelephony;

	private TextView tv_BFlixLogin, tv_BFlixSignUp;
	private TextView tv_login, tv_signup, tv_forget_password;
	private EditText et_msisdn, et_password;
	private LinearLayout loginLayout, signupLayout;

	private String user_msisdn, user_password, result;
	private String val = null;
	private ProgressDialog pd;
	String pastText = "";
	private String deviceId = null;
	private String imei = "";
	private String imsi = "";
	private String softwareVersion = "";
	private String simSerialNumber = "";
	private String brand = "";
	private String model = "";
	private String operator = "";
	private String operatorName = "";
	private String release = "";
	private int sdkVersion = 0;
	private int versionCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bangla_dhol_signup_login);

		mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		tv_BFlixLogin = (TextView) findViewById(R.id.btnBFlixLogin);
		tv_BFlixSignUp = (TextView) findViewById(R.id.btnBlBdholSignUp);
		et_msisdn = (EditText) findViewById(R.id.edtTxtMobileNumber);
		et_password = (EditText) findViewById(R.id.editTxtPassword);

		loginLayout = (LinearLayout) findViewById(R.id.llParentEmailPassword);
		signupLayout = (LinearLayout) findViewById(R.id.llParentSignUpConfirm);

		tv_login = (TextView) findViewById(R.id.tv_LogIn);
		tv_signup = (TextView) findViewById(R.id.tv_SignUp);
		tv_forget_password = (TextView) findViewById(R.id.underlinedtext);

		tv_login.setTextColor(Color.parseColor("#F15B00"));
		tv_signup.setTextColor(Color.parseColor("#f2f2f2"));

		try {

			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

				deviceId = Secure.getString(getApplicationContext()
						.getContentResolver(), Secure.ANDROID_ID);

				imsi = "no access"; // IMSI
				imei = "no access"; // IMEI
				softwareVersion = "no access"; // SOFTWARE
				// VERSION
				simSerialNumber = "no access"; // SIM SERIAL
				operator = mTelephony.getNetworkOperator(); // OPERATOR ID
				operatorName = mTelephony.getNetworkOperatorName(); // OPERATOR
				// NAME

				brand = Build.BRAND; // DEVICE BRAND
				model = Build.MODEL; // DEVICE MODEL
				release = Build.VERSION.RELEASE; // ANDROID VERSION LIKE 4.4.4
				sdkVersion = Build.VERSION.SDK_INT; // API VERSION LIKE 19
				versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

			} else {

				if (mTelephony.getDeviceId() != null) {
					deviceId = mTelephony.getDeviceId();
				} else {
					deviceId = Secure.getString(getApplicationContext()
							.getContentResolver(), Secure.ANDROID_ID);
				}

				imsi = mTelephony.getSubscriberId(); // IMSI
				imei = mTelephony.getDeviceId(); // IMEI
				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					//return;
				}
				softwareVersion = mTelephony.getDeviceSoftwareVersion(); // SOFTWARE
																			// VERSION
				simSerialNumber = mTelephony.getSimSerialNumber(); // SIM SERIAL
				operator = mTelephony.getNetworkOperator(); // OPERATOR ID
				operatorName = mTelephony.getNetworkOperatorName(); // OPERATOR
																	// NAME

				brand = Build.BRAND; // DEVICE BRAND
				model = Build.MODEL; // DEVICE MODEL
				release = Build.VERSION.RELEASE; // ANDROID VERSION LIKE 4.4.4
				sdkVersion = Build.VERSION.SDK_INT; // API VERSION LIKE 19
				versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

			}

		} catch (Exception ex) {

			Log.d("TAG", "Error while retreving deviceId ");

		}

		tv_BFlixLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				user_msisdn = "8801" + et_msisdn.getText().toString().trim();
				user_password = et_password.getText().toString().trim();

				if (!isValidPhoneNumber(user_msisdn)) {

					Toast.makeText(getApplicationContext(),
							"Please enter valid mobile number",
							Toast.LENGTH_LONG).show();

				} else if (user_password.length() == 0) {

					Toast.makeText(getApplicationContext(),
							"Password must not be empty", Toast.LENGTH_LONG)
							.show();

				} else if (isValidPhoneNumber(user_msisdn) && user_password.length() > 0) {

					LoginCheck();

				}

			}
		});

		tv_BFlixSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				user_msisdn = "8801" + et_msisdn.getText().toString().trim();

				if (!isValidPhoneNumber(user_msisdn)) {

					Toast.makeText(getApplicationContext(),
							"Please enter valid mobile number",
							Toast.LENGTH_LONG).show();

				} else if (isValidPhoneNumber(user_msisdn)) {

					SignUp();

				}

			}
		});

		tv_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tv_login.setTextColor(Color.parseColor("#F15B00"));
				tv_signup.setTextColor(Color.parseColor("#f2f2f2"));

				loginLayout.setVisibility(View.VISIBLE);
				signupLayout.setVisibility(View.GONE);

			}
		});

		tv_signup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				tv_login.setTextColor(Color.parseColor("#f2f2f2"));
				tv_signup.setTextColor(Color.parseColor("#F15B00"));

				loginLayout.setVisibility(View.GONE);
				signupLayout.setVisibility(View.VISIBLE);

			}
		});

		tv_forget_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ForgetPassWord();

			}
		});

	}

	public void SignUp() {

		try {

			pd = new ProgressDialog(BanglaDholSignUpLogInActivity.this, ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Sending Request...");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();

			RequestThreadSignUp reqThread = new RequestThreadSignUp();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadSignUp extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					val = ServerUtilities.requestForSignUp(
							BanglaDholSignUpLogInActivity.this, user_msisdn,
							deviceId, imsi, imei, softwareVersion,
							simSerialNumber, operator, operatorName, brand,
							model, release, sdkVersion, versionCode);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			signup_handler.sendEmptyMessage(0);

		}
	}

	Handler signup_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			if (val != null) {

				try {

					JSONObject respObject = new JSONObject(val);
					String res = respObject.getString("message");
					if (res != null) {

						Toast.makeText(BanglaDholSignUpLogInActivity.this, res,
								Toast.LENGTH_LONG).show();

					}

				} catch (Exception e) {

					Log.d("Tag", "Error while get from signup");

				}

			} else {

				Toast.makeText(BanglaDholSignUpLogInActivity.this,
						"Couldn't Proceess your request now. Please Try Again",
						Toast.LENGTH_LONG).show();

			}

		}

	};

	private void ForgetPassWord() {

		try {

			LayoutInflater li = getLayoutInflater();
			View feedbackView = li.inflate(R.layout.forget_password, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					BanglaDholSignUpLogInActivity.this, AlertDialog.THEME_HOLO_DARK);
			alertDialogBuilder.setTitle(Html
					.fromHtml("<font color='#F2F2F2'>Forgot password</font>"));
			alertDialogBuilder.setView(feedbackView);
			final EditText userNumber = (EditText) feedbackView
					.findViewById(R.id.etForgetMobileNumber);

			userNumber.setText(et_msisdn.getText().toString().trim());

			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// get user input and set it to result

									user_msisdn = "8801"
											+ userNumber.getText().toString()
													.trim();

									if (isValidPhoneNumber(user_msisdn)) {

										Forget();

									} else {

										Toast.makeText(
												BanglaDholSignUpLogInActivity.this,
												"Please type valid mobile number",
												Toast.LENGTH_LONG).show();

									}

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");

		}

	}

	public void Forget() {

		try {

			pd = new ProgressDialog(BanglaDholSignUpLogInActivity.this, ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Sending Request ...");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();

			RequestThreadForget reqThread = new RequestThreadForget();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadForget extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					val = ServerUtilities.requestForPassword(
							BanglaDholSignUpLogInActivity.this, user_msisdn,
							deviceId, imsi, imei, softwareVersion,
							simSerialNumber, operator, operatorName, brand,
							model, release, sdkVersion, versionCode);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			forget_handler.sendEmptyMessage(0);

		}
	}

	Handler forget_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			if (val != null) {

				try {

					JSONObject respObject = new JSONObject(val);
					String res = respObject.getString("message");
					if (res != null) {

						Toast.makeText(BanglaDholSignUpLogInActivity.this, res,
								Toast.LENGTH_LONG).show();

					}

				} catch (Exception e) {

					Log.d("Tag", "Error while get from forget password");

				}

			} else {

				Toast.makeText(BanglaDholSignUpLogInActivity.this,
						"Couldn't Proceess your request now. Please Try Again",
						Toast.LENGTH_LONG).show();

			}

		}

	};

	private static boolean isValidPhoneNumber(String mobile) {
		String regEx = "^[0-9]{13}$";
		return mobile.matches(regEx);
	}

	public void LoginCheck() {

		try {

			pd = new ProgressDialog(BanglaDholSignUpLogInActivity.this, ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Loading Please Wait ...");
			pd.setIndeterminate(false);
			pd.setCancelable(false);
			pd.show();

			RequestThreadLogIn reqThread = new RequestThreadLogIn();

			reqThread.start();

		} catch (Exception ex) {

			Log.d("TAG", "Error Occured");
		}
	}

	public class RequestThreadLogIn extends Thread {

		@Override
		public void run() {

			synchronized (this) {

				try {

					CheckUserInfo.getUserLoginInfo(getApplicationContext(),
							user_msisdn, user_password);

				} catch (Exception e) {

					Log.d("TAG", "Error Occured");

				}

			}

			login_handler.sendEmptyMessage(0);

		}
	}

	Handler login_handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			pd.dismiss();

			try {

				String result = CheckUserInfo.getUserLoginResult();

				if (result.contains("success")) {

					MainActivity.signback = true;
					HomeFragment.cdback = true;
					BanglaDholSignUpLogInActivity.this.finish();

				} else {

					Toast.makeText(getApplicationContext(), "User name or Password incorrect. Try Again",
							Toast.LENGTH_LONG).show();

				}

			} catch (Exception ex) {

				Log.d("Login Error", "Error while parsing login data");
			}

		}

	};

}
