package com.monllao.david.androidrestclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

/**
 * Application internal properties manager
 */
public class PropertiesManager {

	static Properties serverProperties = null;
	private static Context context = null;
	
	/**
	 * Initializer 
	 * 
	 * Assigns the application context to the PropertiesManager 
	 * in order to know where should it look for resources
	 * @param context
	 */
	public static void init(Context context) {
		PropertiesManager.context = context;
	}
	
	/**
	 * Gets a property from the properties file
	 * 
	 * @param key The property name
	 * @return String
	 * @throws NotFoundException
	 * @throws IOException
	 */
	public static String get(String key) throws NotFoundException, IOException {

		if (PropertiesManager.serverProperties == null) {
			loadInternalProperties();
		}
		
		return serverProperties.getProperty(key, "");
	}
	

    protected static void loadInternalProperties() throws NotFoundException, IOException {
    
		InputStream rawResource = context.getResources().openRawResource(R.raw.server);
		serverProperties = new Properties();
		serverProperties.load(rawResource);
    }
    
}
