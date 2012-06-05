package com.monllao.david.androidrestclient.camera;

import android.content.Context;
import android.widget.VideoView;

/**
 * VideoView extension to assign the Media Player size
 */
public class RecordingViewer extends VideoView {

	private int width;
	private int height;
	public RecordingViewer(Context context, int width, int height) {
		super(context);
		this.width = width;
		this.height = height;
	}

	protected void onMeasure(int width, int height) {
		setMeasuredDimension(this.width, this.height);
	}
}
