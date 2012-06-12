package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.Video;


/**
 * Initiates the share activity
 */
public class PutServerVideoReceiver extends BroadcastReceiver {

	
	/**
	 * Fired when received
	 */
	public void onReceive(Context context, Intent intent) {
		
		Log.v(AndroidRestClientActivity.APP_NAME, "PutServerVideoReceiver");
		
		Video video = (Video) intent.getSerializableExtra("video");
		
		// Just to send feedback
		if (video == null) {
			// Commented as it gives poor info
			Log.e(AndroidRestClientActivity.APP_NAME, "Failed to update the video description");
//			Toast.makeText(context, "Failed to update the video description", Toast.LENGTH_SHORT).show();
		}
	}

}
