package com.monllao.david.androidrestclient.share;

import java.io.IOException;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;

public class TwitterShare {

	private static final String APP_ID = "YOUR_APP_ID";
	private static final String SECRET_ID = "YOUR_SECRET_ID";
	
	Twitter twitter;

	public boolean getToken(String twitter_token, String twitter_token_secret) throws NotFoundException, IOException {

		String twitter_app_id = TwitterShare.APP_ID;
		String twitter_secret_id = TwitterShare.SECRET_ID;
		
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

    	String twitter_app_id = TwitterShare.APP_ID;
		String twitter_secret_id = TwitterShare.SECRET_ID;
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(twitter_app_id);
		configurationBuilder.setOAuthConsumerSecret(twitter_secret_id);
		Configuration conf = configurationBuilder.build();
		
		twitter = new TwitterFactory(conf).getInstance();
	}
    
	
	public Twitter getTwitter() {
		return twitter;
	}
	
	
	/**
	 * Executes the share petition
	 * @param message
	 * @return Success?
	 */
	public boolean share(String message) {

		Log.i(AndroidRestClientActivity.APP_NAME, "Twitter sharing");
		
		try {
		    Status status = twitter.updateStatus(message);
		    Log.i(AndroidRestClientActivity.APP_NAME, "Shared to Twitter: " + status.getText());
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	
	public String getUserTimelineUrl() throws TwitterException {

		User user = twitter.verifyCredentials();
		String url = "http://www.twitter.com/" + user.getName();
		Log.i(AndroidRestClientActivity.APP_NAME, "Twitter user URL: " + url);
		
		return url;
	}
}
