package com.monllao.david.androidrestclient.utils;

import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.http.connector.HttpClientHelper;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;


/**
 * Adapter to Restlet client
 */
public class RestClient {

	public RestClient() {
		
	}
	
	/**
	 * Adapter to a get method petition
	 * @param url
	 * @return
	 */
	public Representation get(String url) {
		
		ClientResource cr = this.getClientResource(url);
		return cr.get();
	}
	
	
	/**
	 * Adapter to a post method petition
	 * @param url
	 * @param form
	 * @return
	 */
	public Representation post(String url, Form form) {
	
		ClientResource cr = this.getClientResource(url);
		return cr.post(form);
	}

	
	/**
	 * Adapter to a put method petition
	 * @param url
	 * @param form
	 * @return
	 */
	public Representation put(String url, Form form) {
		
		ClientResource cr = this.getClientResource(url);
		return cr.put(form);
	}
	
	/**
	 * Initializes the client resource
	 * @param url
	 * @return
	 */
	public ClientResource getClientResource(String url) {

		// Creating the client resource
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
		
		// ClientResource 
		Client client = new Client(Protocol.HTTP);
		client.setConnectTimeout(3000);
		ClientResource cr = new ClientResource(url);
		cr.setNext(client);
		
		// Accepting only JSON
		Preference<MediaType> accept = new Preference<MediaType>(MediaType.APPLICATION_JSON);
		cr.getClientInfo().getAcceptedMediaTypes().add(accept);
		
		return cr;
	}
}
