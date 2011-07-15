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
package com.madrobot.tasks;

import java.io.Serializable;

/**
 * 
 */
@SuppressWarnings("serial")
public class DataResponse implements Serializable {

	private int responseid;
	private int responseStatus;
	private Throwable t;

	Throwable getT() {
		return t;
	}

	void setT(Throwable t) {
		this.t = t;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}

	private Object data;

	public int getResponseId() {
		return responseid;
	}

	void setResponseId(int response) {
		this.responseid = response;
	}

	public Object getData() {
		return data;
	}

	void setData(Object data) {
		this.data = data;
	}

}
