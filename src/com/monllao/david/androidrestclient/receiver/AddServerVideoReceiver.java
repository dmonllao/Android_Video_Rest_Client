package com.monllao.david.androidrestclient.receiver;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.User;
import com.monllao.david.androidrestclient.Video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class AddServerVideoReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {

		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerVideoReceiver");
		
		Video video = (Video) intent.getSerializableExtra("video");
		
		AndroidRestClientActivity activity = (AndroidRestClientActivity)context;
		
		if (video == null) {
			activity.showProblem("Failed to get the video");
			
		} else {
			// Return to the activity
			activity.processServerVideo(video);
		}
	}

}
