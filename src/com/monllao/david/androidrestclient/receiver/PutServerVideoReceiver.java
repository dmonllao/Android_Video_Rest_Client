package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.Video;
import com.monllao.david.androidrestclient.VideoDataActivity;

public class PutServerVideoReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		
		Log.v(AndroidRestClientActivity.APP_NAME, "PutServerVideoReceiver");
		
		Video video = (Video) intent.getSerializableExtra("video");

		VideoDataActivity activity = (VideoDataActivity)context;
		
		if (video == null) {
			activity.showProblem("Failed to update the video");
		
		} else {
			// Return to the activity to reload user
			activity.processShare();
		}
	}

}
