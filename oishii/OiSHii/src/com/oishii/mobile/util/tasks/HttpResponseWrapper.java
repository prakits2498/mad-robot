package com.oishii.mobile.util.tasks;

class HttpResponseWrapper {
	boolean isSuccess;
	String errorMessage;
	Object responseBean;
	int operationId;
	IHttpCallback callback;
}
