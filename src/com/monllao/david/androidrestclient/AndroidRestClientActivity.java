package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.monllao.david.androidrestclient.camera.VideoRecorder;
import com.monllao.david.androidrestclient.receiver.AddServerUserReceiver;
import com.monllao.david.androidrestclient.receiver.GetServerUserReceiver;
import com.monllao.david.androidrestclient.utils.PropertiesManager;

public class AndroidRestClientActivity extends Activity {
    
	public static String APP_NAME = "AndroidRestClient";
	
	public static String ACTION_GETUSER = "event-getuser";
	public static String ACTION_ADDUSER = "event-adduser";
	
	/**
	 * The application user
	 */
	private User user;
	
	private VideoRecorder videoRecorder;
	
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

    	// Release camera and/or stop recording
    	videoRecorder.release();
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
     * Sets the add user and initalises the camera
     * 
     * Refreshes the application user data with the  
     * server data and loads the activity context
     * 
     * @param user
     */
    public void processServerUser(User user) {
    	
    	this.user = user;
    	Log.i(AndroidRestClientActivity.APP_NAME, "processServerUser: " + this.user.getEmail());
    	
    	// Iteration 1 Purposes
    	String text = getString(R.string.app_user_set_up) + ": " + this.user.getEmail();
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    	
    	// Output the camera preview
    	videoRecorder = new VideoRecorder(this, user);
    	videoRecorder.fillLayout();
    }
    
    
    /**
     * Outputs info about a problem, used to notice system failures
     * @param message
     */
    public void showProblem(String message) {

    	Log.e(AndroidRestClientActivity.APP_NAME, message);
    	Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
}