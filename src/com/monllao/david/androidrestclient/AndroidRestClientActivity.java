package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.monllao.david.androidrestclient.receiver.GetServerUserReceiver;
import com.monllao.david.androidrestclient.service.GetServerUserService;

public class AndroidRestClientActivity extends Activity {
    
	public static String APP_NAME = "AndroidRestClient";
	private GetServerUserReceiver receiver;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Setting the app user 
        User user = new User(this);
        
        // Registering the receiver
        receiver = new GetServerUserReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SYNC);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
        
        // Calling the server to create/retrieve the user
        Intent serverUser = new Intent(this, GetServerUserService.class);
        serverUser.putExtra("email", user.getEmail());
        serverUser.putExtra("pwd", user.getPwd());
        startService(serverUser);
    }
    
    /**
     * Processes the GetServerUser service return
     * @param result
     */
    public void processGetServerUser(String result) {
    	Log.e(AndroidRestClientActivity.APP_NAME, "processGetUser: " + result);
    }
    
    
    /**
     * Called when the app stops
     */
    public void onPause() {
    	super.onPause();
    	
    	// Unregistering the broadcastreceiver
    	unregisterReceiver(receiver);
    }
    
}