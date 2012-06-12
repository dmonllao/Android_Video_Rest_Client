package com.monllao.david.androidrestclient.share;

import java.io.IOException;
import java.net.MalformedURLException;

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

}
