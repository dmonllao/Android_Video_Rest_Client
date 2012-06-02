package com.monllao.david.androidrestclient.share;

/**
 * Interface implemented by the applications which allows sharing 
 */
public interface Shareable {

	/**
	 * Shares the message
	 * @return A message with an error if it happens
	 */
	public boolean share();
}
