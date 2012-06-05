package com.monllao.david.androidrestclient.service;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.Video;
import com.monllao.david.androidrestclient.utils.PropertiesManager;
import com.monllao.david.androidrestclient.utils.RestClient;


/**
 * Send a PUT petition to update the video data
 */
public class PutServerVideoService extends RestService {

	public PutServerVideoService() {
		super("PutServerVideoService");
	}

	protected void onHandleIntent(Intent intent) {

		Video videodata = (Video)intent.getExtras().getSerializable("video");

		Log.v(AndroidRestClientActivity.APP_NAME, "PutServerVideoService");

		String url = "";
		Video video = null;

		try {

			// Requested URL			
			url = PropertiesManager.get("server.host") + "Video/" + videodata.getId();

			// Set the user data
			Form form = new Form();
			form.add("id", Integer.toString(videodata.getId()));
			form.add("userid", Integer.toString(videodata.getUserid()));
			form.add("name", videodata.getName());

			// Sending the petition
			RestClient client = new RestClient();
			Representation representation = client.put(url, form);

			// Getting the user
			JacksonRepresentation<Video> videoRepresentation = new JacksonRepresentation<Video> (representation, Video.class);
			video = videoRepresentation.getObject();

			Log.i(AndroidRestClientActivity.APP_NAME, "PutServerUserService Id: " + video.getId());

			// Give feedback to the main activity
			Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_PUTVIDEO);
			broadcastIntent.putExtra("video", video);
			sendBroadcast(broadcastIntent);
			
		} catch (final Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "PutServerUserService - " + e.getMessage());
			this.showToast(this, e.getMessage());
		}
	}

}
