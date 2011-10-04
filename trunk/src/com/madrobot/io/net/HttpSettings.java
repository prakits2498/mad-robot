package com.madrobot.io.net;

public class HttpSettings {

	/**
	 * Default HTTP payload buffer size 4Kb
	 */

	/**
	 * Represents the HTTP GET, POST and DELETE
	 */
	public enum HttpMethod {
		/**
		 * Http GET method
		 */
		HTTP_GET,
		/**
		 * Http POST method
		 */
		HTTP_POST,
		/**
		 * Http DELETE method
		 */
		HTTP_DELETE;
	}

	/**
	 * HTTP session socket time-out key
	 */
	public static final String HTTP_SOCKET_TIME_OUT_PARAM = "http.socket.timeout";
	public static final String HTTP_EXPECT_CONTINUE_PARAM = "http.protocol.expect-continue";
	public static final String HTTP_SINGLE_COOKIE_PARAM="http.protocol.single-cookie-header";
	/**
	 * HTTP session timeout. Default is 30 seconds
	 */
	private int socketTimeout = 30000;

	private boolean expectContinue = true;
	
	private boolean isSingleCookieHeader=false;

	
	private int defaultBufferSize = 1042 * 4;

	private HttpMethod httpMethod = HttpMethod.HTTP_GET;;

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public int getDefaultBufferSize() {
		return defaultBufferSize;
	}

	/**
	 * default: 4kb
	 */
	public void setDefaultBufferSize(int defaultBufferSize) {
		this.defaultBufferSize = defaultBufferSize;
	}

	public boolean isExpectContinue() {
		return expectContinue;
	}

	/**
	 * default :true
	 * 
	 * @param expectContinue
	 */
	public void setExpectContinue(boolean expectContinue) {
		this.expectContinue = expectContinue;
	}

	/**
	 * default: 30 secs
	 * 
	 * @param timeout
	 */
	public void setSocketTimeout(int timeout) {
		this.socketTimeout = timeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	
	public boolean isSingleCookieHeader() {
		return isSingleCookieHeader;
	}

	public void setSingleCookieHeader(boolean isSingleCookieHeader) {
		this.isSingleCookieHeader = isSingleCookieHeader;
	}

}
