package com.monllao.david.androidrestclient.test;

import com.monllao.david.androidrestclient.AndroidRestClientActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ImageButton;

/**
 * AndroidRestClient main activity test
 * 
 * Verifies the availability of the record button
 * @todo Complete it! it's just a testing framework tests 
 */
public class AndroidRestClientTest extends
		ActivityInstrumentationTestCase2<AndroidRestClientActivity> {

	private AndroidRestClientActivity activity;
	private ImageButton viewButton;
	
	public AndroidRestClientTest() {
		super("com.monllao.david.androidrestclient", AndroidRestClientActivity.class);
	}
	
	/**
	 * Setting up the activity
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		activity = this.getActivity();
		viewButton = (ImageButton) activity.findViewById(com.monllao.david.androidrestclient.R.id.button_rec); 
	}
	
	/**
	 * Checks the existence of the viewButton element
	 */
	public void testPreconditions() {
		assertNotNull(viewButton);
	}
	
	/**
	 * Test to check the visibility of the rec button
	 */
	public void testRecButton() {
		
		// The rec button is visible
		assertEquals(viewButton.getVisibility(), View.VISIBLE);
	}
}
