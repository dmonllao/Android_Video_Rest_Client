package com.monllao.david.androidrestclient.receiver;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.service.ShareService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Notifies the twitter token is available in the SharedPreferences
 */
public class TwitterTokenReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		
		Log.v(AndroidRestClientActivity.APP_NAME, "TwitterTokenReceiver");
		
		ShareService service = (ShareService) context;

		// Is the token available?
		boolean available = intent.getBooleanExtra("available", false);
		service.twitterTokenAvailable(available);
	}

}
