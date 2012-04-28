package com.monllao.david.androidrestclient.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;

import android.os.Environment;
import android.util.Log;

public class CameraStorageManager {

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "AndroidRestClient");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.e(AndroidRestClientActivity.APP_NAME, "Failed to create directory " + mediaStorageDir.getAbsolutePath());
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");

        return mediaFile;
    }
}
