package com.monllao.david.androidrestclient.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Base64;

/**
 * Helper to encode streams to base 64 strings
 */
public class Base64Helper {

  
    /**
     * Encodes a file to a base 64 string
     * @param file
     * @return The encoded string
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String binaryToBase64(File file) throws FileNotFoundException, IOException {

        String base64String;
        RandomAccessFile raFile = new RandomAccessFile(file, "r");
       
        // Creating a bytes array with the video length
        long length = raFile.length();
        byte[] binaryVideo = new byte[(int)length];
       
       
        try {
            raFile.readFully(binaryVideo);
        } finally {
            // Closing stream read
        	raFile.close();
        }
       
        // To base 64
        base64String = Base64.encodeToString(binaryVideo, (Base64.URL_SAFE + Base64.NO_WRAP));
       
        // Returning the video as a base 64 string
        return base64String;
    }
    

    /**
     * Just for base64 strings verifications
     * 
     * @param videoBase64
     * @throws IOException
     * @throws FileNotFoundException
     * @throws InterruptedException
    */
    public static void base64ToVideo(String videoBase64) throws IOException, FileNotFoundException, InterruptedException {
  
	  	Thread.sleep(4000);
	  	
	  	byte[] binaryVideo = Base64.decode(videoBase64, (Base64.URL_SAFE + Base64.NO_WRAP));
	  	
	  	
	  	File filePath = CameraStorageManager.getOutputMediaFile();
	  	FileOutputStream outputStream = new FileOutputStream(filePath, true);
	  	
	  	outputStream.write(binaryVideo);
	  	outputStream.close();
    }
    
}
