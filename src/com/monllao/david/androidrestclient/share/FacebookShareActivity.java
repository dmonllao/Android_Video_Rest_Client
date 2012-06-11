package com.monllao.david.androidrestclient.share;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.monllao.david.androidrestclient.AndroidRestClientActivity;

/**
 * Manages the facebook sharing options
 */
public class FacebookShareActivity extends Activity {

	private String[] facebookPermissions = {"publish_stream"};
	
	FacebookShare facebook;
    private SharedPreferences prefs;
    TextView textView;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook);
        
        Log.i(AndroidRestClientActivity.APP_NAME, "FacebookShareActivity - onCreate");
        
        textView = (TextView) this.findViewById(R.id.facebook_feedback);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String access_token = prefs.getString("access_token", null);
        long expires = prefs.getLong("access_expires", 0);
        
		facebook = new FacebookShare(access_token, expires);

        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {

            facebook.authorize(this, facebookPermissions, new DialogListener() {

                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                	finishActivity(true);
                }
    
                public void onFacebookError(FacebookError error) {
                	Log.e(AndroidRestClientActivity.APP_NAME, "Facebook error: " + error.getMessage());

                	finishActivity(false);
                }
    
                public void onError(DialogError e) {
                	Log.e(AndroidRestClientActivity.APP_NAME, "Facebook error: " + e.getMessage());

                	finishActivity(false);
                }
    
                public void onCancel() {
                	Log.e(AndroidRestClientActivity.APP_NAME, "Facebook error: User cancellation");
                	
                	finishActivity(false);
                }
            });
            
        // We already have a valid token
        } else {
        	finishActivity(true);
        }
		
    }

	
	/**
	 * Returns the flow to the previous activity
	 * 
	 * .UI feedback
	 * .Broadcast message for the share service
	 * .Return to VideoDataActivity
	 * 
	 * @param result
	 */
	private void finishActivity(boolean result) {

		if (result == false) {
            textView.setText(R.string.facebook_problem);
        } else {
        	textView.setText(R.string.facebook_ok);
        }
    	
    	// Notify that we have the token on shared preferences
    	sendBroadcast(result);
    	
    	if (result == true) {
    		setResult(RESULT_OK);
    	} else {
    		setResult(RESULT_CANCELED);
    	}
    	
    	finish();
	}
	
	
	/**
	 * Sends a broadcast to notice the share service about the availability of the token
	 * @param available
	 */
	private void sendBroadcast(boolean available) {

		Intent facebookIntent = new Intent(AndroidRestClientActivity.ACTION_FACEBOOK);
		facebookIntent.putExtra("available", available);
		sendBroadcast(facebookIntent);
	}
	
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
}
