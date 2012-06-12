package com.monllao.david.androidrestclient.receiver;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.service.ShareService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Informs the share service that the user has stopped the video share action
 */
public class EndShareReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {	
	
		Log.v(AndroidRestClientActivity.APP_NAME, "EndShareReceiver");
		
		ShareService service = (ShareService) context;
		
		service.endShare();
	}

}
