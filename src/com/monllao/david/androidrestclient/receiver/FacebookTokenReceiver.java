package com.monllao.david.androidrestclient.receiver;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.service.ShareService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Notifies the facebook token is available in the SharedPreferences
 */
public class FacebookTokenReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		
		Log.v(AndroidRestClientActivity.APP_NAME, "FacebookTokenReceiver");
		
		ShareService service = (ShareService) context;
		
		// Is the token available?
		boolean available = intent.getBooleanExtra("available", false);
		service.facebookTokenAvailable(available);
	}

}
