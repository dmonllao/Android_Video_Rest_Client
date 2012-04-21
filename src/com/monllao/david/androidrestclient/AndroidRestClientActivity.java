package com.monllao.david.androidrestclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.monllao.david.androidrestclient.receiver.GetServerUserReceiver;
import com.monllao.david.androidrestclient.service.GetServerUserService;

public class AndroidRestClientActivity extends Activity {
    
	public static String APP_NAME = "AndroidRestClient";
	protected String serverHost;
	
	private GetServerUserReceiver receiver;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // Registering the receiver
        receiver = new GetServerUserReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SYNC);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
        
        // Setting the app user 
        User user = new User(this);
        
        // Calling the server to create/retrieve the user
        Intent serverUser = new Intent(this, GetServerUserService.class);
        serverUser.putExtra("serverHost", serverHost);
        serverUser.putExtra("email", user.getEmail());
        serverUser.putExtra("pwd", user.getPwd());
        startService(serverUser);
    }

    
    public void onDestroy() {
    	super.onDestroy();

    	// Unregistering the broadcastreceiver
    	unregisterReceiver(receiver);
    }
    
    
    /**
     * Processes the GetServerUser service return
     * @param result
     */
    public void processGetServerUser(String result) {
    	Log.e(AndroidRestClientActivity.APP_NAME, "processGetUser: " + result);
    }
    
    
    
}