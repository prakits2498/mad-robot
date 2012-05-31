package com.madrobot.net;

import com.madrobot.net.HttpConstants.HttpMethod;

public class HttpHelperSettings {

	/**
	 * Default HTTP payload buffer size 4Kb
	 */
	private int defaultBufferSize = 1042 * 4;

	private boolean expectContinue = true;

	private HttpMethod httpMethod = HttpMethod.GET;

	private boolean isSingleCookieHeader = false;

	/**
	 * HTTP session timeout. Default is 30 seconds
	 */
	private int socketTimeout = 30000;;

	public int getDefaultBufferSize() {
		return defaultBufferSize;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public boolean isExpectContinue() {
		return expectContinue;
	}

	public boolean isSingleCookieHeader() {
		return isSingleCookieHeader;
	}

	/**
	 * default: 4kb
	 */
	public void setDefaultBufferSize(int defaultBufferSize) {
		this.defaultBufferSize = defaultBufferSize;
	}

	/**
	 * default :true
	 * 
	 * @param expectContinue
	 */
	public void setExpectContinue(boolean expectContinue) {
		this.expectContinue = expectContinue;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setSingleCookieHeader(boolean isSingleCookieHeader) {
		this.isSingleCookieHeader = isSingleCookieHeader;
	}

	/**
	 * default: 30 secs
	 * 
	 * @param timeout
	 */
	public void setSocketTimeout(int timeout) {
		this.socketTimeout = timeout;
	}

}
