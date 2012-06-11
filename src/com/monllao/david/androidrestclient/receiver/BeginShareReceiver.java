package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.service.ShareService;

/**
 * Listener that assigns the video description and the sharing options
 */
public class BeginShareReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {

		Log.v(AndroidRestClientActivity.APP_NAME, "BeginShareReceiver");
		
		String description = intent.getStringExtra("description");
		boolean toFacebook = intent.getBooleanExtra("toFacebook", false);
		boolean toTwitter = intent.getBooleanExtra("toTwitter", false);
		
		ShareService service = (ShareService)context;
		service.setShareOptions(description, toFacebook, toTwitter);
		
	}

}
