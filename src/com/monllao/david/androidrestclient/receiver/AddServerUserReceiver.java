package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.User;

/**
 * Receives the Service data and returns to the main activity
 */
public class AddServerUserReceiver extends BroadcastReceiver {

	
	/**
	 * Fired when received
	 */
	public void onReceive(Context context, Intent intent) {

		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerUserReceiver");
		
		User user = (User) intent.getSerializableExtra("user");
		
		AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
		
		if (user == null) {
			activity.showProblem("Failed to set the user");
			
		} else {

			// Stores the generated id into the shared preferences
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("userid", user.getId());
			editor.commit();
		
			// Return to the activity
			activity.processServerUser(user);
		}
		
	}

}
