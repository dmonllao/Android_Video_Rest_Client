package com.monllao.david.androidrestclient.service;
import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.ExceptionHandler;
import com.monllao.david.androidrestclient.PropertiesManager;
import com.monllao.david.androidrestclient.RestClient;
import com.monllao.david.androidrestclient.User;


/**
 * Service to create a server user
 */
public class AddServerUserService extends RestService {
	
	public AddServerUserService() {
		super("AddServerUserService");
	}
	
	protected void onHandleIntent(Intent intent) {
		
		// Provided user data (with password)
		User userdata = (User)intent.getSerializableExtra("user");

		Log.v(AndroidRestClientActivity.APP_NAME, "AddServerUserService");

		String url = "";
		User user = null;
		
		// requested URL
		try {
			
			url = PropertiesManager.get("server.host") + "User/";

			RestClient client = new RestClient();

			// Set the user data
			Form form = new Form();
			form.add("email", userdata.getEmail());
			form.add("password", userdata.getPassword());

			Representation representation = client.post(url, form);
			
			// Getting the user and loading it into user
			JacksonRepresentation<User> userRepresentation = new JacksonRepresentation<User> (representation, User.class);
			user = userRepresentation.getObject();
			
			Log.i(AndroidRestClientActivity.APP_NAME, "AddServerUserService user email: " + user.getEmail());

			// Give feedback to the activity
			Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_ADDUSER);
			broadcastIntent.putExtra("user", user);
			sendBroadcast(broadcastIntent);
			
		} catch (Exception e) {
			Log.e(AndroidRestClientActivity.APP_NAME, "AddServerUserService - " + e.getMessage());
			this.showToast(AddServerUserService.this, e.getMessage());
		}

	}

}
