package com.oishii.mobile.util.tasks;

class HttpResponseWrapper {
	boolean isSuccess;
	int errorMessage;
	Object responseBean;
	int operationId;
	IHttpCallback callback;
}
