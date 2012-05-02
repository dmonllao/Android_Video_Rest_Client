package com.monllao.david.androidrestclient.camera;

import java.io.IOException;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraVideoPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	
	public CameraVideoPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}


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
		
		// TODO; Set preview size, resizable or not, rotate...
		
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Error starting preview " + e.getMessage());
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "Error setting the camera preview " + e.getMessage());
		}
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Take care of it in the Activity
	}

}
