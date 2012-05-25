package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
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
	public static String ACTION_ADDVIDEO = "event-addvideo";
	public static String ACTION_PUTVIDEO = "event-putvideo";
	public static final int ACTIVITY_VIDEODATA = 1;
	public static final int ACTIVITY_SHARE = 2;
	public static int VIDEO_SECS = 10;
	
	/**
	 * Screen blocker
	 */
	PowerManager.WakeLock wl;
	
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
        
        Log.i(AndroidRestClientActivity.APP_NAME, "-------------------------------------");
        Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.oncreate");
        
        try {
        	
        	PropertiesManager.init(getApplicationContext());
        	
        	// Registering the receivers
	        IntentFilter getuserfilter = new IntentFilter(AndroidRestClientActivity.ACTION_GETUSER);
	        getUserReceiver = new GetServerUserReceiver();
	        registerReceiver(getUserReceiver, getuserfilter);

	        IntentFilter adduserfilter = new IntentFilter(AndroidRestClientActivity.ACTION_ADDUSER);
	        addUserReceiver = new AddServerUserReceiver();
	        registerReceiver(addUserReceiver, adduserfilter);

	        // Setting up the application user 
	        user = new User(this);
	    	
	    // Global "set user" catcher
        } catch (Exception e) {
        	String text = "Failed to set the user";
        	Log.e(AndroidRestClientActivity.APP_NAME, text);
        	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }

    }

    
    public void onResume() {
    	super.onResume();
    	
    	Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.onresume");

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();

		// Fill the layouts with the video recorder, we don't wait for 
		// user server response, VideoRecorder will manage it
		videoRecorder = new VideoRecorder(this);
		
		// If we already have the user assign it to the video recorder
		if (user != null) {
			videoRecorder.setUser(user);
		}
    }
    
    
    public void onPause() {
    	super.onPause();

    	Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.onpause");

    	// Free the screen blocker
    	wl.release();
    	
    	// Release camera and/or stop recording
        if (videoRecorder != null) {
        	videoRecorder.release();
        	videoRecorder = null;
        }
    }
    
    
    public void onDestroy() {
    	super.onDestroy();
    	
    	Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.ondestroy");
    	
    	// Unregistering the broadcast receivers
    	unregisterReceiver(getUserReceiver);
    	unregisterReceiver(addUserReceiver);
    }
    
    
    /**
     * If the videodata activity is cancelled finish the activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
	        case RESULT_CANCELED:
	            setResult(RESULT_CANCELED);
	            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * Sets the user who are recording
     * 
     * Refreshes the application user data with the  
     * server data and loads the activity context
     * 
     * @param user
     */
    public void processServerUser(User user) {
    	
    	this.user = user;
    	Log.i(AndroidRestClientActivity.APP_NAME, "processServerUser: " + this.user.getEmail());
    	
    	// If the application is paused don't set the user
    	if (videoRecorder != null) {
    		
    		// Note that we received the server user
    		videoRecorder.setUser(this.user);
    	}
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