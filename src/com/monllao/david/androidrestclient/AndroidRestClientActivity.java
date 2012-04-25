package com.monllao.david.androidrestclient;

import java.io.IOException;

import android.app.Activity;
import android.content.IntentFilter;
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
	 * Initialises and gets the application user
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
	        
	    // Global "set user" catcher
        } catch (Exception e) {
        	String text = "Failed to set the user";
        	Log.e(AndroidRestClientActivity.APP_NAME, text);
        	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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
     * Processes the server user services
     * 
     * Refreshes the application user data with the  
     * server data and loads the activity context
     * 
     * @param user
     */
    public void processServerUser(User user) throws NotFoundException, IOException {
    	
    	this.user = user;
    	Log.i(AndroidRestClientActivity.APP_NAME, "processServerUser: " + this.user.getEmail());
    	
    	// Iteration 1 Purposes
    	String text = R.string.app_user_set_up + ": " + this.user.getEmail();
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    
}