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
import android.widget.Button;
import android.widget.FrameLayout;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.R;
import com.monllao.david.androidrestclient.User;
import com.monllao.david.androidrestclient.service.AddServerVideoService;

public class VideoRecorder {
	
	private User user;
	private Activity activity;

	private Camera camera = null;
	private CameraVideoPreview preview = null;
	private MediaRecorder mediaRecorder = null;
    private File outputFile;
    
	Button captureButton;
	
	private boolean isRecording = false;
	
	public VideoRecorder(Activity activity, User user) {

		this.activity = activity;
		this.user = user;
		
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

    
    public void addCaptureButton() {
        
        // Capture button
        captureButton = (Button) activity.findViewById(R.id.button_capture);
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
    	                
    	                // Start the service to add the file
    	                Intent intent = new Intent(activity, AddServerVideoService.class);
    	                intent.putExtra("user", user);
    	                intent.putExtra("outputPath", outputFile.getPath());
    	                activity.startService(intent);
    	                
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
