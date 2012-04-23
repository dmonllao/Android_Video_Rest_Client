package com.monllao.david.androidrestclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.monllao.david.androidrestclient.receiver.AddServerUserReceiver;
import com.monllao.david.androidrestclient.receiver.GetServerUserReceiver;

public class AndroidRestClientActivity extends Activity {
    
	public static String APP_NAME = "AndroidRestClient";
	
	public static String ACTION_GETUSER = "event-getuser";
	public static String ACTION_ADDUSER = "event-adduser";
	
	/**
	 * The application user
	 */
	private User user;
	
	private GetServerUserReceiver getUserReceiver;
	private AddServerUserReceiver addUserReceiver;
	
	/**
	 * Initializes and gets the application user
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
        	
        	PropertiesManager.init(getApplicationContext());
        	
        	// Registering the receivers
	        IntentFilter getfilter = new IntentFilter(AndroidRestClientActivity.ACTION_GETUSER);
	        getUserReceiver = new GetServerUserReceiver();
	        registerReceiver(getUserReceiver, getfilter);

	        IntentFilter addfilter = new IntentFilter(AndroidRestClientActivity.ACTION_ADDUSER);
	        addUserReceiver = new AddServerUserReceiver();
	        registerReceiver(addUserReceiver, addfilter);
	        
	        // Setting up the app user 
	        user = new User(this);
	        
	    // Global catcher
        } catch (Exception e) {
        	
        	Log.e(AndroidRestClientActivity.APP_NAME, "Error" + e.toString());
			
			Toast.makeText(this, e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_LONG)
				.show();
        }
       
    }

    public void onStart(Bundle savedInstanceState) {
    	super.onStart();

    }
    
    public void onResume(Bundle savedInstanceState) {
    	super.onResume();
    }
    
    
    public void onPause() {
    	super.onPause();
    }
    
    
    public void onStop() {
    	super.onStop();
    }
    
    
    public void onDestroy() {
    	super.onDestroy();

    	// Unregistering the broadcast receivers
    	unregisterReceiver(getUserReceiver);
    	unregisterReceiver(addUserReceiver);
    }
    
    
    /**
     * Processes the GetServerUser service return
     * 
     * Refreshes the application user data with the  
     * server data and loads the activity context
     * 
     * @param serverUser
     */
    public void processServerUser(User user) throws NotFoundException, IOException {
    	this.user = user;
    	Log.e(AndroidRestClientActivity.APP_NAME, "processServerUser: " + this.user.getEmail());
    }
    
}