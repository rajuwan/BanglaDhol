package com.ebs.banglalinkbangladhol.others;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ebs.banglalinkbangladhol.bean.DeviceItem;

public class DeviceInfo {

	private static TelephonyManager mTelephony;
	
	public static List<DeviceItem> deviceInfoList = new ArrayList<DeviceItem>();

	public static List<DeviceItem> getDeviceInfo(Context context) {
		
		deviceInfoList.clear();

		try {

			mTelephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
				
				DeviceItem di = new DeviceItem();
				
				di.deviceId = Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
				
				di.imsi = "no access"; // IMSI
				di.imei = "no access"; // IMEI
				di.softwareVersion = "no access"; // SOFTWARE
				// VERSION
				di.simSerialNumber = "no access"; // SIM SERIAL
				di.operator = mTelephony.getNetworkOperator(); // OPERATOR ID
				di.operatorName = mTelephony.getNetworkOperatorName(); // OPERATOR
				// NAME

				di.brand = Build.BRAND; // DEVICE BRAND
				di.model = Build.MODEL; // DEVICE MODEL
				di.release = Build.VERSION.RELEASE; // ANDROID VERSION LIKE 4.4.4
				di.sdkVersion = Build.VERSION.SDK_INT; // API VERSION LIKE 19
				di.versionCode = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0).versionCode;
				
				deviceInfoList.add(di);

			} else {
				
				DeviceItem di = new DeviceItem();

				if (mTelephony.getDeviceId() != null) {
					di.deviceId = mTelephony.getDeviceId();
				} else {
					di.deviceId = Secure.getString(context.getContentResolver(),
							Secure.ANDROID_ID);
				}

				di.imsi = mTelephony.getSubscriberId(); // IMSI
				di.imei = mTelephony.getDeviceId(); // IMEI
				di.softwareVersion = mTelephony.getDeviceSoftwareVersion(); // SOFTWARE
																			// VERSION
				di.simSerialNumber = mTelephony.getSimSerialNumber(); // SIM SERIAL
				di.operator = mTelephony.getNetworkOperator(); // OPERATOR ID
				di.operatorName = mTelephony.getNetworkOperatorName(); // OPERATOR
																	// NAME

				di.brand = Build.BRAND; // DEVICE BRAND
				di.model = Build.MODEL; // DEVICE MODEL
				di.release = Build.VERSION.RELEASE; // ANDROID VERSION LIKE 4.4.4
				di.sdkVersion = Build.VERSION.SDK_INT; // API VERSION LIKE 19
				di.versionCode = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0).versionCode;
				
				deviceInfoList.add(di);

			}

		} catch (Exception ex) {
			
			Log.d("TAG", "Error while retreving deviceInfo ");
			return null;

		}
		
		return deviceInfoList;

	}

}
