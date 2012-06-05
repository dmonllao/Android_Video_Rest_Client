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

	
	/**
	 * Fired when received
	 */
	public void onReceive(Context context, Intent intent) {

		Log.v(AndroidRestClientActivity.APP_NAME, "GetServerUserReceiver");
		
		User user = (User) intent.getSerializableExtra("user");

		AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
		
		if (user == null) {
			activity.showProblem("Failed to set the user");
		
		} else {
			// Return to the activity to reload user
			activity.processServerUser(user);
		}
		
	}

}
