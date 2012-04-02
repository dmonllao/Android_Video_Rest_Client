package com.monllao.david.androidrestclient;

import android.app.Activity;
import android.os.Bundle;

public class AndroidRestClientActivity extends Activity {
    
	public static String APP_NAME = "AndroidRestClient";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Setting the app user 
        User user = new User(this);
        
    }
}