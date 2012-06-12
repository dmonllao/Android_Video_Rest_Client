package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.monllao.david.androidrestclient.service.AddServerVideoService;
import com.monllao.david.androidrestclient.service.ShareService;
import com.monllao.david.androidrestclient.share.FacebookShareActivity;
import com.monllao.david.androidrestclient.share.TwitterShareActivity;


/**
 * Activity to select the social networks and to fill the video description
 */
public class VideoDataActivity extends Activity {

	
	// Form elements
	private EditText titleText;
	private ImageButton facebookButton;
	private ImageButton twitterButton;
	private Button confirmButton;

	private boolean toFacebook = false;
	private boolean toTwitter = false;
	
	/**
	 * Sends the video to the server and fills the form
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_data);
        
        // Create the background service which manages the whole app
        Intent intent = new Intent(this, ShareService.class);
        startService(intent);

        // Add the video as soon as possible
        addVideo();
        
        // Form elements
        titleText = (EditText) findViewById(R.id.title);
        confirmButton = (Button) findViewById(R.id.set_video_data);    
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	titleText.setEnabled(false);
            	confirmButton.setEnabled(false);
            	
            	beginShare();
            }

        });
        
        // To allow facebook sharing
        facebookButton = (ImageButton) findViewById(R.id.facebook_image);
        facebookButton.setOnClickListener(new View.OnClickListener(
        		) {
			
			public void onClick(View v) {
				if (toFacebook == false) {
					facebookButton.setImageResource(R.drawable.facebook_enabled);
					toFacebook = true;
				} else {
					facebookButton.setImageResource(R.drawable.facebook_disabled);
					toFacebook = false;
				}
			}
		});
        
        // To allow twitter sharing
        twitterButton = (ImageButton) findViewById(R.id.twitter_image);
        twitterButton.setOnClickListener(new View.OnClickListener(
        		) {
			
			public void onClick(View v) {
				if (toTwitter == false) {
					twitterButton.setImageResource(R.drawable.twitter_enabled);
					toTwitter = true;
				} else {
					twitterButton.setImageResource(R.drawable.twitter_disabled);
					toTwitter = false;
				}
			}
		});
    }
    
    
    /**
     * Starts the service to add the video
     */
    protected void addVideo() {

        Intent intent = new Intent(this, AddServerVideoService.class);
        intent.setAction(AndroidRestClientActivity.ACTION_ADDVIDEO);
    	
        intent.putExtra("outputPath", getIntent().getStringExtra("outputPath"));
        intent.putExtra("user", getIntent().getSerializableExtra("user"));
        
        startService(intent);
    }
    
    
    /**
     * Throws an intent to share to facebook
     */
    private void facebookShare() {

    	Log.i(AndroidRestClientActivity.APP_NAME, "Sending intent to FacebookShare");
    	
    	Intent facebookIntent = new Intent(this, FacebookShareActivity.class);
        startActivityForResult(facebookIntent, AndroidRestClientActivity.ACTIVITY_SHARE_FACEBOOK);
    }
    
    
    /**
     * Throws an intent to share to twitter
     */
    private void twitterShare() {

        Log.i(AndroidRestClientActivity.APP_NAME, "Sending intent to TwitterShare");
    
    	Intent twitterIntent = new Intent(this, TwitterShareActivity.class);
        startActivityForResult(twitterIntent, AndroidRestClientActivity.ACTIVITY_SHARE_TWITTER);
    }
    

    /**
     * Send the broadcast to notify the confirm click and gets tokens
     */
    private void beginShare() {

    	Log.i(AndroidRestClientActivity.APP_NAME, "beginShare");
    	
		// Notify the service that confirm has been pressed
		Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_BEGINSHARE);
		broadcastIntent.putExtra("description", titleText.getText().toString());
		broadcastIntent.putExtra("toFacebook", toFacebook);
		broadcastIntent.putExtra("toTwitter", toTwitter);
		sendBroadcast(broadcastIntent);
		
		// Facebook is the first one
		if (toFacebook) {
			facebookShare();
		}
		
		// Execute it if we are not going to have facebook return
		if (toTwitter && !toFacebook) {
			twitterShare();
		}

		// Notify in case nothing is selected to allow resubmit
		if (!toTwitter && !toFacebook) {
			Toast.makeText(this, getString(R.string.no_network_selected), Toast.LENGTH_SHORT).show();
	    	titleText.setEnabled(true);
	    	confirmButton.setEnabled(true);
		}
    }

    
    /**
     * Initialises the feedback activity to finish the whole application
     */
    private void initFeedbackActivity() {

		Intent feedback = new Intent(this, FeedbackActivity.class);
        startActivityForResult(feedback, AndroidRestClientActivity.ACTIVITY_FEEDBACK);
    }
    
    /**
     * Disable the form buttons
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);

    	Log.i(AndroidRestClientActivity.APP_NAME, "VideoDataActivity onActivityResult " + requestCode + "--" + resultCode);
    	
    	// Enable form only when we came from the feedback activity
    	if (resultCode != RESULT_OK && requestCode == AndroidRestClientActivity.ACTIVITY_FEEDBACK) {
	    	titleText.setEnabled(true);
	    	confirmButton.setEnabled(true);
    	}
        
    	switch (requestCode) {
    	case AndroidRestClientActivity.ACTIVITY_SHARE_FACEBOOK:

    		if (toTwitter) {
    			twitterShare();
    		} else {
    			initFeedbackActivity();
    		}
    		break;
    		
    	case AndroidRestClientActivity.ACTIVITY_SHARE_TWITTER:
    		initFeedbackActivity();
    		break;
    	}
    	
    }
}
