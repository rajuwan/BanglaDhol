package com.ebs.banglalinkbangladhol.others;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.ebs.banglalinkbangladhol.bean.DeviceItem;
import com.ebs.banglalinkbangladhol.bean.LoginJson;
import com.ebs.banglalinkbangladhol.bean.MsisdnJson;

public class CheckUserInfo {

	public static String deviceId = null;
	public static String imei = "";
	public static String imsi = "";
	public static String softwareVersion = "";
	public static String simSerialNumber = "";
	public static String brand = "";
	public static String model = "";
	public static String operator = "";
	public static String operatorName = "";
	public static String release = "";
	public static int sdkVersion = 0;
	public static int versionCode = 0;

	public static List<DeviceItem> deviceInfoList;

	public static String val_login = null;
	public static String val_msisdn = null;

	private static List<MsisdnJson> jsonMsisdn;
	private static List<LoginJson> jsonLogin;

	private static String packname, packtext, infotext, ticker, loginresult, token,
			enforcetext, concurrenttext, consenttext, consenturl = null;
	private static int userCanPlay = 0;
	private static int currentversion = 0;
	private static int network = 0;
	private static int enforce = 0;
	public static int concurrent = 0;
	public static int consent = 0;
	private static String packcode = "nopack";
	private static String server_msisdn = "";
	private static String server_msisdn_found = "";
	private static String user_pin = "";
	private static String fcm_saved = "no";
	public static int referral = 0;
	private static int showadhome = 0;
	private static int showad = 0;

	private static SharedPreferences sharedpreference;
	private static String PREFERENCES = "loginCheckMyPrefs_BBD";
	private static String RESULT = "loginresultKey";
	private static String MSISDN = "loginmsisdnKey";
	private static String PINCODE = "loginpinKey";
	private static String TOKEN = "tokenpassKey";
	private static String PLAY = "playpassKey";
	private static String PACKCODE = "packcodeKey";
	private static String PACKNAME = "packnameKey";
	private static String PACKTEXT = "packtextKey";
	private static String INFOTEXT = "infotextKey";
	private static String TICKERTEXT = "tickertextKey";
	private static String ENFORCE = "enforceKey";
	private static String ENFORCETEXT = "enforcetextKey";
	private static String VERSION = "currentversionKey";
	private static String NETWORK = "networkaccessKey";
	private static String MSISDN_FOUND = "msisdnfoundKey";
	private static String CONCURRENT = "concurrentKey";
	private static String CONCURRENTTEXT = "concurrenttextKey";

	private static String CONSENT = "consentKey";
	private static String CONSENTTEXT = "consenttextKey";
	private static String CONSENTURL = "consenturlKey";
	private static String FCM_SERVER = "fcmserverKey";
	private static String REFERRAL = "	referralKey";
	private static String SHOW_AD_HOME = "showadhomeKey";
	private static String SHOW_AD = "showadKey";

	public static void init(Context context) {

		sharedpreference = context.getSharedPreferences(PREFERENCES,
				Context.MODE_PRIVATE);

	}

	public static void getUserMsisdnInfo(Context context) {

		try {

			init(context);

			val_msisdn = ServerUtilities.requestForMsisdn(context);

			if (val_msisdn != null && val_msisdn.length() > 10) {

				jsonMsisdn = HTTPGateway.getMsisdnCredential(val_msisdn);

				for (int i = 0; i < jsonMsisdn.size(); i++) {

					MsisdnJson tmpData = jsonMsisdn.get(i);

					server_msisdn = tmpData.getMsisdn();
					network = tmpData.getNetwork();
					infotext = tmpData.getInfoText();
					ticker = tmpData.getTickerText();
					showadhome = tmpData.getShowAdHome();
				}

				Editor editor = sharedpreference.edit();

				if (isValidPhoneNumber(server_msisdn)) {

					editor.putString(MSISDN, server_msisdn);
					editor.putString(MSISDN_FOUND, "yes");
					editor.remove(PINCODE);

				} else {

					editor.putString(MSISDN_FOUND, "no");

				}

				editor.putInt(NETWORK, network);
				editor.putString(INFOTEXT, infotext);
				editor.putString(TICKERTEXT, ticker);
				editor.putInt(SHOW_AD_HOME, showadhome);

				editor.commit();

			}

		} catch (Exception ex) {

			Log.d("TAG", "Error while retreving user UserMsisdnInfo");

		}

	}

