package com.monllao.david.androidrestclient.service;
import java.io.IOException;

import org.restlet.data.Form;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.PropertiesManager;
import com.monllao.david.androidrestclient.RestClient;
import com.monllao.david.androidrestclient.User;


/**
 * Service to create a server user
 */
public class AddServerUserService extends IntentService {

	public AddServerUserService() {
		super("AddServerUserService");
	}

	protected void onHandleIntent(Intent intent) {
		
		// Provided user data (with password)
		User userdata = (User)intent.getSerializableExtra("user");

		Log.e(AndroidRestClientActivity.APP_NAME, "AddServerUserService");
		
		// requested URL
		String url = "";
		try {
			url = PropertiesManager.get("server.host") + "User/";
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RestClient client = new RestClient();

		// Set the user data
		Form form = new Form();
		form.add("email", userdata.getEmail());
		form.add("password", userdata.getPassword());

		Representation representation = client.post(url, form);
		
		Log.e(AndroidRestClientActivity.APP_NAME, "AddServerUserService plain response: " + representation.toString());
			
		// Getting the user and loading it into user
		User user = null;
		JacksonRepresentation<User> userRepresentation = new JacksonRepresentation<User> (representation, User.class);
		user = userRepresentation.getObject();

		Log.e(AndroidRestClientActivity.APP_NAME, "AddServerUserService user email: " + user.getEmail());

		// Give feedback to the activity
		Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_ADDUSER);
		broadcastIntent.putExtra("user", user);
		sendBroadcast(broadcastIntent);
	}

}
