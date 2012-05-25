package com.monllao.david.androidrestclient.shares;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.ShareActivity;

public class FacebookShare implements Shareable {

	private static final String FACEBOOK_APP_ID = "265738966836585";
	private String[] facebookPermissions = {"publish_stream"};
	Facebook facebook;
	
	/**
	 * We will store the valid tokens
	 */
	SharedPreferences prefs;
	Activity activity;
	
	public FacebookShare(Activity activity) {
		this.activity = activity;

        prefs = activity.getPreferences(activity.MODE_PRIVATE);
        
    	facebook = new Facebook(FacebookShare.FACEBOOK_APP_ID);
    	
    	// Getting access token
    	String access_token = prefs.getString("access_token", null);
        long expires = prefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }

        // Show the facebook authorization dialog
        if (!facebook.isSessionValid()) {
	    	facebook.authorize(activity, facebookPermissions, 
	    		new DialogListener() {
	                public void onComplete(Bundle values) {
	                	SharedPreferences.Editor editor = prefs.edit();
	                    editor.putString("access_token", facebook.getAccessToken());
	                    editor.putLong("access_expires", facebook.getAccessExpires());
	                    editor.commit();
	                }
	
	                public void onFacebookError(FacebookError error) {}
	
	                public void onError(DialogError e) {}
	
	                public void onCancel() {}
	        });
        }
	}
	
	public String share(String message) {
		
		String feedback;
		
		// Only the message
        Bundle parameters = new Bundle();
        parameters.putString("type", "link");
        parameters.putString("message", message);
        try {
			feedback = facebook.request("me/feed", parameters, "POST");
			Log.e(AndroidRestClientActivity.APP_NAME, "Published into facebook");
		} catch (MalformedURLException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Malformed URL when sharing in facebook");
			feedback = "Malformed URL when sharing in facebook";
		} catch (IOException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "IO Exception when sharing in facebook");
			feedback = "IO Exception when sharing in facebook";
		}
        
        return feedback;
	}
	

    protected void facebookShare() {
    	

        
    }

    public void end(int requestCode, int resultCode, Intent data) {
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