	public static void getUserLoginInfo(Context context, String msisdn,
			String pincode) {

		try {

			init(context);

			deviceInfoList = DeviceInfo.getDeviceInfo(context);

			if (deviceInfoList != null) {

				for (int i = 0; i < deviceInfoList.size(); i++) {

					DeviceItem tmpData = deviceInfoList.get(i);

					deviceId = tmpData.getDeviceId();
					imsi = tmpData.getDeviceIMSI();
					imei = tmpData.getDeviceIMEI();
					softwareVersion = tmpData.getDeviceSoftwareVersion();
					simSerialNumber = tmpData.getDeviceSimSerialNumber();
					operator = tmpData.getDeviceOperator();
					operatorName = tmpData.getDeviceOperatorName();
					brand = tmpData.getDeviceBrand();
					model = tmpData.getDeviceModel();
					release = tmpData.getDeviceRelease();
					sdkVersion = tmpData.getDeviceSdkVersion();
					versionCode = tmpData.getDeviceVersionCode();
				}

			}

			if (isValidPhoneNumber(msisdn) && pincode.length() >= 4) { // from login box

				user_pin = pincode;

				val_login = ServerUtilities.checkUserLogin(context, msisdn,
						pincode, deviceId, imei, imsi, softwareVersion,
						simSerialNumber, operator, operatorName, brand, model,
						release, sdkVersion, versionCode, "");

			} else {

				if (isValidPhoneNumber(msisdn) && pincode.length() <= 0) { // data user

					val_login = ServerUtilities.checkUserLogin(context,
							getUserMsisdn(), getUserPinCode(), deviceId, imei,
							imsi, softwareVersion, simSerialNumber, operator,
							operatorName, brand, model, release, sdkVersion,
							versionCode, "yes");

				} else { //

					val_login = ServerUtilities.checkUserLogin(context,
							getUserMsisdn(), getUserPinCode(), deviceId, imei,
							imsi, softwareVersion, simSerialNumber, operator,
							operatorName, brand, model, release, sdkVersion,
							versionCode, "");

				}

			}

			if (val_login != null && val_login.length() > 10) {

				jsonLogin = HTTPGateway.getLoginCredential(val_login);

				for (int i = 0; i < jsonLogin.size(); i++) {

					LoginJson tmpData = jsonLogin.get(i);

					loginresult = tmpData.getResult();
					token = tmpData.getToken();
					userCanPlay = tmpData.getPlay();
					packcode = tmpData.getPackCode();
					packname = tmpData.getPackName();
					packtext = tmpData.getPackText();
					server_msisdn = tmpData.getUserName();
					enforce = tmpData.getEnforce();
					enforcetext = tmpData.getEnforceText();
					currentversion = tmpData.getCurrentVersion();
					concurrent = tmpData.getConCurrent();
					concurrenttext = tmpData.getConCurrentText();
					showad = tmpData.getShowAd();
					//consent = tmpData.getConsent();
					//consenttext = tmpData.getConsentText();
					//consenturl = tmpData.getConsentUrl();

				}

				if (loginresult.contains("success")) {

					Editor editor = sharedpreference.edit();

					if (user_pin.length() >= 4) {

						editor.putString(PINCODE, user_pin);

					}
					editor.putString(RESULT, loginresult);
					editor.putString(TOKEN, token);
					editor.putInt(PLAY, userCanPlay);
					editor.putString(PACKCODE, packcode);
					editor.putString(PACKNAME, packname);
					editor.putString(PACKTEXT, packtext);

					if (isValidPhoneNumber(server_msisdn)) {

						editor.putString(MSISDN, server_msisdn);
					}

					editor.putInt(ENFORCE, enforce);
					editor.putString(ENFORCETEXT, enforcetext);
					editor.putInt(VERSION, currentversion);
					editor.putInt(CONCURRENT, concurrent);
					editor.putString(CONCURRENTTEXT, concurrenttext);
					//editor.putInt(CONSENT, consent);
					editor.putString(CONSENTTEXT, consenttext);
					editor.putString(CONSENTURL, consenturl);
					editor.putInt(SHOW_AD, showad);
					editor.commit();

				} else {

					Editor editor = sharedpreference.edit();
					editor.putInt(PLAY, userCanPlay);
					editor.putInt(ENFORCE, enforce);
					editor.putString(ENFORCETEXT, enforcetext);
					editor.putInt(VERSION, currentversion);
					editor.putInt(CONCURRENT, concurrent);
					editor.putString(CONCURRENTTEXT, concurrenttext);
					editor.putInt(CONSENT, consent);
					editor.putString(CONSENTTEXT, consenttext);
					editor.putString(CONSENTURL, consenturl);
					editor.putInt(SHOW_AD, showad);
					editor.commit();

				}

			}

		} catch (Exception ex) {

			Log.d("TAG", "Error while retreving user login info ");

		}

	}

