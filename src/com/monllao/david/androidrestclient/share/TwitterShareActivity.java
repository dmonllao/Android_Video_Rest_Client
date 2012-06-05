package com.monllao.david.androidrestclient.share;

import java.io.IOException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.FeedbackActivity;
import com.monllao.david.androidrestclient.R;
import com.monllao.david.androidrestclient.ShareActivity;


/**
 * Manages the twitter sharing
 */
public class TwitterShareActivity extends Activity implements Shareable {

	private static final String APP_ID = "YOURAPPID";
	private static final String SECRET_ID = "YOURSECRETID";
	
	private Twitter twitter;
	private RequestToken requestToken;

	String message;
	
	SharedPreferences prefs;
	
	TextView textView;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);

        Log.i(AndroidRestClientActivity.APP_NAME, "TwitterShare onCreate");
        
        prefs = getPreferences(MODE_PRIVATE);
        
        textView = (TextView) this.findViewById(R.id.twitter_feedback);
        
        message = getIntent().getStringExtra("message");
        
        boolean loading = false;
        String feedback;
        try {
        	
        	// If we don't get the token is the first access
        	if (getToken()) {
	        	if (share() == false) {
	        		feedback = getString(R.string.twitter_problem);
	        	} else {
	        		feedback = getString(R.string.twitter_ok);
	        	}
	        	
	        // The first twitter authentication
        	} else {
        		feedback = getString(R.string.twitter_loading);
        		loading = true;
        	}
        	
        } catch (Exception e) {
        	Log.e(AndroidRestClientActivity.APP_NAME, "Twitter exception: " + e.getMessage());
        	feedback = getString(R.string.twitter_problem);
        }
        
        Log.i(AndroidRestClientActivity.APP_NAME, "Twitter feedback: " + feedback);

        textView.setText(feedback);

        // Return to ShareActivity
        if (loading == false) {
	    	setResult(RESULT_OK);
	    	finish();
        }
	}
	
	
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	
    	Log.i(AndroidRestClientActivity.APP_NAME, "TwitterShareActivity onNewIntent");
    	    	
        // If the Intent comes from a request to androidrestclient://twittershareactivity is the twitter oauth callback
        Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith("androidrestclient://twittershareactivity")) {
			
			String feedback;
			try {
				
				// Stores the received token into the shared preferences
	        	handleCallback(uri);
	        	
	        	// Obtains the token from the shared preferences
	        	getToken();
	        	
	        	// Process the share to twitter
	        	if (share() == false) {
	        		feedback = getString(R.string.twitter_problem);
	        	} else {
	        		feedback = getString(R.string.twitter_ok);
	        	}
	        	
			} catch (Exception e) {
				Log.e(AndroidRestClientActivity.APP_NAME, "Twitter exception: " + e.getMessage());
				feedback = this.getString(R.string.twitter_problem);
			}

	        Log.i(AndroidRestClientActivity.APP_NAME, "Twitter feedback: " + feedback);	

	        textView.setText(feedback);

	        // Display feedback layout (if we finish the activity we'll return to the api.twitter.com activity)
	    	initFeedbackActivity();
		}

    }
    
    
	public boolean getToken() throws NotFoundException, IOException {

		String twitter_app_id = TwitterShareActivity.APP_ID;
		String twitter_secret_id = TwitterShareActivity.SECRET_ID;

		String twitter_token = prefs.getString("twitter_token", "");
		String twitter_token_secret = prefs.getString("twitter_token_secret", "");
		
		// If there is no stored valid token let's get one
		if (twitter_token == "" || twitter_token_secret == "") {
			getNewToken();
			return false;
		}
		
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		Configuration conf = confbuilder
							.setOAuthConsumerKey(twitter_app_id)
							.setOAuthConsumerSecret(twitter_secret_id)
							.setOAuthAccessToken(twitter_token)
							.setOAuthAccessTokenSecret(twitter_token_secret)
							.build();
		twitter = new TwitterFactory(conf).getInstance();
		
		return true;
	}
	
	
	/**
	 * Will ask the user to allow the app to publish to the wall
	 * @throws NotFoundException
	 * @throws IOException
	 */
    public void getNewToken() throws NotFoundException, IOException {

    	String twitter_app_id = TwitterShareActivity.APP_ID;
		String twitter_secret_id = TwitterShareActivity.SECRET_ID;
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(twitter_app_id);
		configurationBuilder.setOAuthConsumerSecret(twitter_secret_id);
		Configuration conf = configurationBuilder.build();
		
		twitter = new TwitterFactory(conf).getInstance();
		
		try {
			requestToken = twitter.getOAuthRequestToken();
			Toast.makeText(this, getString(R.string.twitter_allowme), Toast.LENGTH_LONG).show();
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
		} catch (TwitterException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Twitter exception: " + e.getMessage());
		}
		
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
	 * Executes the share petition
	 * @return Success?
	 */
	public boolean share() {

		Log.i(AndroidRestClientActivity.APP_NAME, "Twitter sharing");
		
		try {
		    Status status = twitter.updateStatus(message);
		    Log.i(AndroidRestClientActivity.APP_NAME, "Shared to Twitter: " + status.getText());
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	
	private void initFeedbackActivity() {

        Log.i(AndroidRestClientActivity.APP_NAME, "Sending intent to Feedback");
    
    	Intent intent = new Intent(this, FeedbackActivity.class);
    	intent.putExtra("message", message);
        startActivityForResult(intent, ShareActivity.ACTIVITY_FEEDBACK);
	}
}
