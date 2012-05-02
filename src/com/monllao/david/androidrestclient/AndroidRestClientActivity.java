package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
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
	public static String ACTION_ADDVIDEO = "event-addvideo";
	public static String ACTION_PUTVIDEO = "event-putvideo";
	public static int ACTIVITY_VIDEODATA = 1;
	
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

    	// Output the camera preview
    	videoRecorder = new VideoRecorder(this);
    	videoRecorder.fillLayout();       
    }

    public void onStart(Bundle savedInstanceState) {
    	super.onStart();
Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.onstart");
    }
    
    public void onResume(Bundle savedInstanceState) {
    	super.onResume();
Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.onresume");
    }
    
    
    public void onPause() {
    	super.onPause();
    	
Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.onpause");
    	
    	// Release camera and/or stop recording
        if (videoRecorder != null) {
        	videoRecorder.release();
        }
    }
    
    
    public void onStop() {
    	super.onStop();
Log.i(AndroidRestClientActivity.APP_NAME, "AndroidRestClientActivity.onstop");
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
     * Sets the add user and initialises the camera
     * 
     * Refreshes the application user data with the  
     * server data and loads the activity context
     * 
     * @param user
     */
    public void processServerUser(User user) {
    	
    	this.user = user;
    	Log.i(AndroidRestClientActivity.APP_NAME, "processServerUser: " + this.user.getEmail());
    	
    	// Note that we received the server user
    	videoRecorder.setUser(this.user);
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