	public static void changeConsentToSp(Context context, int con) {

		try {

			init(context);

			Editor editor = sharedpreference.edit();
			editor.putInt(CONSENT, con);
			 
			editor.commit();

		} catch (Exception ex) {

			Log.d("TAG", "Error while retreving user UserMsisdnInfo");

		}

	}
	
	public static void changePlayToSp(Context context, int play) {

		try {

			init(context);

			Editor editor = sharedpreference.edit();
			editor.putInt(PLAY, play);

			editor.commit();

		} catch (Exception ex) {

			Log.d("TAG", "Error while retreving user UserMsisdnInfo");

		}

	}

	public static void changeFCMToSp(Context context, String fcm_status) {

		try {

			init(context);

			Editor editor = sharedpreference.edit();
			editor.putString(FCM_SERVER, fcm_status);

			editor.commit();

		} catch (Exception ex) {

			Log.d("TAG", "Error while retreving user UserMsisdnInfo");

		}

	}

	/* 1. User Login Result -- success or fail */
	public static String getUserLoginResult() {

		if (sharedpreference.contains(RESULT)) {

			loginresult = sharedpreference.getString(RESULT, "");

		}

		return loginresult;

	}

	/* 2. User Login token -- notoken or efsaldjasdff44efeqfouifjqwe */
	public static String getUserToken() {

		if (sharedpreference.contains(TOKEN)) {

			token = sharedpreference.getString(TOKEN, "");

		}

		return token;

	}

	/* 3. User Login MSISDN -- null or 88019xxxxxxxx */
	public static String getUserMsisdn() {

		if (sharedpreference.contains(MSISDN)) {

			server_msisdn = sharedpreference.getString(MSISDN, "");

		}

		return server_msisdn;

	}

	/* 12. User pincode */
	public static String getUserPinCode() {

		if (sharedpreference.contains(PINCODE)) {

			user_pin = sharedpreference.getString(PINCODE, "");

		}

		return user_pin;

	}

	/* 4. User Play Status -- 0 or 1 */
	public static int getUserPlayStatus() {

		if (sharedpreference.contains(PLAY)) {

			userCanPlay = sharedpreference.getInt(PLAY, 0);

		}

		return userCanPlay;

	}

	/* 5. User pack code -- nopack or start1/start7/start30 */
	public static String getUserPackCode() {

		if (sharedpreference.contains(PACKCODE)) {

			packcode = sharedpreference.getString(PACKCODE, "");

		}

		return packcode;

	}

	/* 6. User pack name -- nopack or 1 day plan */
	public static String getUserPackName() {

		if (sharedpreference.contains(PACKNAME)) {

			packname = sharedpreference.getString(PACKNAME, "");

		}

		return packname;

	}

	/*
	 * 7. User pack title -- nopack or Please use banglalink mobile data to use
	 * service!
	 */
	public static String getUserPackText() {

		if (sharedpreference.contains(PACKTEXT)) {

			packtext = sharedpreference.getString(PACKTEXT, "");

		}

		return packtext;

	}

	/* 8. User network access -- 7(111) or 1(001) */
	public static int getUserNetworkAccess() {

		if (sharedpreference.contains(NETWORK)) {

			network = sharedpreference.getInt(NETWORK, 0);

		}

		return network;

	}

	/* 9. User update decision -- 0 or 1 */
	public static int getEnforceStatus() {

		if (sharedpreference.contains(ENFORCE)) {

			enforce = sharedpreference.getInt(ENFORCE, 0);

		}

		return enforce;

	}

	/*
	 * 10. User update title -- Dear User, New Update of Banglaflix is available.
	 * Please update or app might not work properly.
	 */
	public static String getEnforceText() {

		if (sharedpreference.contains(ENFORCETEXT)) {

			enforcetext = sharedpreference.getString(ENFORCETEXT, "");

		}

		return enforcetext;

	}

	/* 11. User app version from server -- 10 */
	public static int getAppVersion() {

		if (sharedpreference.contains(VERSION)) {

			currentversion = sharedpreference.getInt(VERSION, 0);

		}

		return currentversion;

	}

