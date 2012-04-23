package com.monllao.david.androidrestclient;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.engine.Engine;
import org.restlet.engine.http.connector.HttpClientHelper;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class RestClient {

	public RestClient() {
		
	}
	
	
	public Representation get(String url) {
		
		ClientResource cr = this.getClientResource(url);
		return cr.get();
	}
	
	public Representation post(String url, Form form) {
	
		ClientResource cr = this.getClientResource(url);
		return cr.post(form);
	}
	
	/**
	 * Initializes the client resource to accept only JSON
	 * 
	 * @param url
	 * @return
	 */
	public ClientResource getClientResource(String url) {

		// Creating the client resource
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
		
		// Send request to the server 
		ClientResource cr = new ClientResource(url);
		Preference<MediaType> accept = new Preference<MediaType>(MediaType.APPLICATION_JSON);
		cr.getClientInfo().getAcceptedMediaTypes().add(accept);
		
		return cr;
	}
}
