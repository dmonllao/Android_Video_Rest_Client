package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.Video;
import com.monllao.david.androidrestclient.VideoDataActivity;


public class AddServerVideoReceiver extends BroadcastReceiver {

	
	/**
	 * Fired when received
	 */
	public void onReceive(Context context, Intent intent) {

		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerVideoReceiver");
		
		Video video = (Video) intent.getSerializableExtra("video");
		
		VideoDataActivity activity = (VideoDataActivity)context;
		
		if (video == null) {
			activity.showProblem("Failed to get the video");
			
		} else {
			// Return to the activity
			activity.processServerVideo(video);
		}
	}

}
