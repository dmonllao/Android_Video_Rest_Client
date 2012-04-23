package com.monllao.david.androidrestclient.receiver;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.User;

public class GetServerUserReceiver extends BroadcastReceiver {

	/**
	 * Receives the Service data
	 */
	public void onReceive(Context context, Intent intent) {

		User user = (User) intent.getSerializableExtra("user");
		
		Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserReceiver");
		
		// Return to the activity to reload user
		AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
		try {
			activity.processServerUser(user);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
