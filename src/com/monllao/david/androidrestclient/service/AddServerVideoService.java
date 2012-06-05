package com.monllao.david.androidrestclient.service;

import java.io.File;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.User;
import com.monllao.david.androidrestclient.Video;
import com.monllao.david.androidrestclient.camera.Base64Helper;
import com.monllao.david.androidrestclient.utils.PropertiesManager;
import com.monllao.david.androidrestclient.utils.RestClient;


/**
 * Sends a POST petition the server to create the video
 */
public class AddServerVideoService extends RestService {

	public AddServerVideoService() {
		super("AddServerVideoService");
	}

	protected void onHandleIntent(Intent intent) {
		
		// Provided user data (with password)
		User userdata = (User)intent.getSerializableExtra("user");
		String outputPath = intent.getExtras().getString("outputPath");

		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerUserService");

		String url = "";
		try {
			
			// Requested URL
			url = PropertiesManager.get("server.host") + "Video/";

			File outputFile = new File(outputPath);
			String base64String = Base64Helper.binaryToBase64(outputFile);
			
			// Set the user data
			Form form = new Form();
			form.add("userid", Integer.toString(userdata.getId()));
			form.add("videodata", base64String);

			// Sending the petition
			RestClient client = new RestClient();
			Representation representation = client.post(url, form);
			
			// Getting the video
			JacksonRepresentation<Video> videoRepresentation = new JacksonRepresentation<Video> (representation, Video.class);
			Video video = videoRepresentation.getObject();
			
			Log.i(AndroidRestClientActivity.APP_NAME, "AddServerVideoService videoid: " + video.getId());
			
			// Give feedback to the main activity
			Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_ADDVIDEO);
			broadcastIntent.putExtra("video", video);
			sendBroadcast(broadcastIntent);
			
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "AddServerVideoService - " + e.getMessage());
			this.showToast(AddServerVideoService.this, e.getMessage());
		}
	}

}
