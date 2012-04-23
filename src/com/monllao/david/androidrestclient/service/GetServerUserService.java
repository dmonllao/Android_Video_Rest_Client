package com.monllao.david.androidrestclient.service;

import java.io.IOException;
import java.util.Map;

import org.restlet.data.Form;
import org.restlet.engine.Engine;
import org.restlet.engine.http.connector.HttpClientHelper;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;
import com.monllao.david.androidrestclient.PropertiesManager;
import com.monllao.david.androidrestclient.User;

/**
 * Service to get the user data from her/his user id
 */
public class GetServerUserService extends IntentService {

	public GetServerUserService() {
		super("GetServerUserService");
	}

	protected void onHandleIntent(Intent intent) {
		
		// Provided user data (with password)
		User userdata = (User)intent.getSerializableExtra("user");

		Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserService");
		
		// requested URL
		String url = "";
		try {
			url = PropertiesManager.get("server.host") + "User/" + userdata.getId();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Creating the client resource
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
		
		// Send request to the server 
		ClientResource cr = new ClientResource(url);
		Representation representation = cr.get();

		Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserService plain response: " + representation.toString());
			
		// Getting the user and loading it into user
		User user = null;
		JacksonRepresentation<User> userRepresentation = new JacksonRepresentation<User> (representation, User.class);
		user = userRepresentation.getObject();

		Log.e(AndroidRestClientActivity.APP_NAME, "GetServerUserService user email: " + user.getEmail());

		// Give feedback to the activity
		Intent broadcastIntent = new Intent(AndroidRestClientActivity.ACTION_GETUSER);
		broadcastIntent.putExtra("user", user);
		sendBroadcast(broadcastIntent);
	}

}
