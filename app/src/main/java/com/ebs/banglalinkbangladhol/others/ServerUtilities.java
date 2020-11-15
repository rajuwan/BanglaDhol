package com.ebs.banglalinkbangladhol.others;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public final class ServerUtilities {

	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();
	private static final String TAG = "ServerUtilities";

	/**
	 * Add Video Content to server (User Watch List)
	 *
	 */
	public static void registerToAddMyList(final Context context, String contentid, String msisdn) {

		Log.i(TAG, "saving id (contentId = " + contentid + ")");
		String serverUrl = HTTPGateway.ADD_MY_LIST;
		Map<String, String> params = new HashMap<String, String>();
		params.put("myval", contentid);
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				post(serverUrl, params);

				return;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

	}

	/**
	 * Remove Video Content from server (User Watch List)
	 *
	 */
	public static void registerRemoveFromMyList(final Context context, String contentid, String msisdn) {

		Log.i(TAG, "saving id (contentId = " + contentid + ")");
		String serverUrl = HTTPGateway.REMOVE_MY_LIST;
		Map<String, String> params = new HashMap<String, String>();
		params.put("myval", contentid);
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				post(serverUrl, params);

				return;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

	}

	/**
	 * Sending Request For MyList
	 *
	 */
	public static String requestForMyList(final Context context, String msisdn, int page) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.MY_LIST;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("page", String.valueOf(page));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

		return val;

	}

	/**
	 * Sending Subscription Request to Server
	 *
	 */
	public static String requestForSubscription(String msisdn, String pack) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.INSTANT_SUB;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("d", pack);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

		return val;

	}

	/**
	 * Sending Unsubscription Request to server
	 *
	 */
	public static String requestForUnSubscription(final Context context,
												  String msisdn, String deviceId, String imsi, String imei,
												  String softwareVersion, String simSerialNumber, String operator,
												  String operatorName, String brand, String model, String release,
												  int sdkVersion, int versionCode) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.INSTANT_UNSUB;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("deviceId", deviceId);
		params.put("imsi", imsi);
		params.put("imei", imei);
		params.put("softwareVersion", softwareVersion);
		params.put("simSerialNumber", simSerialNumber);
		params.put("operator", operator);
		params.put("operatorName", operatorName);
		params.put("brand", brand);
		params.put("model", model);
		params.put("release", release);
		params.put("sdkVersion", String.valueOf(sdkVersion));
		params.put("versionCode", String.valueOf(versionCode));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Sub Scheme Request to Server
	 *
	 */
	public static JSONObject requestForSubSchemes(final Context context,
												  String msisdn) {

		JSONObject val = null;
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.SUBSCHEMES;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post3(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Post with new Dynamic json
	 *
	 */
	public static JSONObject requestForCatJsonPost(String msisdn, String ct , int tc, int page) {

		JSONObject val = null;
		Log.i(TAG, "saving (ct = " + ct + ")");
		String serverUrl = HTTPGateway.CAT_JSON_POST;

		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("ct", ct);
		if (tc != -1) {
			params.put("tc", String.valueOf(tc));
		}
		if (page != 0) {
			params.put("page", String.valueOf(page));
		}

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post3(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Post with new Dynamic json Album
	 *
	 */

	public static JSONObject requestForAlbumJsonPost(String msisdn, String ct , String type, int page) {

		JSONObject val = null;
		Log.i(TAG, "saving (ct = " + ct + ")");
		String serverUrl = HTTPGateway.CAT_JSON_ALBUM;

		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("ct", ct);
		params.put("type", type);

		if (page != 0) {
			params.put("page", String.valueOf(page));
		}

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post3(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Request for detail data for album
	 *
	 */
	public static String requestForAlbumSongs(String msisdn, String albumcode) {

		String val = null;
		String serverUrl = HTTPGateway.ALBUM_DETAILS;

		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("albumcode", albumcode);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Login Request to Server
	 *
	 */
	public static String requestForMsisdn(final Context context) {

		String val = "";

		String serverUrl = HTTPGateway.CHECK_MSISDN;
		Map<String, String> params = new HashMap<String, String>();

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {

			Log.d(TAG, "Attempt #" + i + " to register");

			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Login Request to Server
	 *
	 */
	public static String checkUserLogin(final Context context, String msisdn, String password,
										String deviceId, String imei, String imsi,
										String softwareVersion, String simSerialNumber, String operator,
										String operatorName, String brand, String model, String release,
										int sdkVersion, int versionCode, String haspin) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.LOGIN_DETAILS;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("password", password);
		params.put("deviceId", deviceId);
		params.put("imei", imei);
		params.put("imsi", imsi);
		params.put("softwareVersion", softwareVersion);
		params.put("simSerialNumber", simSerialNumber);
		params.put("operator", operator);
		params.put("operatorName", operatorName);
		params.put("brand", brand);
		params.put("model", model);
		params.put("release", release);
		params.put("sdkVersion", String.valueOf(sdkVersion));
		params.put("versionCode", String.valueOf(versionCode));
		params.put("haspin", haspin);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}


	/**
	 * Sending Request FOR sdp url
	 *
	 */
	public static String requestForSDP(String msisdn, String pack) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.CHECK_SDP;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("d", pack);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending update request for SDP sub status
	 *
	 */
	public static String updateSubStatus(String username, String serviceid,
										 String chargestatus, String referenceid) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + username + ")");
		String serverUrl = HTTPGateway.SUB_STATUS_CHECK;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", username);
		params.put("serviceid", serviceid);
		params.put("isChargeSuccess", chargestatus);
		params.put("referenceId", referenceid);


		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Logout Request to Server
	 *
	 */
	public static String requestForLogout(final Context context, String msisdn,
										  String password, String deviceId, String imei, String imsi,
										  String softwareVersion, String simSerialNumber, String operator,
										  String operatorName, String brand, String model, String release,
										  int sdkVersion, int versionCode, String haspin, String requesttype) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.LOGOUT_REQ;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("password", password);
		params.put("deviceId", deviceId);
		params.put("imei", imei);
		params.put("imsi", imsi);
		params.put("softwareVersion", softwareVersion);
		params.put("simSerialNumber", simSerialNumber);
		params.put("operator", operator);
		params.put("operatorName", operatorName);
		params.put("brand", brand);
		params.put("model", model);
		params.put("release", release);
		params.put("sdkVersion", String.valueOf(sdkVersion));
		params.put("versionCode", String.valueOf(versionCode));
		params.put("haspin", haspin);
		params.put("changedevice", requesttype);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {

			Log.d(TAG, "Attempt #" + i + " to register");

			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending SignUp Request to Server
	 *
	 */
	public static String requestForSignUp(final Context context, String msisdn,
										  String deviceId, String imsi, String imei, String softwareVersion,
										  String simSerialNumber, String operator, String operatorName,
										  String brand, String model, String release, int sdkVersion,
										  int versionCode) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.SIGNUP;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("deviceId", deviceId);
		params.put("imsi", imsi);
		params.put("imei", imei);
		params.put("softwareVersion", softwareVersion);
		params.put("simSerialNumber", simSerialNumber);
		params.put("operator", operator);
		params.put("operatorName", operatorName);
		params.put("brand", brand);
		params.put("model", model);
		params.put("release", release);
		params.put("sdkVersion", String.valueOf(sdkVersion));
		params.put("versionCode", String.valueOf(versionCode));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Forgot Password Request to Server
	 *
	 */
	public static String requestForPassword(final Context context,
											String msisdn, String deviceId, String imsi, String imei,
											String softwareVersion, String simSerialNumber, String operator,
											String operatorName, String brand, String model, String release,
											int sdkVersion, int versionCode) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.SIGNUP;
		Map<String, String> params = new HashMap<String, String>();
		params.put("forget", "forget");
		params.put("msisdn", msisdn);
		params.put("deviceId", deviceId);
		params.put("imsi", imsi);
		params.put("imei", imei);
		params.put("softwareVersion", softwareVersion);
		params.put("simSerialNumber", simSerialNumber);
		params.put("operator", operator);
		params.put("operatorName", operatorName);
		params.put("brand", brand);
		params.put("model", model);
		params.put("release", release);
		params.put("sdkVersion", String.valueOf(sdkVersion));
		params.put("versionCode", String.valueOf(versionCode));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending Post Play Time to server
	 *
	 */

	public static void registerPostPlayTime(final Context context, int time,
											String contentid, String msisdn, int playtime) {
		Log.i(TAG, "saving id (contentId = " + contentid + ")");
		String serverUrl = HTTPGateway.POST_PLAY_TIME;
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", String.valueOf(time));
		params.put("contentid", contentid);
		params.put("msisdn", msisdn);
		params.put("playtime", String.valueOf(playtime));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				post(serverUrl, params);

				return;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

	}

	/**
	 * Posting user Comment on content
	 *
	 */
	public static void postComment(final Context context, String contentid,
								   String msisdn, String comment) {

		Log.i(TAG, "saving id (contentId = " + contentid + ")");
		String serverUrl = HTTPGateway.POST_COMMENT;
		Map<String, String> params = new HashMap<String, String>();
		params.put("myval", contentid);
		params.put("msisdn", msisdn);
		params.put("comment", comment);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				post(serverUrl, params);

				return;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

	}

	/**
	 * Posting user Feedback
	 *
	 */
	public static void postFeedBack(String msisdn, String comment) {
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.POST_FEEDBACK;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("comment", comment);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				post(serverUrl, params);

				return;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

	}

	/**
	 * Posting User Consent
	 *
	 */
	public static String postConsent(final Context context, String msisdn,
									 String url, String consent, String deviceId) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = url;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);
		params.put("consent", consent);
		params.put("deviceid", deviceId);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

		return val;

	}

	/**
	 * Posting user Comment on content
	 *
	 */
	public static String postRating(final Context context, String contentid,
									String msisdn, String rating, String deviceId) {

		String val = "";
		Log.i(TAG, "saving id (contentId = " + contentid + ")");
		String serverUrl = HTTPGateway.POST_RATING;
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentid", contentid);
		params.put("msisdn", msisdn);
		params.put("rating", rating);
		params.put("deviceid", deviceId);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

		return val;

	}

	/**
	 * Sending request for App Info
	 *
	 */
	public static String checkAppInfo(final Context context, String appinfo,
									  String msisdn) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.APPINFO;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appinfo", appinfo);
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}

		return val;

	}

	/**
	 * Sending request for home data
	 */

	public static JSONArray requestForHomeContent(String msisdn) {

		JSONArray val = null;
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.HOME_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post4(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending request for search result
	 *
	 */
	public static JSONObject checkAppSearch(String query, String msisdn, int page) {

		JSONObject val = null;
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.SEARCH;
		Map<String, String> params = new HashMap<String, String>();
		params.put("s", query);
		params.put("msisdn", msisdn);
		params.put("page", String.valueOf(page));

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post3(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending request for single detail page
	 *
	 */
	public static String checkDetailInfo(final Context context,
										 String contentid, String msisdn, String token) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.VIDEO_DETAILS;
		Map<String, String> params = new HashMap<String, String>();
		params.put("cc", contentid);
		params.put("msisdn", msisdn);
		params.put("token", token);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending request for continue watching
	 *
	 */
	public static JSONObject requestForHomeData() {

		JSONObject val = null;
		String serverUrl = "https://robibanglabeats.com/v1/bangladhol_json_app_home2.php";

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post3(serverUrl, null);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending request for continue watching
	 *
	 */
	public static JSONObject requestForRecentPlayed(String msisdn) {

		JSONObject val = null;
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.RECENTLY_PLAYED;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post3(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Sending request for My Account Deatils
	 *
	 */
	public static String requestForMyAccount(String msisdn) {

		String val = "";
		Log.i(TAG, "saving (msisdn = " + msisdn + ")");
		String serverUrl = HTTPGateway.MY_ACCOUNT;
		Map<String, String> params = new HashMap<String, String>();
		params.put("msisdn", msisdn);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {

				val = post2(serverUrl, params);

				return val;

			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return val;

	}

	/**
	 * Register this account/device pair within the server.
	 *
	 */
	public static String register(final Context context, final String msisdn, final String fireBaseId) {

		String val = "";
		Log.i(TAG, "registering device (regId = " + fireBaseId + ")");
		String serverUrl = HTTPGateway.FCM_SERVER_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("src", "android");
		params.put("msisdn", msisdn);
		params.put("gcmid", fireBaseId);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register on our server
		// As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			try {
				//displayMessage(context, context.getString(
				//R.string.server_registering, i, MAX_ATTEMPTS));
				val = post2(serverUrl, params);
				//GCMRegistrar.setRegisteredOnServer(context, true);
				//String message = context.getString(R.string.server_registered);
				//displayMessage(context, message);
				return val;
			} catch (IOException e) {
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i + ":" + e);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return null;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		//String message = context.getString(R.string.server_register_error,
		//MAX_ATTEMPTS);
		//displayMessage(context, message);
		return val;
	}

	/**
	 * Issue a POST request to the server.
	 *
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 *
	 * @throws IOException
	 *             propagated from POST.
	 */
	private static void post(String endpoint, Map<String, String> params)
			throws IOException {

		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private static String post2(String endpoint, Map<String, String> params)
			throws IOException {

		String val = "";
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			} else {

				BufferedReader reader = null;
				InputStream stream = null;
				StringBuffer output = new StringBuffer("");

				stream = conn.getInputStream();
				//Map<String, List<String>> map = conn.getHeaderFields();
				reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
				String line = "";
				while ((line = reader.readLine()) != null)
					output.append(line + "\n");
				val = output.toString();
				return val;

			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private static JSONObject post3(String endpoint, Map<String, String> params)
			throws IOException {

		String val = "";

		JSONObject jsonObject = null;
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			} else {

				BufferedReader reader = null;
				InputStream stream = null;
				StringBuffer output = new StringBuffer("");

				stream = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(stream,
						"UTF-8"), 8);
				String line = "";
				while ((line = reader.readLine()) != null)
					output.append(line + "\n");
				val = output.toString();
				jsonObject = new JSONObject(val);
				return jsonObject;

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return jsonObject;
	}

	private static JSONArray post4(String endpoint, Map<String, String> params)
			throws IOException {

		String val = "";

		JSONArray jsonArray = null;
		URL url;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			} else {

				BufferedReader reader = null;
				InputStream stream = null;
				StringBuffer output = new StringBuffer("");

				stream = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(stream,
						"UTF-8"), 8);
				String line = "";
				while ((line = reader.readLine()) != null)
					output.append(line + "\n");
				val = output.toString();
				jsonArray = new JSONArray(val);
				return jsonArray;

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return jsonArray;
	}
}
