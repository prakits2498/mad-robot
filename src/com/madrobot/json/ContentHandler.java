/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot.json;

import java.io.IOException;

/**
 * A simplified and stoppable SAX-like content handler for stream processing of JSON text. 
 * 
 * @see org.xml.sax.ContentHandler
 * @see com.madrobot.json.JSONParser#parse(java.io.Reader, IContentHandler, boolean)
 * 
 */
public interface ContentHandler {
	/**
	 * Receive notification of the beginning of JSON processing.
	 * The parser will invoke this method only once.
     * 
	 * @throws JSONParseException 
	 * 			- JSONParser will stop and throw the same exception to the caller when receiving this exception.
	 */
	void startJSON() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the end of JSON processing.
	 * 
	 * @throws JSONParseException
	 */
	void endJSON() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the beginning of a JSON object.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
     *          - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @see #endJSON
	 */
	boolean startObject() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the end of a JSON object.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
     * 
     * @see #startObject
	 */
	boolean endObject() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the beginning of a JSON object entry.
	 * 
	 * @param key - Key of a JSON object entry. 
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
     * 
     * @see #endObjectEntry
	 */
	boolean startObjectEntry(String key) throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the end of the value of previous object entry.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
     * 
     * @see #startObjectEntry
	 */
	boolean endObjectEntry() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the beginning of a JSON array.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
     * 
     * @see #endArray
	 */
	boolean startArray() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the end of a JSON array.
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
     * 
     * @see #startArray
	 */
	boolean endArray() throws JSONParseException, IOException;
	
	/**
	 * Receive notification of the JSON primitive values:
	 * 	java.lang.String,
	 * 	java.lang.Number,
	 * 	java.lang.Boolean
	 * 	null
	 * 
	 * @param value - Instance of the following:
	 * 			java.lang.String,
	 * 			java.lang.Number,
	 * 			java.lang.Boolean
	 * 			null
	 * 
	 * @return false if the handler wants to stop parsing after return.
	 * @throws JSONParseException
	 */
	boolean primitive(Object value) throws JSONParseException, IOException;
		
}
