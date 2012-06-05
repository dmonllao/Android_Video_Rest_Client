package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.monllao.david.androidrestclient.receiver.AddServerVideoReceiver;
import com.monllao.david.androidrestclient.receiver.PutServerVideoReceiver;
import com.monllao.david.androidrestclient.service.AddServerVideoService;
import com.monllao.david.androidrestclient.service.PutServerVideoService;


/**
 * Activity to select the social networks and to fill the video description
 */
public class VideoDataActivity extends Activity {

	private AddServerVideoReceiver addVideoReceiver;
	private PutServerVideoReceiver putVideoReceiver;
	
	// Form elements
	private EditText titleText;
	private ImageButton facebookButton;
	private ImageButton twitterButton;
	private Button confirmButton;
	private boolean submitted = false;

	private Video video;

	private boolean toFacebook = false;
	private boolean toTwitter = false;
	
	/**
	 * Sends the video to the server and fills the form
	 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_data);
        
        // Registering receivers
        IntentFilter addvideofilter = new IntentFilter(AndroidRestClientActivity.ACTION_ADDVIDEO);
        addVideoReceiver = new AddServerVideoReceiver();
        registerReceiver(addVideoReceiver, addvideofilter);

        IntentFilter putvideofilter = new IntentFilter(AndroidRestClientActivity.ACTION_PUTVIDEO);
        putVideoReceiver = new PutServerVideoReceiver();
        registerReceiver(putVideoReceiver, putvideofilter);

        // Add the video as soon as possible
        addVideo();
        
        // Form elements
        titleText = (EditText) findViewById(R.id.title);
        confirmButton = (Button) findViewById(R.id.set_video_data);    
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

            	submitted = true;
            	titleText.setEnabled(false);
            	confirmButton.setEnabled(false);
            	
            	// The video id has been received so send the petition
            	if (video != null) {
            		video.setName(titleText.getText().toString());
            		putVideo();
            	}
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
    
    
    public void onDestroy() {
    	super.onDestroy();
    	
    	// Unregistering the broadcast receivers
    	unregisterReceiver(addVideoReceiver);
    	unregisterReceiver(putVideoReceiver);
    }
    

    /**
     * Starts the service the update the video
     */
    protected void putVideo() {

    	Intent intent = new Intent(this, PutServerVideoService.class);
    	intent.setAction(AndroidRestClientActivity.ACTION_PUTVIDEO);
    	
    	intent.putExtra("video", video);
    	
    	startService(intent);
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
     * When the video is uploaded
     * @param video
     */
    public void processServerVideo(Video video) {
    	this.video = video;
    	
    	// Send the PutVideo petition if the description was set
    	if (submitted == true) {
    		putVideo();
    	}
    }
    
    
    public void processShare(Video video) {
    	
    	Log.i(AndroidRestClientActivity.APP_NAME, "processShare - with video id = " + video.getId());
    	Toast.makeText(this, "Video uploaded!", Toast.LENGTH_LONG).show();
    	
    	// Set up the video data while the video is being sent
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra("video", video);

        // Set up the sharing options
    	intent.putExtra("facebook", toFacebook);
    	intent.putExtra("twitter", toTwitter);
        
        startActivityForResult(intent, AndroidRestClientActivity.ACTIVITY_SHARE);
    }
    

    /**
     * Outputs info about a problem, used to notice system failures
     * @param message
     */
    public void showProblem(String message) {

    	Log.i(AndroidRestClientActivity.APP_NAME, message);
    	Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    /**
     * If the share activity is cancelled finish the activity and return the user to the recordin activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	titleText.setEnabled(true);
    	confirmButton.setEnabled(true);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
