package com.monllao.david.androidrestclient.receiver;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GetServerUserReceiver extends BroadcastReceiver {

	/**
	 * Receives the Service data
	 */
	public void onReceive(Context context, Intent intent) {
		String result = intent.getStringExtra("result");
		
		AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
		activity.processGetServerUser(result);
	}

}