	/* 12. From server Msisdn found -- yes or no */
	public static String getMsisdnFromServer() {

		if (sharedpreference.contains(MSISDN_FOUND)) {

			server_msisdn_found = sharedpreference.getString(MSISDN_FOUND, "");

		}

		return server_msisdn_found;

	}

	/*
	 * 13. User title -- nopack or Please use banglalink mobile data to use
	 * service!
	 */
	public static String getUserInfoText() {

		if (sharedpreference.contains(INFOTEXT)) {

			infotext = sharedpreference.getString(INFOTEXT, "");

		}

		return infotext;

	}

	/* 15. Concurrent Status -- 0 or 1 */
	public static int getConCurrentStatus() {

		if (sharedpreference.contains(CONCURRENT)) {

			concurrent = sharedpreference.getInt(CONCURRENT, 0);

		}

		return concurrent;

	}

	/*
	 * 16. ConCurrent title -- You are signed in from another device, please sign
	 * out from that device!
	 */
	public static String getConCurrentText() {

		if (sharedpreference.contains(CONCURRENTTEXT)) {

			concurrenttext = sharedpreference.getString(CONCURRENTTEXT, "");

		}

		return concurrenttext;

	}

	/* 17. Consent Status -- 0 or 1 */
	/*public static int getConsentStatus() {

		if (sharedpreference.contains(CONSENT)) {

			consent = sharedpreference.getInt(CONSENT, 0);

		}

		return consent;

	}*/

	/*
	 * 18. Consent title -- Free subscription will end soon, please confirm
	 */
	public static String getConsentText() {

		if (sharedpreference.contains(CONSENTTEXT)) {

			consenttext = sharedpreference.getString(CONSENTTEXT, "");

		}

		return consenttext;

	}

	/*
	 * 19. Consent url --
	 */
	public static String getConsentUrl() {

		if (sharedpreference.contains(CONSENTURL)) {

			consenturl = sharedpreference.getString(CONSENTURL, "");

		}

		return consenturl;

	}

	/* 22. User Fcm Status -- yes or no */
	public static String getUserFcmStatus() {

		if (sharedpreference.contains(FCM_SERVER)) {

			fcm_saved = sharedpreference.getString(FCM_SERVER, "");

		}

		return fcm_saved;

	}
	
	/* 23. Referral Status -- 0 or 1 */
	public static int getReferralStatus() {

		if (sharedpreference.contains(REFERRAL)) {

			referral = sharedpreference.getInt(REFERRAL, 0);

		}

		return referral;

	}

	/*
	 * 24. Ticker Text
	 */
	public static String getTickerText() {

		if (sharedpreference.contains(TICKERTEXT)) {

			ticker = sharedpreference.getString(TICKERTEXT, "");

		}

		return ticker;

	}

	/* 25. Show Ad Status -- 0 or 1 */
	public static int getShowAdStatus() {

		if (sharedpreference.contains(SHOW_AD)) {

			showad = sharedpreference.getInt(SHOW_AD, 0);

		}

		return showad;

	}

	/* 26. Show Ad Home Status -- 0 or 1 */
	public static int getShowAdHomeStatus() {

		if (sharedpreference.contains(SHOW_AD_HOME)) {

			showadhome = sharedpreference.getInt(SHOW_AD_HOME, 0);

		}

		return showadhome;

	}

	public static void removeUserInfo(Context context) {

		try {

			init(context);

			Editor editor = sharedpreference.edit();

			editor.remove(RESULT);
			editor.remove(PINCODE);
			editor.remove(MSISDN);
			editor.remove(TOKEN);
			editor.remove(PLAY);
			editor.remove(PACKCODE);
			editor.remove(PACKNAME);
			editor.remove(PACKTEXT);
			editor.remove(MSISDN_FOUND);
			editor.remove(NETWORK);
			editor.remove(CONCURRENT);
			editor.remove(CONCURRENTTEXT);
			editor.remove(CONSENT);
			editor.remove(CONSENTTEXT);
			editor.remove(CONSENTURL);
			editor.remove(SHOW_AD_HOME);
			editor.remove(SHOW_AD);
			editor.apply();
			editor.commit();

		} catch (Exception ex) {

			Log.d("TAG", "Error while removing user info");

		}

	}

	public static boolean isValidPhoneNumber(String mobile) {
		String regEx = "^[0-9]{13}$";
		return mobile.matches(regEx);
	}

}
