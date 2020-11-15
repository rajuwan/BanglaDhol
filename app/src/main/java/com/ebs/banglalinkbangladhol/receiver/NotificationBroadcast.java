package com.ebs.banglalinkbangladhol.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.ebs.banglalinkbangladhol.service.PlayerService;

public class NotificationBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
			KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
					Intent.EXTRA_KEY_EVENT);
			if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
				return;

			switch (keyEvent.getKeyCode()) {
			case KeyEvent.KEYCODE_HEADSETHOOK:
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				 
				break;
			case KeyEvent.KEYCODE_MEDIA_PLAY:
				break;
			case KeyEvent.KEYCODE_MEDIA_PAUSE:
				break;
			case KeyEvent.KEYCODE_MEDIA_STOP:
				break;
			case KeyEvent.KEYCODE_MEDIA_NEXT:
				Log.d("TAG", "TAG: KEYCODE_MEDIA_NEXT");
			
				break;
			case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
				Log.d("TAG", "TAG: KEYCODE_MEDIA_PREVIOUS");
				
				break;
			}
		} else {
			
			if (intent.getAction().equals(PlayerService.ACTION_PLAY)) {
			
			} else if (intent.getAction().equals(PlayerService.ACTION_PAUSE)) {
				 
			} else if (intent.getAction().equals(PlayerService.ACTION_NEXT)) {
				 
			} else if (intent.getAction().equals(PlayerService.ACTION_STOP)) {
				
				Intent i = new Intent(context, PlayerService.class);
				context.stopService(i);
				//Intent in = new Intent(context, MainActivity.class);
				//in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//context.startActivity(in);
				
			} else if (intent.getAction().equals(PlayerService.ACTION_PREVIOUS)) {
				 
			}
		}
	}

	public String ComponentName() {
		return this.getClass().getName();
	}
}
