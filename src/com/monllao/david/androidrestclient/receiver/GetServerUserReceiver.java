package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.User;

/**
 * Receives the Service data and returns to the main activity
 */
public class GetServerUserReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {

		User user = (User) intent.getSerializableExtra("user");
		
		Log.v(AndroidRestClientActivity.APP_NAME, "GetServerUserReceiver");
		
		// Return to the activity to reload user
		try {
			AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
			activity.processServerUser(user);
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserReceiver - Failed to set the user");
		}
	}

}
