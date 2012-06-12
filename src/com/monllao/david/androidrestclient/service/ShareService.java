package com.monllao.david.androidrestclient.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.Video;
import com.monllao.david.androidrestclient.receiver.AddServerVideoReceiver;
import com.monllao.david.androidrestclient.receiver.BeginShareReceiver;
import com.monllao.david.androidrestclient.receiver.EndShareReceiver;
import com.monllao.david.androidrestclient.receiver.FacebookTokenReceiver;
import com.monllao.david.androidrestclient.receiver.TwitterTokenReceiver;
import com.monllao.david.androidrestclient.share.FacebookShare;
import com.monllao.david.androidrestclient.share.TwitterShare;


/**
 * Services to manage the share
 * 
 * Catches all the application events to share the 
 * video without a user interaction nor UI (activity) dependency
 */
public class ShareService extends Service {

	// Video data
	private Video video = null;
	private String description = null;
	
	private boolean shareEnded = false;
	private boolean toFacebook = false;
	private boolean toTwitter = false;
	private boolean facebookAvailable = false;
	private boolean twitterAvailable = false;
	

	private AddServerVideoReceiver addVideoReceiver;
	private BeginShareReceiver beginShareReceiver;
	private FacebookTokenReceiver facebookTokenReceiver;
	private TwitterTokenReceiver twitterTokenReceiver;
	private EndShareReceiver endShareReceiver;
	
	private SharedPreferences prefs;
	
	/**
	 * Just to register the receivers
	 */
	public void onCreate() {
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
        // Registering receivers
        IntentFilter addvideofilter = new IntentFilter(AndroidRestClientActivity.ACTION_ADDVIDEO);
        addVideoReceiver = new AddServerVideoReceiver();
        registerReceiver(addVideoReceiver, addvideofilter);

        IntentFilter beginsharefilter = new IntentFilter(AndroidRestClientActivity.ACTION_BEGINSHARE);
        beginShareReceiver = new BeginShareReceiver();
        registerReceiver(beginShareReceiver, beginsharefilter);

        IntentFilter facebookfilter = new IntentFilter(AndroidRestClientActivity.ACTION_FACEBOOK);
        facebookTokenReceiver = new FacebookTokenReceiver();
        registerReceiver(facebookTokenReceiver, facebookfilter);

        IntentFilter twitterfilter = new IntentFilter(AndroidRestClientActivity.ACTION_TWITTER);
        twitterTokenReceiver = new TwitterTokenReceiver();
        registerReceiver(twitterTokenReceiver, twitterfilter);
        
        IntentFilter endsharefilter = new IntentFilter(AndroidRestClientActivity.ACTION_ENDSHARE);
        endShareReceiver = new EndShareReceiver();
        registerReceiver(endShareReceiver, endsharefilter);
	}

	
	/**
	 * Sets the video data
	 * 
	 * It also sends data to the server if 
	 * the description have been received
	 * 
	 * @param url
	 */
	public void setVideo(Video video) {
		this.video = video;

		// Has the share options been received
		if (this.description != null) {
			
			// Send the video to the server
			video.setName(description);
			putVideo();
			
			if (facebookAvailable && toFacebook) {
				shareToFacebook();
			}
			
			if (twitterAvailable && toTwitter) {
				shareToTwitter();
			}

			// If all is done kill the service
			killService();
		}
		
	}
	
	
	/**
	 * Start the share actions as long as the video data is received
	 * @param description
	 * @param toFacebook
	 * @param toTwitter
	 */
	public void setShareOptions(String description, boolean toFacebook, boolean toTwitter) {
		this.description = description;
		
		this.toFacebook = toFacebook;
		this.toTwitter = toTwitter;
		
		if (this.video != null) {
			
			// Updating the video description
			video.setName(description);
			putVideo();
			
			if (facebookAvailable && toFacebook) {
				shareToFacebook();
			}
			
			if (twitterAvailable && toTwitter) {
				shareToTwitter();
			}
		}
	}
	
	
	/**
	 * Inits the service to update the video description on the server 
	 */
	private void putVideo() {

		Intent intent = new Intent(this, PutServerVideoService.class);
    	intent.setAction(AndroidRestClientActivity.ACTION_PUTVIDEO);	    	
    	intent.putExtra("video", video);
    	
    	startService(intent);
	}
	
	
	/**
	 * Facebook token available on shared preferences
	 * @param bool Just to maintain a method call lexic coherence
	 */
	public void facebookTokenAvailable(boolean bool) {
		
		facebookAvailable = bool;
		
		// If the facebook token is not available we can't share it on Facebook
		if (facebookAvailable == false) {
			toFacebook = false;
		}
		
		// Video + description + toFacebook + facebookTokenAvailable = share
		if (this.video != null && this.description != null && toFacebook == true && facebookAvailable == true) {
			shareToFacebook();
		}
		
		killService();
	}
	
	
	/**
	 * Twitter token available on shared preferences
	 * @param bool Just to maintain a method call lexic coherence
	 */
	public void twitterTokenAvailable(boolean bool) {
		
		twitterAvailable = bool;

		// If the twitter token is not available we can't share it on Twitter
		if (twitterAvailable == false) {
			toTwitter = false;
		}

		// Video + description + toTwitter +  twitterTokenAvailable = share
		if (this.video != null && this.description != null && toTwitter == true && twitterAvailable == true) {
			shareToTwitter();
		}
		
		killService();
	}
	
	
	private void shareToFacebook() {
		
		Log.v(AndroidRestClientActivity.APP_NAME, "shareToFacebook");
		
        String access_token = prefs.getString("access_token", null);
        long expires = prefs.getLong("access_expires", 0);
        
		FacebookShare facebook = new FacebookShare(access_token, expires);
		facebook.share(getMessage());
		toFacebook = false;
	}
	
	
	private void shareToTwitter() {

		Log.v(AndroidRestClientActivity.APP_NAME, "shareToTwitter");
		
		// Getting the token		
		String twitter_token = prefs.getString("twitter_token", "");
		String twitter_token_secret = prefs.getString("twitter_token_secret", "");
		
		TwitterShare twitter = new TwitterShare();
		try {
			twitter.getToken(twitter_token, twitter_token_secret);
			twitter.share(getMessage());
		} catch (NotFoundException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Twitter share error: Not found exception, " + e.getMessage());
		} catch (IOException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Twitter share error: IO Exception, " + e.getMessage());
		}
		
		toTwitter = false;
	}
	

    /**
     * Returns the message to share on the social networks
     * @return
     */
    private String getMessage() {
    	return this.video.getName() + " " + this.video.getUrl();
    }
    
    /**
     * The user has returned to the recording video activity
     */
    public void endShare() {
    	shareEnded = true;
    	
    	// killService will check if all has already shared
    	killService();
    }
	
	/**
	 * When both facebook and twitter actions are finished kill the service
	 */
	private void killService() {
		
		// Checks if the share has ended because users can share again on the VideoData activity
		if (toFacebook == false && toTwitter == false && shareEnded == true) {
			Log.i(AndroidRestClientActivity.APP_NAME, "Killing share service");
			stopSelf();
		}
	}
	

	public IBinder onBind(Intent intent) {
    	// We don't provide binding, so return null
    	return null;
	}
	  

    
    public void onDestroy() {
    	super.onDestroy();
    	
    	// Unregistering the broadcast receivers
    	unregisterReceiver(addVideoReceiver);
    	unregisterReceiver(beginShareReceiver);
    	unregisterReceiver(facebookTokenReceiver);
    	unregisterReceiver(twitterTokenReceiver);
    	unregisterReceiver(endShareReceiver);
    }
}
