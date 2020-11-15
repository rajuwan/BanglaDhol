package com.ebs.banglalinkbangladhol.others;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ebs.banglalinkbangladhol.R;

public class AlertDialogManager {
	/**
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * @param status
	 *            - success/failure (used to set icon) - pass null if you don't
	 *            want icon
	 * */
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_DARK).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog
					.setIcon((status) ? R.drawable.no_img : R.drawable.no_img);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
