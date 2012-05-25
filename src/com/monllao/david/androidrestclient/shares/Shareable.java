package com.monllao.david.androidrestclient.shares;

/**
 * Interface implemented by the applications which allows sharing 
 */
public interface Shareable {

	/**
	 * Shares the message
	 * @param message
	 * @return A message with feedback about how has it been
	 */
	public String share(String message);
}
