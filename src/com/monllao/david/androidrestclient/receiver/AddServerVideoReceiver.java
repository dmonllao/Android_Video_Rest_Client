package com.monllao.david.androidrestclient.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.Video;
import com.monllao.david.androidrestclient.service.ShareService;

/**
 * Sets the video data on the share service
 */
public class AddServerVideoReceiver extends BroadcastReceiver {

	
	/**
	 * Fired when received
	 */
	public void onReceive(Context context, Intent intent) {

		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerVideoReceiver");
		
		Video video = (Video) intent.getSerializableExtra("video");
		
		ShareService service = (ShareService)context;
		
		if (video == null) {
			Toast.makeText(context, "Failed to get the video", Toast.LENGTH_LONG).show();
			
		} else {
			service.setVideo(video);
		}
	}

}
