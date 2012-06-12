package com.monllao.david.androidrestclient.camera;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;

/**
 * Surface View extension 
 * 
 * To put the camera into the layout with the appropiate config
 */
public class CameraVideoPreview extends SurfaceView implements SurfaceHolder.Callback {

	/**
	 * Configure
	 */
	private int videoBitRate = 2000000;
	private boolean blackWhite = true;
	
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private File outputFile;
	
	private int width;
	private int height;
	
	public MediaRecorder mediaRecorder;
	private boolean userReady = false;
	private boolean surfaceReady = false;
	
	
	/**
	 * Inits the previewer
	 * 
	 * @param context
	 * @param camera
	 * @param width
	 * @param height
	 */
	public CameraVideoPreview(Context context, Camera camera, int width, int height) {
		super(context);
		
		mCamera = camera;
		
		mHolder = getHolder();
		
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		this.width = width;
		this.height = height;
	}
	
	
	/**
	 * Notifies the user clicked the rec button
	 * 
	 * It waits until the surface has changed and the preview size is assigned
	 * @param outputFile
	 */
	public void readyToRec(MediaRecorder mediaRecorder, File outputFile) {
		
		this.mediaRecorder = mediaRecorder;
		this.outputFile = outputFile;
		
		userReady = true;

		// When the user has clicked and the surface is ready begin the recording
		if (surfaceReady == true) {
			beginRec();
		}
	}

	
	/**
	 * Creates a MediaRecorder and 
	 */
	protected void beginRec() {
    	
		// Unlocks camera to be used
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Set the Profile
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        CamcorderProfile highProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
        profile.videoCodec = MediaRecorder.VideoEncoder.H264;
        profile.videoBitRate = this.videoBitRate;
        profile.videoFrameRate = highProfile.videoFrameRate;
        profile.videoFrameWidth = width;
        profile.videoFrameHeight = height;
        
        // High audio quality
        profile.audioCodec = highProfile.audioCodec;
        profile.audioChannels = highProfile.audioChannels;
        profile.audioSampleRate = highProfile.audioSampleRate;
        profile.audioBitRate = highProfile.audioBitRate;
        
        mediaRecorder.setProfile(profile);
        mediaRecorder.setMaxDuration(AndroidRestClientActivity.VIDEO_SECS * 1000);
		
        // Set output file
        mediaRecorder.setOutputFile(outputFile.toString());
        
        // Set the preview output
        mediaRecorder.setPreviewDisplay(getHolder().getSurface());

        // Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e(AndroidRestClientActivity.APP_NAME, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            return;
        } catch (IOException e) {
            Log.e(AndroidRestClientActivity.APP_NAME, "IOException preparing MediaRecorder: " + e.getMessage());
            return;
        }
        
		// Record start
        mediaRecorder.start();
        
	}


	/**
	 * Assigns the size
	 */
	protected void onMeasure(int width, int height) {
		setMeasuredDimension(this.width, this.height);
	}
	
	
	/**
	 * Sets the preview size
	 * 
	 * Initialises the recording if user already clicked the rec button
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		if (mHolder.getSurface() == null) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Preview surface does not exists");
			return;
		}

		// Before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Can\'t stop preview");
		}

		// Setting preview size
		Camera.Parameters cameraParameters = mCamera.getParameters();
		cameraParameters.setPreviewSize(this.width, this.height);
		
		// Effects
		if (blackWhite == true) {
			cameraParameters.setColorEffect(Camera.Parameters.EFFECT_MONO);
		}
		
		mCamera.setParameters(cameraParameters);
		
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Error starting preview " + e.getMessage());
		}
		

		// Inform the preview that user clicked
		surfaceReady = true;
		
		// Record if user has already clicked
		if (userReady == true) {
			beginRec();
		}
	}

	
	public void surfaceCreated(SurfaceHolder holder) {
		
		Log.i(AndroidRestClientActivity.APP_NAME, "surfaceCreated");

		// Before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Can\'t stop preview");
		}
		
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Error setting the camera preview " + e.getMessage());
		}
		
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// VideoRecorder manages the destruction
	}

	
    /**
     * All released
     */
    public void release() {

    	releaseMediaRecorder();
    	if (mCamera != null) {
    		mCamera.stopPreview();
    	}
    	releaseCamera();
    }
    
    
    /**
     * Releasing the camera
     */
    public void releaseCamera() {
    	if (mCamera != null) {
    		mCamera.release();
    		mCamera = null;
    	}
    }
    

    /**
     * Releasing the media recorder
     */
    public void releaseMediaRecorder() {
		if (mediaRecorder != null) {
		    mediaRecorder.reset();
		    mediaRecorder.release();
		    mediaRecorder = null;
		    mCamera.lock();
		}
	}
    
}
