package com.monllao.david.androidrestclient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

import com.monllao.david.androidrestclient.R;

/**
 * Application internal properties manager
 * 
 * Exceptions managed by callers
 */
public class PropertiesManager {

	static Properties serverProperties = null;
	private static Context context = null;
	
	/**
	 * Initialiser 
	 * 
	 * Assigns the application context to the PropertiesManager 
	 * in order to know where should it look for resources
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		PropertiesManager.context = context;
	}
	
	/**
	 * Gets a property from the properties file
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 * @param key The property name
	 * @return String
	 */
	public static String get(String key) throws NotFoundException, IOException {

		if (PropertiesManager.serverProperties == null) {
			loadInternalProperties();
		}
		
		return serverProperties.getProperty(key, "");
	}
	
	/**
	 * Loads the file into this.serverProperties
	 * 
	 * @throws NotFoundException
	 * @throws IOException
	 */
    protected static void loadInternalProperties() throws NotFoundException, IOException {
    
		InputStream rawResource = context.getResources().openRawResource(R.raw.server);
		serverProperties = new Properties();
		serverProperties.load(rawResource);
    }
    
}
