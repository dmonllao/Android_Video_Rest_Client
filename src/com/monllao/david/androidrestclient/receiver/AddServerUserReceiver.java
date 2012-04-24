package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.User;

/**
 * Receives the Service data and returns to the main activity
 */
public class AddServerUserReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {

		User user = (User) intent.getSerializableExtra("user");
		
		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerUserReceiver");
		
		// Stores the generated id into the shared preferences
		SharedPreferences prefs = context.getSharedPreferences(AndroidRestClientActivity.APP_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("userid", user.getId());
		editor.commit();
		
		// Return to the activity to reload user
		try {
			AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
			activity.processServerUser(user);
		}  catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "AddServerUserReceiver - Failed to set the user");
		}
	}

}
