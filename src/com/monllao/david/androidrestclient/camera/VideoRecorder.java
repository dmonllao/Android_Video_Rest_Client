package com.monllao.david.androidrestclient.camera;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.R;
import com.monllao.david.androidrestclient.User;
import com.monllao.david.androidrestclient.VideoDataActivity;

public class VideoRecorder {
	
	private Activity activity;
	private User user;
	
	private boolean submitted = false;

	private Camera camera = null;
	private CameraVideoPreview preview = null;
	private MediaRecorder mediaRecorder = null;
    private File outputFile;
    
	ImageButton captureButton;
	ImageButton shareButton;
	
	private boolean isRecording = false;
	
	public VideoRecorder(Activity activity) {

		this.activity = activity;
		
		// Getting the camera
    	try {
    		camera = Camera.open();
    	} catch (Exception e) {
    		Log.e(AndroidRestClientActivity.APP_NAME, "Can\'t get Camera Instance");
    	}
	}
	
	public void fillLayout() {

        preview = new CameraVideoPreview(activity, camera);

        FrameLayout previewLayout = (FrameLayout) activity.findViewById(R.id.camera_preview);
        previewLayout.addView(preview);
        
        this.addCaptureButton();
        this.addShareButton();
	}

	
    private boolean prepareVideoRecorder() {

        mediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        camera.unlock();
        mediaRecorder.setCamera(camera);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setMaxDuration(10000);
        
        // Coping from QUALITY_HIGH profile
        // TODO: Better quality
        CamcorderProfile highProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mediaRecorder.setVideoFrameRate(highProfile.videoFrameRate);
//        mediaRecorder.setVideoSize(highProfile.videoFrameWidth, highProfile.videoFrameHeight);
        mediaRecorder.setAudioSamplingRate(highProfile.audioSampleRate);
        mediaRecorder.setAudioEncodingBitRate(highProfile.audioBitRate);
        mediaRecorder.setAudioChannels(highProfile.audioChannels);
        mediaRecorder.setVideoFrameRate(highProfile.videoBitRate);

        // Step 4: Set output file
        outputFile = CameraStorageManager.getOutputMediaFile();
        mediaRecorder.setOutputFile(outputFile.toString());

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(preview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e(AndroidRestClientActivity.APP_NAME, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.e(AndroidRestClientActivity.APP_NAME, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    /**
     * Initialises the button the share. 
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
				    submitted = true;

				    // Redirect to the new activity VideoData if we have the application user
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

	    // Redirect to the new activity VideoData if "share" has been clicked
	    if (submitted == true) {
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
    			
    			@Override
    			public void onClick(View v) {
    				
    				// stop recording and release camera
    				if (isRecording) {
    	                
    	                mediaRecorder.stop();  // stop the recording
    	                releaseMediaRecorder(); // release the MediaRecorder object
    	                camera.lock();         // take camera access back from MediaRecorder
    	                isRecording = false;
    	                
    	                // Make it visible
    	                shareButton.setVisibility(View.VISIBLE);
    	                
    	            // start recording
    	            } else {
    	            	
    	                // initialise video camera
    	                if (prepareVideoRecorder()) {
    	                	
    	                    // Camera is available and unlocked, MediaRecorder is prepared,
    	                    // now you can start recording
    	                    mediaRecorder.start();
    	                    isRecording = true;
    	                    
    	                 // prepare didn't work, release the camera
    	                } else {
    	                    releaseMediaRecorder();
    	                }
    	            }

    			}
    		}
    	);
    	
    }
    
    
    protected void initVideoData() {

        // Set up the video data while the video is being sent
        Intent intent = new Intent(activity, VideoDataActivity.class);
        intent.putExtra("outputPath", outputFile.getPath());
        intent.putExtra("user", user);
        
        activity.startActivityForResult(intent, AndroidRestClientActivity.ACTIVITY_VIDEODATA);
    }
    
    /**
     * All stopped
     */
    public void release() {

    	releaseMediaRecorder();
    	releaseCamera();
    }
    
    /**
     * Releasing the camera
     */
    private void releaseCamera() {
    	if (camera != null) {
    		camera.stopPreview();
    		camera.release();
    		camera = null;
    	}
    }
    

    /**
     * Releasing the media recorder
     */
    private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
		    mediaRecorder.reset();   // clear recorder configuration
		    mediaRecorder.release(); // release the recorder object
		    mediaRecorder = null;
		    camera.lock();           // lock camera for later use
		}
	}
}
