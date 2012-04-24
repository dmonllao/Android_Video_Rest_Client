package com.monllao.david.androidrestclient.service;

import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

import android.content.Intent;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.PropertiesManager;
import com.monllao.david.androidrestclient.RestClient;
import com.monllao.david.androidrestclient.User;

/**
 * Service to get the user data from her/his user id
 */
public class GetServerUserService extends RestService {

	public GetServerUserService() {
		super("GetServerUserService");
	}
	
	protected void onHandleIntent(Intent intent) {
		
		// Provided user data (with password)
		User userdata = (User)intent.getSerializableExtra("user");

		Log.v(AndroidRestClientActivity.APP_NAME, "GetServerUserService");

		String url = "";
		User user = null;

		try {

			// Requested URL			
			url = PropertiesManager.get("server.host") + "User/" + userdata.getId();

			// Sending the petition
			RestClient client = new RestClient();
			Representation representation = client.get(url);

			// Getting the user
			JacksonRepresentation<User> userRepresentation = new JacksonRepresentation<User> (representation, User.class);
			user = userRepresentation.getObject();

			Log.i(AndroidRestClientActivity.APP_NAME, "GetServerUserService user email: " + user.getEmail());

			// Give feedback to the main activity
			Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_GETUSER);
			broadcastIntent.putExtra("user", user);
			sendBroadcast(broadcastIntent);
			
		} catch (final Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserService - " + e.getMessage());
			this.showToast(this, e.getMessage());
		}
	}

}
