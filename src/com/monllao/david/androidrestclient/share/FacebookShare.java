package com.monllao.david.androidrestclient.share;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.Facebook;
import com.monllao.david.androidrestclient.AndroidRestClientActivity;

public class FacebookShare extends Facebook {

	private static final String APP_ID = "YOUR_APP_ID";
	
	/**
	 * Inits the facebook integration and sets the access token 
	 * @param context
	 * @param access_token
	 * @param expires
	 */
	public FacebookShare(String access_token, long expires) {
		super(FacebookShare.APP_ID);

        if(access_token != null) {
            this.setAccessToken(access_token);
        }
        if(expires != 0) {
            this.setAccessExpires(expires);
        }
	}
	
	
	/**
	 * Executes the share petition
	 * @param message
	 * @return Success?
	 */
	public boolean share(String message) {
		
		// Only the message
        Bundle parameters = new Bundle();
        parameters.putString("type", "link");
        parameters.putString("message", message);
        
        try {
			String feedback = this.request("me/feed", parameters, "POST");
			Log.i(AndroidRestClientActivity.APP_NAME, "Shared to Facebook: " + feedback);
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
        
        return true;
	}

	
	/**
	 * Returns the user wall URI
	 * 
	 * The URI depends on the facebook app availability
	 * @param context
	 * @return
	 */
	public String getUserWallUrl(Context context) {
		
		// If the user has the facebook app installed let's redirect it to the facebook app
		if (isFacebookAppInstalled(context)) {
			return "fb://profile";
		} else {
			return "http://www.facebook.com/profile.php?&sk=wall";
		}
	}
	
	
	/**
	 * Checks if the facebook app for android is installed on the device
	 * @return
	 */
	private boolean isFacebookAppInstalled(Context context) {
		
		
		try{
			ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
		    return true;
		} catch( PackageManager.NameNotFoundException e ){
		    return false;
		}
	}
}
