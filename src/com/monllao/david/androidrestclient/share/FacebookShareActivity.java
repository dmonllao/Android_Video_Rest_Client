package com.monllao.david.androidrestclient.share;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.monllao.david.androidrestclient.AndroidRestClientActivity;

/**
 * Manages the facebook sharing options
 */
public class FacebookShareActivity extends Activity {

	private static final String APP_ID = "YOURAPPID";
	
	private String[] facebookPermissions = {"publish_stream"};
    Facebook facebook;
    private SharedPreferences prefs;
    TextView textView;
    
    String message;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook);

        textView = (TextView) this.findViewById(R.id.facebook_feedback);
        
		facebook = new Facebook(FacebookShareActivity.APP_ID);
        
        message = getIntent().getStringExtra("message");
        
        prefs = getPreferences(MODE_PRIVATE);
        String access_token = prefs.getString("access_token", null);
        long expires = prefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {

            facebook.authorize(this, facebookPermissions, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                    if (share() == false) {
                        textView.setText(R.string.facebook_problem);
                    } else {
                    	textView.setText(R.string.facebook_ok);
                    }

                	setResult(RESULT_OK);
                	finish();
                }
    
                public void onFacebookError(FacebookError error) {
                	Log.e(AndroidRestClientActivity.APP_NAME, "Facebook error: " + error.getMessage());
                }
    
                public void onError(DialogError e) {
                	Log.e(AndroidRestClientActivity.APP_NAME, "Facebook error: " + e.getMessage());
                }
    
                public void onCancel() {
                	Log.e(AndroidRestClientActivity.APP_NAME, "Facebook error: User cancellation");
                	setResult(RESULT_CANCELED);
                	finish();
                }
            });
        } else {
        	
        	// TODO Unify with the upper code
        	if (share() == false) {
        		if (share() == false) {
                    textView.setText(R.string.facebook_problem);
                } else {
                	textView.setText(R.string.facebook_ok);
                }
        	}
        	setResult(RESULT_OK);
        	finish();
        }
    }

	
	/**
	 * Executes the share petition
	 * @return Success?
	 */
	public boolean share() {
		
		// Only the message
        Bundle parameters = new Bundle();
        parameters.putString("type", "link");
        parameters.putString("message", message);
        
        try {
			String feedback = facebook.request("me/feed", parameters, "POST");
			Log.i(AndroidRestClientActivity.APP_NAME, "Shared to Facebook: " + feedback);
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
        
        return true;
	}

	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
}
