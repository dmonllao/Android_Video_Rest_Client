package com.monllao.david.androidrestclient.share;

import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.FeedbackActivity;
import com.monllao.david.androidrestclient.R;


/**
 * Manages the twitter sharing
 */
public class TwitterShareActivity extends Activity {

	
	private Twitter twitter;
	private RequestToken requestToken;
	private TwitterShare twitterShare;

	SharedPreferences prefs;
	
	TextView textView;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);

        Log.i(AndroidRestClientActivity.APP_NAME, "TwitterShare onCreate");
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        textView = (TextView) this.findViewById(R.id.twitter_feedback);
        
        twitterShare = new TwitterShare();
        
        boolean loading = false;
        String feedback;
        boolean available;
        try {
        	
        	// If we don't get the token is the first access
        	if (getToken()) {
        		feedback = getString(R.string.twitter_ok);
        		available = true;
	        	
	        // The first twitter authentication
        	} else {
        		feedback = getString(R.string.twitter_loading);
        		loading = true;
        		available = false;
        	}
        	
        } catch (Exception e) {
        	Log.e(AndroidRestClientActivity.APP_NAME, "Twitter exception: " + e.getMessage());
        	feedback = getString(R.string.twitter_problem);
        	available = false;
        }
        
//        Log.i(AndroidRestClientActivity.APP_NAME, "Twitter feedback: " + feedback);

        textView.setText(feedback);

        // Return to VideoDataActivity
        if (loading == false) {
        	Log.i(AndroidRestClientActivity.APP_NAME, "TwitterShare - Finishing activity, loading = false");
        	finishActivity(available);
        }
	}
	
	
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	
    	Log.i(AndroidRestClientActivity.APP_NAME, "TwitterShareActivity onNewIntent");
    	    	
        // If the Intent comes from a request to androidrestclient://twittershareactivity is the twitter oauth callback
        Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith("androidrestclient://twittershareactivity")) {
			
			// To give feedback both to the user and to the share service
			String feedback;
			boolean available;
			
			try {
				
				// Stores the received token into the shared preferences
	        	handleCallback(uri);
	        	
	        	// Obtains the token from the shared preferences
	        	if (getToken() == true) {
	        		available = true;
	        		feedback = getString(R.string.twitter_ok);
	        	} else {
	        		available = false;
	        		feedback = getString(R.string.twitter_problem);
	        	}
	        	
			} catch (Exception e) {
				Log.e(AndroidRestClientActivity.APP_NAME, "Twitter exception: " + e.getMessage());
				feedback = this.getString(R.string.twitter_problem);
				available = false;
			}

//	        Log.i(AndroidRestClientActivity.APP_NAME, "Twitter feedback: " + feedback);	

	        textView.setText(feedback);

	    	sendBroadcast(available);
	    	
	        // Display feedback layout (if we finish the activity we'll return to the api.twitter.com activity)
	    	initFeedbackActivity();
		}

    }
    
    
	public boolean getToken() throws NotFoundException, IOException {

		String twitter_token = prefs.getString("twitter_token", "");
		String twitter_token_secret = prefs.getString("twitter_token_secret", "");
		
		// Adapter to TwitterShare
		boolean result = twitterShare.getToken(twitter_token, twitter_token_secret);
		
		// Get the twitter instance
		twitter = twitterShare.getTwitter();
		
		// If result == false there is no token available, so let's get one 
		if (result == false) {

			try {
				requestToken = twitter.getOAuthRequestToken();
				Toast.makeText(this, getString(R.string.twitter_allowme), Toast.LENGTH_LONG).show();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
			} catch (TwitterException e) {
				Log.e(AndroidRestClientActivity.APP_NAME, "Twitter exception: " + e.getMessage());
			}
		}
		
		return result;
	}

    
	/**
	 * Handle OAuth Callback and store the user token
	 * @throws IOException 
	 * @throws NotFoundException 
	 * @throws TwitterException 
	 */
	public void handleCallback(Uri uri) throws NotFoundException, IOException, TwitterException {
	
		String verifier = uri.getQueryParameter("oauth_verifier");
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
        
        storeToken(accessToken);
	}
	
	
	/**
	 * Stores the user token
	 * @param accessToken
	 */
	private void storeToken(AccessToken accessToken) {

        // Store the accessToken for future reference
    	SharedPreferences.Editor editor = prefs.edit();
        editor.putString("twitter_token", accessToken.getToken());
        editor.putString("twitter_token_secret", accessToken.getTokenSecret());
        editor.commit();
	}
	
	
	/**
	 * Initializes the feedback activity
	 * 
	 * Twitter authentication does not allow return to the previous activity
	 * cause is an HTTP auth, so let's show the feedback activity directly
	 */
	private void initFeedbackActivity() {

        Log.i(AndroidRestClientActivity.APP_NAME, "Sending intent to Feedback");
    
    	Intent intent = new Intent(this, FeedbackActivity.class);
        startActivityForResult(intent, AndroidRestClientActivity.ACTIVITY_FEEDBACK);
	}
	
	
	/**
	 * Ends the current activity returning to the caller 
	 * @param result
	 */
	private void finishActivity(boolean result) {

		if (result == true) {
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);
		}
		
		// Info for the share service
		sendBroadcast(result);
		
    	finish();
	}
	
	
	/**
	 * Sends a broadcast to notice the share service about the availability of the token
	 * @param available
	 */
	private void sendBroadcast(boolean available) {

		Intent facebookIntent = new Intent(AndroidRestClientActivity.ACTION_TWITTER);
		facebookIntent.putExtra("available", available);
		sendBroadcast(facebookIntent);
	}
}
