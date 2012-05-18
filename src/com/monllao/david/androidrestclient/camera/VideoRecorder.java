package com.monllao.david.androidrestclient.camera;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.R;
import com.monllao.david.androidrestclient.User;
import com.monllao.david.androidrestclient.VideoDataActivity;

public class VideoRecorder {
	
	/**
	 * The caller activity
	 */
	private Activity activity;
	
	/**
	 * Application user
	 */
	private User user;
	
	private boolean shareButtonClicked = false;
	private boolean isRecording = false;

	private Camera camera = null;
    private File outputFile;
    
    private FrameLayout frameLayout;
	private CameraVideoPreview preview = null;
    private VideoView videoView = null;
    
	ImageButton captureButton;
	ImageButton shareButton;
	
	/**
	 * Video height
	 */
	private int height;
	
	/**
	 * Video width
	 */
	private int width;
	
	
	/**
	 * Fills the main layout
	 * @param activity
	 */
	public VideoRecorder(Activity activity) {

		this.activity = activity;
    	frameLayout = (FrameLayout) activity.findViewById(R.id.screen);
    	
    	// Display the camera preview
    	setPreviewView();
        
    	// Buttons to share and capture
        this.addCaptureButton();
        this.addShareButton();
	}
		

    /**
     * Initialises the share button 
     * 
     * Hidden until a recording is available 
     */
    protected void addShareButton() {
    	
    	shareButton = (ImageButton) activity.findViewById(R.id.button_share);

        // Make it invisible until we have a recording
        shareButton.setVisibility(View.INVISIBLE);
        
    	shareButton.setOnClickListener(
    		new View.OnClickListener() {
			
				public void onClick(View v) {
				    shareButtonClicked = true;

				    // We need both share click and the user to be set before we send the video
				    if (user != null) {
				    	initVideoData();
				    }
				}
			}
    	);
    }

    /**
     * Sets the video user and redirects to VideoData if the shared button is pressed
     * @param user
     */
    public void setUser(User user) {
	    
    	this.user = user;

	    // We need both share click and the user to be set before we send the video
	    if (shareButtonClicked == true) {
	    	initVideoData();
	    }
    }
    
    /**
     * Initialises the button to capture
     */
    protected void addCaptureButton() {
        
        // Capture button
        captureButton = (ImageButton) activity.findViewById(R.id.button_rec);
    	captureButton.setOnClickListener(
    		new View.OnClickListener() {
    			
    			public void onClick(View v) {
    				
    				// stop recording, release camera and show the video
    				if (isRecording) {    	                
    	                preview.mediaRecorder.stop();
    	                release();
    	                camera = null;
	                    
    	                isRecording = false;
    	                
	                    // Display the recorded video
	                    setViewerView();
	                    
    	                // share button available 
    	                shareButton.setVisibility(View.VISIBLE);
	                    
    	            // start recording
    	            } else {
    	            	
    	            	// Not the first recording
    	            	if (outputFile != null) {
    	            		setPreviewView();
    	            	}
    	            	
    	            	// New path / filename for the video 
    	            	outputFile = CameraStorageManager.getOutputMediaFile();
	            		preview.readyToRec(outputFile);
	            		
	            		isRecording = true;
    	            }

    			}
    		}
    	);
    	
    }

    
    /**
     * New activity to set up the video data
     */
    protected void initVideoData() {

        // Set up the video data while the video is being sent
        Intent intent = new Intent(activity, VideoDataActivity.class);
        intent.putExtra("outputPath", outputFile.getPath());
        intent.putExtra("user", user);
        
        activity.startActivityForResult(intent, AndroidRestClientActivity.ACTIVITY_VIDEODATA);
    }
    
    
    /**
     * Fills the surface view with the video preview
     */
    protected void setPreviewView() {

		// Getting the camera
    	try {
			camera = Camera.open();
    	} catch (Exception e) {
    		Log.e(AndroidRestClientActivity.APP_NAME, "Can\'t get Camera Instance");
    	}
	    
    	calculateOptimalScreenSize();
	    preview = new CameraVideoPreview(this.activity, camera, width, height);
		
		frameLayout.removeAllViews();
		frameLayout.addView(preview);

    }
    
    
    /**
     * Fills the surface view with the player
     */
    protected void setViewerView() {

    	videoView = new VideoView(activity);
    	
    	frameLayout.removeAllViews();
    	frameLayout.addView(videoView);

    	// Video controls
    	MediaController mediaController = new MediaController(activity);
    	
    	// Assign the video controls to the view and start the reproduction
    	videoView.setVideoPath(outputFile.getPath());
    	videoView.setMediaController(mediaController);
    	videoView.requestFocus();
    	videoView.start();
    }


    /**
     * @todo Big TODO
     */
    protected void calculateOptimalScreenSize() {
//width = 1280;
//height = 720;
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		int screenHeight = metrics.heightPixels;
		int screenWidth = metrics.widthPixels;

		List<Size> supportedSizes = camera.getParameters().getSupportedPreviewSizes();
		Iterator<Size> it = supportedSizes.iterator();
		while (it.hasNext()) {
			Size size = it.next();

			Log.e(AndroidRestClientActivity.APP_NAME, "Supports: " + size.width+ ":" + size.height);
			// Look for the max resolution
//			if (size.height == screenHeight && size.width < screenWidth) {

				Log.e(AndroidRestClientActivity.APP_NAME, "Selected size: " + size.width+ ":" + size.height);
				
				height = size.height;
				width = size.width;				
				return;
//			}
		}
		
    }
    

    /**
     * Releases both media recorder and camera
     * 
     * Redirects the call to CameraVideoPreview which owns the mediaRecorder
     */
    public void release() {
    	preview.release();
    }
    
}
