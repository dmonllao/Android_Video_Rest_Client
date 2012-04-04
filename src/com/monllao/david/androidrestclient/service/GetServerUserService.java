package com.monllao.david.androidrestclient.service;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.receiver.GetServerUserReceiver;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class GetServerUserService extends IntentService {

	public GetServerUserService() {
		super("GetServerUserService");
	}

	protected void onHandleIntent(Intent intent) {
		
		String email = intent.getStringExtra("email");
		String pwd = intent.getStringExtra("pwd");
		
		String returned = email + " & " + pwd;
		Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserService: " + returned);
		
		// Give feedback to the activity
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(Intent.ACTION_SYNC);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("result", returned);
		sendBroadcast(broadcastIntent);
	}

}
