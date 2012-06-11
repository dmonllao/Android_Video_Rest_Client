package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.monllao.david.androidrestclient.share.FacebookShareActivity;
import com.monllao.david.androidrestclient.share.TwitterShareActivity;

/**
 * Activity which manages the video sharing options
 */
public class ShareActivity extends Activity {
	

	Video video;
	
	private String message;
	
	private boolean toTwitter = false;
	private boolean toFacebook = false;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing);

        Log.i(AndroidRestClientActivity.APP_NAME, "ShareActivity onCreate");
        
        video = (Video) getIntent().getSerializableExtra("video");
        
        message = video.getName() + " " + video.getUrl();
        
        // Share it on facebook?
    	toFacebook = getIntent().getBooleanExtra("facebook", false);

        // Share it on Twitter?
    	toTwitter = getIntent().getBooleanExtra("twitter", false);

        // Share to Facebook
        if (toFacebook == true) {
        	toFacebook = false;
        	facebookShare();
        	
    	// Share to Twitter
        } else if (toFacebook == false && toTwitter == true) {
        	toTwitter = false;
        	twitterShare(); 	
        	
        // Close
        } else if (toFacebook == false && toTwitter == false) {
        	finished();
        }
    }
    
    

    
    
    /**
     * Displays the link to the video
     */
    private void showLink(Video video) {
    	
    	TextView linkView = (TextView) findViewById(R.id.link);
    	
    	linkView.setText(video.getName() + " " + video.getUrl());
    	linkView.setVisibility(View.VISIBLE);
    }

	
    /**
     * Share activities result listener
     * 
     * Depending on the result activity show feedback 
     * or calls the other shared network method 
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(AndroidRestClientActivity.APP_NAME, "ShareActivity onActivityResult " + requestCode + "--" + resultCode);
        
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
            
            case ShareActivity.ACTIVITY_SHARE_FACEBOOK:

            	if (toTwitter) {
            		toTwitter = false;
            		twitterShare();
            	}
                break;
                
            case ShareActivity.ACTIVITY_SHARE_TWITTER:

            	if (toFacebook) {
            		toFacebook = false;
            		facebookShare();
            	}
                break;
            }
            
        // Somewhing went wrong
        } else {
        	switch (requestCode) {
        	
    		case ShareActivity.ACTIVITY_SHARE_FACEBOOK:
    			Toast.makeText(this, getString(R.string.facebook_problem), Toast.LENGTH_SHORT);
    			break;
    			
    		case ShareActivity.ACTIVITY_SHARE_TWITTER:
    			Toast.makeText(this, getString(R.string.twitter_problem), Toast.LENGTH_SHORT);
    			break;
        	}
        	// TODO Display an error
        }
        
        if (!toFacebook && !toTwitter) {
        	finished();
        }
    }
    
    
    private void finished() {

    	Log.i(AndroidRestClientActivity.APP_NAME, "Sharing finished");
    	
    	TextView sharingText = (TextView) findViewById(R.id.share_text);
    	sharingText.setText(R.string.sharing_result);
    	
        showLink(video);
    }

}
